package com.kreative.templature.template;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateUtils {
	private TemplateUtils() {}
	
	private static final Pattern NAME = Pattern.compile("^(\\p{L}[\\p{L}\\p{M}\\p{N}]*)$");
	
	public static String escapeName(String name) {
		if (name == null || name.length() == 0) return "``";
		if (NAME.matcher(name).matches()) return name;
		return quote(name, '`');
	}
	
	public static String quote(String s, char q) {
		StringBuffer sb = new StringBuffer();
		sb.append(q);
		if (s != null) {
			for (int i = 0, n = s.length(); i < n; i++) {
				char ch = s.charAt(i);
				switch (ch) {
					case 0: ch = '0'; break;
					case 7: ch = 'a'; break;
					case 8: ch = 'b'; break;
					case 9: ch = 't'; break;
					case 10: ch = 'n'; break;
					case 11: ch = 'v'; break;
					case 12: ch = 'f'; break;
					case 13: ch = 'r'; break;
					case 14: ch = 'o'; break;
					case 15: ch = 'i'; break;
					case 26: ch = 'z'; break;
					case 27: ch = 'e'; break;
					case 127: ch = 'd'; break;
					case '\\': break;
					default:
						if (ch == q) break;
						sb.append(ch);
						continue;
				}
				sb.append('\\');
				sb.append(ch);
			}
		}
		sb.append(q);
		return sb.toString();
	}
	
	public static String formatBigInteger(BigInteger value, int width, int radix) {
		boolean negative = (value.signum() < 0);
		String s = value.abs().toString(radix).toUpperCase();
		switch (radix) {
			case 2:
				while (s.length() < width) s = "0" + s;
				return (negative ? "-%" : "%") + s;
			case 8:
				width = (width + 2) / 3;
				while (s.length() < width) s = "0" + s;
				return (negative ? "-0" : "0") + s;
			case 16:
				width = (width + 3) / 4;
				while (s.length() < width) s = "0" + s;
				return (negative ? "-$" : "$") + s;
			default:
				s = radix + "r" + s;
				// fallthrough;
			case 10:
				return (negative ? ("-" + s) : s);
		}
	}
	
	private static final Pattern RADIX_INT = Pattern.compile("^([+-]?)(([0-9]+)[Rr]|0([XxHhDdBbOo])|([#$%0]))([0-9A-Za-z]+)$");
	
	public static BigInteger parseBigInteger(String s, int radix) {
		Matcher m = RADIX_INT.matcher(s);
		if (m.matches()) return parseBigInteger(m, radix);
		return new BigInteger(s, radix);
	}
	
	private static BigInteger parseBigInteger(Matcher m, int radix) {
		String g3 = m.group(3);
		if (g3 != null && g3.length() > 0) {
			radix = Integer.parseInt(g3);
			if (radix < 2) radix = 2;
			if (radix > 36) radix = 36;
		}
		String g4 = m.group(4);
		if (g4 != null && g4.length() > 0) {
			switch (g4.charAt(0)) {
				case 'X': case 'x': radix = 16; break;
				case 'H': case 'h': radix = 16; break;
				case 'D': case 'd': radix = 10; break;
				case 'B': case 'b': radix = 2; break;
				case 'O': case 'o': radix = 8; break;
			}
		}
		String g5 = m.group(5);
		if (g5 != null && g5.length() > 0) {
			switch (g5.charAt(0)) {
				case '#': radix = 16; break;
				case '$': radix = 16; break;
				case '%': radix = 2; break;
				case '0': radix = 8; break;
			}
		}
		String s = m.group(1) + m.group(6);
		return new BigInteger(s, radix);
	}
	
	private static final Pattern ZERO = Pattern.compile("^([+-]?)(0+([.]0*)?|[.]0+)([Ee][+-]?[0-9]+)?$");
	
	public static BigDecimal parseBigDecimal(String s) {
		Matcher zm = ZERO.matcher(s);
		if (zm.matches()) return BigDecimal.ZERO;
		Matcher m = RADIX_INT.matcher(s);
		if (m.matches()) return new BigDecimal(parseBigInteger(m, 10));
		return new BigDecimal(s);
	}
	
	private static final Pattern INFINITY = Pattern.compile("^([+-]?)(Infinity|Infty|Inf|∞)$", Pattern.CASE_INSENSITIVE);
	
	public static Number parseNumber(String s) {
		if (s.equalsIgnoreCase("NaN")) return FloatFormat.NaN.NaN;
		Matcher im = INFINITY.matcher(s);
		if (im.matches()) return (
			im.group(1).equals("-") ?
			FloatFormat.NaN.NEGATIVE_INFINITY :
			FloatFormat.NaN.POSITIVE_INFINITY
		);
		Matcher zm = ZERO.matcher(s);
		if (zm.matches()) return (
			zm.group(1).equals("-") ?
			FloatFormat.Zero.NEGATIVE_ZERO :
			FloatFormat.Zero.POSITIVE_ZERO
		);
		Matcher m = RADIX_INT.matcher(s);
		if (m.matches()) return parseBigInteger(m, 10);
		return new BigDecimal(s);
	}
	
	private static final int[] DIGITS24 = {
		'0'<<24, '1'<<24, '2'<<24, '3'<<24, '4'<<24, '5'<<24, '6'<<24, '7'<<24,
		'8'<<24, '9'<<24, 'A'<<24, 'B'<<24, 'C'<<24, 'D'<<24, 'E'<<24, 'F'<<24,
	};
	
	private static final int[] DIGITS16 = {
		'0'<<16, '1'<<16, '2'<<16, '3'<<16, '4'<<16, '5'<<16, '6'<<16, '7'<<16,
		'8'<<16, '9'<<16, 'A'<<16, 'B'<<16, 'C'<<16, 'D'<<16, 'E'<<16, 'F'<<16,
	};
	
	private static final int[] DIGITS8 = {
		'0'<<8, '1'<<8, '2'<<8, '3'<<8, '4'<<8, '5'<<8, '6'<<8, '7'<<8,
		'8'<<8, '9'<<8, 'A'<<8, 'B'<<8, 'C'<<8, 'D'<<8, 'E'<<8, 'F'<<8,
	};
	
	private static final int[] DIGITS0 = {
		'0'<<0, '1'<<0, '2'<<0, '3'<<0, '4'<<0, '5'<<0, '6'<<0, '7'<<0,
		'8'<<0, '9'<<0, 'A'<<0, 'B'<<0, 'C'<<0, 'D'<<0, 'E'<<0, 'F'<<0,
	};
	
	public static int maskDecValue(int typeConstant, int maskedValue, boolean le) {
		typeConstant &= (le ? 0x0000FFFF : 0xFFFF0000);
		typeConstant |= (le ? DIGITS24 : DIGITS0)[maskedValue % 10]; maskedValue /= 10;
		typeConstant |= (le ? DIGITS16 : DIGITS8)[maskedValue % 10]; return typeConstant;
	}
	
	public static int maskHexValue(int typeConstant, int maskedValue, boolean le) {
		typeConstant &= (le ? 0x000000FF : 0xFF000000);
		typeConstant |= (le ? DIGITS24 : DIGITS0)[maskedValue & 15]; maskedValue >>= 4;
		typeConstant |= (le ? DIGITS16 : DIGITS8)[maskedValue & 15]; maskedValue >>= 4;
		typeConstant |= (le ? DIGITS8 : DIGITS16)[maskedValue & 15]; return typeConstant;
	}
}
