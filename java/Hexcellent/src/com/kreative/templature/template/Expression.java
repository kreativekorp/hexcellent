package com.kreative.templature.template;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class Expression {
	public abstract Object evaluate(Closure closure);
	
	public static Expression compile(String s) {
		return parse(new TextTemplateTokenStream(s));
	}
	
	public static Expression compile(TextTemplateTokenStream ts) {
		return parse(ts);
	}
	
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		for (String arg : args) { sb.append(arg); sb.append("\n"); }
		try { System.out.println(compile(sb.toString()).evaluate(new Closure(null))); }
		catch (ExpressionException e) { System.err.println(e.getMessage()); }
	}
	
	// *************
	// * EVALUATOR *
	// *************
	
	private static class ValueExpression extends Expression {
		private final Object value;
		private ValueExpression(Object value) {
			this.value = value;
		}
		public Object evaluate(Closure closure) {
			return value;
		}
	}
	
	private static class VariableExpression extends Expression {
		private final String key;
		private VariableExpression(String key) {
			this.key = key;
		}
		public Object evaluate(Closure closure) {
			return closure.getValue(key);
		}
	}
	
	private static class BareWordExpression extends Expression {
		private final String word;
		private BareWordExpression(String word) {
			this.word = word;
		}
		public Object evaluate(Closure closure) {
			Object value = closure.getValue(word);
			return (value != null) ? value : word;
		}
	}
	
	private static enum UnaryOperator {
		PLUS, NEGATE, BITWISE_NOT, BOOLEAN_NOT, SQRT, CBRT, QTRT;
		public static UnaryOperator forString(String op) {
			if (op.equals("+")) return PLUS;
			if (op.equals("-")) return NEGATE;
			if (op.equals("~")) return BITWISE_NOT;
			if (op.equals("!")) return BOOLEAN_NOT;
			if (op.equals("¬")) return BOOLEAN_NOT;
			if (op.equals("√")) return SQRT;
			if (op.equals("∛")) return CBRT;
			if (op.equals("∜")) return QTRT;
			return null;
		}
	}
	
	private static class UnaryExpression extends Expression {
		private final UnaryOperator op;
		private final Expression a;
		private UnaryExpression(String op, Expression a) {
			this.op = UnaryOperator.forString(op);
			this.a = a;
		}
		public Object evaluate(Closure c) {
			switch (op) {
				case PLUS: return toNumber(a.evaluate(c));
				case NEGATE: return negate(a.evaluate(c));
				case BITWISE_NOT: return toBigInteger(a.evaluate(c)).not();
				case BOOLEAN_NOT: return !toBoolean(a.evaluate(c));
				case SQRT: return Math.sqrt(toDouble(a.evaluate(c)));
				case CBRT: return Math.cbrt(toDouble(a.evaluate(c)));
				case QTRT: return Math.sqrt(Math.sqrt(toDouble(a.evaluate(c))));
			}
			return null;
		}
	}
	
	private static enum BinaryOperator {
		POW, LOG, ROOT, MULTIPLY, DIVIDE, DIV, MOD, ADD, SUBTRACT,
		SHIFT_LEFT, SHIFT_RIGHT, BITWISE_AND, BITWISE_XOR, BITWISE_OR,
		LESS_THAN, GREATER_THAN, LESS_OR_EQUAL, GREATER_OR_EQUAL,
		EQUAL, STRICT_EQUAL, NOT_EQUAL, STRICT_NOT_EQUAL, COMPARE,
		BOOLEAN_AND, BOOLEAN_XOR, BOOLEAN_OR;
		public static BinaryOperator forString(String op) {
			if (op.equals("**") || op.equals("^") || op.equals("↑")) return POW;
			if (op.equals("_") || op.equals("↓")) return LOG;
			if (op.equals("√")) return ROOT;
			if (op.equals("*") || op.equals("·") || op.equals("×")) return MULTIPLY;
			if (op.equals("/") || op.equals("÷")) return DIVIDE;
			if (op.equals("\\")) return DIV;
			if (op.equals("%")) return MOD;
			if (op.equals("+")) return ADD;
			if (op.equals("-")) return SUBTRACT;
			if (op.equals("<<<") || op.equals("<<")) return SHIFT_LEFT;
			if (op.equals(">>>") || op.equals(">>")) return SHIFT_RIGHT;
			if (op.equals("&")) return BITWISE_AND;
			if (op.equals("#")) return BITWISE_XOR;
			if (op.equals("|")) return BITWISE_OR;
			if (op.equals("<")) return LESS_THAN;
			if (op.equals(">")) return GREATER_THAN;
			if (op.equals("<=") || op.equals("=<") || op.equals("≤")) return LESS_OR_EQUAL;
			if (op.equals(">=") || op.equals("=>") || op.equals("≥")) return GREATER_OR_EQUAL;
			if (op.equals("=") || op.equals("==")) return EQUAL;
			if (op.equals("===")) return STRICT_EQUAL;
			if (op.equals("<>") || op.equals("!=") || op.equals("≠") || op.equals("≠≠")) return NOT_EQUAL;
			if (op.equals("!==") || op.equals("≠≠≠")) return STRICT_NOT_EQUAL;
			if (op.equals("<=>") || op.equals("⇔")) return COMPARE;
			if (op.equals("&&")) return BOOLEAN_AND;
			if (op.equals("##")) return BOOLEAN_XOR;
			if (op.equals("||")) return BOOLEAN_OR;
			return null;
		}
	}
	
	private static class BinaryExpression extends Expression {
		private final BinaryOperator op;
		private final Expression a;
		private final Expression b;
		private BinaryExpression(String op, Expression a, Expression b) {
			this.op = BinaryOperator.forString(op);
			this.a = a;
			this.b = b;
		}
		public Object evaluate(Closure c) {
			switch (op) {
				case POW: return Math.pow(toDouble(a.evaluate(c)), toDouble(b.evaluate(c)));
				case LOG: return Math.log(toDouble(a.evaluate(c))) / Math.log(toDouble(b.evaluate(c)));
				case ROOT: return Math.pow(toDouble(b.evaluate(c)), 1 / toDouble(a.evaluate(c)));
				case MULTIPLY: return multiply(a.evaluate(c), b.evaluate(c));
				case DIVIDE: return divide(a.evaluate(c), b.evaluate(c));
				case DIV: return divideToIntegralValue(a.evaluate(c), b.evaluate(c));
				case MOD: return remainder(a.evaluate(c), b.evaluate(c));
				case ADD: return add(a.evaluate(c), b.evaluate(c));
				case SUBTRACT: return subtract(a.evaluate(c), b.evaluate(c));
				case SHIFT_LEFT: return toBigInteger(a.evaluate(c)).shiftLeft(toInteger(b.evaluate(c)));
				case SHIFT_RIGHT: return toBigInteger(a.evaluate(c)).shiftRight(toInteger(b.evaluate(c)));
				case BITWISE_AND: return toBigInteger(a.evaluate(c)).and(toBigInteger(b.evaluate(c)));
				case BITWISE_XOR: return toBigInteger(a.evaluate(c)).xor(toBigInteger(b.evaluate(c)));
				case BITWISE_OR: return toBigInteger(a.evaluate(c)).or(toBigInteger(b.evaluate(c)));
				case LESS_THAN: return compare(a.evaluate(c), b.evaluate(c)).doubleValue() < 0;
				case GREATER_THAN: return compare(a.evaluate(c), b.evaluate(c)).doubleValue() > 0;
				case LESS_OR_EQUAL: return compare(a.evaluate(c), b.evaluate(c)).doubleValue() <= 0;
				case GREATER_OR_EQUAL: return compare(a.evaluate(c), b.evaluate(c)).doubleValue() >= 0;
				case EQUAL: return equal(a.evaluate(c), b.evaluate(c));
				case STRICT_EQUAL: return strictEqual(a.evaluate(c), b.evaluate(c));
				case NOT_EQUAL: return !equal(a.evaluate(c), b.evaluate(c));
				case STRICT_NOT_EQUAL: return !strictEqual(a.evaluate(c), b.evaluate(c));
				case COMPARE: return compare(a.evaluate(c), b.evaluate(c));
				case BOOLEAN_AND: return toBoolean(a.evaluate(c)) && toBoolean(b.evaluate(c));
				case BOOLEAN_XOR: return toBoolean(a.evaluate(c)) != toBoolean(b.evaluate(c));
				case BOOLEAN_OR: return toBoolean(a.evaluate(c)) || toBoolean(b.evaluate(c));
			}
			return null;
		}
	}
	
	public static boolean isFinite(Object v) {
		if (v == null) return false;
		if (v instanceof BigInteger) return true;
		if (v instanceof BigDecimal) return true;
		if (v instanceof FloatFormat.NaN) return false;
		if (v instanceof FloatFormat.Zero) return true;
		if (v instanceof Float) return !(Float.isNaN((Float)v) || Float.isInfinite((Float)v));
		if (v instanceof Double) return !(Double.isNaN((Double)v) || Double.isInfinite((Double)v));
		if (v instanceof Byte) return true;
		if (v instanceof Short) return true;
		if (v instanceof Integer) return true;
		if (v instanceof Long) return true;
		if (v instanceof Boolean) return true;
		if (v instanceof Character) return true;
		try { return isFinite(TemplateUtils.parseNumber(v.toString())); }
		catch (NumberFormatException e) { return false; }
	}
	
	public static boolean isFloating(Object v) {
		if (v == null) return false;
		if (v instanceof BigInteger) return false;
		if (v instanceof BigDecimal) return true;
		if (v instanceof FloatFormat.NaN) return true;
		if (v instanceof FloatFormat.Zero) return true;
		if (v instanceof Float) return true;
		if (v instanceof Double) return true;
		if (v instanceof Byte) return false;
		if (v instanceof Short) return false;
		if (v instanceof Integer) return false;
		if (v instanceof Long) return false;
		if (v instanceof Boolean) return false;
		if (v instanceof Character) return false;
		try { return isFloating(TemplateUtils.parseNumber(v.toString())); }
		catch (NumberFormatException e) { return false; }
	}
	
	public static Number negate(Object v) {
		if (v == null) return FloatFormat.NaN.NaN;
		if (v instanceof BigInteger) return ((BigInteger)v).negate();
		if (v instanceof BigDecimal) return ((BigDecimal)v).negate();
		if (v instanceof FloatFormat.NaN) return ((FloatFormat.NaN)v).negate();
		if (v instanceof FloatFormat.Zero) return ((FloatFormat.Zero)v).negate();
		if (v instanceof Float) return -((Float)v);
		if (v instanceof Double) return -((Double)v);
		if (v instanceof Byte) return BigInteger.valueOf((Byte)v).negate();
		if (v instanceof Short) return BigInteger.valueOf((Short)v).negate();
		if (v instanceof Integer) return BigInteger.valueOf((Integer)v).negate();
		if (v instanceof Long) return BigInteger.valueOf((Long)v).negate();
		if (v instanceof Boolean) return ((Boolean)v) ? -1 : 0;
		if (v instanceof Character) return -((Character)v);
		try { return negate(TemplateUtils.parseNumber(v.toString())); }
		catch (NumberFormatException e) { return FloatFormat.NaN.NaN; }
	}
	
	public static Number multiply(Object a, Object b) {
		Number an = toNumber(a); Number bn = toNumber(b);
		if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() * bn.doubleValue();
		if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).multiply(toBigDecimal(bn));
		if (isFloating(an) || isFloating(bn)) return an.doubleValue() * bn.doubleValue();
		return toBigInteger(an).multiply(toBigInteger(bn));
	}
	
	private static final MathContext DIVISION = new MathContext(100, RoundingMode.HALF_EVEN);
	
	public static Number divide(Object a, Object b) {
		Number an = toNumber(a); Number bn = toNumber(b);
		if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() / bn.doubleValue();
		if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).divide(toBigDecimal(bn), DIVISION);
		if (isFloating(an) || isFloating(bn)) return an.doubleValue() / bn.doubleValue();
		return toBigInteger(an).divide(toBigInteger(bn));
	}
	
	public static Number divideToIntegralValue(Object a, Object b) {
		Number an = toNumber(a); Number bn = toNumber(b);
		if (!isFinite(an) || !isFinite(bn)) return Math.rint(an.doubleValue() / bn.doubleValue());
		if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).divideToIntegralValue(toBigDecimal(bn));
		if (isFloating(an) || isFloating(bn)) return Math.rint(an.doubleValue() / bn.doubleValue());
		return toBigInteger(an).divide(toBigInteger(bn));
	}
	
	public static Number remainder(Object a, Object b) {
		Number an = toNumber(a); Number bn = toNumber(b);
		if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() % bn.doubleValue();
		if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).remainder(toBigDecimal(bn));
		if (isFloating(an) || isFloating(bn)) return an.doubleValue() % bn.doubleValue();
		return toBigInteger(an).remainder(toBigInteger(bn));
	}
	
	public static Object add(Object a, Object b) {
		if (a instanceof Number && b instanceof Number) {
			Number an = (Number)a; Number bn = (Number)b;
			if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() + bn.doubleValue();
			if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).add(toBigDecimal(bn));
			if (isFloating(an) || isFloating(bn)) return an.doubleValue() + bn.doubleValue();
			return toBigInteger(an).add(toBigInteger(bn));
		} else {
			if (a == null) return b; if (b == null) return a;
			return a.toString() + b.toString();
		}
	}
	
	public static Number subtract(Object a, Object b) {
		Number an = toNumber(a); Number bn = toNumber(b);
		if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() - bn.doubleValue();
		if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).subtract(toBigDecimal(bn));
		if (isFloating(an) || isFloating(bn)) return an.doubleValue() - bn.doubleValue();
		return toBigInteger(an).subtract(toBigInteger(bn));
	}
	
	public static Number compare(Object a, Object b) {
		if (a instanceof Number && b instanceof Number) {
			Number an = (Number)a; Number bn = (Number)b;
			if (!isFinite(an) || !isFinite(bn)) return Math.signum(an.doubleValue() - bn.doubleValue());
			if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).compareTo(toBigDecimal(bn));
			if (isFloating(an) || isFloating(bn)) return Math.signum(an.doubleValue() - bn.doubleValue());
			return toBigInteger(an).compareTo(toBigInteger(bn));
		} else {
			if (a == null || b == null) return FloatFormat.NaN.NaN;
			return a.toString().compareTo(b.toString());
		}
	}
	
	public static boolean equal(Object a, Object b) {
		if (a instanceof Number && b instanceof Number) {
			Number an = (Number)a; Number bn = (Number)b;
			if (!isFinite(an) || !isFinite(bn)) return an.doubleValue() == bn.doubleValue();
			if (an instanceof BigDecimal || bn instanceof BigDecimal) return toBigDecimal(an).compareTo(toBigDecimal(bn)) == 0;
			if (isFloating(an) || isFloating(bn)) return an.doubleValue() == bn.doubleValue();
			return toBigInteger(an).compareTo(toBigInteger(bn)) == 0;
		} else {
			if (a == null) return (b == null);
			if (b == null) return (a == null);
			return a.equals(b);
		}
	}
	
	public static boolean strictEqual(Object a, Object b) {
		if (a == null) return (b == null);
		if (b == null) return (a == null);
		return a.equals(b);
	}
	
	public static BigDecimal toBigDecimal(Object v) {
		if (v == null) return BigDecimal.ZERO;
		if (v instanceof BigDecimal) return ((BigDecimal)v);
		if (v instanceof BigInteger) return new BigDecimal((BigInteger)v);
		if (v instanceof FloatFormat.NaN) return BigDecimal.ZERO;
		if (v instanceof FloatFormat.Zero) return BigDecimal.ZERO;
		if (v instanceof Float || v instanceof Double) {
			try { return BigDecimal.valueOf(((Number)v).doubleValue()); }
			catch (NumberFormatException e) { return BigDecimal.ZERO; }
		}
		if (v instanceof Byte || v instanceof Short || v instanceof Integer || v instanceof Long) {
			return BigDecimal.valueOf(((Number)v).longValue());
		}
		if (v instanceof Character) return BigDecimal.valueOf((Character)v);
		if (v instanceof Boolean) return ((Boolean)v) ? BigDecimal.ONE : BigDecimal.ZERO;
		try { return new BigDecimal(v.toString()); }
		catch (NumberFormatException e) { return BigDecimal.ZERO; }
	}
	
	public static BigInteger toBigInteger(Object v) {
		if (v == null) return BigInteger.ZERO;
		if (v instanceof BigInteger) return ((BigInteger)v);
		if (v instanceof BigDecimal) return ((BigDecimal)v).toBigInteger();
		if (v instanceof FloatFormat.NaN) return BigInteger.ZERO;
		if (v instanceof FloatFormat.Zero) return BigInteger.ZERO;
		if (v instanceof Float || v instanceof Double) {
			try { return BigDecimal.valueOf(((Number)v).doubleValue()).toBigInteger(); }
			catch (NumberFormatException e) { return BigInteger.ZERO; }
		}
		if (v instanceof Byte || v instanceof Short || v instanceof Integer || v instanceof Long) {
			return BigInteger.valueOf(((Number)v).longValue());
		}
		if (v instanceof Character) return BigInteger.valueOf((Character)v);
		if (v instanceof Boolean) return ((Boolean)v) ? BigInteger.ONE : BigInteger.ZERO;
		try { return new BigInteger(v.toString()); }
		catch (NumberFormatException e) { return BigInteger.ZERO; }
	}
	
	public static boolean toBoolean(Object v) {
		if (v == null) return false;
		if (v instanceof Boolean) return ((Boolean)v);
		if (v instanceof BigInteger) return ((BigInteger)v).signum() != 0;
		if (v instanceof BigDecimal) return ((BigDecimal)v).signum() != 0;
		if (v instanceof FloatFormat.NaN) return ((FloatFormat.NaN)v).signum() != 0;
		if (v instanceof FloatFormat.Zero) return false;
		if (v instanceof Float || v instanceof Double) {
			double dv = ((Number)v).doubleValue();
			return (dv == dv) && (dv != 0);
		}
		if (v instanceof Byte || v instanceof Short || v instanceof Integer || v instanceof Long) {
			return ((Number)v).longValue() != 0;
		}
		if (v instanceof String) return ((String)v).length() > 0;
		if (v instanceof Character) return ((Character)v) != '\u0000';
		if (v instanceof Collection<?>) return !((Collection<?>)v).isEmpty();
		if (v instanceof Map<?,?>) return !((Map<?,?>)v).isEmpty();
		if (v instanceof Object[]) return ((Object[])v).length > 0;
		if (v instanceof byte[]) return ((byte[])v).length > 0;
		if (v instanceof short[]) return ((short[])v).length > 0;
		if (v instanceof int[]) return ((int[])v).length > 0;
		if (v instanceof long[]) return ((long[])v).length > 0;
		if (v instanceof float[]) return ((float[])v).length > 0;
		if (v instanceof double[]) return ((double[])v).length > 0;
		if (v instanceof boolean[]) return ((boolean[])v).length > 0;
		if (v instanceof char[]) return ((char[])v).length > 0;
		return true;
	}
	
	public static double toDouble(Object v) {
		if (v == null) return Double.NaN;
		if (v instanceof Number) return ((Number)v).doubleValue();
		if (v instanceof Boolean) return ((Boolean)v) ? 1 : 0;
		if (v instanceof Character) return ((Character)v);
		try { return Double.parseDouble(v.toString()); }
		catch (NumberFormatException e) { return Double.NaN; }
	}
	
	public static float toFloat(Object v) {
		if (v == null) return Float.NaN;
		if (v instanceof Number) return ((Number)v).floatValue();
		if (v instanceof Boolean) return ((Boolean)v) ? 1 : 0;
		if (v instanceof Character) return ((Character)v);
		try { return Float.parseFloat(v.toString()); }
		catch (NumberFormatException e) { return Float.NaN; }
	}
	
	public static int toInteger(Object v) {
		if (v == null) return 0;
		if (v instanceof Number) return ((Number)v).intValue();
		if (v instanceof Boolean) return ((Boolean)v) ? 1 : 0;
		if (v instanceof Character) return ((Character)v);
		try { return Integer.parseInt(v.toString()); }
		catch (NumberFormatException e) { return 0; }
	}
	
	public static long toLong(Object v) {
		if (v == null) return 0;
		if (v instanceof Number) return ((Number)v).longValue();
		if (v instanceof Boolean) return ((Boolean)v) ? 1 : 0;
		if (v instanceof Character) return ((Character)v);
		try { return Long.parseLong(v.toString()); }
		catch (NumberFormatException e) { return 0; }
	}
	
	public static Number toNumber(Object v) {
		if (v == null) return FloatFormat.NaN.NaN;
		if (v instanceof Number) return ((Number)v);
		if (v instanceof Boolean) return ((Boolean)v) ? 1 : 0;
		if (v instanceof Character) return ((Character)v) | 0;
		try { return TemplateUtils.parseNumber(v.toString()); }
		catch (NumberFormatException e) { return FloatFormat.NaN.NaN; }
	}
	
	// **********
	// * PARSER *
	// **********
	
	private static final List<List<String>> OPERATORS = Arrays.asList(
		Arrays.asList("!", "+", "-", "~", "¬", "√", "∛", "∜"),
		Arrays.asList("**", "^", "_", "↑", "↓", "√"),
		Arrays.asList("*", "/", "\\", "%", "·", "×", "÷"),
		Arrays.asList("+", "-"),
		Arrays.asList("<<<", ">>>", "<<", ">>"),
		Arrays.asList("&"), Arrays.asList("#"), Arrays.asList("|"),
		Arrays.asList("<", ">", "<=", ">=", "=<", "=>", "≤", "≥"),
		Arrays.asList("=", "==", "===", "<>", "!=", "!==", "≠", "≠≠", "≠≠≠", "<=>", "⇔"),
		Arrays.asList("&&"), Arrays.asList("##"), Arrays.asList("||")
	);
	
	private static Expression parse(TextTemplateTokenStream tokens) {
		Expression expr = parseExpression(tokens, OPERATORS.size() - 1);
		if (tokens.hasNext()) {
			TextTemplateTokenStream.Token token = tokens.next();
			throw new ExpressionException("end of input", token.image);
		}
		return expr;
	}
	
	private static Expression parseExpression(TextTemplateTokenStream tokens, int prec) {
		Expression expr = (prec <= 1) ? parseFactor(tokens) : parseExpression(tokens, prec - 1);
		for (;;) {
			if (tokens.hasNext()) {
				TextTemplateTokenStream.Token token = tokens.lookNext();
				if (token.type == TextTemplateTokenStream.TokenType.OPERATOR) {
					if (OPERATORS.get(prec).contains(token.value)) {
						tokens.next();
						Expression nexpr = parseExpression(tokens, ((prec <= 1) ? prec : (prec - 1)));
						expr = new BinaryExpression(token.value.toString(), expr, nexpr);
						continue;
					}
				}
			}
			break;
		}
		return expr;
	}
	
	private static Expression parseFactor(TextTemplateTokenStream tokens) {
		if (tokens.hasNext()) {
			TextTemplateTokenStream.Token token = tokens.next();
			switch (token.type) {
				case VALUE: return new ValueExpression(token.value);
				case IDENTIFIER: return new VariableExpression(token.value.toString());
				case KEYWORD: return new BareWordExpression(token.value.toString());
				case OPERATOR:
					if (token.value.equals("(")) return parseToParen(tokens, ")");
					if (token.value.equals("[")) return parseToParen(tokens, "]");
					if (token.value.equals("{")) return parseToParen(tokens, "}");
					if (OPERATORS.get(0).contains(token.value)) {
						String op = token.value.toString();
						Expression expr = parseFactor(tokens);
						return new UnaryExpression(op, expr);
					}
			}
			throw new ExpressionException("value", token.image);
		}
		throw new ExpressionException("value", "end of input");
	}
	
	private static Expression parseToParen(TextTemplateTokenStream tokens, String end) {
		Expression expr = parseExpression(tokens, OPERATORS.size() - 1);
		if (tokens.hasNext()) {
			TextTemplateTokenStream.Token token = tokens.next();
			if (token.type == TextTemplateTokenStream.TokenType.OPERATOR) {
				if (token.value.equals(end)) {
					return expr;
				}
			}
			throw new ExpressionException(end, token.image);
		}
		throw new ExpressionException(end, "end of input");
	}
}
