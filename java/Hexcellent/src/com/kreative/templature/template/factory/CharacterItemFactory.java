package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.CharacterItem;

public class CharacterItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$CHAR|TI|00, $RAHC|TI|00|LE,
			$WCHR|TI|00, $RHCW|TI|00|LE,
			$TNAM|TI|00, $MANT|TI|00|LE,
			$SYMB|TI|00, $BMYS|TI|00|LE,
			$UCHR|TI|00, $RHCU|TI|00|LE,
			$LCHR|TI|00, $RHCL|TI|00|LE,
			$CH__|TI|DM, $__HC|TI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int width; String encoding = in.getEncoding(); String padding = " ";
		switch (mtc) {
			case $CHAR: typeString = "charbe"; le = false; width = 8; break;
			case $RAHC: typeString = "charle"; le = true; width = 8; break;
			case $WCHR: typeString = "wcharbe"; le = false; width = 16; break;
			case $RHCW: typeString = "wcharle"; le = true; width = 16; break;
			case $TNAM: typeString = "tnamebe"; le = false; width = 32; break;
			case $MANT: typeString = "tnamele"; le = true; width = 32; break;
			case $SYMB: typeString = "symbolbe"; le = false; width = 64; break;
			case $BMYS: typeString = "symbolle"; le = true; width = 64; break;
			case $UCHR: typeString = "ucharbe"; le = false; width = 16; encoding = "UTF-16BE"; break;
			case $RHCU: typeString = "ucharle"; le = true; width = 16; encoding = "UTF-16BE"; break;
			case $LCHR: typeString = "lcharbe"; le = false; width = 32; encoding = "UTF-32BE"; break;
			case $RHCL: typeString = "lcharle"; le = true; width = 32; encoding = "UTF-32BE"; break;
			case $CH__: typeString = "char" + mv + "be"; le = false; width = mv; break;
			case $__HC: typeString = "char" + mv + "le"; le = true; width = mv; break;
			default: return null;
		}
		return new CharacterItem(it.type, typeString, it.label, le, width, encoding, padding);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"char&", "wchar&", "uchar&", "lchar&", "char#&", "symb#&", "symbol#&",
			"fcc&", "tname&", "typename&", "OSType&", "ecc&", "symb&", "symbol&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"char&", "wchar&", "uchar&", "lchar&", "char#&", "symb#&", "symbol#&",
		"fcc&", "tname&", "typename&", "OSType&", "ecc&", "symb&", "symbol&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; String encoding = state.getEncoding(); String padding = " ";
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $RAHC : $CHAR); mv = 8; break;
			case 1: tc = (le ? $RHCW : $WCHR); mv = 16; break;
			case 2: tc = (le ? $RHCU : $UCHR); mv = 16; encoding = "UTF-16BE"; break;
			case 3: tc = (le ? $RHCL : $LCHR); mv = 32; encoding = "UTF-32BE"; break;
			case 4: case 5: case 6: tc = TemplateUtils.maskDecValue($__HC|$CH__, mv, le); break;
			case 7: case 8: case 9: case 10: tc = (le ? $MANT : $TNAM); mv = 32; break;
			case 11: case 12: case 13: tc = (le ? $BMYS : $SYMB); mv = 64; break;
			default: return null;
		}
		return new CharacterItem(tc, ts, name, le, mv, encoding, padding);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
}
