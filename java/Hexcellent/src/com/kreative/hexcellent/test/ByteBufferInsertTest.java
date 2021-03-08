package com.kreative.hexcellent.test;

import java.io.IOException;
import java.util.Arrays;
import com.kreative.hexcellent.buffer.ByteBuffer;

public class ByteBufferInsertTest extends ByteBufferTestBase {
	private static void testInsert() throws IOException {
		int offset = random.nextInt(257);
		int insLen = random.nextInt(257);
		byte insVal = (byte)random.nextInt(256);
		byte[] inserted = new byte[insLen];
		byte[] expected = new byte[256 + insLen];
		for (int ei = 0, v = 0; v < offset; v++, ei++) expected[ei] = (byte)v;
		for (int ei = offset, ii = 0; ii < insLen; ii++, ei++) expected[ei] = inserted[ii] = insVal;
		for (int ei = offset + insLen, v = offset; v < 256; v++, ei++) expected[ei] = (byte)v;
		System.out.print("[" + toHexString(offset) + "]");
		
		ByteBuffer b = randomByteBuffer(0, 256, true);
		b.insert(offset, inserted, 0, insLen);
		byte[] actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		b = randomByteBuffer(0, 0, true);
		b.insert(0, inserted, 0, insLen);
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(inserted, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		int padding = random.nextInt(256) + 1;
		int srcOffset = random.nextInt(padding);
		byte[] src = new byte[insLen + padding];
		for (int i = 0; i < insLen; i++) src[srcOffset + i] = insVal;
		
		b = randomByteBuffer(0, 256, true);
		b.insert(offset, src, srcOffset, insLen);
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		b = randomByteBuffer(0, 0, true);
		b.insert(0, src, srcOffset, insLen);
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(inserted, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testInsert();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
