package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.HexItem;
import com.kreative.templature.template.item.TheRestIsHexItem;

public class HexTest extends AbstractTest {
	private static byte[] b(int... a) {
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) b[i] = (byte)a[i];
		return b;
	}
	
	private static BufferedCountedInputStream s(byte[] b) {
		return new BufferedCountedInputStream(new ByteArrayInputStream(b));
	}
	
	private final TheRestIsHexItem hexd = new TheRestIsHexItem(0, "", "", "ISO-8859-1", false);
	private final HexItem hxxx = new HexItem(0, "", "", 4, "ISO-8859-1", false);
	private final HexItem zero = new HexItem(0, "", "", 0, "ISO-8859-1", false);
	
	private void testRead(byte[] a, byte[] b, int t, int z) throws IOException {
		BufferedCountedInputStream in;
		TheRestIsHexItem.Instance inst1 = hexd.read(new Closure(null), (in = s(a)));
		expectEquals("hexd", inst1.getHexValue(), a);
		expectEquals(in.read(), -1);
		HexItem.Instance inst2 = hxxx.read(new Closure(null), (in = s(a)));
		expectEquals("hxxx", inst2.getHexValue(), b);
		expectEquals(in.read(), t);
		HexItem.Instance inst3 = zero.read(new Closure(null), (in = s(a)));
		expectEquals("zero", inst3.getHexValue(), b());
		expectEquals(in.read(), z);
	}
	
	private void testWrite(byte[] a, byte[] b) throws IOException {
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		TheRestIsHexItem.Instance inst1 = hexd.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		inst1.setHexValue(a); inst1.setHexValue(null);
		record(inst1.getHexValue() == a, inst1.getHexValue(), a);
		inst1.write(os); os.close(); expectEquals("hexd", aos.toByteArray(), a);
		HexItem.Instance inst2 = hxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		inst2.setHexValue(a); inst1.setHexValue(null);
		record(inst1.getHexValue() == a, inst1.getHexValue(), a);
		inst2.write(os); os.close(); expectEquals("hxxx", aos.toByteArray(), b);
		HexItem.Instance inst3 = zero.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		inst3.setHexValue(a); inst1.setHexValue(null);
		record(inst1.getHexValue() == a, inst1.getHexValue(), a);
		inst3.write(os); os.close(); expectEquals("zero", aos.toByteArray(), b());
	}
	
	public void run() throws Exception {
		testRead(b(), b(), -1, -1);
		testRead(b(1), b(1), -1, 1);
		testRead(b(1,2), b(1,2), -1, 1);
		testRead(b(1,2,3), b(1,2,3), -1, 1);
		testRead(b(1,2,3,4), b(1,2,3,4), -1, 1);
		testRead(b(1,2,3,4,5), b(1,2,3,4), 5, 1);
		testRead(b(1,2,3,4,5,6), b(1,2,3,4), 5, 1);
		testWrite(b(), b(0,0,0,0));
		testWrite(b(1), b(1,0,0,0));
		testWrite(b(1,2), b(1,2,0,0));
		testWrite(b(1,2,3), b(1,2,3,0));
		testWrite(b(1,2,3,4), b(1,2,3,4));
		testWrite(b(1,2,3,4,5), b(1,2,3,4));
		testWrite(b(1,2,3,4,5,6), b(1,2,3,4));
	}
	
	public static void main(String[] args) {
		new HexTest().main();
	}
}
