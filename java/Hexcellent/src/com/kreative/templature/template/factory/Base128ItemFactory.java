package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.Base128Item;
import com.kreative.templature.template.item.VLFPItem;
import com.kreative.templature.template.item.VLPDItem;

public class Base128ItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$DIVL|TI, $LVID|TI|LE, // decimal integer VLQ/Base128
			$DSVL|TI, $LVSD|TI|LE, // decimal signed VLQ/Base128
			$DUVL|TI, $LVUD|TI|LE, // decimal unsigned VLQ/Base128
			$HIVL|TI, $LVIH|TI|LE, // hexadecimal integer VLQ/Base128
			$HSVL|TI, $LVSH|TI|LE, // hexadecimal signed VLQ/Base128
			$HUVL|TI, $LVUH|TI|LE, // hexadecimal unsigned VLQ/Base128
			$OIVL|TI, $LVIO|TI|LE, // octal integer VLQ/Base128
			$OSVL|TI, $LVSO|TI|LE, // octal signed VLQ/Base128
			$OUVL|TI, $LVUO|TI|LE, // octal unsigned VLQ/Base128
			$BIVL|TI, $LVIB|TI|LE, // binary integer VLQ/Base128
			$BSVL|TI, $LVSB|TI|LE, // binary signed VLQ/Base128
			$BUVL|TI, $LVUB|TI|LE, // binary unsigned VLQ/Base128
			$VLPD|TI, $DPLV|TI|LE, // variable length packed decimal
			$VLFP|TI, $PFLV|TI|LE, // variable length floating point
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			// decimal
			case $DIVL: return new Base128Item(it.type, "beb128", it.label, false, true, 10);
			case $LVID: return new Base128Item(it.type, "leb128", it.label, true, true, 10);
			case $DSVL: return new Base128Item(it.type, "sbeb128", it.label, false, true, 10);
			case $LVSD: return new Base128Item(it.type, "sleb128", it.label, true, true, 10);
			case $DUVL: return new Base128Item(it.type, "ubeb128", it.label, false, false, 10);
			case $LVUD: return new Base128Item(it.type, "uleb128", it.label, true, false, 10);
			// hexadecimal
			case $HIVL: return new Base128Item(it.type, "hbeb128", it.label, false, false, 16);
			case $LVIH: return new Base128Item(it.type, "hleb128", it.label, true, false, 16);
			case $HSVL: return new Base128Item(it.type, "hsbeb128", it.label, false, true, 16);
			case $LVSH: return new Base128Item(it.type, "hsleb128", it.label, true, true, 16);
			case $HUVL: return new Base128Item(it.type, "hubeb128", it.label, false, false, 16);
			case $LVUH: return new Base128Item(it.type, "huleb128", it.label, true, false, 16);
			// octal
			case $OIVL: return new Base128Item(it.type, "obeb128", it.label, false, false, 8);
			case $LVIO: return new Base128Item(it.type, "oleb128", it.label, true, false, 8);
			case $OSVL: return new Base128Item(it.type, "osbeb128", it.label, false, true, 8);
			case $LVSO: return new Base128Item(it.type, "osleb128", it.label, true, true, 8);
			case $OUVL: return new Base128Item(it.type, "oubeb128", it.label, false, false, 8);
			case $LVUO: return new Base128Item(it.type, "ouleb128", it.label, true, false, 8);
			// binary
			case $BIVL: return new Base128Item(it.type, "bbeb128", it.label, false, false, 2);
			case $LVIB: return new Base128Item(it.type, "bleb128", it.label, true, false, 2);
			case $BSVL: return new Base128Item(it.type, "bsbeb128", it.label, false, true, 2);
			case $LVSB: return new Base128Item(it.type, "bsleb128", it.label, true, true, 2);
			case $BUVL: return new Base128Item(it.type, "bubeb128", it.label, false, false, 2);
			case $LVUB: return new Base128Item(it.type, "buleb128", it.label, true, false, 2);
			// packed decimal
			case $VLPD: return new VLPDItem(it.type, "vlpdbe", it.label, false, 10);
			case $DPLV: return new VLPDItem(it.type, "vlpdle", it.label, true, 10);
			// floating point
			case $VLFP: return new VLFPItem(it.type, "vlfpbe", it.label, false, true);
			case $PFLV: return new VLFPItem(it.type, "vlfple", it.label, true, true);
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
			"beb128^", "leb128!", "sbeb128^", "sleb128!", "ubeb128^", "uleb128!",
			"dbeb128^", "dleb128!", "dsbeb128^", "dsleb128!", "dubeb128^", "duleb128!",
			"hbeb128^", "hleb128!", "hsbeb128^", "hsleb128!", "hubeb128^", "huleb128!",
			"obeb128^", "oleb128!", "osbeb128^", "osleb128!", "oubeb128^", "ouleb128!",
			"bbeb128^", "bleb128!", "bsbeb128^", "bsleb128!", "bubeb128^", "buleb128!",
			"vlpd&", "vlfp&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"beb128^", "leb128!", "sbeb128^", "sleb128!", "ubeb128^", "uleb128!",
		"dbeb128^", "dleb128!", "dsbeb128^", "dsleb128!", "dubeb128^", "duleb128!",
		"hbeb128^", "hleb128!", "hsbeb128^", "hsleb128!", "hubeb128^", "huleb128!",
		"obeb128^", "oleb128!", "osbeb128^", "osleb128!", "oubeb128^", "ouleb128!",
		"bbeb128^", "bleb128!", "bsbeb128^", "bsleb128!", "bubeb128^", "buleb128!",
		"vlpd&", "vlfp&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		switch (MTS.indexOf(mts)) {
			// decimal
			case 0: case 6: return new Base128Item($DIVL, ts, name, false, true, 10);
			case 1: case 7: return new Base128Item($LVID, ts, name, true, true, 10);
			case 2: case 8: return new Base128Item($DSVL, ts, name, false, true, 10);
			case 3: case 9: return new Base128Item($LVSD, ts, name, true, true, 10);
			case 4: case 10: return new Base128Item($DUVL, ts, name, false, false, 10);
			case 5: case 11: return new Base128Item($LVUD, ts, name, true, false, 10);
			// hexadecimal
			case 12: return new Base128Item($HIVL, ts, name, false, false, 16);
			case 13: return new Base128Item($LVIH, ts, name, true, false, 16);
			case 14: return new Base128Item($HSVL, ts, name, false, true, 16);
			case 15: return new Base128Item($LVSH, ts, name, true, true, 16);
			case 16: return new Base128Item($HUVL, ts, name, false, false, 16);
			case 17: return new Base128Item($LVUH, ts, name, true, false, 16);
			// octal
			case 18: return new Base128Item($OIVL, ts, name, false, false, 8);
			case 19: return new Base128Item($LVIO, ts, name, true, false, 8);
			case 20: return new Base128Item($OSVL, ts, name, false, true, 8);
			case 21: return new Base128Item($LVSO, ts, name, true, true, 8);
			case 22: return new Base128Item($OUVL, ts, name, false, false, 8);
			case 23: return new Base128Item($LVUO, ts, name, true, false, 8);
			// binary
			case 24: return new Base128Item($BIVL, ts, name, false, false, 2);
			case 25: return new Base128Item($LVIB, ts, name, true, false, 2);
			case 26: return new Base128Item($BSVL, ts, name, false, true, 2);
			case 27: return new Base128Item($LVSB, ts, name, true, true, 2);
			case 28: return new Base128Item($BUVL, ts, name, false, false, 2);
			case 29: return new Base128Item($LVUB, ts, name, true, false, 2);
			// packed decimal / floating point
			case 30: return new VLPDItem((le ? $DPLV : $VLPD), ts, name, le, 10);
			case 31: return new VLFPItem((le ? $PFLV : $VLFP), ts, name, le, true);
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
