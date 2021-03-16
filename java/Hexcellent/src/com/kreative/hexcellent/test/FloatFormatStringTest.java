package com.kreative.hexcellent.test;

import java.math.BigInteger;
import java.util.Random;
import com.kreative.hexcellent.buffer.FloatFormat;

public class FloatFormatStringTest {
	public static void main(String[] args) {
		Random random = new Random();
		testMini(FloatFormat.RESPL4, 4);
		testMini(FloatFormat.RESPL8, 8);
		testMini(FloatFormat.WIKIPEDIA, 8);
		testSingle(random, 10);
		testDouble(random, 10);
	}
	
	private static void testMini(FloatFormat fmt, int bits) {
		System.out.println(fmt + " (" + fmt.getDecimalDigits() + " digits):");
		for (int i = 0, n = (1 << bits); i < n; i++) {
			String s = fmt.round(fmt.bitsToNumber(BigInteger.valueOf(i))).toString();
			System.out.println("\t" + s);
		}
	}
	
	private static void testSingle(Random random, int count) {
		FloatFormat fmt = FloatFormat.SINGLE;
		System.out.println(fmt + " (" + fmt.getDecimalDigits() + " digits):");
		for (int i = 0; i < count; i++) {
			int bits = random.nextInt();
			String actual = fmt.round(fmt.bitsToNumber(BigInteger.valueOf(bits))).toString();
			String expected = Float.toString(Float.intBitsToFloat(bits));
			System.out.println("\t" + expected + "\t" + actual + "\t" + expected.equals(actual));
		}
	}
	
	private static void testDouble(Random random, int count) {
		FloatFormat fmt = FloatFormat.DOUBLE;
		System.out.println(fmt + " (" + fmt.getDecimalDigits() + " digits):");
		for (int i = 0; i < count; i++) {
			long bits = random.nextLong();
			String actual = fmt.round(fmt.bitsToNumber(BigInteger.valueOf(bits))).toString();
			String expected = Double.toString(Double.longBitsToDouble(bits));
			System.out.println("\t" + expected + "\t" + actual + "\t" + expected.equals(actual.replace("+","")));
		}
	}
}
