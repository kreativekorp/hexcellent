package com.kreative.templature.test;

import java.util.Arrays;
import java.util.HashMap;
import com.kreative.templature.template.Expression;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.EnumItem;
import com.kreative.templature.template.item.IntegerItem;
import com.kreative.templature.template.item.SwitchBlock;

public class SwitchTest extends AbstractUITest {
	public String getTitle() {
		return "Switch Test";
	}
	
	public TemplateItem createItem() {
		Template t1 = new Template();
		t1.add(new IntegerItem(0, "", "The Byte", false, 8, true, 10));

		Template t2 = new Template();
		t2.add(new IntegerItem(0, "", "The Short", false, 16, true, 10));
		
		Template t3 = new Template();
		t3.add(new IntegerItem(0, "", "The Int", false, 32, true, 10));
		
		Template t4 = new Template();
		t4.add(new IntegerItem(0, "", "The Long", false, 64, true, 10));
		
		HashMap<Integer,String> types = new HashMap<Integer,String>();
		types.put(1, "Byte");
		types.put(2, "Short");
		types.put(3, "Int");
		types.put(4, "Long");
		
		Template t5 = new Template();
		t5.add(new EnumItem(0, 0, 0, "", "The Type", "", false, 8, types));
		t5.add(new SwitchBlock(
			0, Arrays.asList(0,0,0,0), 0, "",
			Arrays.asList("","","",""), "", "",
			Arrays.asList("","","",""),
			Arrays.asList("","","",""), "",
			Expression.compile("`The Type`"),
			Arrays.asList(
				Expression.compile("1"),
				Expression.compile("2"),
				Expression.compile("3"),
				Expression.compile("4")
			),
			Arrays.asList(t1, t2, t3, t4),
			4
		));
		
		return t5;
	}
	
	public static void main(String[] args) {
		new SwitchTest().main();
	}
}
