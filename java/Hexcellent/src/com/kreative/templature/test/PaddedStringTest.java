package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.PaddedStringItem;
import com.kreative.templature.template.item.TheRestIsTextItem;

public class PaddedStringTest extends AbstractTest {
	private static byte[] b(int... a) {
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) b[i] = (byte)a[i];
		return b;
	}
	
	private static BufferedCountedInputStream s(byte[] b) {
		return new BufferedCountedInputStream(new ByteArrayInputStream(b));
	}
	
	private final TheRestIsTextItem text = new TheRestIsTextItem(0, "", "", "MacRoman", "\r");
	private final PaddedStringItem nxxx = new PaddedStringItem(0, "", "", 4, (byte)0, "ISO-8859-1", "\n");
	private final PaddedStringItem sxxx = new PaddedStringItem(0, "", "", 4, (byte)32, "MacRoman", "\r");
	private final PaddedStringItem zero = new PaddedStringItem(0, "", "", 0, (byte)0, "ISO-8859-1", "\n");
	
	private void testRead(byte[] b, TheRestIsTextItem i, String s) throws IOException {
		BufferedCountedInputStream in = s(b);
		TheRestIsTextItem.Instance inst = i.read(new Closure(null), in);
		expectEquals(inst.getStringValue(), s);
		expectEquals(in.read(), -1);
	}
	
	private void testRead(byte[] b, PaddedStringItem i, String s, int t) throws IOException {
		BufferedCountedInputStream in = s(b);
		PaddedStringItem.Instance inst = i.read(new Closure(null), in);
		expectEquals(inst.getStringValue(), s);
		expectEquals(in.read(), t);
	}
	
	private void testWrite(String s0, byte[] s1) throws IOException {
		TheRestIsTextItem.Instance i;
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		i = text.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s1);
	}
	
	private void testWrite(String s0, byte[] s1, byte[] s2) throws IOException {
		PaddedStringItem.Instance i;
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		i = nxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s1);
		i = sxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s2);
		i = zero.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), b());
	}
	
	public void run() throws Exception {
		testRead(b(), text, "");
		testRead(b(0), text, "\u0000");
		testRead(b(32), text, " ");
		testRead(b('A'), text, "A");
		testRead(b('A',0), text, "A\u0000");
		testRead(b('A',32), text, "A ");
		testRead(b('A','B'), text, "AB");
		testRead(b('A','B',0), text, "AB\u0000");
		testRead(b('A','B',32), text, "AB ");
		testRead(b('A','B','C'), text, "ABC");
		testRead(b('A','B','C',0), text, "ABC\u0000");
		testRead(b('A','B','C',32), text, "ABC ");
		testRead(b('A',0,'C'), text, "A\u0000C");
		testRead(b('A',0,'C',0), text, "A\u0000C\u0000");
		testRead(b('A',0,'C',32), text, "A\u0000C ");
		testRead(b('A',32,'C'), text, "A C");
		testRead(b('A',32,'C',0), text, "A C\u0000");
		testRead(b('A',32,'C',32), text, "A C ");
		testRead(b(0xA1,0xC2,'\n',0xA8), text, "°¬\n®");
		testRead(b(0xA1,0xC2,'\r',0xA8), text, "°¬\n®");
		
		testRead(b(), nxxx, "", -1);
		testRead(b(0), nxxx, "", -1);
		testRead(b(0,0), nxxx, "", -1);
		testRead(b(0,0,0), nxxx, "", -1);
		testRead(b(0,0,0,0), nxxx, "", -1);
		testRead(b(0,0,0,0,0), nxxx, "", 0);
		testRead(b('A'), nxxx, "A", -1);
		testRead(b('A',0), nxxx, "A", -1);
		testRead(b('A',0,0), nxxx, "A", -1);
		testRead(b('A',0,0,0), nxxx, "A", -1);
		testRead(b('A',0,0,0,0), nxxx, "A", 0);
		testRead(b('A','B'), nxxx, "AB", -1);
		testRead(b('A','B',0), nxxx, "AB", -1);
		testRead(b('A','B',0,0), nxxx, "AB", -1);
		testRead(b('A','B',0,0,0), nxxx, "AB", 0);
		testRead(b('A','B','C'), nxxx, "ABC", -1);
		testRead(b('A','B','C',0), nxxx, "ABC", -1);
		testRead(b('A','B','C',0,0), nxxx, "ABC", 0);
		testRead(b('A','B','C','D'), nxxx, "ABCD", -1);
		testRead(b('A','B','C','D',0), nxxx, "ABCD", 0);
		testRead(b('A','B','C','D','E'), nxxx, "ABCD", 'E');
		testRead(b('A',0,'C'), nxxx, "A\u0000C", -1);
		testRead(b('A',0,'C',0), nxxx, "A\u0000C", -1);
		testRead(b('A',0,'C',0,0), nxxx, "A\u0000C", 0);
		testRead(b('A',32,'C'), nxxx, "A C", -1);
		testRead(b('A',32,'C',0), nxxx, "A C", -1);
		testRead(b('A',32,'C',0,0), nxxx, "A C", 0);
		testRead(b('A',32), nxxx, "A ", -1);
		testRead(b(32), nxxx, " ", -1);
		
		testRead(b(), sxxx, "", -1);
		testRead(b(32), sxxx, "", -1);
		testRead(b(32,32), sxxx, "", -1);
		testRead(b(32,32,32), sxxx, "", -1);
		testRead(b(32,32,32,32), sxxx, "", -1);
		testRead(b(32,32,32,32,32), sxxx, "", 32);
		testRead(b('A'), sxxx, "A", -1);
		testRead(b('A',32), sxxx, "A", -1);
		testRead(b('A',32,32), sxxx, "A", -1);
		testRead(b('A',32,32,32), sxxx, "A", -1);
		testRead(b('A',32,32,32,32), sxxx, "A", 32);
		testRead(b('A','B'), sxxx, "AB", -1);
		testRead(b('A','B',32), sxxx, "AB", -1);
		testRead(b('A','B',32,32), sxxx, "AB", -1);
		testRead(b('A','B',32,32,32), sxxx, "AB", 32);
		testRead(b('A','B','C'), sxxx, "ABC", -1);
		testRead(b('A','B','C',32), sxxx, "ABC", -1);
		testRead(b('A','B','C',32,32), sxxx, "ABC", 32);
		testRead(b('A','B','C','D'), sxxx, "ABCD", -1);
		testRead(b('A','B','C','D',32), sxxx, "ABCD", 32);
		testRead(b('A','B','C','D','E'), sxxx, "ABCD", 'E');
		testRead(b('A',0,'C'), sxxx, "A\u0000C", -1);
		testRead(b('A',0,'C',32), sxxx, "A\u0000C", -1);
		testRead(b('A',0,'C',32,32), sxxx, "A\u0000C", 32);
		testRead(b('A',32,'C'), sxxx, "A C", -1);
		testRead(b('A',32,'C',32), sxxx, "A C", -1);
		testRead(b('A',32,'C',32,32), sxxx, "A C", 32);
		testRead(b('A',0), sxxx, "A\u0000", -1);
		testRead(b(0), sxxx, "\u0000", -1);
		
		testRead(b(), zero, "", -1);
		testRead(b(0), zero, "", 0);
		testRead(b(1), zero, "", 1);
		
		testRead(b(0xA1,0xC2,'\n',0xA8), nxxx, "¡Â\n¨", -1);
		testRead(b(0xA1,0xC2,'\r',0xA8), nxxx, "¡Â\r¨", -1);
		testRead(b(0xA1,0xC2,'\n',0xA8), sxxx, "°¬\n®", -1);
		testRead(b(0xA1,0xC2,'\r',0xA8), sxxx, "°¬\n®", -1);
		
		testWrite("", b());
		testWrite("A", b('A'));
		testWrite("AB", b('A','B'));
		testWrite("ABC", b('A','B','C'));
		testWrite("ABCD", b('A','B','C','D'));
		testWrite("ABCDE", b('A','B','C','D','E'));
		testWrite("°¬\n®", b(0xA1,0xC2,'\r',0xA8));
		testWrite("°¬\r®", b(0xA1,0xC2,'\r',0xA8));
		
		testWrite("", b(0,0,0,0), b(32,32,32,32));
		testWrite("A", b('A',0,0,0), b('A',32,32,32));
		testWrite("AB", b('A','B',0,0), b('A','B',32,32));
		testWrite("ABC", b('A','B','C',0), b('A','B','C',32));
		testWrite("ABCD", b('A','B','C','D'), b('A','B','C','D'));
		testWrite("ABCDE", b('A','B','C','D'), b('A','B','C','D'));
		testWrite("°¬\n®", b(0xB0,0xAC,'\n',0xAE), b(0xA1,0xC2,'\r',0xA8));
		testWrite("°¬\r®", b(0xB0,0xAC,'\r',0xAE), b(0xA1,0xC2,'\r',0xA8));
	}
	
	public static void main(String[] args) {
		new PaddedStringTest().main();
	}
}
