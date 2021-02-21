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

public class ByteBufferKeyboardDeleteTest {
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
	
	private static void testDelete() throws IOException {
		ByteBuffer b = randomByteBuffer(0, 256, true);
		ByteBufferDocument d = new ByteBufferDocument(b);
		
		int origin = 64 + random.nextInt(129);
		d.setSelectionRange(origin, origin);
		
		int count = random.nextInt(32) + 1;
		int forward = 0;
		int backward = 0;
		for (int i = 0; i < count; i++) {
			if (random.nextBoolean()) {
				d.deleteForward(); forward++;
			} else {
				d.deleteBackward(); backward++;
			}
		}
		
		System.out.print(origin + "\t" + forward + "\t" + backward + "\t" + count);
		
		byte[] expected = new byte[256 - count];
		for (int i = 0, v = 0; v < origin - backward; v++, i++) expected[i] = (byte)v;
		for (int i = origin - backward, v = origin + forward; v < 256; v++, i++) expected[i] = (byte)v;
		
		byte[] expected2 = new byte[256];
		for (int i = 0; i < 256; i++) expected2[i] = (byte)i;
		
		byte[] actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == origin - backward) ? "\tPASS" : "\tFAIL\u0007");
		
		d.undo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected2, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == origin) ? "\tPASS" : "\tFAIL\u0007");
		
		d.redo();
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		System.out.print((d.getSelectionStart() == origin - backward) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testDelete();
	}
}
