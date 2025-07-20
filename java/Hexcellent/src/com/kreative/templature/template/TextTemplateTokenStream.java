package com.kreative.templature.template;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTemplateTokenStream implements Closeable, Iterator<TextTemplateTokenStream.Token> {
	public static enum TokenType { VALUE, IDENTIFIER, KEYWORD, OPERATOR }
	
	public static class Token {
		public final String image;
		public final TokenType type;
		public final Object value;
		private Token(String image, TokenType type, Object value) {
			this.image = image;
			this.type = type;
			this.value = value;
		}
	}
	
	public static String join(List<? extends Token> tokens, boolean pretty) {
		if (pretty) {
			StringBuffer sb = new StringBuffer();
			boolean space = false;
			for (Token token : tokens) {
				if (space && !(token.image.length() == 1 && ")]}".contains(token.image))) sb.append(" ");
				sb.append(token.image);
				space = !(token.image.length() == 1 && "([{".contains(token.image));
			}
			return sb.toString();
		} else {
			String s = "";
			ArrayList<String> t = new ArrayList<String>();
			for (Token token : tokens) t.add(token.image);
			ArrayList<String> t2 = new ArrayList<String>();
			for (int i = 0, n = t.size(); i < n; i++) {
				String s2 = s + t.get(i);
				Matcher m = TOKEN.matcher(s2);
				while (m.find()) t2.add(m.group());
				if (t2.equals(t.subList(0,i+1))) s = s2;
				else s += " " + t.get(i);
				t2.clear();
			}
			return s;
		}
	}
	
	public static List<Token> split(String source) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokenize(tokens, source);
		return tokens;
	}
	
	// ******************
	// * Initialization *
	// ******************
	
	private final Scanner input;
	private final ArrayList<Token> tokens;
	private int i;
	
	public TextTemplateTokenStream(List<? extends Token> tokens) {
		this.input = null;
		this.tokens = new ArrayList<Token>(tokens);
		this.i = 0;
	}
	
	public TextTemplateTokenStream(Scanner in) {
		this.input = in;
		this.tokens = new ArrayList<Token>();
		this.i = 0;
	}
	
	public TextTemplateTokenStream(File in) throws FileNotFoundException {
		this(new Scanner(in, "UTF-8"));
	}
	
	public TextTemplateTokenStream(File in, String charsetName) throws FileNotFoundException {
		this(new Scanner(in, charsetName));
	}
	
	public TextTemplateTokenStream(InputStream in) {
		this(new Scanner(in, "UTF-8"));
	}
	
	public TextTemplateTokenStream(InputStream in, String charsetName) {
		this(new Scanner(in, charsetName));
	}
	
	public TextTemplateTokenStream(String in) {
		this(new Scanner(in));
	}
	
	// ****************
	// * Iterator API *
	// ****************
	
	public void close() {
		if (input != null) input.close();
	}
	
	public boolean hasNext() {
		for (;;) {
			if (i < tokens.size()) return true;
			if (input == null || !input.hasNextLine()) return false;
			tokens.clear(); i = 0;
			tokenize(tokens, input.nextLine());
		}
	}
	
	public Token lookNext() {
		for (;;) {
			if (i < tokens.size()) return tokens.get(i);
			if (input == null || !input.hasNextLine()) return null;
			tokens.clear(); i = 0;
			tokenize(tokens, input.nextLine());
		}
	}
	
	public Token next() {
		for (;;) {
			if (i < tokens.size()) return tokens.get(i++);
			if (input == null || !input.hasNextLine()) return null;
			tokens.clear(); i = 0;
			tokenize(tokens, input.nextLine());
		}
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	// *****************************
	// * Template Item Factory API *
	// *****************************
	
	public boolean hasNextType() {
		return hasNext() && lookNext().type == TokenType.KEYWORD;
	}
	
	public void nextBlockBeginOrThrow() {
		nextOperatorOrThrow("{");
	}
	
	public boolean nextBlockEnd() {
		return nextOperator("}");
	}
	
	public String nextKeywordOrNull(String... keywords) {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.KEYWORD) {
				String tk = token.value.toString();
				for (String keyword : keywords) {
					if (tk.equalsIgnoreCase(keyword)) {
						next(); return tk;
					}
				}
			}
		}
		return null;
	}
	
	public String nextKeywordOrThrow(String... keywords) {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.KEYWORD) {
				String tk = token.value.toString();
				for (String keyword : keywords) {
					if (tk.equalsIgnoreCase(keyword)) {
						next(); return tk;
					}
				}
			}
			throw new TemplateException(TemplateConstants.ERROR_SYNTAX_TOKEN, join(", ", keywords), token.image);
		}
		throw new TemplateException(TemplateConstants.ERROR_SYNTAX_TOKEN_EOF, join(", ", keywords));
	}
	
	public void nextLabelEndOrThrow() {
		nextOperatorOrThrow(":");
	}
	
	public String nextNameOrNull() {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.IDENTIFIER || token.type == TokenType.KEYWORD) {
				next(); return token.value.toString();
			}
		}
		return null;
	}
	
	public String nextNameOrThrow() {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.IDENTIFIER || token.type == TokenType.KEYWORD) {
				next(); return token.value.toString();
			}
			throw new TemplateException(TemplateConstants.ERROR_SYNTAX_NAME, token.image);
		}
		throw new TemplateException(TemplateConstants.ERROR_SYNTAX_NAME_EOF);
	}
	
	public boolean nextOperator(String op) {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.OPERATOR && token.value.equals(op)) {
				next(); return true;
			}
		}
		return false;
	}
	
	public void nextOperatorOrThrow(String op) {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.OPERATOR && token.value.equals(op)) {
				next(); return;
			}
			throw new TemplateException(TemplateConstants.ERROR_SYNTAX_TOKEN, op, token.image);
		}
		throw new TemplateException(TemplateConstants.ERROR_SYNTAX_TOKEN_EOF, op);
	}
	
	public void nextStatementEndOrThrow() {
		nextOperatorOrThrow(";");
	}
	
	public void nextValueDelimiterOrThrow() {
		nextOperatorOrThrow(",");
	}
	
	public String nextValueStringOrThrow() {
		if (hasNext()) {
			Token token = lookNext();
			if (token.type == TokenType.VALUE) {
				next(); return token.value.toString();
			}
			throw new TemplateException(TemplateConstants.ERROR_SYNTAX_STR, token.image);
		}
		throw new TemplateException(TemplateConstants.ERROR_SYNTAX_STR_EOF);
	}
	
	// *********
	// * LEXER *
	// *********
	
	// 1 - double-quoted string
	//   2 - character in double-quoted string
	// 3 - single-quoted string
	//   4 - character in single-quoted string
	// 5 - back-quoted identifier
	//   6 - character in back-quoted identifier
	// 7 - bare identifier
	// 8 - radix integer constant
	//   9 - prefix
	// 10 - decimal constant
	//   11 - mantissa
	//   12 - fractional part after integer part
	//   13 - exponent
	// 14 - multi-character operator
	// 15 - single-character operator
	private static final Pattern TOKEN = Pattern.compile("(\"(\\\\.|[^\"])*\")|('(\\\\.|[^'])*')|(`(\\\\.|[^`])*`)|(\\p{L}[\\p{L}\\p{M}\\p{N}]*)|(([0-9]+[Rr]|0[XxHhDdBbOo]|[#$%])[0-9A-Za-z]+)|(([0-9]+([.][0-9]*)?|[.][0-9]+)([Ee][+-]?[0-9]+)?)|(<<<|>>>|<=>|===|!==|≠≠≠|<<|>>|<>|<=|>=|=<|=>|==|!=|≠≠|&&|##|\\|\\||\\*\\*)|([\\S&&\\P{Z}])");
	
	private static void tokenize(ArrayList<Token> tokens, String s) {
		Matcher m = TOKEN.matcher(s);
		while (m.find()) tokens.add(tokenize(m));
	}
	
	private static Token tokenize(Matcher m) {
		if (m.group(1) != null) {
			// double-quoted string
			return new Token(m.group(), TokenType.VALUE, unquote(m.group(1)));
		}
		if (m.group(3) != null) {
			// single-quoted string
			return new Token(m.group(), TokenType.VALUE, unquote(m.group(3)));
		}
		if (m.group(5) != null) {
			// back-quoted identifier
			return new Token(m.group(), TokenType.IDENTIFIER, unquote(m.group(5)));
		}
		if (m.group(7) != null) {
			// bare identifier
			String s = m.group(7);
			if (s.equalsIgnoreCase("null")) return new Token(m.group(), TokenType.VALUE, null);
			if (s.equalsIgnoreCase("true")) return new Token(m.group(), TokenType.VALUE, true);
			if (s.equalsIgnoreCase("false")) return new Token(m.group(), TokenType.VALUE, false);
			if (s.equalsIgnoreCase("NaN")) return new Token(m.group(), TokenType.VALUE, FloatFormat.NaN.NaN);
			if (s.equalsIgnoreCase("Infinity")) return new Token(m.group(), TokenType.VALUE, FloatFormat.NaN.POSITIVE_INFINITY);
			if (s.equalsIgnoreCase("Infty")) return new Token(m.group(), TokenType.VALUE, FloatFormat.NaN.POSITIVE_INFINITY);
			if (s.equalsIgnoreCase("Inf")) return new Token(m.group(), TokenType.VALUE, FloatFormat.NaN.POSITIVE_INFINITY);
			return new Token(m.group(), TokenType.KEYWORD, s);
		}
		if (m.group(8) != null) {
			// radix integer constant
			return new Token(m.group(), TokenType.VALUE, TemplateUtils.parseNumber(m.group(8)));
		}
		if (m.group(10) != null) {
			// decimal constant
			return new Token(m.group(), TokenType.VALUE, TemplateUtils.parseNumber(m.group(10)));
		}
		if (m.group(14) != null) {
			// multi-character operator
			return new Token(m.group(), TokenType.OPERATOR, m.group(14));
		}
		if (m.group(15) != null) {
			// single-character operator
			String s = m.group(15);
			if (s.equals("∞")) return new Token(m.group(), TokenType.VALUE, FloatFormat.NaN.POSITIVE_INFINITY);
			return new Token(m.group(), TokenType.OPERATOR, s);
		}
		throw new IllegalArgumentException(m.group());
	}
	
	// *************
	// * UNESCAPER *
	// *************
	
	// 1 - 8-digit Unicode code point \U0010FFFF
	// 2 - 6-digit Unicode code point \w10FFFF
	// 3 - 4-digit Unicode code point \uFFFF
	// 4 - 2-digit Unicode code point \xFF
	// 5 - C0 control character \cA = \x01
	// 6 - C1 control character \CA = \x81
	// 7 - other escape \a
	// 8 - other character A
	private static final Pattern SEQUENCE = Pattern.compile("\\\\U([0-9A-Fa-f]{8})|\\\\w([0-9A-Fa-f]{6})|\\\\u([0-9A-Fa-f]{4})|\\\\x([0-9A-Fa-f]{2})|\\\\c(.)|\\\\C(.)|\\\\(.)|(.)");
	
	private static String unquote(String s) {
		return unescape((s.length() >= 2 && s.endsWith(s.substring(0, 1))) ? s.substring(1, s.length() - 1) : s);
	}
	
	private static String unescape(String s) {
		StringBuffer sb = new StringBuffer();
		Matcher m = SEQUENCE.matcher(s);
		while (m.find()) {
			if (m.group(1) != null) {
				int cp = (int)Long.parseLong(m.group(1), 16);
				if (Character.isValidCodePoint(cp)) sb.append(Character.toChars(cp));
				else sb.append('\uFFFD');
			}
			if (m.group(2) != null) {
				int cp = Integer.parseInt(m.group(2), 16);
				if (Character.isValidCodePoint(cp)) sb.append(Character.toChars(cp));
				else sb.append('\uFFFD');
			}
			if (m.group(3) != null) {
				int cp = Integer.parseInt(m.group(3), 16);
				sb.append((char)cp);
			}
			if (m.group(4) != null) {
				int cp = Integer.parseInt(m.group(4), 16);
				sb.append((char)cp);
			}
			if (m.group(5) != null) {
				int cp = m.group(5).charAt(0) & 0x1F;
				sb.append((char)cp);
			}
			if (m.group(6) != null) {
				int cp = (m.group(6).charAt(0) & 0x1F) | 0x80;
				sb.append((char)cp);
			}
			if (m.group(7) != null) {
				char ch = m.group(7).charAt(0);
				switch (ch) {
					case '0': sb.append((char)0); break;
					case 'a': sb.append((char)7); break;
					case 'b': sb.append((char)8); break;
					case 't': sb.append((char)9); break;
					case 'n': sb.append((char)10); break;
					case 'v': sb.append((char)11); break;
					case 'f': sb.append((char)12); break;
					case 'r': sb.append((char)13); break;
					case 'o': sb.append((char)14); break;
					case 'i': sb.append((char)15); break;
					case 'z': sb.append((char)26); break;
					case 'e': sb.append((char)27); break;
					case 'd': sb.append((char)127); break;
					default: sb.append(ch); break;
				}
			}
			if (m.group(8) != null) {
				sb.append(m.group(8));
			}
		}
		return sb.toString();
	}
	
	// ***********
	// * Utility *
	// ***********
	
	private static String join(String d, String... s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++) {
			if (i > 0) sb.append(d);
			sb.append(s[i]);
		}
		return sb.toString();
	}
}
