# If this Makefile is not being run from the NetLogo-6.x.x/app/extensions/shell
# directory, NETLOGO must be set before the make to the location of the NetLogo
# package.  E.g., in Cygwin this would be something like 
# export NETLOGO = "c:/Program Files/NetLogo 6.1.0"

ifeq ($(origin JAVA_HOME), undefined)
	JAVA_HOME = /usr
endif

ifeq ($(origin NETLOGO), undefined)
	NETLOGO = ../../..
endif

# NetLogo.jar files are now modefied by the NetLogo version number,
# e.g., netlogo-6.0.1.jar. Thus we shell to the "find" command which 
# looks in the app forlder to get the .jar file name. I tried to 
# accomplish this with the wildcard function, but it doesn't seem to 
# handle cases where there are blanks in file/direcotry names, as is 
# common in Windows.
NETLOGO_JAR := "$(shell find "$(NETLOGO)"/app -name netlogo-*.jar)"
#NETLOGO_JAR := $(wildcard $(NETLOGO)/app/netlogo-*.jar)
JAVAC := "$(JAVA_HOME)/bin/javac"
SRCS := $(wildcard src/*.java)

shell.zip: shell.jar README.md Makefile src ShellTest manifest.txt
	-rm -rf shell
	mkdir shell
	cp -rp shell.jar README.md Makefile manifest.txt src ShellTest shell
	zip -rv shell.zip shell
	rm -rf shell

shell.jar: $(SRCS) manifest.txt Makefile
	-rm -rf classes
	mkdir classes
	$(JAVAC) -g -deprecation -Xlint:all -Xlint:-serial -Xlint:-path -encoding us-ascii -source 1.8 -target 1.8 -classpath $(NETLOGO_JAR) -d classes $(SRCS)
	jar cmf manifest.txt shell.jar -C classes .
	-rm -rf classes
