package com.kreative.templature.template.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.HexItem;
import com.kreative.templature.template.item.TheRestIsHexItem;

public class HexItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$H___|TI|HM, $___H|TI|HM|LE,
			$HEXD|TI|00, $DXEH|TI|00|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		switch (mtc) {
			case $H___: return new HexItem(it.type, "hex" + mv + "be", it.label, mv, in.getEncoding(), false);
			case $___H: return new HexItem(it.type, "hex" + mv + "le", it.label, mv, in.getEncoding(), true);
			case $HEXD:
				if (in.read() != null) throw new TemplateException(ERROR_HEX_NOT_END, it.type);
				return new TheRestIsHexItem(it.type, "hexdumpbe", it.label, in.getEncoding(), false);
			case $DXEH:
				if (in.read() != null) throw new TemplateException(ERROR_HEX_NOT_END, it.type);
				return new TheRestIsHexItem(it.type, "hexdumple", it.label, in.getEncoding(), true);
			default: return null;
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
			"hex$&", "hexdump&"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"hex$&", "hexdump&"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		switch (MTS.indexOf(mts)) {
			case 0: return new HexItem(TemplateUtils.maskHexValue($___H|$H___, mv, le), ts, name, mv, state.getEncoding(), le);
			case 1:
				if (tokens.hasNextType()) throw new TemplateException(ERROR_HEX_NOT_END, ts);
				return new TheRestIsHexItem((le ? $DXEH : $HEXD), ts, name, state.getEncoding(), le);
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
