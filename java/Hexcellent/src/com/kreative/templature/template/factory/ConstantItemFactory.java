package com.kreative.templature.template.factory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.ConstantItem;
import com.kreative.templature.template.item.PackedConstantItem;

public class ConstantItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$FBIT|00|PI|00, $TIBF|00|PI|00|LE,
			$FNIB|00|PI|00, $BINF|00|PI|00|LE,
			$FBYT|TI|PI|00, $TYBF|TI|PI|00|LE,
			$FWRD|TI|PI|00, $DRWF|TI|PI|00|LE,
			$FLNG|TI|PI|00, $GNLF|TI|PI|00|LE,
			$FLLG|TI|PI|00, $GLLF|TI|PI|00|LE,
			$FL__|TI|PI|DM, $__LF|TI|PI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int width;
		switch (mtc) {
			case $FBIT: typeString = "fbitbe"; le = false; width = 1; break;
			case $TIBF: typeString = "fbitle"; le = true; width = 1; break;
			case $FNIB: typeString = "fnibbe"; le = false; width = 4; break;
			case $BINF: typeString = "fnible"; le = true; width = 4; break;
			case $FBYT: typeString = "fbytebe"; le = false; width = 8; break;
			case $TYBF: typeString = "fbytele"; le = true; width = 8; break;
			case $FWRD: typeString = "fshortbe"; le = false; width = 16; break;
			case $DRWF: typeString = "fshortle"; le = true; width = 16; break;
			case $FLNG: typeString = "fintbe"; le = false; width = 32; break;
			case $GNLF: typeString = "fintle"; le = true; width = 32; break;
			case $FLLG: typeString = "flongbe"; le = false; width = 64; break;
			case $GLLF: typeString = "flongle"; le = true; width = 64; break;
			case $FL__: typeString = "fill" + mv + "be"; le = false; width = mv; break;
			case $__LF: typeString = "fill" + mv + "le"; le = true; width = mv; break;
			default: return null;
		}
		return new ConstantItem(it.type, typeString, it.label, le, width, BigInteger.ZERO);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; int width;
		switch (mtc) {
			case $FBIT: typeString = "fbitbe"; width = 1; break;
			case $TIBF: typeString = "fbitle"; width = 1; break;
			case $FNIB: typeString = "fnibbe"; width = 4; break;
			case $BINF: typeString = "fnible"; width = 4; break;
			case $FBYT: typeString = "fbytebe"; width = 8; break;
			case $TYBF: typeString = "fbytele"; width = 8; break;
			case $FWRD: typeString = "fshortbe"; width = 16; break;
			case $DRWF: typeString = "fshortle"; width = 16; break;
			case $FLNG: typeString = "fintbe"; width = 32; break;
			case $GNLF: typeString = "fintle"; width = 32; break;
			case $FLLG: typeString = "flongbe"; width = 64; break;
			case $GLLF: typeString = "flongle"; width = 64; break;
			case $FL__: typeString = "fill" + mv + "be"; width = mv; break;
			case $__LF: typeString = "fill" + mv + "le"; width = mv; break;
			default: return null;
		}
		return new PackedConstantItem(it.type, typeString, it.label, width, BigInteger.ZERO);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"fbyte&", "fshort&", "fint&", "flong&", "fill#&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"fbit&", "fnib&", "fbyte&", "fshort&", "fint&", "flong&", "fill#&"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"fbit&", "fnib&", "fbyte&", "fshort&", "fint&", "flong&", "fill#&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TIBF : $FBIT); mv = 1; break;
			case 1: tc = (le ? $BINF : $FNIB); mv = 4; break;
			case 2: tc = (le ? $TYBF : $FBYT); mv = 8; break;
			case 3: tc = (le ? $DRWF : $FWRD); mv = 16; break;
			case 4: tc = (le ? $GNLF : $FLNG); mv = 32; break;
			case 5: tc = (le ? $GLLF : $FLLG); mv = 64; break;
			case 6: tc = TemplateUtils.maskDecValue($__LF|$FL__, mv, le); break;
			default: return null;
		}
		return new ConstantItem(tc, ts, name, le, mv, BigInteger.ZERO);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int tc;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le ? $TIBF : $FBIT); mv = 1; break;
			case 1: tc = (le ? $BINF : $FNIB); mv = 4; break;
			case 2: tc = (le ? $TYBF : $FBYT); mv = 8; break;
			case 3: tc = (le ? $DRWF : $FWRD); mv = 16; break;
			case 4: tc = (le ? $GNLF : $FLNG); mv = 32; break;
			case 5: tc = (le ? $GLLF : $FLLG); mv = 64; break;
			case 6: tc = TemplateUtils.maskDecValue($__LF|$FL__, mv, le); break;
			default: return null;
		}
		return new PackedConstantItem(tc, ts, name, mv, BigInteger.ZERO);
	}
}
