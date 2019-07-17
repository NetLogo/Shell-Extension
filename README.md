# NetLogo shell extension

This package contains the NetLogo shell extension.

It lets you do things like:

> (Mac) `observer> show shell:exec "ls"`

> (Windows) `observer> show (shell:exec "cmd" "/c" "dir")`

The included commands are:

 * `shell:pwd` => reports current working directory
 * `shell:cd <directory>` => change current working directory (relative to current directory unless <directory> begins with a drive letter on Windows or a forward slash on Mac/Unix)
 * `shell:getenv <name>` => reports value of environment variable
 * `shell:setenv <name> <value>` => sets environment variable
 * `shell:exec <command> (shell:exec <command> <param> ...)` => execute command synchronously and report a string of the results it prints to stdout
 * `shell:fork <command> (shell:fork <command> <param> ...)` => execute command asynchronously and discard the results
 * `shell:reset` => clears any environment variables set  by `shell:setenv` and returns the working directory to the NetLogo model directory.

## Downloads

 * for NetLogo 6.0: https://github.com/downloads/NetLogo/Shell-Extension/shell-6.0.zip
 * for NetLogo 5.x: https://github.com/downloads/NetLogo/Shell-Extension/shell-5.0.zip
 * for NetLogo 4.1: https://github.com/downloads/NetLogo/Shell-Extension/shell-4.1.zip

## Using

The shell extension inherits all the environment variables that were in place when NetLogo was started. That makes `shell:getenv` very useful for passing the values of ennvironment variables on to NetLogo. HOWEVER, any environment variable set by `shell:setenv` is local to the shell extension only and will disappear when the NetLogo session is ended. Similarly, the working directory for the shell extension is initially set to the directory in which the NetLogo model is located. Changing the extension's working directory with `shell:cd` changes the working directory for the extension only. **It does not change the working directory for NetLogo itself.** That is done with NetLogo's `set-current-directory` command. 

`shell:setenv` and `shell:cd` thus work only within the shell extension and are in the service of `shell:exec` and `shell:fork`, which do inherit their environment and working directory from the extension's.

## Installing

Download and unzip to the extensions folder, which is inside the app folder inside the NetLogo program folder. This should create a `shell` subfolder that contains the `shell.jar` file and other potentially useful files. The `shell.jar` file in this package was built under NetLogo 6.1.0 and Java 1.8.

For more information about NetLogo extensions in general, see the NetLogo User Manual.

## Building

The Makefile included in this package can be used to create the shell.jar file from the java source file. If you are running it from the app/extensions/shell folder in the NetLogo installation, you can simply enter "make". (Depending on the system, you may need to do this as "administrator".) If you are in any other directory, you will need to first set the NETLOGO environment variable to tell the Makefile where to find the NetLogo installation.  For example:

    export NETLOGO="/Applications/NetLogo 6.1.0"
    export NETLOGO="c:/Program Files/NetLogo 6.1.0"

The "" are required because the directory names contain spaces.

If compilation succeeds, `shell.jar` will be created.

## Credits

The shell extension was written by Eric Russell and updated to NetLogo 6.0 and then 6.1 by Charles Staelin.

## Terms of Use

[![CC0](http://i.creativecommons.org/p/zero/1.0/88x31.png)](http://creativecommons.org/publicdomain/zero/1.0/)

The NetLogo shell extension is in the public domain.  To the extent possible under law, Eric Russell has waived all copyright and related or neighboring rights.
