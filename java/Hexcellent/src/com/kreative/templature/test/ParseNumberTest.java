package com.kreative.templature.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.templature.template.FloatFormat;
import com.kreative.templature.template.TemplateUtils;

public class ParseNumberTest {
	private static final class TC {
		private final String s; private final Number n;
		private TC(String s, Number n) { this.s = s; this.n = n; }
		private boolean test() {
			try {
				Number m = TemplateUtils.parseNumber(s);
				if (m.equals(n) || (
					m instanceof BigDecimal &&
					n instanceof BigDecimal &&
					((BigDecimal)m).compareTo((BigDecimal)n) == 0
				)) return true;
				System.out.println("FAIL: [ " + m + " != " + n + " ]");
				return false;
			} catch (NumberFormatException e) {
				if (n == null) return true;
				System.out.println("FAIL: [ " + e + " != " + n + " ]");
				return false;
			}
		}
	}
	
	private static final TC[] testCases = {
		new TC("NaN", FloatFormat.NaN.NaN),
		new TC("NAN", FloatFormat.NaN.NaN),
		new TC("nan", FloatFormat.NaN.NaN),
		new TC("Infinity", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("infty", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("INF", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("∞", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("+INFINITY", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("+Infty", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("+inf", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("+∞", FloatFormat.NaN.POSITIVE_INFINITY),
		new TC("-infinity", FloatFormat.NaN.NEGATIVE_INFINITY),
		new TC("-INFTY", FloatFormat.NaN.NEGATIVE_INFINITY),
		new TC("-Inf", FloatFormat.NaN.NEGATIVE_INFINITY),
		new TC("-∞", FloatFormat.NaN.NEGATIVE_INFINITY),
		new TC("*", null),
		new TC("", null),
		new TC(".", null),
		new TC("+.", null),
		new TC("-.", null),
		new TC("0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC(".0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+.0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-.0", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("0.", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0.", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0.", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("00", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+00", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-00", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC(".00", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+.00", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-.00", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("0.0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0.0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0.0", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("00.", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+00.", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-00.", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("0E-1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0e-1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0E-1", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC(".0e1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+.0E1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-.0e1", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("0.E+1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0.e+1", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0.E+1", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("00e0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+00E0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-00e0", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("0.0E-0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("+0.0e+0", FloatFormat.Zero.POSITIVE_ZERO),
		new TC("-0.0E00", FloatFormat.Zero.NEGATIVE_ZERO),
		new TC("2r", null),
		new TC("+3r", null),
		new TC("-4r", null),
		new TC("0X", null),
		new TC("+0x", null),
		new TC("-0H", null),
		new TC("0h", null),
		new TC("+0D", null),
		new TC("-0d", null),
		new TC("0B", null),
		new TC("+0b", null),
		new TC("-0O", null),
		new TC("0o", null),
		new TC("#", null),
		new TC("+#", null),
		new TC("-#", null),
		new TC("$", null),
		new TC("+$", null),
		new TC("-$", null),
		new TC("%", null),
		new TC("+%", null),
		new TC("-%", null),
		new TC("2r10", BigInteger.valueOf(2)),
		new TC("+3r10", BigInteger.valueOf(3)),
		new TC("-4r10", BigInteger.valueOf(-4)),
		new TC("0X10", BigInteger.valueOf(16)),
		new TC("+0x10", BigInteger.valueOf(16)),
		new TC("-0H10", BigInteger.valueOf(-16)),
		new TC("0h10", BigInteger.valueOf(16)),
		new TC("+0D10", BigInteger.valueOf(10)),
		new TC("-0d10", BigInteger.valueOf(-10)),
		new TC("0B10", BigInteger.valueOf(2)),
		new TC("+0b10", BigInteger.valueOf(2)),
		new TC("-0O10", BigInteger.valueOf(-8)),
		new TC("0o10", BigInteger.valueOf(8)),
		new TC("#10", BigInteger.valueOf(16)),
		new TC("+#10", BigInteger.valueOf(16)),
		new TC("-#10", BigInteger.valueOf(-16)),
		new TC("$10", BigInteger.valueOf(16)),
		new TC("+$10", BigInteger.valueOf(16)),
		new TC("-$10", BigInteger.valueOf(-16)),
		new TC("%10", BigInteger.valueOf(2)),
		new TC("+%10", BigInteger.valueOf(2)),
		new TC("-%10", BigInteger.valueOf(-2)),
		new TC("010", BigInteger.valueOf(8)),
		new TC("+010", BigInteger.valueOf(8)),
		new TC("-010", BigInteger.valueOf(-8)),
		new TC("16rFf", BigInteger.valueOf(255)),
		new TC("+0XFf", BigInteger.valueOf(255)),
		new TC("-0hFf", BigInteger.valueOf(-255)),
		new TC("#Ff", BigInteger.valueOf(255)),
		new TC("+$Ff", BigInteger.valueOf(255)),
		new TC("15rFf", null),
		new TC("+0dFf", null),
		new TC("-0BFf", null),
		new TC("0oFf", null),
		new TC("+%Ff", null),
		new TC("-0Ff", null),
		new TC("1", BigDecimal.valueOf(1)),
		new TC("+1", BigDecimal.valueOf(1)),
		new TC("-1", BigDecimal.valueOf(-1)),
		new TC("1.", BigDecimal.valueOf(1)),
		new TC("+1.", BigDecimal.valueOf(1)),
		new TC("-1.", BigDecimal.valueOf(-1)),
		new TC(".1", BigDecimal.valueOf(0.1)),
		new TC("+.1", BigDecimal.valueOf(0.1)),
		new TC("-.1", BigDecimal.valueOf(-0.1)),
		new TC("10", BigDecimal.valueOf(10)),
		new TC("+10", BigDecimal.valueOf(10)),
		new TC("-10", BigDecimal.valueOf(-10)),
		new TC(".10", BigDecimal.valueOf(0.1)),
		new TC("+.10", BigDecimal.valueOf(0.1)),
		new TC("-.10", BigDecimal.valueOf(-0.1)),
		new TC("1.0", BigDecimal.valueOf(1.0)),
		new TC("+1.0", BigDecimal.valueOf(1.0)),
		new TC("-1.0", BigDecimal.valueOf(-1.0)),
		new TC("10.", BigDecimal.valueOf(10)),
		new TC("+10.", BigDecimal.valueOf(10)),
		new TC("-10.", BigDecimal.valueOf(-10)),
		new TC("1E-1", BigDecimal.valueOf(0.1)),
		new TC("+1e-1", BigDecimal.valueOf(0.1)),
		new TC("-1E-1", BigDecimal.valueOf(-0.1)),
		new TC(".1e1", BigDecimal.valueOf(1.0)),
		new TC("+.1E1", BigDecimal.valueOf(1.0)),
		new TC("-.1e1", BigDecimal.valueOf(-1.0)),
		new TC("1.E+1", BigDecimal.valueOf(10)),
		new TC("+1.e+1", BigDecimal.valueOf(10)),
		new TC("-1.E+1", BigDecimal.valueOf(-10)),
		new TC("10e0", BigDecimal.valueOf(10)),
		new TC("+10E0", BigDecimal.valueOf(10)),
		new TC("-10e0", BigDecimal.valueOf(-10)),
		new TC("1.0E-0", BigDecimal.valueOf(1.0)),
		new TC("+1.0e+0", BigDecimal.valueOf(1.0)),
		new TC("-1.0E00", BigDecimal.valueOf(-1.0)),
	};
	
	public static void main(String[] args) {
		if (args.length == 0) {
			int total = 0, passed = 0, failed = 0;
			for (TC tc : testCases) {
				total++;
				if (tc.test()) passed++;
				else failed++;
			}
			System.out.println("Passed: " + passed + "/" + total);
			System.out.println("Failed: " + failed + "/" + total);
		} else {
			for (String arg : args) {
				try { System.out.println(TemplateUtils.parseNumber(arg)); }
				catch (NumberFormatException e) { System.out.println(e); }
			}
		}
	}
}
