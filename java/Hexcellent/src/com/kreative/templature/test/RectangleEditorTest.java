package com.kreative.templature.test;

import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.RectangleItem;

public class RectangleEditorTest extends AbstractUITest {
	private int[] ltrbShift = { 32, 48, 0, 16 };
	private int[] ltrbWidth = { 16, 16, 16, 16 };
	private int width = 64;
	
	public String getTitle() {
		return "Rectangle Editor Test";
	}
	
	public TemplateItem createItem() {
		return new RectangleItem(
			0, "", "", false, width,
			ltrbShift[0], ltrbWidth[0],
			ltrbShift[1], ltrbWidth[1],
			ltrbShift[2], ltrbWidth[2],
			ltrbShift[3], ltrbWidth[3]
		);
	}
	
	public static void main(String[] args) {
		new RectangleEditorTest().main();
	}
}
