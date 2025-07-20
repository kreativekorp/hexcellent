package com.kreative.templature.test;

import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.ColorItem;

public class ColorEditorTest extends AbstractUITest {
	private int[] rgbaShift = { 16, 8, 0, 24 };
	private int[] rgbaWidth = { 8, 8, 8, 8 };
	private int width = 32;
	
	public String getTitle() {
		return "Color Editor Test";
	}
	
	public TemplateItem createItem() {
		return new ColorItem(
			0, "", "", false, width,
			rgbaShift[0], rgbaWidth[0],
			rgbaShift[1], rgbaWidth[1],
			rgbaShift[2], rgbaWidth[2],
			rgbaShift[3], rgbaWidth[3]
		);
	}
	
	public static void main(String[] args) {
		new ColorEditorTest().main();
	}
}
