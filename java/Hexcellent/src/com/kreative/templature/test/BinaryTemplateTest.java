package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BinaryTemplateFactory;
import com.kreative.templature.template.BinaryTemplateInputStream;
import com.kreative.templature.template.BinaryTemplateOutputStream;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateConstants;
import com.kreative.templature.template.TemplateItem;

public class BinaryTemplateTest extends AbstractUITest implements TemplateConstants {
	public String getTitle() {
		return "Binary Template Test";
	}
	
	public TemplateItem createItem() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BinaryTemplateOutputStream tos = new BinaryTemplateOutputStream(bos);
			tos.write($UCHR, "stuff");
			tos.write($EWRD, "things");
			tos.write($ENMV, "16705 - Thing One");
			tos.write($ENMV, "Thing Two (16962)");
			tos.write($ENMV, "[17219] Red Fish");
			tos.write($ENMV, "Blue Fish = 17476");
			tos.write($ENME, "*****");
			tos.write($CONB, "things >= 17400");
			tos.write($CHAR, "blue fish");
			tos.write($CONC, "things >= 17000");
			tos.write($CHAR, "red fish");
			tos.write($COND, "*****");
			tos.write($CHAR, "thing");
			tos.write($CONE, "*****");
			tos.write($SELB, "things");
			tos.write($SELC, "17476");
			tos.write($CHAR, "blue fish");
			tos.write($SELC, "17219");
			tos.write($CHAR, "red fish");
			tos.write($SELD, "*****");
			tos.write($CHAR, "thing");
			tos.write($SELE, "*****");
			tos.write($H___|0x303130, "data");
			tos.write($S___|0x303130, "words");
			tos.write($OC__|0x3038, "items");
			tos.write($LSTC, "*****");
			tos.write($CHAR, "item");
			tos.write($LSTE, "*****");
			tos.write($HEXD, "and the rest");
			tos.flush();
			tos.close();
			
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			BinaryTemplateInputStream tis = new BinaryTemplateInputStream(bis);
			BinaryTemplateFactory f = new BinaryTemplateFactory();
			f.registerDefaults();
			f.registerExtensions();
			Template t = f.createTemplate(tis);
			tis.close();
			return t;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		new BinaryTemplateTest().main();
	}
}
