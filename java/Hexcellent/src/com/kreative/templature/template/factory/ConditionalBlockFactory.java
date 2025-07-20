package com.kreative.templature.template.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.ConditionalBlock;

public class ConditionalBlockFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$CONB|TI, $BNOC|TI|LE,
			$CONC|TI, $CNOC|TI|LE,
			$COND|TI, $DNOC|TI|LE,
			$CONE|TI, $ENOC|TI|LE,
			$IF__|TI, $__FI|TI|LE,
			$IFTH|TI, $HTFI|TI|LE,
			$ELIF|TI, $FILE|TI|LE,
			$ELSE|TI, $ESLE|TI|LE,
			$ENIF|TI, $FINE|TI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		switch (mtc) {
			case $CONB: return createConditionalBlock(factory, in, it, $CONE);
			case $BNOC: return createConditionalBlock(factory, in, it, $ENOC);
			case $CONC: throw new TemplateException(ERROR_COND_CASE, it.type, $CONB);
			case $CNOC: throw new TemplateException(ERROR_COND_CASE, it.type, $BNOC);
			case $COND: throw new TemplateException(ERROR_COND_DEF, it.type, $CONB);
			case $DNOC: throw new TemplateException(ERROR_COND_DEF, it.type, $BNOC);
			case $CONE: throw new TemplateException(ERROR_COND_END, it.type, $CONB);
			case $ENOC: throw new TemplateException(ERROR_COND_END, it.type, $BNOC);
			case $IF__: case $IFTH: return createConditionalBlock(factory, in, it, $ENIF);
			case $__FI: case $HTFI: return createConditionalBlock(factory, in, it, $FINE);
			case $ELIF: throw new TemplateException(ERROR_COND_CASE, it.type, $IFTH);
			case $FILE: throw new TemplateException(ERROR_COND_CASE, it.type, $HTFI);
			case $ELSE: throw new TemplateException(ERROR_COND_DEF, it.type, $IFTH);
			case $ESLE: throw new TemplateException(ERROR_COND_DEF, it.type, $HTFI);
			case $ENIF: throw new TemplateException(ERROR_COND_END, it.type, $IFTH);
			case $FINE: throw new TemplateException(ERROR_COND_END, it.type, $HTFI);
			default: return null;
		}
	}
	
	private TemplateItem createConditionalBlock(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int cone
	) throws IOException {
		ArrayList<Integer> blockTypeConstant = new ArrayList<Integer>();
		ArrayList<String> blockTypeString = new ArrayList<String>();
		ArrayList<String> binBlockExprString = new ArrayList<String>();
		ArrayList<String> textBlockExprString = new ArrayList<String>();
		ArrayList<Expression> blockExpression = new ArrayList<Expression>();
		ArrayList<Template> blockContents = new ArrayList<Template>();
		Template blockContent; int blockCount = 0;
		// Create an "if" block.
		blockTypeConstant.add(it.type);
		blockTypeString.add("if");
		binBlockExprString.add(it.label);
		textBlockExprString.add(TextTemplateTokenStream.join(TextTemplateTokenStream.split(it.label), true));
		blockExpression.add(compileOrThrow(it, ERROR_SYNTAX_CONB));
		blockContents.add((blockContent = new Template()));
		blockCount++;
		for (;;) {
			// We are in an "if" or "else if" block.
			it = in.read();
			if (it == null) {
				throw new TemplateException(ERROR_COND_NO_END, blockTypeConstant.get(0), cone);
			} else if (it.type == $CONC || it.type == $CNOC || it.type == $ELIF || it.type == $FILE) {
				// Create an "else if" block.
				blockTypeConstant.add(it.type);
				blockTypeString.add("elif");
				binBlockExprString.add(it.label);
				textBlockExprString.add(TextTemplateTokenStream.join(TextTemplateTokenStream.split(it.label), true));
				blockExpression.add(compileOrThrow(it, ERROR_SYNTAX_CONC));
				blockContents.add((blockContent = new Template()));
				blockCount++;
				continue;
			} else if (it.type == $COND || it.type == $DNOC || it.type == $ELSE || it.type == $ESLE) {
				// Create an "else" block.
				blockTypeConstant.add(it.type);
				blockTypeString.add("else");
				binBlockExprString.add(it.label);
				textBlockExprString.add(it.label);
				blockExpression.add(null);
				blockContents.add((blockContent = new Template()));
				blockCount++;
				for (;;) {
					// We are in an "else" block.
					it = in.read();
					if (it == null) {
						throw new TemplateException(ERROR_COND_NO_END, blockTypeConstant.get(0), cone);
					} else if (it.type == $CONC || it.type == $CNOC || it.type == $ELIF || it.type == $FILE) {
						throw new TemplateException(ERROR_COND_DEAD_CASE, it.type, blockTypeConstant.get(blockCount - 1));
					} else if (it.type == $COND || it.type == $DNOC || it.type == $ELSE || it.type == $ESLE) {
						throw new TemplateException(ERROR_COND_DEAD_DEF, it.type, blockTypeConstant.get(blockCount - 1));
					} else if (it.type == $CONE || it.type == $ENOC || it.type == $ENIF || it.type == $FINE) {
						// Return a completed structure.
						return new ConditionalBlock(
							blockTypeConstant, it.type, blockTypeString,
							binBlockExprString, textBlockExprString, it.label,
							blockExpression, blockContents, blockCount
						);
					} else {
						// Add items to an "else" block.
						blockContent.add(factory.createTemplateItem(in, it));
					}
				}
			} else if (it.type == $CONE || it.type == $ENOC || it.type == $ENIF || it.type == $FINE) {
				// Return a completed structure.
				return new ConditionalBlock(
					blockTypeConstant, it.type, blockTypeString,
					binBlockExprString, textBlockExprString, it.label,
					blockExpression, blockContents, blockCount
				);
			} else {
				// Add items to an "if" or "else if" block.
				blockContent.add(factory.createTemplateItem(in, it));
			}
		}
	}
	
	private Expression compileOrThrow(BinaryTemplateInputStream.Item it, String format) {
		try { return Expression.compile(it.label); }
		catch (ExpressionException e) { throw new TemplateException(format, it.type, e.getMessage()); }
	}
	
	public PackedItem createPackedItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		return null;
	}
	
	public String[] getTemplateTypeStrings() {
		return new String[] {
			"if", "elif", "elsif", "elseif", "else"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"if", "elif", "elsif", "elseif", "else"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0:
				return createConditionalBlock(
					factory, state, tokens, token,
					(le?$BNOC:$CONB), (le?$CNOC:$CONC),
					(le?$DNOC:$COND), (le?$ENOC:$CONE)
				);
			case 1: case 2: case 3:
				throw new TemplateException(ERROR_COND_CASE, token.value.toString(), "if");
			case 4:
				throw new TemplateException(ERROR_COND_DEF, token.value.toString(), "if");
			default:
				return null;
		}
	}
	
	private TemplateItem createConditionalBlock(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int conb, int conc, int cond, int cone
	) {
		ArrayList<Integer> blockTypeConstant = new ArrayList<Integer>();
		ArrayList<String> blockTypeString = new ArrayList<String>();
		ArrayList<String> binBlockExprString = new ArrayList<String>();
		ArrayList<String> textBlockExprString = new ArrayList<String>();
		ArrayList<Expression> blockExpression = new ArrayList<Expression>();
		ArrayList<Template> blockContents = new ArrayList<Template>();
		Template blockContent; int blockCount = 0;
		// Create an "if" block.
		String ts = token.value.toString();
		List<TextTemplateTokenStream.Token> et = getExpressionTokens(tokens);
		tokens.nextBlockBeginOrThrow();
		blockTypeConstant.add(conb);
		blockTypeString.add(ts);
		binBlockExprString.add(TextTemplateTokenStream.join(et, false));
		textBlockExprString.add(TextTemplateTokenStream.join(et, true));
		blockExpression.add(compileOrThrow(ts, et, ERROR_SYNTAX_CONB));
		blockContents.add((blockContent = new Template()));
		blockCount++;
		while (!tokens.nextBlockEnd()) {
			blockContent.add(factory.createTemplateItem(state, tokens, tokens.next()));
		}
		for (;;) {
			// We are at the end of an "if" or "else if" block.
			String elseToken = tokens.nextKeywordOrNull("else");
			if (elseToken != null) {
				String ifToken = tokens.nextKeywordOrNull("if");
				if (ifToken != null) {
					ts = elseToken + " " + ifToken;
				} else {
					// Create an "else" block.
					String name = tokens.nextNameOrNull();
					tokens.nextBlockBeginOrThrow();
					blockTypeConstant.add(cond);
					blockTypeString.add(elseToken);
					binBlockExprString.add(name);
					textBlockExprString.add(name);
					blockExpression.add(null);
					blockContents.add((blockContent = new Template()));
					blockCount++;
					while (!tokens.nextBlockEnd()) {
						blockContent.add(factory.createTemplateItem(state, tokens, tokens.next()));
					}
					// We are at the end of an "else" block.
					// Return a completed structure.
					name = tokens.nextNameOrNull();
					tokens.nextStatementEndOrThrow();
					return new ConditionalBlock(
						blockTypeConstant, cone, blockTypeString,
						binBlockExprString, textBlockExprString, name,
						blockExpression, blockContents, blockCount
					);
				}
			} else {
				String elseifToken = tokens.nextKeywordOrNull("elif", "elsif", "elseif");
				if (elseifToken != null) {
					ts = elseifToken;
				} else {
					// Return a completed structure.
					String name = tokens.nextNameOrNull();
					tokens.nextStatementEndOrThrow();
					return new ConditionalBlock(
						blockTypeConstant, cone, blockTypeString,
						binBlockExprString, textBlockExprString, name,
						blockExpression, blockContents, blockCount
					);
				}
			}
			// Create an "else if" block.
			et = getExpressionTokens(tokens);
			tokens.nextBlockBeginOrThrow();
			blockTypeConstant.add(conc);
			blockTypeString.add(ts);
			binBlockExprString.add(TextTemplateTokenStream.join(et, false));
			textBlockExprString.add(TextTemplateTokenStream.join(et, true));
			blockExpression.add(compileOrThrow(ts, et, ERROR_SYNTAX_CONC));
			blockContents.add((blockContent = new Template()));
			blockCount++;
			while (!tokens.nextBlockEnd()) {
				blockContent.add(factory.createTemplateItem(state, tokens, tokens.next()));
			}
		}
	}
	
	private Expression compileOrThrow(String ts, List<TextTemplateTokenStream.Token> tokens, String format) {
		try { return Expression.compile(new TextTemplateTokenStream(tokens)); }
		catch (ExpressionException e) { throw new TemplateException(format, ts, e.getMessage()); }
	}
	
	public PackedItem createPackedItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		return null;
	}
	
	private static List<TextTemplateTokenStream.Token> getExpressionTokens(TextTemplateTokenStream in) {
		TextTemplateTokenStream.Token token = in.next();
		if (token == null) throw new TemplateException(ERROR_SYNTAX_EXPR_EOF);
		if (token.type == TextTemplateTokenStream.TokenType.OPERATOR) {
			List<TextTemplateTokenStream.Token> out = new ArrayList<TextTemplateTokenStream.Token>();
			if (token.value.equals("(")) { while (getExpressionTokens2(in, out, ")") == null); return out; }
			if (token.value.equals("[")) { while (getExpressionTokens2(in, out, "]") == null); return out; }
			if (token.value.equals("{")) { while (getExpressionTokens2(in, out, "}") == null); return out; }
			out.add(token); getExpressionTokens2(in, out, null); return out;
		} else {
			return Arrays.asList(token);
		}
	}
	
	private static TextTemplateTokenStream.Token getExpressionTokens2(TextTemplateTokenStream in, List<TextTemplateTokenStream.Token> out, String end) {
		// Get zero or more operators followed by a non-operator token or a group of tokens in parentheses.
		// If end is specified and a matching token is encountered, return the matching token without adding it.
		for (;;) {
			TextTemplateTokenStream.Token token = in.next();
			if (token == null) throw new TemplateException(ERROR_SYNTAX_EXPR_EOF);
			if (token.type == TextTemplateTokenStream.TokenType.OPERATOR) {
				if (token.value.equals(end)) return token;
				if (token.value.equals("(")) { out.add(token); while ((token = getExpressionTokens2(in, out, ")")) == null); out.add(token); return null; }
				if (token.value.equals("[")) { out.add(token); while ((token = getExpressionTokens2(in, out, "]")) == null); out.add(token); return null; }
				if (token.value.equals("{")) { out.add(token); while ((token = getExpressionTokens2(in, out, "}")) == null); out.add(token); return null; }
				out.add(token); continue;
			} else {
				out.add(token); return null;
			}
		}
	}
}
