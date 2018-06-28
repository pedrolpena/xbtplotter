JC          = javac
JRT         = java
JAR         = jar
JCFLAGS     = -source 1.7 -target 1.7 -d ./
JFLAGS      = -jar
CP          = .:./lib/commons-net-3.3.jar:./lib/AmverseasBinFileUtils.jar
PACKAGE     = xbtplotter
SOURCEDIR   = src/$(PACKAGE)
FILENAME    = XBTPlotter.jar
INSTALLDIR  = /usr/local
STARTDIR    = /usr/local/sbin
DESKTOPDIR  = $(HOME)/.local/share/applications

SOURCEFILES = $(SOURCEDIR)/DistanceCalculator.java \
              $(SOURCEDIR)/DTPair.java \
              $(SOURCEDIR)/Profile.java \
              $(SOURCEDIR)/ProfileResult.java \
              $(SOURCEDIR)/SeasBINFileThird.java \
              $(SOURCEDIR)/SeasProfileBuilder.java \
              $(SOURCEDIR)/SeasXBTProfile.java \
              $(SOURCEDIR)/SoopDataFTP.java \
              $(SOURCEDIR)/TemperatureMeasurement.java \
              $(SOURCEDIR)/TemperatureProfile.java \
              $(SOURCEDIR)/TransmittedProfile.java \
              $(SOURCEDIR)/WMO1770.java \
              $(SOURCEDIR)/WMO4770.java \
              $(SOURCEDIR)/XBTBinField.java \
              $(SOURCEDIR)/XBTBinFieldList.java \
              $(SOURCEDIR)/XBTDepthCalculator.java \
              $(SOURCEDIR)/XBT.java \
              $(SOURCEDIR)/XBTPlotter.java



all: build copyres archive dist desktop

build:
	$(JC) $(JCFLAGS) -cp $(CP) $(JDP) $(SOURCEFILES)

copyres:
	cp -R $(SOURCEDIR)/resources $(PACKAGE)

archive:
	$(JAR) cfm $(FILENAME) manifest.txt $(PACKAGE)/*.class 
	$(JAR) vfu  $(FILENAME) $(PACKAGE)/resources

dist:
	mkdir -p dist/lib
	mv  $(FILENAME) dist
	cp ./lib/* ./dist/lib

desktop:
	echo "[Desktop Entry]" > $(PACKAGE).desktop
	echo "Comment=IES Telemetry Application" >> $(PACKAGE).desktop
	echo "Terminal=false" >> $(PACKAGE).desktop
	echo "Name=IES Telemetry Application" >> $(PACKAGE).desktop
	echo "Exec=$(STARTDIR)/$(PACKAGE)" >> $(PACKAGE).desktop
	echo "Type=Application" >> $(PACKAGE).desktop
	echo "Icon=$(INSTALLDIR)/$(PACKAGE)/icon.png" >> $(PACKAGE).desktop
	echo "NoDisplay=false" >> $(PACKAGE).desktop
	echo "Categories=science" >> $(PACKAGE).desktop
run:
	$(JRT) $(JFLAGS) dist/$(FILENAME)

install:
	mkdir -p $(INSTALLDIR)/$(PACKAGE)/lib
	cp dist/$(FILENAME) $(INSTALLDIR)/$(PACKAGE)
	cp dist/lib/* $(INSTALLDIR)/$(PACKAGE)/lib
	echo "#!/bin/bash" > $(STARTDIR)/$(PACKAGE)
	echo "java -jar $(INSTALLDIR)/$(PACKAGE)/$(FILENAME)" >> $(STARTDIR)/$(PACKAGE)
	chmod +x $(STARTDIR)/$(PACKAGE)
	cp icon.png $(INSTALLDIR)/$(PACKAGE)
	cp $(PACKAGE).desktop $(DESKTOPDIR)
	chown $(SUDO_USER):$(SUDO_USER) $(DESKTOPDIR)/$(PACKAGE).desktop
	
uninstall:
	$(RM) $(STARTDIR)/$(PACKAGE)
	$(RM) $(INSTALLDIR)/$(PACKAGE)/lib/*
	rmdir $(INSTALLDIR)/$(PACKAGE)/lib
	$(RM) $(INSTALLDIR)/$(PACKAGE)/$(FILENAME)
	$(RM) $(INSTALLDIR)/$(PACKAGE)/icon.png
	rmdir $(INSTALLDIR)/$(PACKAGE)
	rm -rf $(HOME)/bindownloads
clean:
	$(RM) -r ./$(PACKAGE)
	$(RM) -r ./dist
	$(RM) $(PACKAGE).desktop
