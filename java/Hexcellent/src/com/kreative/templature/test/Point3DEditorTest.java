package com.kreative.templature.test;

import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.item.Point3DItem;

public class Point3DEditorTest extends AbstractUITest {
	private int[] xyzShift = { 0, 16, 32 };
	private int[] xyzWidth = { 16, 16, 16 };
	private int width = 48;
	
	public String getTitle() {
		return "Point3D Editor Test";
	}
	
	public TemplateItem createItem() {
		return new Point3DItem(
			0, "", "", false, width,
			xyzShift[0], xyzWidth[0],
			xyzShift[1], xyzWidth[1],
			xyzShift[2], xyzWidth[2]
		);
	}
	
	public static void main(String[] args) {
		new Point3DEditorTest().main();
	}
}
