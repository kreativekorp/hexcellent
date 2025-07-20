package com.kreative.templature.template.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.kreative.templature.template.*;
import com.kreative.templature.template.item.SwitchBlock;

public class SwitchBlockFactory implements BinaryTemplateItemFactory, TextTemplateItemFactory, TemplateConstants {
	public long[] getTypeConstants() {
		return new long[] {
			$CASB|TI, $BSAC|TI|LE,
			$CASC|TI, $CSAC|TI|LE,
			$CASD|TI, $DSAC|TI|LE,
			$CASE|TI, $ESAC|TI|LE,
			$SELB|TI, $BLES|TI|LE,
			$SELC|TI, $CLES|TI|LE,
			$SELD|TI, $DLES|TI|LE,
			$SELE|TI, $ELES|TI|LE,
			$CSOF|TI, $FOSC|TI|LE,
			$CSLB|TI, $BLSC|TI|LE,
			$CSDF|TI, $FDSC|TI|LE,
			$ENCS|TI, $SCNE|TI|LE,
		};
	}
	
	public TemplateItem createTemplateItem(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int mtc, int mv
	) throws IOException {
		switch (mtc) {
			case $CASB: return createSwitchBlock(factory, in, it, $CASC, $CASD, $CASE);
			case $BSAC: return createSwitchBlock(factory, in, it, $CSAC, $DSAC, $ESAC);
			case $CASC: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $CASB);
			case $CSAC: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $BSAC);
			case $CASD: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $CASB);
			case $DSAC: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $BSAC);
			case $CASE: throw new TemplateException(ERROR_SWITCH_END, it.type, $CASB);
			case $ESAC: throw new TemplateException(ERROR_SWITCH_END, it.type, $BSAC);
			case $SELB: return createSwitchBlock(factory, in, it, $SELC, $SELD, $SELE);
			case $BLES: return createSwitchBlock(factory, in, it, $CLES, $DLES, $ELES);
			case $SELC: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $SELB);
			case $CLES: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $BLES);
			case $SELD: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $SELB);
			case $DLES: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $BLES);
			case $SELE: throw new TemplateException(ERROR_SWITCH_END, it.type, $SELB);
			case $ELES: throw new TemplateException(ERROR_SWITCH_END, it.type, $BLES);
			case $CSOF: return createSwitchBlock(factory, in, it, $CSLB, $CSDF, $ENCS);
			case $FOSC: return createSwitchBlock(factory, in, it, $BLSC, $FDSC, $SCNE);
			case $CSLB: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $CSOF);
			case $BLSC: throw new TemplateException(ERROR_SWITCH_CASE, it.type, $FOSC);
			case $CSDF: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $CSOF);
			case $FDSC: throw new TemplateException(ERROR_SWITCH_DEF, it.type, $FOSC);
			case $ENCS: throw new TemplateException(ERROR_SWITCH_END, it.type, $CSOF);
			case $SCNE: throw new TemplateException(ERROR_SWITCH_END, it.type, $FOSC);
			default: return null;
		}
	}
	
	private TemplateItem createSwitchBlock(
		BinaryTemplateFactory factory, BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it, int selc, int seld, int sele
	) throws IOException {
		int switchTypeConstant = it.type;
		String switchTypeString = "switch";
		String binSwitchExprString = it.label;
		String textSwitchExprString = TextTemplateTokenStream.join(TextTemplateTokenStream.split(it.label), true);
		Expression switchExpression = compileOrThrow(it, ERROR_SYNTAX_SELB);
		ArrayList<Integer> caseTypeConstant = new ArrayList<Integer>();
		ArrayList<String> caseTypeString = new ArrayList<String>();
		ArrayList<String> binCaseExprString = new ArrayList<String>();
		ArrayList<String> textCaseExprString = new ArrayList<String>();
		ArrayList<Expression> caseExpression = new ArrayList<Expression>();
		ArrayList<Template> caseContents = new ArrayList<Template>();
		Template caseContent = null; int caseCount = 0;
		for (;;) {
			// We are in a "switch" or "case" block.
			it = in.read();
			if (it == null) {
				throw new TemplateException(ERROR_SWITCH_NO_END, switchTypeConstant, sele);
			} else if (it.type == $CASC || it.type == $CSAC || it.type == $SELC || it.type == $CLES || it.type == $CSLB || it.type == $BLSC) {
				// Create a "case" block.
				caseTypeConstant.add(it.type);
				caseTypeString.add("case");
				binCaseExprString.add(it.label);
				textCaseExprString.add(TextTemplateTokenStream.join(TextTemplateTokenStream.split(it.label), true));
				caseExpression.add(compileOrThrow(it, ERROR_SYNTAX_SELC));
				caseContents.add((caseContent = new Template()));
				caseCount++;
				continue;
			} else if (it.type == $CASD || it.type == $DSAC || it.type == $SELD || it.type == $DLES || it.type == $CSDF || it.type == $FDSC) {
				// Create a "default" block.
				caseTypeConstant.add(it.type);
				caseTypeString.add("default");
				binCaseExprString.add(it.label);
				textCaseExprString.add(it.label);
				caseExpression.add(null);
				caseContents.add((caseContent = new Template()));
				caseCount++;
				for (;;) {
					// We are in a "default" block.
					it = in.read();
					if (it == null) {
						throw new TemplateException(ERROR_SWITCH_NO_END, switchTypeConstant, sele);
					} else if (it.type == $CASC || it.type == $CSAC || it.type == $SELC || it.type == $CLES || it.type == $CSLB || it.type == $BLSC) {
						throw new TemplateException(ERROR_SWITCH_DEAD_CASE, it.type, caseTypeConstant.get(caseCount - 1));
					} else if (it.type == $CASD || it.type == $DSAC || it.type == $SELD || it.type == $DLES || it.type == $CSDF || it.type == $FDSC) {
						throw new TemplateException(ERROR_SWITCH_DEAD_DEF, it.type, caseTypeConstant.get(caseCount - 1));
					} else if (it.type == $CASE || it.type == $ESAC || it.type == $SELE || it.type == $ELES || it.type == $ENIF || it.type == $FINE) {
						// Return a completed structure.
						return new SwitchBlock(
							switchTypeConstant, caseTypeConstant, it.type,
							switchTypeString, caseTypeString,
							binSwitchExprString, textSwitchExprString,
							binCaseExprString, textCaseExprString, it.label,
							switchExpression, caseExpression,
							caseContents, caseCount
						);
					} else {
						// Add items to a "default" block.
						caseContent.add(factory.createTemplateItem(in, it));
					}
				}
			} else if (it.type == $CASE || it.type == $ESAC || it.type == $SELE || it.type == $ELES || it.type == $ENIF || it.type == $FINE) {
				// Return a completed structure.
				return new SwitchBlock(
					switchTypeConstant, caseTypeConstant, it.type,
					switchTypeString, caseTypeString,
					binSwitchExprString, textSwitchExprString,
					binCaseExprString, textCaseExprString, it.label,
					switchExpression, caseExpression,
					caseContents, caseCount
				);
			} else if (caseContent == null || caseCount == 0) {
				// We are not in a "case" block.
				throw new TemplateException(ERROR_SWITCH_NO_CASE, switchTypeConstant, selc, seld);
			} else {
				// Add items to a "case" block.
				caseContent.add(factory.createTemplateItem(in, it));
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
			"switch", "case", "default"
		};
	}
	
	public String[] getPackedTypeStrings() {
		return null;
	}
	
	private static final List<String> MTS = Arrays.asList(
		"switch", "case", "default"
	);
	
	public TemplateItem createTemplateItem(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, String mts, int mv, boolean le
	) {
		switch (MTS.indexOf(mts)) {
			case 0:
				return createSwitchBlock(
					factory, state, tokens, token,
					(le?$BLES:$SELB), (le?$CLES:$SELC),
					(le?$DLES:$SELD), (le?$ELES:$SELE)
				);
			case 1:
				throw new TemplateException(ERROR_SWITCH_CASE, token.value.toString(), "switch");
			case 2:
				throw new TemplateException(ERROR_SWITCH_DEF, token.value.toString(), "switch");
			default:
				return null;
		}
	}
	
	private TemplateItem createSwitchBlock(
		TextTemplateFactory factory, TextTemplateInputState state, TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token, int selb, int selc, int seld, int sele
	) {
		String ts = token.value.toString();
		List<TextTemplateTokenStream.Token> et = getExpressionTokens(tokens);
		tokens.nextBlockBeginOrThrow();
		String binSwitchExprString = TextTemplateTokenStream.join(et, false);
		String textSwitchExprString = TextTemplateTokenStream.join(et, true);
		Expression switchExpression = compileOrThrow(ts, et, ERROR_SYNTAX_SELB);
		ArrayList<Integer> caseTypeConstant = new ArrayList<Integer>();
		ArrayList<String> caseTypeString = new ArrayList<String>();
		ArrayList<String> binCaseExprString = new ArrayList<String>();
		ArrayList<String> textCaseExprString = new ArrayList<String>();
		ArrayList<Expression> caseExpression = new ArrayList<Expression>();
		ArrayList<Template> caseContents = new ArrayList<Template>();
		Template caseContent = null; int caseCount = 0;
		for (;;) {
			// We are in a "switch" or "case" block.
			String caseToken = tokens.nextKeywordOrNull("case");
			if (caseToken != null) {
				// Create a "case" block.
				et = getExpressionTokens2(tokens);
				tokens.nextLabelEndOrThrow();
				caseTypeConstant.add(selc);
				caseTypeString.add(caseToken);
				binCaseExprString.add(TextTemplateTokenStream.join(et, false));
				textCaseExprString.add(TextTemplateTokenStream.join(et, true));
				caseExpression.add(compileOrThrow(caseToken, et, ERROR_SYNTAX_SELC));
				caseContents.add((caseContent = new Template()));
				caseCount++;
				continue;
			}
			String defaultToken = tokens.nextKeywordOrNull("default");
			if (defaultToken != null) {
				// Create a "default" block.
				String name = tokens.nextNameOrNull();
				tokens.nextLabelEndOrThrow();
				caseTypeConstant.add(seld);
				caseTypeString.add(defaultToken);
				binCaseExprString.add(name);
				textCaseExprString.add(name);
				caseExpression.add(null);
				caseContents.add((caseContent = new Template()));
				caseCount++;
				for (;;) {
					// We are in a "default" block.
					String caseToken2 = tokens.nextKeywordOrNull("case");
					if (caseToken2 != null) throw new TemplateException(ERROR_SWITCH_DEAD_CASE, caseToken2, defaultToken);
					String defaultToken2 = tokens.nextKeywordOrNull("default");
					if (defaultToken2 != null) throw new TemplateException(ERROR_SWITCH_DEAD_DEF, defaultToken2, defaultToken);
					if (tokens.nextBlockEnd()) {
						// Return a completed structure.
						name = tokens.nextNameOrNull();
						tokens.nextStatementEndOrThrow();
						return new SwitchBlock(
							selb, caseTypeConstant, sele,
							ts, caseTypeString,
							binSwitchExprString, textSwitchExprString,
							binCaseExprString, textCaseExprString, name,
							switchExpression, caseExpression,
							caseContents, caseCount
						);
					}
					// Add items to a "default" block.
					caseContent.add(factory.createTemplateItem(state, tokens, tokens.next()));
				}
			}
			if (tokens.nextBlockEnd()) {
				// Return a completed structure.
				String name = tokens.nextNameOrNull();
				tokens.nextStatementEndOrThrow();
				return new SwitchBlock(
					selb, caseTypeConstant, sele,
					ts, caseTypeString,
					binSwitchExprString, textSwitchExprString,
					binCaseExprString, textCaseExprString, name,
					switchExpression, caseExpression,
					caseContents, caseCount
				);
			}
			if (caseContent == null || caseCount == 0) {
				// We are not in a "case" block.
				throw new TemplateException(ERROR_SWITCH_NO_CASE, ts, "case", "default");
			} else {
				// Add items to a "case" block.
				caseContent.add(factory.createTemplateItem(state, tokens, tokens.next()));
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
	
	private static List<TextTemplateTokenStream.Token> getExpressionTokens2(TextTemplateTokenStream in) {
		List<TextTemplateTokenStream.Token> out = new ArrayList<TextTemplateTokenStream.Token>();
		getExpressionTokens2(in, out, null);
		return out;
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
