package com.kreative.templature.test;

import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateConstants;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.TextTemplateFactory;
import com.kreative.templature.template.TextTemplateTokenStream;

public class TextTemplateTest extends AbstractUITest implements TemplateConstants {
	public String getTitle() {
		return "Text Template Test";
	}
	
	public TemplateItem createItem() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("  H 'Text Template Test';");
		sb.append("  P 'This is a test of the text-based template compiler.';");
		sb.append("  hr `-----`;");
		sb.append("  QDRect rect;");
		sb.append("  hr;");
		sb.append("  rem 'This is a comment.';");
		sb.append("  comment 'This is a comment.';");
		sb.append("  meta 'This is nothing.';");
		sb.append("  charset 'ISO-8859-1';");
		sb.append("  endian little;");
		sb.append("  endianness big;");
		sb.append("  newline crlf;");
		
		sb.append("  uchar stuff;");
		sb.append("  enum16 things {");
		sb.append("    `16705 - Thing One`,");
		sb.append("    `Thing Two (16962)`,");
		sb.append("    `[17219] Red Fish`,");
		sb.append("    `Blue Fish = 17476`,");
		sb.append("  } `*****`;");
		sb.append("  if (things >= 17400) {");
		sb.append("    char `blue fish`;");
		sb.append("  } elif (things >= 17000) {");
		sb.append("    char `red fish`;");
		sb.append("  } else `*****` {");
		sb.append("    char thing;");
		sb.append("  } `*****`;");
		sb.append("  switch (things) {");
		sb.append("    case 17476: char `blue fish`;");
		sb.append("    case 17219: char `red fish`;");
		sb.append("    default `*****`: char thing;");
		sb.append("  } `*****`;");
		sb.append("  hex16 data;");
		sb.append("  sstring16 words;");
		sb.append("  ocount8 items;");
		sb.append("  listc `*****` {");
		sb.append("    char item;");
		sb.append("  } `*****`;");
//		sb.append("  listz `*****` {");
//		sb.append("    char item;");
//		sb.append("  } `*****`;");
//		sb.append("  list `*****` {");
//		sb.append("    char item;");
//		sb.append("  } `*****`;");
		sb.append("  hexdump `and the rest`;");
		
//		sb.append("  int32 a;");
//		sb.append("  hshort b;");
//		sb.append("  obyte c;");
//		sb.append("  bit z;");
//		sb.append("  bbit y;");
//		sb.append("  boolean1 x;");
//		sb.append("  fbool1 w;");
//		sb.append("  bool4 v;");
//		sb.append("  uint64 d;");
//		sb.append("  fixed32 e;");
//		sb.append("  double f;");
		
//		sb.append("  vlfpbe g;");
//		sb.append("  symbol s;");
		
		TextTemplateTokenStream ts = new TextTemplateTokenStream(sb.toString());
		TextTemplateFactory f = new TextTemplateFactory();
		f.registerDefaults();
		f.registerExtensions();
		Template t = f.createTemplate(ts);
		ts.close();
		return t;
	}
	
	public static void main(String[] args) {
		new TextTemplateTest().main();
	}
}
