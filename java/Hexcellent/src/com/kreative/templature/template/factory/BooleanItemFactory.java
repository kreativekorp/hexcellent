package com.kreative.templature.template.factory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.BooleanItem;
import com.kreative.templature.template.item.PackedBooleanItem;

public class BooleanItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	private static final BigInteger ZERO = BigInteger.ZERO;
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger B256 = BigInteger.valueOf(256);
	private static final BigInteger N1 = BigInteger.valueOf(-1);
	
	public long[] getTypeConstants() {
		return new long[] {
			$BBIT|00|PI|00, $TIBB|00|PI|00|LE,
			$BOOL|TI|PI|00, $LOOB|TI|PI|00|LE,
			$BL__|TI|PI|DM, $__LB|TI|PI|DM|LE,
			$BN__|TI|PI|DM, $__NB|TI|PI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int width; BigInteger fv; BigInteger tv;
		switch (mtc) {
			case $BBIT: typeString = "bbitbe"; le = false; width = 1; fv = ZERO; tv = ONE; break;
			case $TIBB: typeString = "bbitle"; le = true; width = 1; fv = ZERO; tv = ONE; break;
			case $BOOL: typeString = "boolbe"; le = false; width = 16; fv = ZERO; tv = B256; break;
			case $LOOB: typeString = "boolle"; le = true; width = 16; fv = ZERO; tv = ONE; break;
			case $BL__: typeString = "bool" + mv + "be"; le = false; width = mv; fv = ZERO; tv = ONE; break;
			case $__LB: typeString = "bool" + mv + "le"; le = true; width = mv; fv = ZERO; tv = ONE; break;
			case $BN__: typeString = "fbool" + mv + "be"; le = false; width = mv; fv = ZERO; tv = N1; break;
			case $__NB: typeString = "fbool" + mv + "le"; le = true; width = mv; fv = ZERO; tv = N1; break;
			default: return null;
		}
		return new BooleanItem(it.type, typeString, it.label, le, width, fv, tv, true);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; int width; BigInteger fv; BigInteger tv;
		switch (mtc) {
			case $BBIT: typeString = "bbitbe"; width = 1; fv = ZERO; tv = ONE; break;
			case $TIBB: typeString = "bbitle"; width = 1; fv = ZERO; tv = ONE; break;
			case $BOOL: typeString = "boolbe"; width = 16; fv = ZERO; tv = B256; break;
			case $LOOB: typeString = "boolle"; width = 16; fv = ZERO; tv = ONE; break;
			case $BL__: typeString = "bool" + mv + "be"; width = mv; fv = ZERO; tv = ONE; break;
			case $__LB: typeString = "bool" + mv + "le"; width = mv; fv = ZERO; tv = ONE; break;
			case $BN__: typeString = "fbool" + mv + "be"; width = mv; fv = ZERO; tv = N1; break;
			case $__NB: typeString = "fbool" + mv + "le"; width = mv; fv = ZERO; tv = N1; break;
			default: return null;
		}
		return new PackedBooleanItem(it.type, typeString, it.label, width, fv, tv, true);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"bool&", "boolean&", "bool#&", "boolean#&", "fbool#&", "fboolean#&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"bit&", "bbit&", "bool&", "boolean&", "bool#&", "boolean#&", "fbool#&", "fboolean#&"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"bit&", "bbit&", "bool&", "boolean&", "bool#&", "boolean#&", "fbool#&", "fboolean#&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; BigInteger fv; BigInteger tv;
		switch (MTS.indexOf(mts) >> 1) {
			case 0: tc = (le ? $TIBB : $BBIT); mv = 1; fv = ZERO; tv = ONE; break;
			case 1: tc = (le ? $LOOB : $BOOL); mv = 16; fv = ZERO; tv = (le ? ONE : B256); break;
			case 2: tc = TemplateUtils.maskDecValue($__LB|$BL__, mv, le); fv = ZERO; tv = ONE; break;
			case 3: tc = TemplateUtils.maskDecValue($__NB|$BN__, mv, le); fv = ZERO; tv = N1; break;
			default: return null;
		}
		return new BooleanItem(tc, ts, name, le, mv, fv, tv, true);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; BigInteger fv; BigInteger tv;
		switch (MTS.indexOf(mts) >> 1) {
			case 0: tc = (le ? $TIBB : $BBIT); mv = 1; fv = ZERO; tv = ONE; break;
			case 1: tc = (le ? $LOOB : $BOOL); mv = 16; fv = ZERO; tv = (le ? ONE : B256); break;
			case 2: tc = TemplateUtils.maskDecValue($__LB|$BL__, mv, le); fv = ZERO; tv = ONE; break;
			case 3: tc = TemplateUtils.maskDecValue($__NB|$BN__, mv, le); fv = ZERO; tv = N1; break;
			default: return null;
		}
		return new PackedBooleanItem(tc, ts, name, mv, fv, tv, true);
	}
}
