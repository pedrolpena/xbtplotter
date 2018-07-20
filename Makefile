
##############Compiler and JVM info#####################################
JC             = javac
JRT            = java
JAR            = jar
JCFLAGS        = -source 1.7 -target 1.7 -d ./
###############Paths####################################################
DESTDIR        =
DESTDIR_B4     = $(DESTDIR)/..
PREFIX         = $(DESTDIR)/usr/lib
STARTDIR       = $(DESTDIR)/usr/bin
ICONDIR        = $(DESTDIR)/usr/share/doc/$(JPACKAGE)
DESKTOPDIR     = $(DESTDIR)/usr/share/applications
MANDIR         = $(DESTDIR)/usr/share/man/man7
JAVA_LIBS      = $(DESTDIR)/usr/share/java
#Desktop file paths
DESTDIRI       = $(DESTDIR)
PREFIXI        = $(PREFIX)
STARTDIRI      = $(STARTDIR)
ICONDIRI       = $(ICONDIR)
##############Program specific info.####################################
CP             = .:lib/commons-net-3.3.jar:lib/AmverseasBinFileUtils.jar
CP1            = .:/usr/share/java/commons-net-3.3.jar:/usr/share/java/AmverseasBinFileUtils.jar
JPACKAGE       = xbtplotter
MAIN           = XBTPlotter
SOURCEDIR      = src/$(JPACKAGE)
FILENAME       = XBTPlotter.jar
SOURCEFILES    = $(SOURCEDIR)/DistanceCalculator.java \
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
                 $(SOURCEDIR)/XBTPlotter.java \
##############Desktop file fields#######################################
TITLE          = "AOML XBT Plotter"
COMMENT        = "AOML XBT Plotter"
CATEGORIES     = "Application;Science;Education"
##############control file##############################################
# Source section
SOURCE         = $(JPACKAGE)
SECTION        = "x11"
PRIORITY       = "optional"
MAINTAINER     = "Pedro Pena"
EMAIL          = "pedro.pena@noaa.gov"
STDVER         = "3.9.7"
BUILDDEPENDS   = "gzip (>=1.5), debhelper (>=9), default-jre | \
java7-runtime"

# Package section
DESCRIPTION    = "This is a platform independent program to view realtime XBT profiles.\n\
View XBT profiles transmitted from SOOP ships"
ARCH           = "all"
HOMEPAGE       = "https://github.com/pedrolpena/xbtplotter"
DEPENDS        = "\$${misc:Depends}, jarwrapper, default-jre | \
java7-runtime"
##############MISC######################################################
MAKEDEB        = 0



all: build copyres archive dist desktop

build:
	$(JC) $(JCFLAGS) -cp $(CP) $(JDP) $(SOURCEFILES)

copyres:
	cp -R $(SOURCEDIR)/resources $(JPACKAGE)

archive:
	cp manifest_2.txt manifest.txt 
	$(JAR) cfm $(FILENAME) manifest.txt $(JPACKAGE)/*.class 
	$(JAR) vfu  $(FILENAME) $(JPACKAGE)/resources

dist:
	mkdir dist
	cp $(FILENAME) dist
	rm $(FILENAME)
desktop:
	echo "[Desktop Entry]" > $(JPACKAGE).desktop
	echo "Comment="$(COMMENT) >> $(JPACKAGE).desktop
	echo "Terminal=false" >> $(JPACKAGE).desktop
	echo "Name="$(TITLE) >> $(JPACKAGE).desktop
	echo "Exec=$(STARTDIRI)/$(JPACKAGE)" >> $(JPACKAGE).desktop
	echo "Type=Application" >> $(JPACKAGE).desktop
	echo "Icon=$(ICONDIRI)/icon.png" >> $(JPACKAGE).desktop
	echo "NoDisplay=false" >> $(JPACKAGE).desktop
	echo "Categories="$(CATEGORIES) >> $(JPACKAGE).desktop
run:
	$(JRT) -cp $(CP):dist/$(FILENAME) $(JPACKAGE).$(MAIN)

install:
	mkdir -p $(PREFIX)/$(JPACKAGE)
	mkdir -p $(ICONDIR)
	mkdir -p $(STARTDIR)
	mkdir -p $(DESKTOPDIR)
	mkdir -p $(MANDIR)
	mkdir -p $(JAVA_LIBS)
	
	cp dist/$(FILENAME) $(PREFIX)/$(JPACKAGE)
	chmod +x $(PREFIX)/$(JPACKAGE)/$(FILENAME)
	echo "#!/bin/bash" > $(STARTDIR)/$(JPACKAGE)
	echo "java -cp $(CP1):$(PREFIXI)/$(JPACKAGE)/$(FILENAME) $(JPACKAGE).$(MAIN)" >> $(STARTDIR)/$(JPACKAGE)
	chmod +x $(STARTDIR)/$(JPACKAGE)
	cp icon.png $(ICONDIR)
	cp copyright $(ICONDIR)
#	cp share/doc/$(JPACKAGE)/* $(ICONDIR)
	cp $(JPACKAGE).desktop $(DESKTOPDIR)
	cp -u lib/* $(JAVA_LIBS)
	gzip -9 --no-name -c changelog > $(ICONDIR)/changelog.gz
	gzip -9 --no-name -c $(JPACKAGE).7 > $(MANDIR)/$(JPACKAGE).7.gz

ifeq ($(MAKEDEB),1)
	mkdir -p $(DESTDIR)/DEBIAN
	echo "#!/bin/bash" > $(DESTDIR)/DEBIAN/postinst
	echo "set -e" >> $(DESTDIR)/DEBIAN/postinst
	
	echo 'LIBPATH="/usr/lib/jni"' >> $(DESTDIR)/DEBIAN/postinst
	echo 'LIB="librxtxSerial.so"' >> $(DESTDIR)/DEBIAN/postinst
	
	echo 'if ! [ -f "/lib/$$LIB"  ] && [ "$$LIBPATH"/"$$LIB" ]; then' >> $(DESTDIR)/DEBIAN/postinst
	echo '    ln -s "$$LIBPATH"/"$$LIB" /lib/$$LIB'>> $(DESTDIR)/DEBIAN/postinst
	echo "fi" >> $(DESTDIR)/DEBIAN/postinst

	chmod +x $(DESTDIR)/DEBIAN/postinst
else

	echo "Not making deb package"
	
endif
	
uninstall:
	$(RM) $(STARTDIR)/$(JPACKAGE)
	$(RM) $(PREFIX)/$(JPACKAGE)/$(FILENAME)
	$(RM) $(ICONDIR)/*
	rmdir $(ICONDIR)
	rmdir $(PREFIX)/$(JPACKAGE)
	
deb:
	mkdir -p $(DESTDIR)/usr/lib
	mkdir -p $(DESTDIR)/usr/bin
	mkdir -p $(DESTDIR)/usr/share/applications
	mkdir -p $(DESTDIR)/DEBIAN
	mkdir -p $(DESTDIR_B4)/source
	
	echo "Source: "$(SOURCE) > $(DESTDIR_B4)/control
	echo "Section: "$(SECTION) >> $(DESTDIR_B4)/control
	echo "Priority: "$(PRIORITY) >> $(DESTDIR_B4)/control
	echo "Maintainer: "$(MAINTAINER)" <"$(EMAIL)">" >> $(DESTDIR_B4)/control
	echo "Standards-Version: "$(STDVER) >> $(DESTDIR_B4)/control
	echo "Build-Depends: "$(BUILDDEPENDS) >> $(DESTDIR_B4)/control
	echo "" >> $(DESTDIR_B4)/control
	
	echo "Package: "$(JPACKAGE) >> $(DESTDIR_B4)/control
	echo "Description: "$(DESCRIPTION) >> $(DESTDIR_B4)/control
	echo "Architecture: "$(ARCH) >> $(DESTDIR_B4)/control
	echo "Homepage: "$(HOMEPAGE) >> $(DESTDIR_B4)/control
	echo "Depends: "$(DEPENDS) >> $(DESTDIR_B4)/control
	
	echo "#!/usr/bin/make -f"  > $(DESTDIR_B4)/rules
	echo "%:" >> $(DESTDIR_B4)/rules
	echo "	dh \$$@"  >> $(DESTDIR_B4)/rules
	echo ""  >> $(DESTDIR_B4)/rules
	echo "binary:"  >> $(DESTDIR_B4)/rules
	echo "	make install DESTDIR=$(DESTDIR) DESTDIRI=/usr PREFIXI=/usr/lib STARTDIRI=/usr/bin MAKEDEB=1"  >> $(DESTDIR_B4)/rules
	echo "	dh_gencontrol"  >> $(DESTDIR_B4)/rules
	echo "	dh_builddeb"  >> $(DESTDIR_B4)/rules
	chmod +x $(DESTDIR_B4)/rules
	
	echo "1.0" > $(DESTDIR_B4)/source/format
	
	
	echo "9" > $(DESTDIR_B4)/compat

	
	cp changelog $(DESTDIR_B4)/changelog
	cp copyright $(DESTDIR_B4)/copyright
	cp LICENSE $(DESTDIR_B4)/LICENSE
	cp license.txt $(DESTDIR_B4)

	
	
clean:
	$(RM) -r ./$(JPACKAGE)
	$(RM) -r ./dist
	$(RM) $(JPACKAGE).desktop
	$(RM) ./manifest.txt
