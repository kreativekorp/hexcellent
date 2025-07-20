package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.Point2DItem;
import com.kreative.templature.template.item.Point3DItem;
import com.kreative.templature.template.item.RectangleItem;

public class PointItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$PT2D|TI, $D2TP|TI|LE,
			$PT3D|TI, $D3TP|TI|LE,
			$RECT|TI, $TCER|TI|LE,
			$2DPT|TI, $TPD2|TI|LE,
			$3DPT|TI, $TPD3|TI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $PT2D: case $2DPT: return new Point2DItem(it.type, "point2Dbe", it.label, false, 32, 0, 16, 16, 16);
			case $D2TP: case $TPD2: return new Point2DItem(it.type, "point2Dle", it.label, true, 32, 0, 16, 16, 16);
			case $PT3D: case $3DPT: return new Point3DItem(it.type, "point3Dbe", it.label, false, 48, 0, 16, 16, 16, 32, 16);
			case $D3TP: case $TPD3: return new Point3DItem(it.type, "point3Dle", it.label, true, 48, 0, 16, 16, 16, 32, 16);
			case $RECT: return new RectangleItem(it.type, "rectbe", it.label, false, 64, 32, 16, 48, 16, 0, 16, 16, 16);
			case $TCER: return new RectangleItem(it.type, "rectle", it.label, true, 64, 32, 16, 48, 16, 0, 16, 16, 16);
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"point&", "point2D&", "QDPoint&", "point3D&", "rect&", "rectangle&", "QDRect&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"point&", "point2D&", "QDPoint&", "point3D&", "rect&", "rectangle&", "QDRect&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		switch (MTS.indexOf(mts)) {
			case 0: case 1: case 2: return new Point2DItem((le ? $D2TP : $PT2D), ts, name, le, 32, 0, 16, 16, 16);
			case 3: return new Point3DItem((le ? $D3TP : $PT3D), ts, name, le, 48, 0, 16, 16, 16, 32, 16);
			case 4: case 5: case 6: return new RectangleItem((le ? $TCER : $RECT), ts, name, le, 64, 32, 16, 48, 16, 0, 16, 16, 16);
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
}
