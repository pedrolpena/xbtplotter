echo off

javac -source 1.7 -target 1.7 -d .\ -cp .\lib\commons-net-3.3.jar;.\lib\AmverseasBinFileUtils.jar .\src\xbtplotter\*.java   
copy manifest_1.txt manifest.txt
jar cfm XBTPlotter.jar manifest.txt xbtplotter\*.class

IF EXIST .\dist goto deletedist

:deletedist
del /q /s .\dist  > nul
rmdir /q /s .\dist  > nul
:exit

mkdir .\dist
mkdir .\dist\lib
move /y XBTPlotter.jar .\dist > nul
copy /y .\lib .\dist\lib > nul
del /s /q .\xbtplotter  > nul
rmdir /s /q .\xbtplotter  > nul


