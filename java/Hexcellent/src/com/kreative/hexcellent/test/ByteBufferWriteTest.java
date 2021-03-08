package com.kreative.hexcellent.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteBufferWriteTest extends ByteBufferTestBase {
	private static void testWrite() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		byte[] expected = new byte[end - start];
		for (int i = 0, v = start; v < end; v++, i++) expected[i] = (byte)v;
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		randomByteBuffer(start, end, false).write(actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(0, 256, false).write(actual, start, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(start, end, false).write(actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(0, 256, false).write(actual, start, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testWrite();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
