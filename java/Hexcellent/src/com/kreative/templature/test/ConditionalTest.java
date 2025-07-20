package com.kreative.templature.test;

import java.util.Arrays;
import com.kreative.templature.template.Expression;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.ConditionalBlock;
import com.kreative.templature.template.item.IntegerItem;
import com.kreative.templature.template.item.ParagraphItem;

public class ConditionalTest extends AbstractUITest {
	public String getTitle() {
		return "Conditional Test";
	}
	
	public TemplateItem createItem() {
		Template t1 = new Template();
		t1.add(new ParagraphItem(0, "", "This will take 1 byte in UTF-8."));
		
		Template t2 = new Template();
		t2.add(new ParagraphItem(0, "", "This will take 2 bytes in UTF-8."));
		
		Template t3 = new Template();
		t3.add(new ParagraphItem(0, "", "This will take 3 bytes in UTF-8."));
		
		Template t4 = new Template();
		t4.add(new ParagraphItem(0, "", "This will take 4 bytes in UTF-8."));
		
		Template t5 = new Template();
		t5.add(new ParagraphItem(0, "", "This is not a valid code point."));
		
		Template t6 = new Template();
		t6.add(new IntegerItem(0, "", "Code Point", false, 32, false, 16));
		t6.add(new ConditionalBlock(
			Arrays.asList(0,0,0,0,0), 0,
			Arrays.asList("","","","",""),
			Arrays.asList("","","","",""),
			Arrays.asList("","","","",""), "",
			Arrays.asList(
				Expression.compile("`Code Point` >= 0 && `Code Point` < 0x80"),
				Expression.compile("`Code Point` >= 0x80 && `Code Point` < 0x800"),
				Expression.compile("`Code Point` >= 0x800 && `Code Point` < 0x10000"),
				Expression.compile("`Code Point` >= 0x10000 && `Code Point` < 0x110000"),
				null
			),
			Arrays.asList(t1, t2, t3, t4, t5), 5
		));
		
		return t6;
	}
	
	public static void main(String[] args) {
		new ConditionalTest().main();
	}
}
