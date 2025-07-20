package com.kreative.templature.template.factory;

import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.HeadingItem;
import com.kreative.templature.template.item.ParagraphItem;
import com.kreative.templature.template.item.SeparatorItem;

public class StaticItemFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$HDNG|TI|PI, $GNDH|TI|PI|LE,
			$HDG1|TI|PI, $1GDH|TI|PI|LE,
			$HDG2|TI|PI, $2GDH|TI|PI|LE,
			$HDG3|TI|PI, $3GDH|TI|PI|LE,
			$HDG4|TI|PI, $4GDH|TI|PI|LE,
			$HDG5|TI|PI, $5GDH|TI|PI|LE,
			$HDG6|TI|PI, $6GDH|TI|PI|LE,
			$PARA|TI|PI, $ARAP|TI|PI|LE,
			$LABL|TI|PI, $LBAL|TI|PI|LE,
			$SEPA|TI|PI, $APES|TI|PI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $HDNG: case $GNDH: return new HeadingItem(it.type, "h", it.label, 1);
			case $HDG1: case $1GDH: return new HeadingItem(it.type, "h1", it.label, 1);
			case $HDG2: case $2GDH: return new HeadingItem(it.type, "h2", it.label, 2);
			case $HDG3: case $3GDH: return new HeadingItem(it.type, "h3", it.label, 3);
			case $HDG4: case $4GDH: return new HeadingItem(it.type, "h4", it.label, 4);
			case $HDG5: case $5GDH: return new HeadingItem(it.type, "h5", it.label, 5);
			case $HDG6: case $6GDH: return new HeadingItem(it.type, "h6", it.label, 6);
			case $PARA: case $ARAP: return new ParagraphItem(it.type, "p", it.label);
			case $LABL: case $LBAL: return new ParagraphItem(it.type, "p", it.label);
			case $SEPA: case $APES: return new SeparatorItem(it.type, "hr", it.label);
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) {
		switch (mtc) {
			case $HDNG: case $GNDH: return new HeadingItem(it.type, "h", it.label, 1);
			case $HDG1: case $1GDH: return new HeadingItem(it.type, "h1", it.label, 1);
			case $HDG2: case $2GDH: return new HeadingItem(it.type, "h2", it.label, 2);
			case $HDG3: case $3GDH: return new HeadingItem(it.type, "h3", it.label, 3);
			case $HDG4: case $4GDH: return new HeadingItem(it.type, "h4", it.label, 4);
			case $HDG5: case $5GDH: return new HeadingItem(it.type, "h5", it.label, 5);
			case $HDG6: case $6GDH: return new HeadingItem(it.type, "h6", it.label, 6);
			case $PARA: case $ARAP: return new ParagraphItem(it.type, "p", it.label);
			case $LABL: case $LBAL: return new ParagraphItem(it.type, "p", it.label);
			case $SEPA: case $APES: return new SeparatorItem(it.type, "hr", it.label);
			default: return null;
		}
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"h", "h1", "h2", "h3", "h4", "h5", "h6", "p", "hr"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return new String[] {
			"h", "h1", "h2", "h3", "h4", "h5", "h6", "p", "hr"
		};
	}
	
	private static final List<String> MTS = Arrays.asList(
		"h", "h1", "h2", "h3", "h4", "h5", "h6", "p", "hr"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0: return createHeadingItem(tokens, token, (le ? $GNDH : $HDNG), 1);
			case 1: return createHeadingItem(tokens, token, (le ? $1GDH : $HDG1), 1);
			case 2: return createHeadingItem(tokens, token, (le ? $2GDH : $HDG2), 2);
			case 3: return createHeadingItem(tokens, token, (le ? $3GDH : $HDG3), 3);
			case 4: return createHeadingItem(tokens, token, (le ? $4GDH : $HDG4), 4);
			case 5: return createHeadingItem(tokens, token, (le ? $5GDH : $HDG5), 5);
			case 6: return createHeadingItem(tokens, token, (le ? $6GDH : $HDG6), 6);
			case 7: return createParagraphItem(tokens, token, (le ? $ARAP : $PARA));
			case 8: return createSeparatorItem(tokens, token, (le ? $APES : $SEPA));
			default: return null;
		}
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0: return createHeadingItem(tokens, token, (le ? $GNDH : $HDNG), 1);
			case 1: return createHeadingItem(tokens, token, (le ? $1GDH : $HDG1), 1);
			case 2: return createHeadingItem(tokens, token, (le ? $2GDH : $HDG2), 2);
			case 3: return createHeadingItem(tokens, token, (le ? $3GDH : $HDG3), 3);
			case 4: return createHeadingItem(tokens, token, (le ? $4GDH : $HDG4), 4);
			case 5: return createHeadingItem(tokens, token, (le ? $5GDH : $HDG5), 5);
			case 6: return createHeadingItem(tokens, token, (le ? $6GDH : $HDG6), 6);
			case 7: return createParagraphItem(tokens, token, (le ? $ARAP : $PARA));
			case 8: return createSeparatorItem(tokens, token, (le ? $APES : $SEPA));
			default: return null;
		}
	}
	
	private HeadingItem createHeadingItem(TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, int tc, int level) {
		String ts = token.value.toString();
		String text = tokens.nextValueStringOrThrow();
		tokens.nextStatementEndOrThrow();
		return new HeadingItem(tc, ts, text, level);
	}
	
	private ParagraphItem createParagraphItem(TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, int tc) {
		String ts = token.value.toString();
		String text = tokens.nextValueStringOrThrow();
		tokens.nextStatementEndOrThrow();
		return new ParagraphItem(tc, ts, text);
	}
	
	private SeparatorItem createSeparatorItem(TextTemplateTokenStream tokens, TextTemplateTokenStream.Token token, int tc) {
		String ts = token.value.toString();
		String name = tokens.nextNameOrNull();
		tokens.nextStatementEndOrThrow();
		return new SeparatorItem(tc, ts, name);
	}
}
