package com.kreative.templature.test;

import java.math.BigDecimal;
import java.util.Arrays;

public abstract class AbstractTest {
	private int total = 0, passed = 0, failed = 0;
	
	protected final void record(boolean pass, Object actual, Object expected) {
		total++;
		if (pass) {
			passed++;
		} else {
			failed++;
			System.out.println("FAIL: [ " + actual + " != " + expected + " ]");
		}
	}
	
	protected final void expectEquals(Object actual, Object expected) {
		final boolean pass;
		if (actual instanceof BigDecimal && expected instanceof BigDecimal) {
			pass = ((BigDecimal)actual).compareTo((BigDecimal)expected) == 0;
		} else {
			pass = actual.equals(expected);
		}
		record(pass, actual, expected);
	}
	
	protected final void expectEquals(Object value, byte[] actual, byte[] expected) {
		total++;
		if (Arrays.equals(actual, expected)) {
			passed++;
		} else {
			failed++;
			System.out.println("FAIL: " + value);
			printHex(actual); System.out.println(" !=");
			printHex(expected); System.out.println();
		}
	}
	
	protected final void printHex(byte[] data) {
		for (byte b : data) {
			String h = "00" + Integer.toHexString(b);
			h = h.substring(h.length() - 2).toUpperCase();
			System.out.print(h);
		}
	}
	
	public abstract void run() throws Exception;
	
	public final void report() {
		System.out.println("Passed: " + passed + "/" + total);
		System.out.println("Failed: " + failed + "/" + total);
	}
	
	public final void main() {
		try {
			run(); report();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
