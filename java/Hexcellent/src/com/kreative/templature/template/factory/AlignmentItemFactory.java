package com.kreative.templature.template.factory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.AlignmentItem;
import com.kreative.templature.template.item.PackedAlignmentItem;

public class AlignmentItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$ANIB|00|PI|00, $BINA|00|PI|00|LE,
			$ABYT|00|PI|00, $TYBA|00|PI|00|LE,
			$AWRD|TI|PI|00, $DRWA|TI|PI|00|LE,
			$ALNG|TI|PI|00, $GNLA|TI|PI|00|LE,
			$ALLG|TI|PI|00, $GLLA|TI|PI|00|LE,
			$AL__|TI|PI|DM, $__LA|TI|PI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int mod;
		switch (mtc) {
			case $ANIB: typeString = "anibbe"; le = false; mod = 4; break;
			case $BINA: typeString = "anible"; le = true; mod = 4; break;
			case $ABYT: typeString = "abytebe"; le = false; mod = 8; break;
			case $TYBA: typeString = "abytele"; le = true; mod = 8; break;
			case $AWRD: typeString = "ashortbe"; le = false; mod = 16; break;
			case $DRWA: typeString = "ashortle"; le = true; mod = 16; break;
			case $ALNG: typeString = "aintbe"; le = false; mod = 32; break;
			case $GNLA: typeString = "aintle"; le = true; mod = 32; break;
			case $ALLG: typeString = "alongbe"; le = false; mod = 64; break;
			case $GLLA: typeString = "alongle"; le = true; mod = 64; break;
			case $AL__: typeString = "align" + mv + "be"; le = false; mod = mv; break;
			case $__LA: typeString = "align" + mv + "le"; le = true; mod = mv; break;
			default: return null;
		}
		return new AlignmentItem(it.type, typeString, it.label, le, mod, BigInteger.ZERO);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; int mod;
		switch (mtc) {
			case $ANIB: typeString = "anibbe"; mod = 4; break;
			case $BINA: typeString = "anible"; mod = 4; break;
			case $ABYT: typeString = "abytebe"; mod = 8; break;
			case $TYBA: typeString = "abytele"; mod = 8; break;
			case $AWRD: typeString = "ashortbe"; mod = 16; break;
			case $DRWA: typeString = "ashortle"; mod = 16; break;
			case $ALNG: typeString = "aintbe"; mod = 32; break;
			case $GNLA: typeString = "aintle"; mod = 32; break;
			case $ALLG: typeString = "alongbe"; mod = 64; break;
			case $GLLA: typeString = "alongle"; mod = 64; break;
			case $AL__: typeString = "align" + mv + "be"; mod = mv; break;
			case $__LA: typeString = "align" + mv + "le"; mod = mv; break;
			default: return null;
		}
		return new PackedAlignmentItem(it.type, typeString, it.label, mod, BigInteger.ZERO);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"ashort&", "aint&", "along&", "align#&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"anib&", "abyte&", "ashort&", "aint&", "along&", "align#&"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"anib&", "abyte&", "ashort&", "aint&", "along&", "align#&"
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
			case 0: tc = (le ? $BINA : $ANIB); mv = 4; break;
			case 1: tc = (le ? $TYBA : $ABYT); mv = 8; break;
			case 2: tc = (le ? $DRWA : $AWRD); mv = 16; break;
			case 3: tc = (le ? $GNLA : $ALNG); mv = 32; break;
			case 4: tc = (le ? $GLLA : $ALLG); mv = 64; break;
			case 5: tc = TemplateUtils.maskDecValue($__LA|$AL__, mv, le); break;
			default: return null;
		}
		return new AlignmentItem(tc, ts, name, le, mv, BigInteger.ZERO);
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
			case 0: tc = (le ? $BINA : $ANIB); mv = 4; break;
			case 1: tc = (le ? $TYBA : $ABYT); mv = 8; break;
			case 2: tc = (le ? $DRWA : $AWRD); mv = 16; break;
			case 3: tc = (le ? $GNLA : $ALNG); mv = 32; break;
			case 4: tc = (le ? $GLLA : $ALLG); mv = 64; break;
			case 5: tc = TemplateUtils.maskDecValue($__LA|$AL__, mv, le); break;
			default: return null;
		}
		return new PackedAlignmentItem(tc, ts, name, mv, BigInteger.ZERO);
	}
}
