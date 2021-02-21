SRCFILES := $(shell find src -name \*.java)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | sort | uniq | tr / .)

all: clean bin doc hexcellent.jar

eclipseall: eclipseclean hexcellent.jar

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar

eclipseclean:
	rm -rf *.jar

bin: src
	mkdir -p bin
	javac -sourcepath src $(SRCFILES) -d bin

doc: src
	mkdir -p doc
	javadoc -sourcepath src $(PACKAGES) -d doc

hexcellent.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF hexcellent.jar     \
		-C bin com/kreative/hexcellent/buffer  \
		-C bin com/kreative/hexcellent/editor  \
		-C bin com/kreative/hexcellent/main
	chmod +x hexcellent.jar

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
