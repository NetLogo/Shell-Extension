package org.nlogo.extensions.shell;

// Originally written by Eric Russell for NetLogo versions 4.1 and 5.0, 
// and updated to work with NetLogo 6.0 and now 6.1, and with Java 1.8 by Charles Staelin,
// May 2017.

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.Reporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

public class ShellExtension extends org.nlogo.api.DefaultClassManager {

  private static ProcessBuilder _pb;

  private static void initializeProcessBuilder(Context context) {
    if (_pb == null) {
      _pb = new ProcessBuilder(new ArrayList<>());
      _pb.redirectErrorStream(true);
        _pb.directory(new java.io.File(context.workspace().getModelDir()));
    }
  }
  
  private static String getProcessOutput(InputStream inputStream) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line).append(System.getProperty("line.separator"));
      }
    }
    return sb.toString();
  }

  @Override
  public void load(PrimitiveManager primManager) {
    primManager.addPrimitive("pwd", new GetWorkingDirectory());
    primManager.addPrimitive("cd", new SetWorkingDirectory());
    primManager.addPrimitive("getenv", new GetEnvironmentVariable());
    primManager.addPrimitive("setenv", new SetEnvironmentVariable());
    primManager.addPrimitive("exec", new Excec());
    primManager.addPrimitive("fork", new Fork());
    primManager.addPrimitive("reset", new Reset());
  }

  public static class GetWorkingDirectory implements Reporter {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[]{}, Syntax.StringType());
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public Object report(Argument args[], Context context) 
            throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      File dir = _pb.directory();
      if (dir != null) {
        return dir.toString();
      } else {
        return System.getProperty("user.dir");
      }
    }
  }
  
  public static class Reset implements Command {
    
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.commandSyntax(new int[]{});
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public void perform(Argument args[], Context context) {
      _pb = null;
    }
  }

  public static class SetWorkingDirectory implements Command {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.commandSyntax(new int[]{Syntax.StringType()});
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public void perform(Argument args[], Context context) throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      File newDir;
      String newDirName = args[0].getString();
      if (newDirName.startsWith("/") || newDirName.matches("^[a-zA-Z]:.*")) {
        // Parse as absolute path if it starts with a slash 
        // (Mac/Unix) or a drive letter and colon (Windows)
        newDir = new File(newDirName);
      } else {
        // Parse as relative to current directory
        File dir = _pb.directory();
        if (dir == null) {
          dir = new File(System.getProperty("user.dir"));
        }
        newDir = new File(dir, newDirName);
      }
      try {
        newDir = newDir.getCanonicalFile();
      } catch (IOException e) {
        ExtensionException ex = new ExtensionException(e);
        ex.setStackTrace(e.getStackTrace());
        throw ex;
      }
      if (!newDir.exists()) {
        throw new ExtensionException("directory '" + newDir.toString() + "' does not exist");
      }
      _pb.directory(newDir);
    }
  }

  public static class SetEnvironmentVariable implements Command {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.commandSyntax(new int[]{Syntax.StringType(), Syntax.StringType()});
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public void perform(Argument args[], Context context) throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      _pb.environment().put(args[0].getString(), args[1].getString());
    }
  }

  public static class GetEnvironmentVariable implements Reporter {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType()}, Syntax.StringType());
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public Object report(Argument args[], Context context) throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      String result = _pb.environment().get(args[0].getString());
      if (result != null) {
        return result;
      } else {
        return "";
      }
    }
  }

  public static class Excec implements Reporter {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType() | Syntax.RepeatableType()},
              Syntax.StringType(), 1);
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public Object report(Argument args[], Context context) 
            throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      List<String> command = new ArrayList<>(args.length);
      for (int i = 0; i < args.length; i += 1) {
        command.add(args[i].getString());
      }
      _pb.command(command);
      try {
        final Process process = _pb.start();
        String results = getProcessOutput(process.getInputStream());
        final int errCode = process.waitFor();
//        if (errCode != 0) {
//          throw new ExtensionException("shell:exec waitfor() error" + errCode);
//        }
        return results;
      } catch (IOException | InterruptedException ex) {
        ExtensionException e = new ExtensionException(ex.getMessage());
        e.setStackTrace(ex.getStackTrace());
        throw e;
      }
    }
  }

  public static class Fork implements Command {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[]{Syntax.StringType() | Syntax.RepeatableType()}, 1);
    }

    public String getAgentClassString() {
      return "OTPL";
    }

    @Override
    public void perform(Argument args[], Context context) throws ExtensionException, LogoException {
      initializeProcessBuilder(context);
      List<String> command = new ArrayList<>(args.length);
      for (int i = 0; i < args.length; i += 1) {
        command.add(args[i].getString());
      }
      _pb.command(command);
      try {
        _pb.start();
      } catch (IOException ioe) {
        ExtensionException e = new ExtensionException(ioe.getMessage());
        e.setStackTrace(ioe.getStackTrace());
        throw e;
      }
    }
  }
}
