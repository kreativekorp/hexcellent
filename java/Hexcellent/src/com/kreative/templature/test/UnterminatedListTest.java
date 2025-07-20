package com.kreative.templature.test;

import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.CharacterItem;
import com.kreative.templature.template.item.UnterminatedListBlock;

public class UnterminatedListTest extends AbstractUITest {
	public String getTitle() {
		return "LSTB Test";
	}
	
	public TemplateItem createItem() {
		Template t1 = new Template();
		t1.add(new CharacterItem(0, "", "The Char", false, 8, "MacRoman", " "));
		
		Template t2 = new Template();
		t2.add(new UnterminatedListBlock(0, 0, "", "#####", "*****", t1));
		
		return t2;
	}
	
	public static void main(String[] args) {
		new UnterminatedListTest().main();
	}
}
