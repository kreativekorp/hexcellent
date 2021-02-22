SRCFILES := $(shell find src -name \*.java)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | sort | uniq | tr / .)

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
	javac -classpath dep/orange-extensions.jar -sourcepath src $(SRCFILES) -d bin

doc: src
	mkdir -p doc
	javadoc -classpath dep/orange-extensions.jar -sourcepath src $(PACKAGES) -d doc

hexcellent.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF hexcellent.jar     \
		-C bin com/kreative/hexcellent/buffer  \
		-C bin com/kreative/hexcellent/editor  \
		-C bin com/kreative/hexcellent/main
	chmod +x hexcellent.jar

Hexcellent.app: dep hexcellent.jar
	mkdir -p Hexcellent.app/Contents/MacOS
	mkdir -p Hexcellent.app/Contents/Resources/Java
	cp -f dep/PkgInfo Hexcellent.app/Contents
	cp -f dep/Info.plist Hexcellent.app/Contents
	cp -f dep/universalJavaApplicationStub Hexcellent.app/Contents/MacOS/Hexcellent
	cp -f dep/Hexcellent.icns Hexcellent.app/Contents/Resources
	cp -f hexcellent.jar Hexcellent.app/Contents/Resources/Java

install: hexcellent.jar
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

.PHONY: all eclipseall clean eclipseclean install
