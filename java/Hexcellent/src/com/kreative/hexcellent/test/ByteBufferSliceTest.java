package com.kreative.hexcellent.test;

import java.io.IOException;
import java.util.Arrays;

public class ByteBufferSliceTest extends ByteBufferTestBase {
	private static void testSlice() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		byte[] expected = new byte[end - start];
		for (int i = 0, v = start; v < end; v++, i++) expected[i] = (byte)v;
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		
		byte[] actual = new byte[end - start];
		randomByteBuffer(start, end, false).slice(0, end - start).get(0, actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new byte[end - start];
		randomByteBuffer(0, 256, false).slice(start, end - start).get(0, actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		int padding = random.nextInt(256) + 1;
		int offset = random.nextInt(padding);
		
		actual = new byte[end - start + padding];
		randomByteBuffer(start, end, false).slice(0, end - start).get(0, actual, offset, end - start);
		System.out.print(equals(expected, 0, actual, offset, end - start) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new byte[end - start + padding];
		randomByteBuffer(0, 256, false).slice(start, end - start).get(0, actual, offset, end - start);
		System.out.print(equals(expected, 0, actual, offset, end - start) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testSlice();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
	
	private static boolean equals(byte[] a, int ao, byte[] b, int bo, int length) {
		while (length > 0) {
			if (a[ao++] != b[bo++]) return false;
			length--;
		}
		return true;
	}
}
