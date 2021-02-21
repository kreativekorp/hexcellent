package com.kreative.hexcellent.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.RafByteBuffer;

public class ByteBufferInsertTest {
	private static final Random random = new Random();
	
	private static ByteBuffer randomByteBuffer(int start, int end, boolean editable) throws IOException {
		switch (random.nextInt(3)) {
			case 0:
				int fileStart = random.nextInt(start + 1);
				int fileEnd = end + random.nextInt(257 - end);
				File file = File.createTempFile("ByteBufferReadTest", ".bin");
				FileOutputStream out = new FileOutputStream(file);
				for (int i = fileStart; i < fileEnd; i++) out.write(i);
				out.flush();
				out.close();
				file.deleteOnExit();
				RafByteBuffer raf = new RafByteBuffer(file, start - fileStart, end - start);
				return editable ? new CompositeByteBuffer(raf) : raf;
			case 1:
				int arrStart = random.nextInt(start + 1);
				int arrEnd = end + random.nextInt(257 - end);
				byte[] arr = new byte[arrEnd - arrStart];
				for (int i = 0, v = arrStart; v < arrEnd; v++, i++) arr[i] = (byte)v;
				return new ArrayByteBuffer(arr, start - arrStart, end - start);
			default:
				List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
				while (start < end) {
					int bufEnd = start + random.nextInt(end - start + 1);
					buffers.add(randomByteBuffer(start, bufEnd, false));
					start = bufEnd;
				}
				return new CompositeByteBuffer(buffers);
		}
	}
	
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
