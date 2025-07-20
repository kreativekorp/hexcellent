package com.kreative.templature.template.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.CStringItem;
import com.kreative.templature.template.item.PaddedStringItem;
import com.kreative.templature.template.item.PascalStringItem;
import com.kreative.templature.template.item.TheRestIsTextItem;

public class StringItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$PSTR|TI|00, $RTSP|TI|00|LE,
			$ESTR|TI|00, $RTSE|TI|00|LE,
			$OSTR|TI|00, $RTSO|TI|00|LE,
			$P___|TI|HM, $___P|TI|HM|LE,
			$WSTR|TI|00, $RTSW|TI|00|LE,
			$EWST|TI|00, $TSWE|TI|00|LE,
			$OWST|TI|00, $TSWO|TI|00|LE,
			$W___|TI|HM, $___W|TI|HM|LE,
			$MSTR|TI|00, $RTSM|TI|00|LE,
			$EMST|TI|00, $TSME|TI|00|LE,
			$OMST|TI|00, $TSMO|TI|00|LE,
			$M___|TI|HM, $___M|TI|HM|LE,
			$LSTR|TI|00, $RTSL|TI|00|LE,
			$ELST|TI|00, $TSLE|TI|00|LE,
			$OLST|TI|00, $TSLO|TI|00|LE,
			$L___|TI|HM, $___L|TI|HM|LE,
			$6STR|TI|00, $RTS6|TI|00|LE,
			$8STR|TI|00, $RTS8|TI|00|LE,
			$CSTR|TI|00, $RTSC|TI|00|LE,
			$ECST|TI|00, $TSCE|TI|00|LE,
			$OCST|TI|00, $TSCO|TI|00|LE,
			$C___|TI|HM, $___C|TI|HM|LE,
			$N___|TI|HM, $___N|TI|HM|LE,
			$S___|TI|HM, $___S|TI|HM|LE,
			$TEXT|TI|00, $TXET|TI|00|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		String typeString; boolean le; int width; int length;
		String encoding = in.getEncoding(); String lineEnding = in.getLineEnding();
		switch (mtc) {
			// 8-bit Pascal string
			case $PSTR: typeString = "pstringbe"; le = false; width = 8; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTSP: typeString = "pstringle"; le = true; width = 8; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $ESTR: typeString = "epstringbe"; le = false; width = 8; length = PascalStringItem.LENGTH_EVEN; break;
			case $RTSE: typeString = "epstringle"; le = true; width = 8; length = PascalStringItem.LENGTH_EVEN; break;
			case $OSTR: typeString = "opstringbe"; le = false; width = 8; length = PascalStringItem.LENGTH_ODD; break;
			case $RTSO: typeString = "opstringle"; le = true; width = 8; length = PascalStringItem.LENGTH_ODD; break;
			case $P___: typeString = "pstring" + mv + "be"; le = false; width = 8; length = mv; break;
			case $___P: typeString = "pstring" + mv + "le"; le = true; width = 8; length = mv; break;
			// 16-bit Pascal string
			case $WSTR: typeString = "wstringbe"; le = false; width = 16; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTSW: typeString = "wstringle"; le = true; width = 16; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $EWST: typeString = "ewstringbe"; le = false; width = 16; length = PascalStringItem.LENGTH_EVEN; break;
			case $TSWE: typeString = "ewstringle"; le = true; width = 16; length = PascalStringItem.LENGTH_EVEN; break;
			case $OWST: typeString = "owstringbe"; le = false; width = 16; length = PascalStringItem.LENGTH_ODD; break;
			case $TSWO: typeString = "owstringle"; le = true; width = 16; length = PascalStringItem.LENGTH_ODD; break;
			case $W___: typeString = "wstring" + mv + "be"; le = false; width = 16; length = mv; break;
			case $___W: typeString = "wstring" + mv + "le"; le = true; width = 16; length = mv; break;
			// 24-bit Pascal string
			case $MSTR: typeString = "mstringbe"; le = false; width = 24; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTSM: typeString = "mstringle"; le = true; width = 24; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $EMST: typeString = "emstringbe"; le = false; width = 24; length = PascalStringItem.LENGTH_EVEN; break;
			case $TSME: typeString = "emstringle"; le = true; width = 24; length = PascalStringItem.LENGTH_EVEN; break;
			case $OMST: typeString = "omstringbe"; le = false; width = 24; length = PascalStringItem.LENGTH_ODD; break;
			case $TSMO: typeString = "omstringle"; le = true; width = 24; length = PascalStringItem.LENGTH_ODD; break;
			case $M___: typeString = "mstring" + mv + "be"; le = false; width = 24; length = mv; break;
			case $___M: typeString = "mstring" + mv + "le"; le = true; width = 24; length = mv; break;
			// 32-bit Pascal string
			case $LSTR: typeString = "lstringbe"; le = false; width = 32; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTSL: typeString = "lstringle"; le = true; width = 32; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $ELST: typeString = "elstringbe"; le = false; width = 32; length = PascalStringItem.LENGTH_EVEN; break;
			case $TSLE: typeString = "elstringle"; le = true; width = 32; length = PascalStringItem.LENGTH_EVEN; break;
			case $OLST: typeString = "olstringbe"; le = false; width = 32; length = PascalStringItem.LENGTH_ODD; break;
			case $TSLO: typeString = "olstringle"; le = true; width = 32; length = PascalStringItem.LENGTH_ODD; break;
			case $L___: typeString = "lstring" + mv + "be"; le = false; width = 32; length = mv; break;
			case $___L: typeString = "lstring" + mv + "le"; le = true; width = 32; length = mv; break;
			// deprecated longer string
			case $6STR: typeString = "mlstringbe"; le = false; width = 48; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTS6: typeString = "mlstringle"; le = true; width = 48; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $8STR: typeString = "llstringbe"; le = false; width = 64; length = PascalStringItem.LENGTH_VARIABLE; break;
			case $RTS8: typeString = "llstringle"; le = true; width = 64; length = PascalStringItem.LENGTH_VARIABLE; break;
			// C string
			case $CSTR: return new CStringItem(it.type, "cstringbe", it.label, CStringItem.LENGTH_VARIABLE, encoding, lineEnding);
			case $RTSC: return new CStringItem(it.type, "cstringle", it.label, CStringItem.LENGTH_VARIABLE, encoding, lineEnding);
			case $ECST: return new CStringItem(it.type, "ecstringbe", it.label, CStringItem.LENGTH_EVEN, encoding, lineEnding);
			case $TSCE: return new CStringItem(it.type, "ecstringle", it.label, CStringItem.LENGTH_EVEN, encoding, lineEnding);
			case $OCST: return new CStringItem(it.type, "ocstringbe", it.label, CStringItem.LENGTH_ODD, encoding, lineEnding);
			case $TSCO: return new CStringItem(it.type, "ocstringle", it.label, CStringItem.LENGTH_ODD, encoding, lineEnding);
			case $C___: return new CStringItem(it.type, "cstring" + mv + "be", it.label, mv, encoding, lineEnding);
			case $___C: return new CStringItem(it.type, "cstring" + mv + "le", it.label, mv, encoding, lineEnding);
			// padded string
			case $N___: return new PaddedStringItem(it.type, "nstring" + mv + "be", it.label, mv, (byte)0x00, encoding, lineEnding);
			case $___N: return new PaddedStringItem(it.type, "nstring" + mv + "le", it.label, mv, (byte)0x00, encoding, lineEnding);
			case $S___: return new PaddedStringItem(it.type, "sstring" + mv + "be", it.label, mv, (byte)0x20, encoding, lineEnding);
			case $___S: return new PaddedStringItem(it.type, "sstring" + mv + "le", it.label, mv, (byte)0x20, encoding, lineEnding);
			// the rest is text
			case $TEXT:
				if (in.read() != null) throw new TemplateException(ERROR_TEXT_NOT_END, it.type);
				return new TheRestIsTextItem(it.type, "textbe", it.label, encoding, lineEnding);
			case $TXET:
				if (in.read() != null) throw new TemplateException(ERROR_TEXT_NOT_END, it.type);
				return new TheRestIsTextItem(it.type, "textle", it.label, encoding, lineEnding);
			default: return null;
		}
		return new PascalStringItem(it.type, typeString, it.label, le, width, length, encoding, lineEnding);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"pstr&", "pstring&", "epstr&", "epstring&", "opstr&", "opstring&", "pstr$&", "pstring$&",
			"wstr&", "wstring&", "ewstr&", "ewstring&", "owstr&", "owstring&", "wstr$&", "wstring$&",
			"mstr&", "mstring&", "emstr&", "emstring&", "omstr&", "omstring&", "mstr$&", "mstring$&",
			"lstr&", "lstring&", "elstr&", "elstring&", "olstr&", "olstring&", "lstr$&", "lstring$&",
			"mlstr&", "mlstring&",
			"llstr&", "llstring&",
			"cstr&", "cstring&", "ecstr&", "ecstring&", "ocstr&", "ocstring&",
			"cstr$&", "cstring$&",
			"nstr$&", "nstring$&",
			"sstr$&", "sstring$&",
			"text&", "textdump&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"pstr&", "pstring&", "epstr&", "epstring&", "opstr&", "opstring&", "pstr$&", "pstring$&",
		"wstr&", "wstring&", "ewstr&", "ewstring&", "owstr&", "owstring&", "wstr$&", "wstring$&",
		"mstr&", "mstring&", "emstr&", "emstring&", "omstr&", "omstring&", "mstr$&", "mstring$&",
		"lstr&", "lstring&", "elstr&", "elstring&", "olstr&", "olstring&", "lstr$&", "lstring$&",
		"mlstr&", "mlstring&",
		"llstr&", "llstring&",
		"cstr&", "cstring&", "ecstr&", "ecstring&", "ocstr&", "ocstring&",
		"cstr$&", "cstring$&",
		"nstr$&", "nstring$&",
		"sstr$&", "sstring$&",
		"text&", "textdump&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; int width;
		String encoding = state.getEncoding();
		String lineEnding = state.getLineEnding();
		switch (MTS.indexOf(mts) >> 1) {
			// 8-bit Pascal string
			case 0: tc = (le ? $RTSP : $PSTR); width = 8; mv = PascalStringItem.LENGTH_VARIABLE; break;
			case 1: tc = (le ? $RTSE : $ESTR); width = 8; mv = PascalStringItem.LENGTH_EVEN; break;
			case 2: tc = (le ? $RTSO : $OSTR); width = 8; mv = PascalStringItem.LENGTH_ODD; break;
			case 3: tc = TemplateUtils.maskHexValue($___P|$P___, mv, le); width = 8; break;
			// 16-bit Pascal string
			case 4: tc = (le ? $RTSW : $WSTR); width = 16; mv = PascalStringItem.LENGTH_VARIABLE; break;
			case 5: tc = (le ? $TSWE : $EWST); width = 16; mv = PascalStringItem.LENGTH_EVEN; break;
			case 6: tc = (le ? $TSWO : $OWST); width = 16; mv = PascalStringItem.LENGTH_ODD; break;
			case 7: tc = TemplateUtils.maskHexValue($___W|$W___, mv, le); width = 16; break;
			// 24-bit Pascal string
			case 8: tc = (le ? $RTSM : $MSTR); width = 24; mv = PascalStringItem.LENGTH_VARIABLE; break;
			case 9: tc = (le ? $TSME : $EMST); width = 24; mv = PascalStringItem.LENGTH_EVEN; break;
			case 10: tc = (le ? $TSMO : $OMST); width = 24; mv = PascalStringItem.LENGTH_ODD; break;
			case 11: tc = TemplateUtils.maskHexValue($___M|$M___, mv, le); width = 24; break;
			// 32-bit Pascal string
			case 12: tc = (le ? $RTSL : $LSTR); width = 32; mv = PascalStringItem.LENGTH_VARIABLE; break;
			case 13: tc = (le ? $TSLE : $ELST); width = 32; mv = PascalStringItem.LENGTH_EVEN; break;
			case 14: tc = (le ? $TSLO : $OLST); width = 32; mv = PascalStringItem.LENGTH_ODD; break;
			case 15: tc = TemplateUtils.maskHexValue($___L|$L___, mv, le); width = 32; break;
			// deprecated longer string
			case 16: tc = (le ? $RTS6 : $6STR); width = 48; mv = PascalStringItem.LENGTH_VARIABLE; break;
			case 17: tc = (le ? $RTS8 : $8STR); width = 64; mv = PascalStringItem.LENGTH_VARIABLE; break;
			// C string
			case 18: return new CStringItem((le ? $RTSC : $CSTR), ts, name, CStringItem.LENGTH_VARIABLE, encoding, lineEnding);
			case 19: return new CStringItem((le ? $TSCE : $ECST), ts, name, CStringItem.LENGTH_EVEN, encoding, lineEnding);
			case 20: return new CStringItem((le ? $TSCO : $OCST), ts, name, CStringItem.LENGTH_ODD, encoding, lineEnding);
			case 21: return new CStringItem(TemplateUtils.maskHexValue($C___, mv, false), ts, name, mv, encoding, lineEnding);
			// padded string
			case 22: return new PaddedStringItem(TemplateUtils.maskHexValue($___N|$N___, mv, le), ts, name, mv, (byte)0x00, encoding, lineEnding);
			case 23: return new PaddedStringItem(TemplateUtils.maskHexValue($___S|$S___, mv, le), ts, name, mv, (byte)0x20, encoding, lineEnding);
			// the rest is text
			case 24:
				if (tokens.hasNextType()) throw new TemplateException(ERROR_TEXT_NOT_END, ts);
				return new TheRestIsTextItem((le ? $TXET : $TEXT), ts, name, encoding, lineEnding);
			default: return null;
		}
		return new PascalStringItem(tc, ts, name, le, width, mv, encoding, lineEnding);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
}
