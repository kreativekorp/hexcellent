package com.kreative.templature.test;

import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.Point2DItem;

public class Point2DEditorTest extends AbstractUITest {
	private int[] xyShift = { 0, 16 };
	private int[] xyWidth = { 16, 16 };
	private int width = 32;
	
	public String getTitle() {
		return "Point2D Editor Test";
	}
	
	public TemplateItem createItem() {
		return new Point2DItem(
			0, "", "", false, width,
			xyShift[0], xyWidth[0],
			xyShift[1], xyWidth[1]
		);
	}
	
	public static void main(String[] args) {
		new Point2DEditorTest().main();
	}
}
