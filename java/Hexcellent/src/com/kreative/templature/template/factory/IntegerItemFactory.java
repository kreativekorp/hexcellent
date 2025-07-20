package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.IntegerItem;
import com.kreative.templature.template.item.PackedIntegerItem;

public class IntegerItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	private static final int $D___ = 0x44000000; private static final int $___D = 0x00000044;
	private static final int $H___ = 0x48000000; private static final int $___H = 0x00000048;
	private static final int $O___ = 0x4F000000; private static final int $___O = 0x0000004F;
	private static final int $B___ = 0x42000000; private static final int $___B = 0x00000042;
	private static final int $_NIB = 0x004E4942; private static final int $BIN_ = 0x42494E00;
	private static final int $_BYT = 0x00425954; private static final int $TYB_ = 0x54594200;
	private static final int $_WRD = 0x00575244; private static final int $DRW_ = 0x44525700;
	private static final int $_LNG = 0x004C4E47; private static final int $GNL_ = 0x474E4C00;
	private static final int $_LLG = 0x004C4C47; private static final int $GLL_ = 0x474C4C00;
	private static final int $_I__ = 0x00490000; private static final int $__I_ = 0x00004900;
	private static final int $_S__ = 0x00530000; private static final int $__S_ = 0x00005300;
	private static final int $_U__ = 0x00550000; private static final int $__U_ = 0x00005500;
	
	public long[] getTypeConstants() {
		return new long[] {
			// decimal
			$DNIB|00|PI|00, $BIND|00|PI|00|LE,
			$DBYT|TI|PI|00, $TYBD|TI|PI|00|LE,
			$DWRD|TI|PI|00, $DRWD|TI|PI|00|LE,
			$DLNG|TI|PI|00, $GNLD|TI|PI|00|LE,
			$DLLG|TI|PI|00, $GLLD|TI|PI|00|LE,
			$DI__|TI|PI|DM, $__ID|TI|PI|DM|LE,
			$DS__|TI|PI|DM, $__SD|TI|PI|DM|LE,
			$DU__|TI|PI|DM, $__UD|TI|PI|DM|LE,
			// hexadecimal
			$HNIB|00|PI|00, $BINH|00|PI|00|LE,
			$HBYT|TI|PI|00, $TYBH|TI|PI|00|LE,
			$HWRD|TI|PI|00, $DRWH|TI|PI|00|LE,
			$HLNG|TI|PI|00, $GNLH|TI|PI|00|LE,
			$HLLG|TI|PI|00, $GLLH|TI|PI|00|LE,
			$HI__|TI|PI|DM, $__IH|TI|PI|DM|LE,
			$HS__|TI|PI|DM, $__SH|TI|PI|DM|LE,
			$HU__|TI|PI|DM, $__UH|TI|PI|DM|LE,
			// octal
			$ONIB|00|PI|00, $BINO|00|PI|00|LE,
			$OBYT|TI|PI|00, $TYBO|TI|PI|00|LE,
			$OWRD|TI|PI|00, $DRWO|TI|PI|00|LE,
			$OLNG|TI|PI|00, $GNLO|TI|PI|00|LE,
			$OLLG|TI|PI|00, $GLLO|TI|PI|00|LE,
			$OI__|TI|PI|DM, $__IO|TI|PI|DM|LE,
			$OS__|TI|PI|DM, $__SO|TI|PI|DM|LE,
			$OU__|TI|PI|DM, $__UO|TI|PI|DM|LE,
			// binary
			$BNIB|00|PI|00, $BINB|00|PI|00|LE,
			$BBYT|TI|PI|00, $TYBB|TI|PI|00|LE,
			$BWRD|TI|PI|00, $DRWB|TI|PI|00|LE,
			$BLNG|TI|PI|00, $GNLB|TI|PI|00|LE,
			$BLLG|TI|PI|00, $GLLB|TI|PI|00|LE,
			$BI__|TI|PI|DM, $__IB|TI|PI|DM|LE,
			$BS__|TI|PI|DM, $__SB|TI|PI|DM|LE,
			$BU__|TI|PI|DM, $__UB|TI|PI|DM|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; boolean le; int width; boolean signed; int radix;
		switch (mtc) {
			// decimal
			case $DNIB: typeString = "nibbe"; le = false; width = 4; signed = true; radix = 10; break;
			case $BIND: typeString = "nible"; le = true; width = 4; signed = true; radix = 10; break;
			case $DBYT: typeString = "bytebe"; le = false; width = 8; signed = true; radix = 10; break;
			case $TYBD: typeString = "bytele"; le = true; width = 8; signed = true; radix = 10; break;
			case $DWRD: typeString = "shortbe"; le = false; width = 16; signed = true; radix = 10; break;
			case $DRWD: typeString = "shortle"; le = true; width = 16; signed = true; radix = 10; break;
			case $DLNG: typeString = "intbe"; le = false; width = 32; signed = true; radix = 10; break;
			case $GNLD: typeString = "intle"; le = true; width = 32; signed = true; radix = 10; break;
			case $DLLG: typeString = "longbe"; le = false; width = 64; signed = true; radix = 10; break;
			case $GLLD: typeString = "longle"; le = true; width = 64; signed = true; radix = 10; break;
			case $DI__: typeString = "int" + mv + "be"; le = false; width = mv; signed = true; radix = 10; break;
			case $__ID: typeString = "int" + mv + "le"; le = true; width = mv; signed = true; radix = 10; break;
			case $DS__: typeString = "sint" + mv + "be"; le = false; width = mv; signed = true; radix = 10; break;
			case $__SD: typeString = "sint" + mv + "le"; le = true; width = mv; signed = true; radix = 10; break;
			case $DU__: typeString = "uint" + mv + "be"; le = false; width = mv; signed = false; radix = 10; break;
			case $__UD: typeString = "uint" + mv + "le"; le = true; width = mv; signed = false; radix = 10; break;
			// hexadecimal
			case $HNIB: typeString = "hnibbe"; le = false; width = 4; signed = false; radix = 16; break;
			case $BINH: typeString = "hnible"; le = true; width = 4; signed = false; radix = 16; break;
			case $HBYT: typeString = "hbytebe"; le = false; width = 8; signed = false; radix = 16; break;
			case $TYBH: typeString = "hbytele"; le = true; width = 8; signed = false; radix = 16; break;
			case $HWRD: typeString = "hshortbe"; le = false; width = 16; signed = false; radix = 16; break;
			case $DRWH: typeString = "hshortle"; le = true; width = 16; signed = false; radix = 16; break;
			case $HLNG: typeString = "hintbe"; le = false; width = 32; signed = false; radix = 16; break;
			case $GNLH: typeString = "hintle"; le = true; width = 32; signed = false; radix = 16; break;
			case $HLLG: typeString = "hlongbe"; le = false; width = 64; signed = false; radix = 16; break;
			case $GLLH: typeString = "hlongle"; le = true; width = 64; signed = false; radix = 16; break;
			case $HI__: typeString = "hint" + mv + "be"; le = false; width = mv; signed = false; radix = 16; break;
			case $__IH: typeString = "hint" + mv + "le"; le = true; width = mv; signed = false; radix = 16; break;
			case $HS__: typeString = "hsint" + mv + "be"; le = false; width = mv; signed = true; radix = 16; break;
			case $__SH: typeString = "hsint" + mv + "le"; le = true; width = mv; signed = true; radix = 16; break;
			case $HU__: typeString = "huint" + mv + "be"; le = false; width = mv; signed = false; radix = 16; break;
			case $__UH: typeString = "huint" + mv + "le"; le = true; width = mv; signed = false; radix = 16; break;
			// octal
			case $ONIB: typeString = "onibbe"; le = false; width = 4; signed = false; radix = 8; break;
			case $BINO: typeString = "onible"; le = true; width = 4; signed = false; radix = 8; break;
			case $OBYT: typeString = "obytebe"; le = false; width = 8; signed = false; radix = 8; break;
			case $TYBO: typeString = "obytele"; le = true; width = 8; signed = false; radix = 8; break;
			case $OWRD: typeString = "oshortbe"; le = false; width = 16; signed = false; radix = 8; break;
			case $DRWO: typeString = "oshortle"; le = true; width = 16; signed = false; radix = 8; break;
			case $OLNG: typeString = "ointbe"; le = false; width = 32; signed = false; radix = 8; break;
			case $GNLO: typeString = "ointle"; le = true; width = 32; signed = false; radix = 8; break;
			case $OLLG: typeString = "olongbe"; le = false; width = 64; signed = false; radix = 8; break;
			case $GLLO: typeString = "olongle"; le = true; width = 64; signed = false; radix = 8; break;
			case $OI__: typeString = "oint" + mv + "be"; le = false; width = mv; signed = false; radix = 8; break;
			case $__IO: typeString = "oint" + mv + "le"; le = true; width = mv; signed = false; radix = 8; break;
			case $OS__: typeString = "osint" + mv + "be"; le = false; width = mv; signed = true; radix = 8; break;
			case $__SO: typeString = "osint" + mv + "le"; le = true; width = mv; signed = true; radix = 8; break;
			case $OU__: typeString = "ouint" + mv + "be"; le = false; width = mv; signed = false; radix = 8; break;
			case $__UO: typeString = "ouint" + mv + "le"; le = true; width = mv; signed = false; radix = 8; break;
			// binary
			case $BNIB: typeString = "bnibbe"; le = false; width = 4; signed = false; radix = 2; break;
			case $BINB: typeString = "bnible"; le = true; width = 4; signed = false; radix = 2; break;
			case $BBYT: typeString = "bbytebe"; le = false; width = 8; signed = false; radix = 2; break;
			case $TYBB: typeString = "bbytele"; le = true; width = 8; signed = false; radix = 2; break;
			case $BWRD: typeString = "bshortbe"; le = false; width = 16; signed = false; radix = 2; break;
			case $DRWB: typeString = "bshortle"; le = true; width = 16; signed = false; radix = 2; break;
			case $BLNG: typeString = "bintbe"; le = false; width = 32; signed = false; radix = 2; break;
			case $GNLB: typeString = "bintle"; le = true; width = 32; signed = false; radix = 2; break;
			case $BLLG: typeString = "blongbe"; le = false; width = 64; signed = false; radix = 2; break;
			case $GLLB: typeString = "blongle"; le = true; width = 64; signed = false; radix = 2; break;
			case $BI__: typeString = "bint" + mv + "be"; le = false; width = mv; signed = false; radix = 2; break;
			case $__IB: typeString = "bint" + mv + "le"; le = true; width = mv; signed = false; radix = 2; break;
			case $BS__: typeString = "bsint" + mv + "be"; le = false; width = mv; signed = true; radix = 2; break;
			case $__SB: typeString = "bsint" + mv + "le"; le = true; width = mv; signed = true; radix = 2; break;
			case $BU__: typeString = "buint" + mv + "be"; le = false; width = mv; signed = false; radix = 2; break;
			case $__UB: typeString = "buint" + mv + "le"; le = true; width = mv; signed = false; radix = 2; break;
			default: return null;
		}
		return new IntegerItem(it.type, typeString, it.label, le, width, signed, radix);
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		String typeString; int width; boolean signed; int radix;
		switch (mtc) {
			// decimal
			case $DNIB: typeString = "nibbe"; width = 4; signed = true; radix = 10; break;
			case $BIND: typeString = "nible"; width = 4; signed = true; radix = 10; break;
			case $DBYT: typeString = "bytebe"; width = 8; signed = true; radix = 10; break;
			case $TYBD: typeString = "bytele"; width = 8; signed = true; radix = 10; break;
			case $DWRD: typeString = "shortbe"; width = 16; signed = true; radix = 10; break;
			case $DRWD: typeString = "shortle"; width = 16; signed = true; radix = 10; break;
			case $DLNG: typeString = "intbe"; width = 32; signed = true; radix = 10; break;
			case $GNLD: typeString = "intle"; width = 32; signed = true; radix = 10; break;
			case $DLLG: typeString = "longbe"; width = 64; signed = true; radix = 10; break;
			case $GLLD: typeString = "longle"; width = 64; signed = true; radix = 10; break;
			case $DI__: typeString = "int" + mv + "be"; width = mv; signed = true; radix = 10; break;
			case $__ID: typeString = "int" + mv + "le"; width = mv; signed = true; radix = 10; break;
			case $DS__: typeString = "sint" + mv + "be"; width = mv; signed = true; radix = 10; break;
			case $__SD: typeString = "sint" + mv + "le"; width = mv; signed = true; radix = 10; break;
			case $DU__: typeString = "uint" + mv + "be"; width = mv; signed = false; radix = 10; break;
			case $__UD: typeString = "uint" + mv + "le"; width = mv; signed = false; radix = 10; break;
			// hexadecimal
			case $HNIB: typeString = "hnibbe"; width = 4; signed = false; radix = 16; break;
			case $BINH: typeString = "hnible"; width = 4; signed = false; radix = 16; break;
			case $HBYT: typeString = "hbytebe"; width = 8; signed = false; radix = 16; break;
			case $TYBH: typeString = "hbytele"; width = 8; signed = false; radix = 16; break;
			case $HWRD: typeString = "hshortbe"; width = 16; signed = false; radix = 16; break;
			case $DRWH: typeString = "hshortle"; width = 16; signed = false; radix = 16; break;
			case $HLNG: typeString = "hintbe"; width = 32; signed = false; radix = 16; break;
			case $GNLH: typeString = "hintle"; width = 32; signed = false; radix = 16; break;
			case $HLLG: typeString = "hlongbe"; width = 64; signed = false; radix = 16; break;
			case $GLLH: typeString = "hlongle"; width = 64; signed = false; radix = 16; break;
			case $HI__: typeString = "hint" + mv + "be"; width = mv; signed = false; radix = 16; break;
			case $__IH: typeString = "hint" + mv + "le"; width = mv; signed = false; radix = 16; break;
			case $HS__: typeString = "hsint" + mv + "be"; width = mv; signed = true; radix = 16; break;
			case $__SH: typeString = "hsint" + mv + "le"; width = mv; signed = true; radix = 16; break;
			case $HU__: typeString = "huint" + mv + "be"; width = mv; signed = false; radix = 16; break;
			case $__UH: typeString = "huint" + mv + "le"; width = mv; signed = false; radix = 16; break;
			// octal
			case $ONIB: typeString = "onibbe"; width = 4; signed = false; radix = 8; break;
			case $BINO: typeString = "onible"; width = 4; signed = false; radix = 8; break;
			case $OBYT: typeString = "obytebe"; width = 8; signed = false; radix = 8; break;
			case $TYBO: typeString = "obytele"; width = 8; signed = false; radix = 8; break;
			case $OWRD: typeString = "oshortbe"; width = 16; signed = false; radix = 8; break;
			case $DRWO: typeString = "oshortle"; width = 16; signed = false; radix = 8; break;
			case $OLNG: typeString = "ointbe"; width = 32; signed = false; radix = 8; break;
			case $GNLO: typeString = "ointle"; width = 32; signed = false; radix = 8; break;
			case $OLLG: typeString = "olongbe"; width = 64; signed = false; radix = 8; break;
			case $GLLO: typeString = "olongle"; width = 64; signed = false; radix = 8; break;
			case $OI__: typeString = "oint" + mv + "be"; width = mv; signed = false; radix = 8; break;
			case $__IO: typeString = "oint" + mv + "le"; width = mv; signed = false; radix = 8; break;
			case $OS__: typeString = "osint" + mv + "be"; width = mv; signed = true; radix = 8; break;
			case $__SO: typeString = "osint" + mv + "le"; width = mv; signed = true; radix = 8; break;
			case $OU__: typeString = "ouint" + mv + "be"; width = mv; signed = false; radix = 8; break;
			case $__UO: typeString = "ouint" + mv + "le"; width = mv; signed = false; radix = 8; break;
			// binary
			case $BNIB: typeString = "bnibbe"; width = 4; signed = false; radix = 2; break;
			case $BINB: typeString = "bnible"; width = 4; signed = false; radix = 2; break;
			case $BBYT: typeString = "bbytebe"; width = 8; signed = false; radix = 2; break;
			case $TYBB: typeString = "bbytele"; width = 8; signed = false; radix = 2; break;
			case $BWRD: typeString = "bshortbe"; width = 16; signed = false; radix = 2; break;
			case $DRWB: typeString = "bshortle"; width = 16; signed = false; radix = 2; break;
			case $BLNG: typeString = "bintbe"; width = 32; signed = false; radix = 2; break;
			case $GNLB: typeString = "bintle"; width = 32; signed = false; radix = 2; break;
			case $BLLG: typeString = "blongbe"; width = 64; signed = false; radix = 2; break;
			case $GLLB: typeString = "blongle"; width = 64; signed = false; radix = 2; break;
			case $BI__: typeString = "bint" + mv + "be"; width = mv; signed = false; radix = 2; break;
			case $__IB: typeString = "bint" + mv + "le"; width = mv; signed = false; radix = 2; break;
			case $BS__: typeString = "bsint" + mv + "be"; width = mv; signed = true; radix = 2; break;
			case $__SB: typeString = "bsint" + mv + "le"; width = mv; signed = true; radix = 2; break;
			case $BU__: typeString = "buint" + mv + "be"; width = mv; signed = false; radix = 2; break;
			case $__UB: typeString = "buint" + mv + "le"; width = mv; signed = false; radix = 2; break;
			default: return null;
		}
		return new PackedIntegerItem(it.type, typeString, it.label, width, signed, radix);
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"byte&", "short&", "int&", "long&", "int#&", "sint#&", "uint#&",
			"dbyte&", "dshort&", "dint&", "dlong&", "dint#&", "dsint#&", "duint#&",
			"hbyte&", "hshort&", "hint&", "hlong&", "hint#&", "hsint#&", "huint#&",
			"obyte&", "oshort&", "oint&", "olong&", "oint#&", "osint#&", "ouint#&",
			"bbyte&", "bshort&", "bint&", "blong&", "bint#&", "bsint#&", "buint#&",
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"nib&", "byte&", "short&", "int&", "long&", "int#&", "sint#&", "uint#&",
			"dnib&", "dbyte&", "dshort&", "dint&", "dlong&", "dint#&", "dsint#&", "duint#&",
			"hnib&", "hbyte&", "hshort&", "hint&", "hlong&", "hint#&", "hsint#&", "huint#&",
			"onib&", "obyte&", "oshort&", "oint&", "olong&", "oint#&", "osint#&", "ouint#&",
			"bnib&", "bbyte&", "bshort&", "bint&", "blong&", "bint#&", "bsint#&", "buint#&",
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"nib&", "byte&", "short&", "int&", "long&", "int#&", "sint#&", "uint#&",
		"dnib&", "dbyte&", "dshort&", "dint&", "dlong&", "dint#&", "dsint#&", "duint#&",
		"hnib&", "hbyte&", "hshort&", "hint&", "hlong&", "hint#&", "hsint#&", "huint#&",
		"onib&", "obyte&", "oshort&", "oint&", "olong&", "oint#&", "osint#&", "ouint#&",
		"bnib&", "bbyte&", "bshort&", "bint&", "blong&", "bint#&", "bsint#&", "buint#&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int index = MTS.indexOf(mts);
		int tc; int radix;
		switch (index >> 3) {
			case  0: tc = (le ? $___D : $D___); radix = 10; break;
			case  1: tc = (le ? $___D : $D___); radix = 10; break;
			case  2: tc = (le ? $___H : $H___); radix = 16; break;
			case  3: tc = (le ? $___O : $O___); radix = 8; break;
			case  4: tc = (le ? $___B : $B___); radix = 2; break;
			default: return null;
		}
		boolean signed = (radix == 10);
		switch (index & 7) {
			case  0: tc |= (le ? $BIN_ : $_NIB); mv = 4; break;
			case  1: tc |= (le ? $TYB_ : $_BYT); mv = 8; break;
			case  2: tc |= (le ? $DRW_ : $_WRD); mv = 16; break;
			case  3: tc |= (le ? $GNL_ : $_LNG); mv = 32; break;
			case  4: tc |= (le ? $GLL_ : $_LLG); mv = 64; break;
			case  5: tc |= TemplateUtils.maskDecValue($__I_|$_I__, mv, le); break;
			case  6: tc |= TemplateUtils.maskDecValue($__S_|$_S__, mv, le); signed = true; break;
			case  7: tc |= TemplateUtils.maskDecValue($__U_|$_U__, mv, le); signed = false; break;
			default: return null;
		}
		return new IntegerItem(tc, ts, name, le, mv, signed, radix);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		int index = MTS.indexOf(mts);
		int tc; int radix;
		switch (index >> 3) {
			case  0: tc = (le ? $___D : $D___); radix = 10; break;
			case  1: tc = (le ? $___D : $D___); radix = 10; break;
			case  2: tc = (le ? $___H : $H___); radix = 16; break;
			case  3: tc = (le ? $___O : $O___); radix = 8; break;
			case  4: tc = (le ? $___B : $B___); radix = 2; break;
			default: return null;
		}
		boolean signed = (radix == 10);
		switch (index & 7) {
			case  0: tc |= (le ? $BIN_ : $_NIB); mv = 4; break;
			case  1: tc |= (le ? $TYB_ : $_BYT); mv = 8; break;
			case  2: tc |= (le ? $DRW_ : $_WRD); mv = 16; break;
			case  3: tc |= (le ? $GNL_ : $_LNG); mv = 32; break;
			case  4: tc |= (le ? $GLL_ : $_LLG); mv = 64; break;
			case  5: tc |= TemplateUtils.maskDecValue($__I_|$_I__, mv, le); break;
			case  6: tc |= TemplateUtils.maskDecValue($__S_|$_S__, mv, le); signed = true; break;
			case  7: tc |= TemplateUtils.maskDecValue($__U_|$_U__, mv, le); signed = false; break;
			default: return null;
		}
		return new PackedIntegerItem(tc, ts, name, mv, signed, radix);
	}
}
