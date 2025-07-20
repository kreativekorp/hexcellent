package com.kreative.templature.template.factory;

import com.kreative.templature.template.*;
import com.kreative.templature.template.item.FixedItem;
import com.kreative.templature.template.item.PackedFixedItem;

public class FixedItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$FX__|TI|PI|DM,
			$__XF|TI|PI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $FX__: return new FixedItem(it.type, "fixed" + mv + "be", it.label, false, mv, true, (mv + 1) / 2);
			case $__XF: return new FixedItem(it.type, "fixed" + mv + "le", it.label, true, mv, true, (mv + 1) / 2);
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $FX__: return new PackedFixedItem(it.type, "fixed" + mv + "be", it.label, mv, true, (mv + 1) / 2);
			case $__XF: return new PackedFixedItem(it.type, "fixed" + mv + "le", it.label, mv, true, (mv + 1) / 2);
			default: return null;
		}
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] { "fixed#&" };
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] { "fixed#&" };
	}
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc = TemplateUtils.maskDecValue($__XF|$FX__, mv, le);
		return new FixedItem(tc, ts, name, le, mv, true, (mv + 1) / 2);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc = TemplateUtils.maskDecValue($__XF|$FX__, mv, le);
		return new PackedFixedItem(tc, ts, name, mv, true, (mv + 1) / 2);
	}
}
