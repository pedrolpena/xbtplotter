#!/bin/bash
javac -source 1.7 -target 1.7 -d ./ -cp ./lib/commons-net-3.3.jar:./lib/AmverseasBinFileUtils.jar src/xbtplotter/*.java 
jar cfm XBTPlotter.jar manifest.txt xbtplotter/*.class 
if [ -d "dist" ]; then
    rm -r dist
fi
mkdir ./dist
rm -r ./xbtplotter
mv ./XBTPlotter.jar ./dist
cp -r ./lib ./dist


