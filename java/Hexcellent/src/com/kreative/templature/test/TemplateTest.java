package com.kreative.templature.test;

import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.IntegerItem;
import com.kreative.templature.template.item.PascalStringItem;

public class TemplateTest extends AbstractUITest {
	public String getTitle() {
		return "Template Test";
	}
	
	public TemplateItem createItem() {
		Template t = new Template();
		t.add(new IntegerItem(0, "", "A number", false, 32, true, 10));
		t.add(new IntegerItem(0, "", "Another number goes here", false, 32, true, 10));
		t.add(new PascalStringItem(0, "", "A string", false, 8, PascalStringItem.LENGTH_VARIABLE, "MacRoman", "\r"));
		return t;
	}
	
	public static void main(String[] args) {
		new TemplateTest().main();
	}
}
