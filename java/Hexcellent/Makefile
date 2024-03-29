SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

all: clean bin doc hexcellent.jar Hexcellent.app

eclipseall: eclipseclean hexcellent.jar Hexcellent.app

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar
	rm -rf *.app

eclipseclean:
	rm -rf *.jar
	rm -rf *.app

bin: src
	mkdir -p bin
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -classpath dep/ual.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -classpath dep/ual.jar -sourcepath src $(PACKAGES) -d doc

hexcellent.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF hexcellent.jar     \
		-C bin com/kreative/hexcellent/buffer  \
		-C bin com/kreative/hexcellent/editor  \
		-C bin com/kreative/hexcellent/main
	chmod +x hexcellent.jar

Hexcellent.app: Hexcellent-Pre10.15.app Hexcellent-MacOS10.15.app Hexcellent-MacOS11.0.app

Hexcellent-Pre10.15.app: dep hexcellent.jar
	mkdir -p Hexcellent-Pre10.15.app/Contents/MacOS
	mkdir -p Hexcellent-Pre10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Hexcellent-Pre10.15.app/Contents
	cp -f dep/Info.plist Hexcellent-Pre10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-Pre10.15 Hexcellent-Pre10.15.app/Contents/MacOS/Hexcellent
	cp -f dep/Hexcellent.icns Hexcellent-Pre10.15.app/Contents/Resources
	cp -f dep/*.jar Hexcellent-Pre10.15.app/Contents/Resources/Java
	cp -f hexcellent.jar Hexcellent-Pre10.15.app/Contents/Resources/Java

Hexcellent-MacOS10.15.app: dep hexcellent.jar
	mkdir -p Hexcellent-MacOS10.15.app/Contents/MacOS
	mkdir -p Hexcellent-MacOS10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Hexcellent-MacOS10.15.app/Contents
	cp -f dep/Info.plist Hexcellent-MacOS10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS10.15 Hexcellent-MacOS10.15.app/Contents/MacOS/Hexcellent
	cp -f dep/Hexcellent.icns Hexcellent-MacOS10.15.app/Contents/Resources
	cp -f dep/*.jar Hexcellent-MacOS10.15.app/Contents/Resources/Java
	cp -f hexcellent.jar Hexcellent-MacOS10.15.app/Contents/Resources/Java

Hexcellent-MacOS11.0.app: dep hexcellent.jar
	mkdir -p Hexcellent-MacOS11.0.app/Contents/MacOS
	mkdir -p Hexcellent-MacOS11.0.app/Contents/Resources/Java
	cp -f dep/PkgInfo Hexcellent-MacOS11.0.app/Contents
	cp -f dep/Info.plist Hexcellent-MacOS11.0.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS11.0 Hexcellent-MacOS11.0.app/Contents/MacOS/Hexcellent
	cp -f dep/Hexcellent.icns Hexcellent-MacOS11.0.app/Contents/Resources
	cp -f dep/*.jar Hexcellent-MacOS11.0.app/Contents/Resources/Java
	cp -f hexcellent.jar Hexcellent-MacOS11.0.app/Contents/Resources/Java

install: dep hexcellent.jar
	sudo mkdir -p /usr/local/lib/
	sudo cp -f hexcellent.jar /usr/local/lib/hexcellent.jar
	sudo chmod -f 644 /usr/local/lib/hexcellent.jar
	sudo mkdir -p /usr/local/bin/
	sudo cp -f dep/hexcellent /usr/local/bin/hexcellent
	sudo chmod -f 755 /usr/local/bin/hexcellent
	sudo mkdir -p /usr/share/icons/hicolor/128x128/apps/
	sudo cp -f dep/hexcellent.png /usr/share/icons/hicolor/128x128/apps/hexcellent.png
	sudo chmod -f 644 /usr/share/icons/hicolor/128x128/apps/hexcellent.png
	sudo mkdir -p /usr/share/applications/
	sudo cp -f dep/hexcellent.desktop /usr/share/applications/hexcellent.desktop
	sudo chmod -f 644 /usr/share/applications/hexcellent.desktop

.PHONY: all eclipseall clean eclipseclean Hexcellent.app install
