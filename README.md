# xbtplotter
-----------------------------------------
**COMPILING AND RUNNING THE FTP PROGRAM**
-----------------------------------------

If you have apache Ant ("Another Neat Tool") installed then it's very easy.
Just open a command prompt/terminal ,navigate to the xbtplotter folder 
and type ant.

After several seconds the program should be available in the dist folder.
If you don't have ant or you don't wish to install it, then keep reading.

To compile, make sure that a java sdk version of at least 1.7 is installed
and that the jar archive tool is installed. Make sure that the location
of the java sdk is in the path. Most of the time you should be able to just 
double click on makeit.bat when compiling under Windows. If this doesn't work  
then you will have to add the SDK location to the PATH environment variable.

You can check by typing "javac -version"
you should see something like "javac 1.7.0_65"

Type "jar" and your screen should scroll with many jar options.

When you list the contents of the directory, you should see the following.

lib             - directory containing libraries
                  This directory contains the AmverseasBinFileUtils.jar & commons-net-3.3.jar
                  libraries for convenience but may not be the latest versions. Get the latest
                  version of "AmverseasBinFileUtils.jar" by cloning the repo and compiling.
                  you could do it from the command line
                  "git clone https://github.com/pedrolpena/xbtplotter.git"
                  or you could download the zip file from 
                  "https://github.com/pedrolpena/xbtplotter/archive/master.zip"
                  
makeit.bat      - batch script to compile and archive the program 
makeit.sh       - bash  script to compile and archive the program 
manifest.txt    - info that will be added to the manifest in the resulting jar file
README.md      - this document
src             - directory with source files.


In Windows run makeit.bat in linux/unix run makeit.sh .
In linux/unix you will have to make makeit.sh executable.
To make it executable type

"chmod +x makeit.sh"

When done compiling, the XBTPLotter program will be placed in the dist directory.
To run the program enter the dist directory and type.

"java -jar XBTPlotter.jar"

If for some reason the script doesn't work you can compile and archive with the following commands.

For linux/unix
"javac -source 1.7 -target 1.7 -d ./ -cp ./lib/commons-net-3.3.jar:./lib/AmverseasBinFileUtils.jar src/xbtplotter/*.java"
"jar cfm XBTPlotter.jar manifest.txt xbtplotter/*.class"


For Windows
"javac -source 1.7 -target 1.7 -d .\ -cp .\lib\commons-net-3.3.jar;.\lib\AmverseasBinFileUtils.jar .\src\xbtplotter\*.java"
"jar cfm XBTPlotter.jar manifest.txt xbtplotter\*.class"

Keep the XBTPlotter.jar file and the lib folder together.
You can create a directory like "dist" and copy XBTPlotter.jar and the lib folder into it.

