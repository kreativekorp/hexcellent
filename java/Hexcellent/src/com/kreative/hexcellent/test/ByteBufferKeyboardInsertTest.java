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
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.RafByteBuffer;

public class ByteBufferKeyboardInsertTest {
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
		int start = random.nextInt(257);
		int end = random.nextInt(257);
		int count = random.nextInt(32) + 1;
		int n1 = random.nextInt(16);
		int n2 = random.nextInt(16);
		byte insVal = (byte)((n1 << 4) | n2);
		byte[] data = new byte[]{ insVal };
		
		byte[] expected = new byte[256 - Math.abs(start - end) + count];
		for (int i = 0, v = 0; v < Math.min(start, end); v++, i++) expected[i] = (byte)v;
		for (int i = Math.min(start, end), v = 0; v < count; v++, i++) expected[i] = insVal;
		for (int i = Math.min(start, end) + count, v = Math.max(start, end); v < 256; v++, i++) expected[i] = (byte)v;
		
		byte[] expected2 = new byte[256];
		for (int i = 0; i < 256; i++) expected2[i] = (byte)i;
		
		System.out.print("[" + toHexString(Math.min(start,end)) + "," + toHexString(Math.max(start,end)-1) + "," + toHexString(insVal & 0xFF) + "," + count + "]");
		
		ByteBuffer b = randomByteBuffer(0, 256, true);
		ByteBufferDocument d = new ByteBufferDocument(b);
		d.setSelectionRange(start, end);
		for (int i = 0; i < count; i++) d.insert(data);
		
		byte[] actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == Math.min(start, end) + count) ? "\tPASS" : "\tFAIL\u0007");
		
		d.undo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected2, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == start) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionEnd() == end) ? "\tPASS" : "\tFAIL\u0007");
		
		d.redo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == Math.min(start, end) + count) ? "\tPASS" : "\tFAIL\u0007");
		
		b = randomByteBuffer(0, 256, true);
		d = new ByteBufferDocument(b);
		d.setSelectionRange(start, end);
		for (int i = 0; i < count; i++) { d.insert(n1); d.insert(n2); }
		
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == Math.min(start, end) + count) ? "\tPASS" : "\tFAIL\u0007");
		
		d.undo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected2, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == start) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionEnd() == end) ? "\tPASS" : "\tFAIL\u0007");
		
		d.redo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == Math.min(start, end) + count) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testInsert();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
