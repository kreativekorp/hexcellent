package com.kreative.templature.test;

import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.DateTimeItem;

public class DateTimeEditorTest extends AbstractUITest {
	public String getTitle() {
		return "Date Time Editor Test";
	}
	
	public TemplateItem createItem() {
		return new DateTimeItem(
			0, "", "", false, 32, false,
			DateTimeItem.MAC_OS_EPOCH,
			DateTimeItem.UNIT_SECONDS
		);
	}
	
	public static void main(String[] args) {
		new DateTimeEditorTest().main();
	}
}
