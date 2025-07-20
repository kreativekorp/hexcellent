package com.kreative.templature.template.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.CountedListBlock;
import com.kreative.templature.template.item.TerminatedListBlock;
import com.kreative.templature.template.item.UnterminatedListBlock;

public class ListBlockFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$ZCNT|TI|00, $TNCZ|TI|00|LE,
			$ZC__|TI|DM, $__CZ|TI|DM|LE,
			$OCNT|TI|00, $TNCO|TI|00|LE,
			$OC__|TI|DM, $__CO|TI|DM|LE,
			$LSTC|TI|00, $CTSL|TI|00|LE,
			$LSTZ|TI|00, $ZTSL|TI|00|LE,
			$LSTB|TI|00, $BTSL|TI|00|LE,
			$LSTE|TI|00, $ETSL|TI|00|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		switch (mtc) {
			case $ZCNT: return createCountedListBlock(factory, in, it, $LSTC, $LSTE, "zcountbe", false, 16, -1);
			case $TNCZ: return createCountedListBlock(factory, in, it, $CTSL, $ETSL, "zcountle", true, 16, -1);
			case $ZC__: return createCountedListBlock(factory, in, it, $LSTC, $LSTE, "zcount" + mv + "be", false, mv, -1);
			case $__CZ: return createCountedListBlock(factory, in, it, $CTSL, $ETSL, "zcount" + mv + "le", true, mv, -1);
			case $OCNT: return createCountedListBlock(factory, in, it, $LSTC, $LSTE, "ocountbe", false, 16, 0);
			case $TNCO: return createCountedListBlock(factory, in, it, $CTSL, $ETSL, "ocountle", true, 16, 0);
			case $OC__: return createCountedListBlock(factory, in, it, $LSTC, $LSTE, "ocount" + mv + "be", false, mv, 0);
			case $__CO: return createCountedListBlock(factory, in, it, $CTSL, $ETSL, "ocount" + mv + "le", true, mv, 0);
			case $LSTC: throw new TemplateException(ERROR_LIST_NO_COUNT, it.type, $ZCNT, $OCNT);
			case $CTSL: throw new TemplateException(ERROR_LIST_NO_COUNT, it.type, $TNCZ, $TNCO);
			case $LSTZ: return createTerminatedListBlock(factory, in, it, $LSTE, 0);
			case $ZTSL: return createTerminatedListBlock(factory, in, it, $ETSL, 0);
			case $LSTB: return createUnterminatedListBlock(factory, in, it, $LSTE);
			case $BTSL: return createUnterminatedListBlock(factory, in, it, $ETSL);
			case $LSTE: throw new TemplateException(ERROR_LIST_END, it.type, $LSTB, $LSTC, $LSTZ);
			case $ETSL: throw new TemplateException(ERROR_LIST_END, it.type, $BTSL, $CTSL, $ZTSL);
			default: return null;
		}
	}
	
	private TemplateItem createCountedListBlock(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in, BinaryTemplateInputStream.Item it1,
		int lstc, int lste, String countTypeString, boolean le, int width, long base
	) throws IOException {
		BinaryTemplateInputStream.Item it2 = in.read();
		if (it2 == null || !(it2.type == $LSTC || it2.type == $CTSL)) throw new TemplateException(ERROR_LIST_COUNTER, it1.type, lstc);
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		for (;;) {
			BinaryTemplateInputStream.Item it3 = in.read();
			if (it3 == null) {
				throw new TemplateException(ERROR_LIST_NO_END, it2.type, lste);
			}
			if (it3.type == $LSTE || it3.type == $ETSL) {
				return new CountedListBlock(
					it1.type, it2.type, it3.type,
					countTypeString, "listc",
					it1.label, it2.label, it3.label,
					le, width, base, items
				);
			}
			items.add(factory.createTemplateItem(in, it3));
		}
	}
	
	private TemplateItem createTerminatedListBlock(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it1, int lste, int z
	) throws IOException {
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		for (;;) {
			BinaryTemplateInputStream.Item it2 = in.read();
			if (it2 == null) {
				throw new TemplateException(ERROR_LIST_NO_END, it1.type, lste);
			}
			if (it2.type == $LSTE || it2.type == $ETSL) {
				return new TerminatedListBlock(it1.type, it2.type, "listz", it1.label, it2.label, z, items);
			}
			items.add(factory.createTemplateItem(in, it2));
		}
	}
	
	private TemplateItem createUnterminatedListBlock(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it1, int lste
	) throws IOException {
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		for (;;) {
			BinaryTemplateInputStream.Item it2 = in.read();
			if (it2 == null) {
				throw new TemplateException(ERROR_LIST_NO_END, it1.type, lste);
			}
			if (it2.type == $LSTB || it2.type == $BTSL) {
				throw new TemplateException(ERROR_LIST_NESTED, it2.type, it1.type);
			}
			if (it2.type == $LSTE || it2.type == $ETSL) {
				return new UnterminatedListBlock(it1.type, it2.type, "list", it1.label, it2.label, items);
			}
			items.add(factory.createTemplateItem(in, it2));
		}
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"zcount&", "zcount#&", "ocount&", "ocount#&", "listc", "listz", "list"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"zcount&", "zcount#&", "ocount&", "ocount#&", "listc", "listz", "list"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		int tc, lstc, lste; long base;
		switch (MTS.indexOf(mts)) {
			case 0: tc = (le?$TNCZ:$ZCNT); lstc = (le?$CTSL:$LSTC); lste = (le?$ETSL:$LSTE); mv = 16; base = -1; break;
			case 1: tc = TemplateUtils.maskDecValue($__CZ|$ZC__,mv,le); lstc = (le?$CTSL:$LSTC); lste = (le?$ETSL:$LSTE); base = -1; break;
			case 2: tc = (le?$TNCO:$OCNT); lstc = (le?$CTSL:$LSTC); lste = (le?$ETSL:$LSTE); mv = 16; base = 0; break;
			case 3: tc = TemplateUtils.maskDecValue($__CO|$OC__,mv,le); lstc = (le?$CTSL:$LSTC); lste = (le?$ETSL:$LSTE); base = 0; break;
			case 4: throw new TemplateException(ERROR_LIST_NO_COUNT, token.value.toString(), "zcount", "ocount");
			case 5: return createTerminatedListBlock(factory, state, tokens, token, (le ? $ZTSL : $LSTZ), (le ? $ETSL : $LSTE), 0);
			case 6: return createUnterminatedListBlock(factory, state, tokens, token, (le ? $BTSL : $LSTB), (le ? $ETSL : $LSTE));
			default: return null;
		}
		return createCountedListBlock(factory, state, tokens, token, tc, lstc, lste, le, mv, base);
	}
	
	private TemplateItem createCountedListBlock(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int tc, int lstc, int lste, boolean le, int width, long base
	) {
		String ts = token.value.toString();
		String countName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		String lts = tokens.nextKeywordOrNull("listc");
		if (lts == null) throw new TemplateException(ERROR_LIST_COUNTER, ts, "listc");
		String beginName = tokens.nextNameOrNull();
		tokens.nextBlockBeginOrThrow();
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		while (!tokens.nextBlockEnd()) {
			items.add(factory.createTemplateItem(state, tokens, tokens.next()));
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		return new CountedListBlock(
			tc, lstc, lste, ts, lts,
			countName, beginName, endName,
			le, width, base, items
		);
	}
	
	private TemplateItem createTerminatedListBlock(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int tc, int lste, int z
	) {
		String ts = token.value.toString();
		String beginName = tokens.nextNameOrNull();
		tokens.nextBlockBeginOrThrow();
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		while (!tokens.nextBlockEnd()) {
			items.add(factory.createTemplateItem(state, tokens, tokens.next()));
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		return new TerminatedListBlock(tc, lste, ts, beginName, endName, z, items);
	}
	
	private TemplateItem createUnterminatedListBlock(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int tc, int lste
	) {
		String ts = token.value.toString();
		String beginName = tokens.nextNameOrNull();
		tokens.nextBlockBeginOrThrow();
		ArrayList<TemplateItem> items = new ArrayList<TemplateItem>();
		while (!tokens.nextBlockEnd()) {
			String lts = tokens.nextKeywordOrNull("list");
			if (lts != null) throw new TemplateException(ERROR_LIST_NESTED, lts, ts);
			items.add(factory.createTemplateItem(state, tokens, tokens.next()));
		}
		String endName = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		return new UnterminatedListBlock(tc, lste, ts, beginName, endName, items);
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
}
