package com.kreative.hexcellent.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import com.kreative.hexcellent.buffer.FloatFormat;

public class FloatFormatTest {
	private static final double[] RESPL4_VALUES = {
		0, 0.5, 1, 1.5, 2, 3, Double.POSITIVE_INFINITY, Double.NaN
	};
	
	private static final double[] RESPL8_VALUES = {
		0, 0.001953125, 0.00390625, 0.005859375, 0.0078125, 0.009765625, 0.01171875, 0.013671875,
		0.015625, 0.017578125, 0.01953125, 0.021484375, 0.0234375, 0.025390625, 0.02734375,
		0.029296875, 0.03125, 0.03515625, 0.0390625, 0.04296875, 0.046875, 0.05078125, 0.0546875,
		0.05859375, 0.0625, 0.0703125, 0.078125, 0.0859375, 0.09375, 0.1015625, 0.109375,
		0.1171875, 0.125, 0.140625, 0.15625, 0.171875, 0.1875, 0.203125, 0.21875, 0.234375, 0.25,
		0.28125, 0.3125, 0.34375, 0.375, 0.40625, 0.4375, 0.46875, 0.5, 0.5625, 0.625, 0.6875,
		0.75, 0.8125, 0.875, 0.9375, 1, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2, 2.25, 2.5,
		2.75, 3, 3.25, 3.5, 3.75, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 9, 10, 11, 12, 13, 14, 15,
		16, 18, 20, 22, 24, 26, 28, 30, 32, 36, 40, 44, 48, 52, 56, 60, 64, 72, 80, 88, 96, 104,
		112, 120, 128, 144, 160, 176, 192, 208, 224, 240, Double.POSITIVE_INFINITY, Double.NaN,
		Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN
	};
	
	private static final double[] WIKIPEDIA_VALUES = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 20, 22, 24, 26, 28, 30,
		32, 36, 40, 44, 48, 52, 56, 60, 64, 72, 80, 88, 96, 104, 112, 120, 128, 144, 160, 176,
		192, 208, 224, 240, 256, 288, 320, 352, 384, 416, 448, 480, 512, 576, 640, 704, 768,
		832, 896, 960, 1024, 1152, 1280, 1408, 1536, 1664, 1792, 1920, 2048, 2304, 2560, 2816,
		3072, 3328, 3584, 3840, 4096, 4608, 5120, 5632, 6144, 6656, 7168, 7680, 8192, 9216,
		10240, 11264, 12288, 13312, 14336, 15360, 16384, 18432, 20480, 22528, 24576, 26624,
		28672, 30720, 32768, 36864, 40960, 45056, 49152, 53248, 57344, 61440, 65536, 73728,
		81920, 90112, 98304, 106496, 114688, 122880, Double.POSITIVE_INFINITY, Double.NaN,
		Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN
	};
	
	public static void main(String[] args) {
		Random random = new Random();
		testMini(FloatFormat.RESPL4, 4, RESPL4_VALUES);
		testMini(FloatFormat.RESPL8, 8, RESPL8_VALUES);
		testMini(FloatFormat.WIKIPEDIA, 8, WIKIPEDIA_VALUES);
		for (;;) {
			testSingle(random, 1000);
			testDouble(random, 1000);
		}
	}
	
	private static void testMini(FloatFormat fmt, int bits, double[] values) {
		System.out.print(fmt + ": ");
		int count = (1 << bits);
		int signMask = (1 << (bits - 1));
		int magMask = signMask - 1;
		for (int i = 0; i < count; i++) {
			Number n = fmt.bitsToNumber(BigInteger.valueOf(i));
			double actual = n.doubleValue();
			double expected = values[i & magMask];
			if ((i & signMask) != 0) expected = -expected;
			if (!equals(actual, expected)) {
				System.out.println("\u0007FAIL");
				System.out.println(actual + " != " + expected);
				return;
			}
			if (!Double.isNaN(expected)) {
				int r1 = fmt.floatToBits(n.floatValue()).intValue();
				int r2 = fmt.doubleToBits(n.doubleValue()).intValue();
				if (r1 != i || r2 != i) {
					System.out.println("\u0007FAIL");
					System.out.println(r1 + " or " + r2 + " != " + i);
					return;
				}
			}
			if (i != signMask && expected == (int)expected) {
				int rev = fmt.intToBits((int)expected).intValue();
				if (rev != i) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + i);
					return;
				}
			}
			if (i != signMask && expected == (long)expected) {
				int rev = fmt.longToBits((long)expected).intValue();
				if (rev != i) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + i);
					return;
				}
			}
			if (n instanceof BigDecimal) {
				int rev = fmt.bigDecimalToBits((BigDecimal)n).intValue();
				if (rev != i) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + i);
					return;
				}
			}
		}
		System.out.println("PASS");
	}
	
	private static void testSingle(Random random, int count) {
		FloatFormat fmt = FloatFormat.SINGLE;
		System.out.print(fmt + ": ");
		for (int i = 0; i < count; i++) {
			int bits = random.nextInt();
			Number n = fmt.bitsToNumber(BigInteger.valueOf(bits));
			float actual = n.floatValue();
			float expected = Float.intBitsToFloat(bits);
			if (!equals(actual, expected)) {
				System.out.println("\u0007FAIL");
				System.out.println(actual + " != " + expected);
				return;
			}
			if (!Float.isNaN(expected)) {
				int rev = fmt.floatToBits(expected).intValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (bits != Integer.MIN_VALUE && expected == (int)expected) {
				int rev = fmt.intToBits((int)expected).intValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (bits != Integer.MIN_VALUE && expected == (long)expected) {
				int rev = fmt.longToBits((long)expected).intValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (n instanceof BigDecimal) {
				int rev = fmt.bigDecimalToBits((BigDecimal)n).intValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
		}
		System.out.println("PASS");
	}
	
	private static void testDouble(Random random, int count) {
		FloatFormat fmt = FloatFormat.DOUBLE;
		System.out.print(fmt + ": ");
		for (int i = 0; i < count; i++) {
			long bits = random.nextLong();
			Number n = fmt.bitsToNumber(BigInteger.valueOf(bits));
			double actual = n.doubleValue();
			double expected = Double.longBitsToDouble(bits);
			if (!equals(actual, expected)) {
				System.out.println("\u0007FAIL");
				System.out.println(actual + " != " + expected);
				return;
			}
			if (!Double.isNaN(expected)) {
				long rev = fmt.doubleToBits(expected).longValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (bits != Long.MIN_VALUE && expected == (int)expected) {
				long rev = fmt.intToBits((int)expected).longValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (bits != Long.MIN_VALUE && expected == (long)expected) {
				long rev = fmt.longToBits((long)expected).longValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
			if (n instanceof BigDecimal) {
				long rev = fmt.bigDecimalToBits((BigDecimal)n).longValue();
				if (rev != bits) {
					System.out.println("\u0007FAIL");
					System.out.println(rev + " != " + bits);
					return;
				}
			}
		}
		System.out.println("PASS");
	}
	
	private static boolean equals(double a, double b) {
		if (Double.isNaN(a)) return Double.isNaN(b);
		if (Double.isNaN(b)) return Double.isNaN(a);
		return a == b;
	}
}
