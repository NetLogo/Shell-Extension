import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

version    := "1.0.4"
isSnapshot := true

scalaVersion          := "3.7.0"
Compile / scalaSource := baseDirectory.value / "src" / "main"
Test / scalaSource    := baseDirectory.value / "src" / "test"
scalacOptions        ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii", "-release",
                             "21")

netLogoExtName      := "shell"
netLogoClassManager := "org.nlogo.extensions.shell.ShellExtension"
netLogoVersion      := "7.1.0-alpha1-931b371"
netLogoZipExtras   ++= Seq(baseDirectory.value / "README.md", baseDirectory.value / "example-models")
