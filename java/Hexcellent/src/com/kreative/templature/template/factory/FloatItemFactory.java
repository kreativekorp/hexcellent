package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.FloatItem;
import com.kreative.templature.template.item.PackedFloatItem;

public class FloatItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$MFLT|00|PI|00, $TLFM|00|PI|00|LE, // micro float (1.2.1)
			$NFLT|TI|PI|00, $TLFN|TI|PI|00|LE, // NVidia float (1.5.2)
			$HFLT|TI|PI|00, $TLFH|TI|PI|00|LE, // half float (1.5.10)
			$BFLT|TI|PI|00, $TLFB|TI|PI|00|LE, // bfloat (1.8.7)
			$TFLT|00|PI|00, $TLFT|00|PI|00|LE, // TensorFloat (1.8.10)
			$AFLT|TI|PI|00, $TLFA|TI|PI|00|LE, // ATI float (1.7.16)
			$PFLT|TI|PI|00, $TLFP|TI|PI|00|LE, // PXR float (1.8.15)
			$SFLT|TI|PI|00, $TLFS|TI|PI|00|LE, // single float (1.8.23)
			$DFLT|TI|PI|00, $TLFD|TI|PI|00|LE, // double float (1.11.52)
			$QFLT|TI|PI|00, $TLFQ|TI|PI|00|LE, // quadruple float (1.15.112)
			$OFLT|TI|PI|00, $TLFO|TI|PI|00|LE, // octuple float (1.19.236)
			$FP__|TI|PI|DM, $__PF|TI|PI|DM|LE, // custom
			$QUAR|TI|PI|00, $RAUQ|TI|PI|00|LE, // [deprecated] quarter float (1.4.3)
			$HALF|TI|PI|00, $FLAH|TI|PI|00|LE, // [deprecated] half float (1.5.10)
			$3QTR|TI|PI|00, $RTQ3|TI|PI|00|LE, // [deprecated] ATI float (1.7.16)
			$SING|TI|PI|00, $GNIS|TI|PI|00|LE, // [deprecated] single float (1.8.23)
			$DOUB|TI|PI|00, $BUOD|TI|PI|00|LE, // [deprecated] double float (1.11.52)
			$QUAD|TI|PI|00, $DAUQ|TI|PI|00|LE, // [deprecated] quadruple float (1.15.112)
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; FloatFormat format;
		switch (mtc) {
			case $MFLT: typeString = "mfloatbe"; le = false; format = FloatFormat.RESPL4; break;
			case $TLFM: typeString = "mfloatle"; le = true; format = FloatFormat.RESPL4; break;
			case $NFLT: typeString = "nfloatbe"; le = false; format = new FloatFormat(5,2); break;
			case $TLFN: typeString = "nfloatle"; le = true; format = new FloatFormat(5,2); break;
			case $HFLT: typeString = "hfloatbe"; le = false; format = FloatFormat.HALF; break;
			case $TLFH: typeString = "hfloatle"; le = true; format = FloatFormat.HALF; break;
			case $BFLT: typeString = "bfloatbe"; le = false; format = FloatFormat.BFLOAT16; break;
			case $TLFB: typeString = "bfloatle"; le = true; format = FloatFormat.BFLOAT16; break;
			case $TFLT: typeString = "tfloatbe"; le = false; format = FloatFormat.TENSORFLOAT; break;
			case $TLFT: typeString = "tfloatle"; le = true; format = FloatFormat.TENSORFLOAT; break;
			case $AFLT: typeString = "afloatbe"; le = false; format = FloatFormat.FP24; break;
			case $TLFA: typeString = "afloatle"; le = true; format = FloatFormat.FP24; break;
			case $PFLT: typeString = "pfloatbe"; le = false; format = FloatFormat.PXR24; break;
			case $TLFP: typeString = "pfloatle"; le = true; format = FloatFormat.PXR24; break;
			case $SFLT: typeString = "sfloatbe"; le = false; format = FloatFormat.SINGLE; break;
			case $TLFS: typeString = "sfloatle"; le = true; format = FloatFormat.SINGLE; break;
			case $DFLT: typeString = "dfloatbe"; le = false; format = FloatFormat.DOUBLE; break;
			case $TLFD: typeString = "dfloatle"; le = true; format = FloatFormat.DOUBLE; break;
			case $QFLT: typeString = "qfloatbe"; le = false; format = FloatFormat.QUADRUPLE; break;
			case $TLFQ: typeString = "qfloatle"; le = true; format = FloatFormat.QUADRUPLE; break;
			case $OFLT: typeString = "ofloatbe"; le = false; format = FloatFormat.OCTUPLE; break;
			case $TLFO: typeString = "ofloatle"; le = true; format = FloatFormat.OCTUPLE; break;
			case $FP__: typeString = "float" + mv + "be"; le = false; if ((format = formatForWidth(mv)) == null) return null; break;
			case $__PF: typeString = "float" + mv + "le"; le = true; if ((format = formatForWidth(mv)) == null) return null; break;
			case $QUAR: typeString = "quarterbe"; le = false; format = FloatFormat.RESPL8; break;
			case $RAUQ: typeString = "quarterle"; le = true; format = FloatFormat.RESPL8; break;
			case $HALF: typeString = "halfbe"; le = false; format = FloatFormat.HALF; break;
			case $FLAH: typeString = "halfle"; le = true; format = FloatFormat.HALF; break;
			case $3QTR: typeString = "threeqbe"; le = false; format = FloatFormat.FP24; break;
			case $RTQ3: typeString = "threeqle"; le = true; format = FloatFormat.FP24; break;
			case $SING: typeString = "singlebe"; le = false; format = FloatFormat.SINGLE; break;
			case $GNIS: typeString = "singlele"; le = true; format = FloatFormat.SINGLE; break;
			case $DOUB: typeString = "doublebe"; le = false; format = FloatFormat.DOUBLE; break;
			case $BUOD: typeString = "doublele"; le = true; format = FloatFormat.DOUBLE; break;
			case $QUAD: typeString = "quadbe"; le = false; format = FloatFormat.QUADRUPLE; break;
			case $DAUQ: typeString = "quadle"; le = true; format = FloatFormat.QUADRUPLE; break;
			default: return null;
		}
		return new FloatItem(it.type, typeString, it.label, le, format);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; FloatFormat format;
		switch (mtc) {
			case $MFLT: typeString = "mfloatbe"; format = FloatFormat.RESPL4; break;
			case $TLFM: typeString = "mfloatle"; format = FloatFormat.RESPL4; break;
			case $NFLT: typeString = "nfloatbe"; format = new FloatFormat(5,2); break;
			case $TLFN: typeString = "nfloatle"; format = new FloatFormat(5,2); break;
			case $HFLT: typeString = "hfloatbe"; format = FloatFormat.HALF; break;
			case $TLFH: typeString = "hfloatle"; format = FloatFormat.HALF; break;
			case $BFLT: typeString = "bfloatbe"; format = FloatFormat.BFLOAT16; break;
			case $TLFB: typeString = "bfloatle"; format = FloatFormat.BFLOAT16; break;
			case $TFLT: typeString = "tfloatbe"; format = FloatFormat.TENSORFLOAT; break;
			case $TLFT: typeString = "tfloatle"; format = FloatFormat.TENSORFLOAT; break;
			case $AFLT: typeString = "afloatbe"; format = FloatFormat.FP24; break;
			case $TLFA: typeString = "afloatle"; format = FloatFormat.FP24; break;
			case $PFLT: typeString = "pfloatbe"; format = FloatFormat.PXR24; break;
			case $TLFP: typeString = "pfloatle"; format = FloatFormat.PXR24; break;
			case $SFLT: typeString = "sfloatbe"; format = FloatFormat.SINGLE; break;
			case $TLFS: typeString = "sfloatle"; format = FloatFormat.SINGLE; break;
			case $DFLT: typeString = "dfloatbe"; format = FloatFormat.DOUBLE; break;
			case $TLFD: typeString = "dfloatle"; format = FloatFormat.DOUBLE; break;
			case $QFLT: typeString = "qfloatbe"; format = FloatFormat.QUADRUPLE; break;
			case $TLFQ: typeString = "qfloatle"; format = FloatFormat.QUADRUPLE; break;
			case $OFLT: typeString = "ofloatbe"; format = FloatFormat.OCTUPLE; break;
			case $TLFO: typeString = "ofloatle"; format = FloatFormat.OCTUPLE; break;
			case $FP__: typeString = "float" + mv + "be"; if ((format = formatForWidth(mv)) == null) return null; break;
			case $__PF: typeString = "float" + mv + "le"; if ((format = formatForWidth(mv)) == null) return null; break;
			case $QUAR: typeString = "quarterbe"; format = FloatFormat.RESPL8; break;
			case $RAUQ: typeString = "quarterle"; format = FloatFormat.RESPL8; break;
			case $HALF: typeString = "halfbe"; format = FloatFormat.HALF; break;
			case $FLAH: typeString = "halfle"; format = FloatFormat.HALF; break;
			case $3QTR: typeString = "threeqbe"; format = FloatFormat.FP24; break;
			case $RTQ3: typeString = "threeqle"; format = FloatFormat.FP24; break;
			case $SING: typeString = "singlebe"; format = FloatFormat.SINGLE; break;
			case $GNIS: typeString = "singlele"; format = FloatFormat.SINGLE; break;
			case $DOUB: typeString = "doublebe"; format = FloatFormat.DOUBLE; break;
			case $BUOD: typeString = "doublele"; format = FloatFormat.DOUBLE; break;
			case $QUAD: typeString = "quadbe"; format = FloatFormat.QUADRUPLE; break;
			case $DAUQ: typeString = "quadle"; format = FloatFormat.QUADRUPLE; break;
			default: return null;
		}
		return new PackedFloatItem(it.type, typeString, it.label, format);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"nfloat&", "hfloat&", "bfloat&", "afloat&", "pfloat&",
			"sfloat&", "dfloat&", "qfloat&", "ofloat&", "float#&", "fp#&",
			"quarter&", "half&", "threeq&", "float&", "single&", "double&", "quad&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"mfloat&", "nfloat&", "hfloat&", "bfloat&", "tfloat&", "afloat&", "pfloat&",
			"sfloat&", "dfloat&", "qfloat&", "ofloat&", "float#&", "fp#&",
			"quarter&", "half&", "threeq&", "float&", "single&", "double&", "quad&"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"mfloat&", "nfloat&", "hfloat&", "bfloat&", "tfloat&", "afloat&", "pfloat&",
		"sfloat&", "dfloat&", "qfloat&", "ofloat&", "float#&", "fp#&",
		"quarter&", "half&", "threeq&", "float&", "single&", "double&", "quad&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; FloatFormat format;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TLFM : $MFLT); format = FloatFormat.RESPL4; break;
			case 1: tc = (le ? $TLFN : $NFLT); format = new FloatFormat(5,2); break;
			case 2: tc = (le ? $TLFH : $HFLT); format = FloatFormat.HALF; break;
			case 3: tc = (le ? $TLFB : $BFLT); format = FloatFormat.BFLOAT16; break;
			case 4: tc = (le ? $TLFT : $TFLT); format = FloatFormat.TENSORFLOAT; break;
			case 5: tc = (le ? $TLFA : $AFLT); format = FloatFormat.FP24; break;
			case 6: tc = (le ? $TLFP : $PFLT); format = FloatFormat.PXR24; break;
			case 7: tc = (le ? $TLFS : $SFLT); format = FloatFormat.SINGLE; break;
			case 8: tc = (le ? $TLFD : $DFLT); format = FloatFormat.DOUBLE; break;
			case 9: tc = (le ? $TLFQ : $QFLT); format = FloatFormat.QUADRUPLE; break;
			case 10: tc = (le ? $TLFO : $OFLT); format = FloatFormat.OCTUPLE; break;
			case 11: tc = TemplateUtils.maskDecValue($__PF|$FP__, mv, le); if ((format = formatForWidth(mv)) == null) return null; break;
			case 12: tc = TemplateUtils.maskDecValue($__PF|$FP__, mv, le); if ((format = formatForWidth(mv)) == null) return null; break;
			case 13: tc = (le ? $RAUQ : $QUAR); format = FloatFormat.RESPL8; break;
			case 14: tc = (le ? $FLAH : $HALF); format = FloatFormat.HALF; break;
			case 15: tc = (le ? $RTQ3 : $3QTR); format = FloatFormat.FP24; break;
			case 16: tc = (le ? $GNIS : $SING); format = FloatFormat.SINGLE; break;
			case 17: tc = (le ? $GNIS : $SING); format = FloatFormat.SINGLE; break;
			case 18: tc = (le ? $BUOD : $DOUB); format = FloatFormat.DOUBLE; break;
			case 19: tc = (le ? $DAUQ : $QUAD); format = FloatFormat.QUADRUPLE; break;
			default: return null;
		}
		return new FloatItem(tc, ts, name, le, format);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc; FloatFormat format;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TLFM : $MFLT); format = FloatFormat.RESPL4; break;
			case 1: tc = (le ? $TLFN : $NFLT); format = new FloatFormat(5,2); break;
			case 2: tc = (le ? $TLFH : $HFLT); format = FloatFormat.HALF; break;
			case 3: tc = (le ? $TLFB : $BFLT); format = FloatFormat.BFLOAT16; break;
			case 4: tc = (le ? $TLFT : $TFLT); format = FloatFormat.TENSORFLOAT; break;
			case 5: tc = (le ? $TLFA : $AFLT); format = FloatFormat.FP24; break;
			case 6: tc = (le ? $TLFP : $PFLT); format = FloatFormat.PXR24; break;
			case 7: tc = (le ? $TLFS : $SFLT); format = FloatFormat.SINGLE; break;
			case 8: tc = (le ? $TLFD : $DFLT); format = FloatFormat.DOUBLE; break;
			case 9: tc = (le ? $TLFQ : $QFLT); format = FloatFormat.QUADRUPLE; break;
			case 10: tc = (le ? $TLFO : $OFLT); format = FloatFormat.OCTUPLE; break;
			case 11: tc = TemplateUtils.maskDecValue($__PF|$FP__, mv, le); if ((format = formatForWidth(mv)) == null) return null; break;
			case 12: tc = TemplateUtils.maskDecValue($__PF|$FP__, mv, le); if ((format = formatForWidth(mv)) == null) return null; break;
			case 13: tc = (le ? $RAUQ : $QUAR); format = FloatFormat.RESPL8; break;
			case 14: tc = (le ? $FLAH : $HALF); format = FloatFormat.HALF; break;
			case 15: tc = (le ? $RTQ3 : $3QTR); format = FloatFormat.FP24; break;
			case 16: tc = (le ? $GNIS : $SING); format = FloatFormat.SINGLE; break;
			case 17: tc = (le ? $GNIS : $SING); format = FloatFormat.SINGLE; break;
			case 18: tc = (le ? $BUOD : $DOUB); format = FloatFormat.DOUBLE; break;
			case 19: tc = (le ? $DAUQ : $QUAD); format = FloatFormat.QUADRUPLE; break;
			default: return null;
		}
		return new PackedFloatItem(tc, ts, name, format);
	}
	
	private FloatFormat formatForWidth(int width) {
		switch (width) {
			case 3: return new FloatFormat(1, 1);
			case 4: return FloatFormat.RESPL4; // 2, 1; 0.5 bytes
			case 5: return new FloatFormat(2, 2);
			case 6: return new FloatFormat(3, 2);
			case 7: return new FloatFormat(3, 3);
			case 8: return FloatFormat.RESPL8; // 4, 3; 1 byte
			case 10: return new FloatFormat(4, 5);
			case 12: return new FloatFormat(5, 6);
			case 14: return new FloatFormat(5, 8);
			case 16: return FloatFormat.HALF; // 5, 10; 2 bytes
			case 20: return new FloatFormat(6, 13);
			case 24: return new FloatFormat(7, 16); // 3 bytes
			case 28: return new FloatFormat(8, 19);
			case 32: return FloatFormat.SINGLE; // 8, 23; 4 bytes
			case 40: return new FloatFormat(9, 30); // 5 bytes
			case 48: return new FloatFormat(10, 37); // 6 bytes
			case 56: return new FloatFormat(11, 44); // 7 bytes
			case 64: return FloatFormat.DOUBLE; // 11, 52; 8 bytes
			case 80: return new FloatFormat(12, 67); // 10 bytes
			case 96: return new FloatFormat(13, 82); // 12 bytes
			case 112: return new FloatFormat(14, 97); // 14 bytes
			case 128: return FloatFormat.QUADRUPLE; // 15, 112; 16 bytes
			case 160: return new FloatFormat(16, 143); // 20 bytes
			case 192: return new FloatFormat(17, 174); // 24 bytes
			case 224: return new FloatFormat(18, 205); // 28 bytes
			case 256: return FloatFormat.OCTUPLE; // 19, 236; 32 bytes
			default: return null;
		}
	}
}
