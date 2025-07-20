package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.CommentItem;
import com.kreative.templature.template.item.MetaItem;
import com.kreative.templature.template.item.SetEncodingItem;
import com.kreative.templature.template.item.SetEndiannessItem;
import com.kreative.templature.template.item.SetLineEndingItem;

public class MetaItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$COMM|TI|PI, $MMOC|TI|PI|LE,
			$CMNT|TI|PI, $TNMC|TI|PI|LE,
			$META|TI|PI, $ATEM|TI|PI|LE,
			$CSET|TI|PI, $TESC|TI|PI|LE,
			$TENC|TI|PI, $CNET|TI|PI|LE,
			$LINE|TI|PI, $ENIL|TI|PI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $COMM: case $MMOC:
			case $CMNT: case $TNMC:
				return new CommentItem(it.type, "rem", it.label);
			case $META: case $ATEM:
				return new MetaItem(it.type, "meta", it.label);
			case $CSET: case $TESC:
			case $TENC: case $CNET:
				in.setEncoding(it.label);
				return new SetEncodingItem(it.type, "charset", it.label);
			case $LINE: case $ENIL:
				String nl = getLineEnding(it.label);
				in.setLineEnding(nl);
				return new SetLineEndingItem(it.type, "newline", it.label, nl);
			default:
				return null;
		}
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $COMM: case $MMOC:
			case $CMNT: case $TNMC:
				return new CommentItem(it.type, "rem", it.label);
			case $META: case $ATEM:
				return new MetaItem(it.type, "meta", it.label);
			case $CSET: case $TESC:
			case $TENC: case $CNET:
				in.setEncoding(it.label);
				return new SetEncodingItem(it.type, "charset", it.label);
			case $LINE: case $ENIL:
				String nl = getLineEnding(it.label);
				in.setLineEnding(nl);
				return new SetLineEndingItem(it.type, "newline", it.label, nl);
			default:
				return null;
		}
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"rem", "comment", "meta", "charset", "endian", "endianness", "newline"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"rem", "comment", "meta", "charset", "endian", "endianness", "newline"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"rem", "comment", "meta", "charset", "endian", "endianness", "newline"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0: case 1: return createCommentItem(tokens, token, (le ? $MMOC : $COMM));
			case 2: return createMetaItem(tokens, token, (le ? $ATEM : $META));
			case 3: return createSetEncodingItem(state, tokens, token, (le ? $TESC : $CSET));
			case 4: case 5: return createSetEndiannessItem(state, tokens, token);
			case 6: return createSetLineEndingItem(state, tokens, token, (le ? $ENIL : $LINE));
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0: case 1: return createCommentItem(tokens, token, (le ? $MMOC : $COMM));
			case 2: return createMetaItem(tokens, token, (le ? $ATEM : $META));
			case 3: return createSetEncodingItem(state, tokens, token, (le ? $TESC : $CSET));
			case 4: case 5: return createSetEndiannessItem(state, tokens, token);
			case 6: return createSetLineEndingItem(state, tokens, token, (le ? $ENIL : $LINE));
			default: return null;
		}
	}
	
	private CommentItem createCommentItem(TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, int tc) {
		String ts = token.value.toString();
		String s = tokens.nextValueStringOrThrow();
		tokens.nextStatementEndOrThrow();
		return new CommentItem(tc, ts, s);
	}
	
	private MetaItem createMetaItem(TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, int tc) {
		String ts = token.value.toString();
		String s = tokens.nextValueStringOrThrow();
		tokens.nextStatementEndOrThrow();
		return new MetaItem(tc, ts, s);
	}
	
	private SetEncodingItem createSetEncodingItem(
		TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int tc
	) {
		String ts = token.value.toString();
		String s = tokens.nextValueStringOrThrow();
		tokens.nextStatementEndOrThrow();
		state.setEncoding(s);
		return new SetEncodingItem(tc, ts, s);
	}
	
	private SetEndiannessItem createSetEndiannessItem(
		TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token
	) {
		String ts = token.value.toString();
		String s = tokens.nextKeywordOrThrow("be", "le", "big", "little");
		tokens.nextStatementEndOrThrow();
		state.setLittleEndian(s.equalsIgnoreCase("le") || s.equalsIgnoreCase("little"));
		return new SetEndiannessItem(ts, s);
	}
	
	private SetLineEndingItem createSetLineEndingItem(
		TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int tc
	) {
		String ts = token.value.toString();
		String s = tokens.nextNameOrThrow();
		tokens.nextStatementEndOrThrow();
		String nl = getLineEnding(s);
		state.setLineEnding(nl);
		return new SetLineEndingItem(tc, ts, s, nl);
	}
	
	private static String getLineEnding(String name) {
		if (name.equalsIgnoreCase("CRLF")) return "\r\n";
		if (name.equalsIgnoreCase("CR")) return "\r";
		if (name.equalsIgnoreCase("LF")) return "\n";
		if (name.equalsIgnoreCase("NL")) return "\u0085";
		if (name.equalsIgnoreCase("NEL")) return "\u0085";
		if (name.equalsIgnoreCase("LSEP")) return "\u2028";
		if (name.equalsIgnoreCase("PSEP")) return "\u2029";
		return name;
	}
}
