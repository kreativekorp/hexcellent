package com.kreative.templature.template.factory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.EnumItem;
import com.kreative.templature.template.item.PackedEnumItem;

public class EnumItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$EBIT|00|PI|00, $TIBE|00|PI|00|LE,
			$ENIB|00|PI|00, $BINE|00|PI|00|LE,
			$EBYT|TI|PI|00, $TYBE|TI|PI|00|LE,
			$EWRD|TI|PI|00, $DRWE|TI|PI|00|LE,
			$ELNG|TI|PI|00, $GNLE|TI|PI|00|LE,
			$ELLG|TI|PI|00, $GLLE|TI|PI|00|LE,
			$EN__|TI|PI|DM, $__NE|TI|PI|DM|LE,
			$ENMV|TI|PI|00, $VMNE|TI|PI|00|LE,
			$ENME|TI|PI|00, $EMNE|TI|PI|00|LE,
			$EOPT|TI|PI|00, $TPOE|TI|PI|00|LE,
			$EEND|TI|PI|00, $DNEE|TI|PI|00|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		int typeConstant = it.type; String beginName = it.label;
		String typeString; boolean le; int width;
		switch (mtc) {
			case $EBIT: typeString = "ebitbe"; le = false; width = 1; break;
			case $TIBE: typeString = "ebitle"; le = true; width = 1; break;
			case $ENIB: typeString = "enibbe"; le = false; width = 4; break;
			case $BINE: typeString = "enible"; le = true; width = 4; break;
			case $EBYT: typeString = "ebytebe"; le = false; width = 8; break;
			case $TYBE: typeString = "ebytele"; le = true; width = 8; break;
			case $EWRD: typeString = "eshortbe"; le = false; width = 16; break;
			case $DRWE: typeString = "eshortle"; le = true; width = 16; break;
			case $ELNG: typeString = "eintbe"; le = false; width = 32; break;
			case $GNLE: typeString = "eintle"; le = true; width = 32; break;
			case $ELLG: typeString = "elongbe"; le = false; width = 64; break;
			case $GLLE: typeString = "elongle"; le = true; width = 64; break;
			case $EN__: typeString = "enum" + mv + "be"; le = false; width = mv; break;
			case $__NE: typeString = "enum" + mv + "le"; le = true; width = mv; break;
			case $ENMV: case $VMNE: case $EOPT: case $TPOE: throw new TemplateException(ERROR_ENUM_VALUE, it.type);
			case $ENME: case $EMNE: case $EEND: case $DNEE: throw new TemplateException(ERROR_ENUM_END, it.type);
			default: return null;
		}
		int valueConstant = 0;
		TreeMap<BigInteger,String> values = new TreeMap<BigInteger,String>();
		for (;;) {
			it = in.read();
			if (it != null) {
				if (it.type == $ENME || it.type == $EMNE || it.type == $EEND || it.type == $DNEE) break;
				if (it.type == $ENMV || it.type == $VMNE || it.type == $EOPT || it.type == $TPOE) {
					valueConstant = it.type;
					values.put(getEnumValue(values, it.label), it.label);
					continue;
				}
			}
			throw new TemplateException(ERROR_ENUM_NO_END, typeConstant, (le ? $EMNE : $ENME));
		}
		if (valueConstant == 0 || values.isEmpty()) throw new TemplateException(ERROR_ENUM_NO_VALUE, typeConstant, (le ? $VMNE : $ENMV));
		return new EnumItem(typeConstant, valueConstant, it.type, typeString, beginName, it.label, le, width, values);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		int typeConstant = it.type; String beginName = it.label;
		String typeString; boolean le; int width;
		switch (mtc) {
			case $EBIT: typeString = "ebitbe"; le = false; width = 1; break;
			case $TIBE: typeString = "ebitle"; le = true; width = 1; break;
			case $ENIB: typeString = "enibbe"; le = false; width = 4; break;
			case $BINE: typeString = "enible"; le = true; width = 4; break;
			case $EBYT: typeString = "ebytebe"; le = false; width = 8; break;
			case $TYBE: typeString = "ebytele"; le = true; width = 8; break;
			case $EWRD: typeString = "eshortbe"; le = false; width = 16; break;
			case $DRWE: typeString = "eshortle"; le = true; width = 16; break;
			case $ELNG: typeString = "eintbe"; le = false; width = 32; break;
			case $GNLE: typeString = "eintle"; le = true; width = 32; break;
			case $ELLG: typeString = "elongbe"; le = false; width = 64; break;
			case $GLLE: typeString = "elongle"; le = true; width = 64; break;
			case $EN__: typeString = "enum" + mv + "be"; le = false; width = mv; break;
			case $__NE: typeString = "enum" + mv + "le"; le = true; width = mv; break;
			case $ENMV: case $VMNE: case $EOPT: case $TPOE: throw new TemplateException(ERROR_ENUM_VALUE, it.type);
			case $ENME: case $EMNE: case $EEND: case $DNEE: throw new TemplateException(ERROR_ENUM_END, it.type);
			default: return null;
		}
		int valueConstant = 0;
		TreeMap<BigInteger,String> values = new TreeMap<BigInteger,String>();
		for (;;) {
			it = in.read();
			if (it != null) {
				if (it.type == $ENME || it.type == $EMNE || it.type == $EEND || it.type == $DNEE) break;
				if (it.type == $ENMV || it.type == $VMNE || it.type == $EOPT || it.type == $TPOE) {
					valueConstant = it.type;
					values.put(getEnumValue(values, it.label), it.label);
					continue;
				}
			}
			throw new TemplateException(ERROR_ENUM_NO_END, typeConstant, (le ? $EMNE : $ENME));
		}
		if (valueConstant == 0 || values.isEmpty()) throw new TemplateException(ERROR_ENUM_NO_VALUE, typeConstant, (le ? $VMNE : $ENMV));
		return new PackedEnumItem(typeConstant, valueConstant, it.type, typeString, beginName, it.label, width, values);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"ebyte&", "eshort&", "eint&", "elong&", "enum#&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"ebit&", "enib&", "ebyte&", "eshort&", "eint&", "elong&", "enum#&"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"ebit&", "enib&", "ebyte&", "eshort&", "eint&", "elong&", "enum#&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String beginName = tokens.nextNameOrNull();
		tokens.nextBlockBeginOrThrow();
		TreeMap<BigInteger,String> values = new TreeMap<BigInteger,String>();
		for (;;) {
			String v = tokens.nextNameOrThrow();
			values.put(getEnumValue(values, v), v);
			if (tokens.nextBlockEnd()) break;
			tokens.nextValueDelimiterOrThrow();
			if (tokens.nextBlockEnd()) break;
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TIBE : $EBIT); mv = 1; break;
			case 1: tc = (le ? $BINE : $ENIB); mv = 4; break;
			case 2: tc = (le ? $TYBE : $EBYT); mv = 8; break;
			case 3: tc = (le ? $DRWE : $EWRD); mv = 16; break;
			case 4: tc = (le ? $GNLE : $ELNG); mv = 32; break;
			case 5: tc = (le ? $GLLE : $ELLG); mv = 64; break;
			case 6: tc = TemplateUtils.maskDecValue($__NE|$EN__, mv, le); break;
			default: return null;
		}
		return new EnumItem(
			tc, (le ? $VMNE : $ENMV), (le ? $EMNE : $ENME),
			ts, beginName, endName, le, mv, values
		);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String beginName = tokens.nextNameOrNull();
		TreeMap<BigInteger,String> values = new TreeMap<BigInteger,String>();
		tokens.nextBlockBeginOrThrow();
		for (;;) {
			String v = tokens.nextNameOrThrow();
			values.put(getEnumValue(values, v), v);
			if (tokens.nextBlockEnd()) break;
			tokens.nextValueDelimiterOrThrow();
			if (tokens.nextBlockEnd()) break;
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TIBE : $EBIT); mv = 1; break;
			case 1: tc = (le ? $BINE : $ENIB); mv = 4; break;
			case 2: tc = (le ? $TYBE : $EBYT); mv = 8; break;
			case 3: tc = (le ? $DRWE : $EWRD); mv = 16; break;
			case 4: tc = (le ? $GNLE : $ELNG); mv = 32; break;
			case 5: tc = (le ? $GLLE : $ELLG); mv = 64; break;
			case 6: tc = TemplateUtils.maskDecValue($__NE|$EN__, mv, le); break;
			default: return null;
		}
		return new PackedEnumItem(
			tc, (le ? $VMNE : $ENMV), (le ? $EMNE : $ENME),
			ts, beginName, endName, mv, values
		);
	}
	
	private static final String INT = "(0[XxHh][0-9A-Fa-f]+|0[Dd][0-9]+|0[Bb][01]+|0[Oo][0-7]+|[#$][0-9A-Fa-f]+|%[01]+|[0-9]+)";
	private static final Pattern VALUE = Pattern.compile("^"+INT+"\\b|\\("+INT+"\\)|\\["+INT+"\\]|\\{"+INT+"\\}|\\b"+INT+"$");
	
	private BigInteger getEnumValue(TreeMap<BigInteger,String> values, String name) {
		// Look for a numeric value in the label.
		Matcher m = VALUE.matcher(name);
		while (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				if (m.group(i) != null && m.group(i).length() > 0) {
					try { return TemplateUtils.parseBigInteger(m.group(i), 10); }
					catch (NumberFormatException e) { continue; }
				}
			}
		}
		// If none found, use the next available value.
		if (values.isEmpty()) return BigInteger.ZERO;
		return values.lastKey().add(BigInteger.ONE);
	}
}
