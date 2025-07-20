package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.CharacterItem;

public class CharacterTest extends AbstractTest {
	private static final String N1 = "\u0000";
	private static final String N2 = "\u0000\u0000";
	private static final String N3 = "\u0000\u0000\u0000";
	private static final String N4 = "\u0000\u0000\u0000\u0000";
	
	private static byte[] b(int... a) {
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) b[i] = (byte)a[i];
		return b;
	}
	
	private static BufferedCountedInputStream s(byte[] b) {
		return new BufferedCountedInputStream(new ByteArrayInputStream(b));
	}
	
	private final CharacterItem item1 = new CharacterItem(0, "", "", false, 32, "MacRoman", null);
	private final CharacterItem item2 = new CharacterItem(0, "", "", false, 32, "MacRoman", " ");
	private final CharacterItem item3 = new CharacterItem(0, "", "", true, 32, "ISO-8859-1", null);
	private final CharacterItem item4 = new CharacterItem(0, "", "", true, 32, "ISO-8859-1", " ");
	
	private void testRead(byte[] s0, String s1, String s2, String s3, String s4) throws IOException {
		CharacterItem.Instance i;
		i = item1.read(new Closure(null), s(s0)); expectEquals(i.getStringValue(), s1);
		i = item2.read(new Closure(null), s(s0)); expectEquals(i.getStringValue(), s2);
		i = item3.read(new Closure(null), s(s0)); expectEquals(i.getStringValue(), s3);
		i = item4.read(new Closure(null), s(s0)); expectEquals(i.getStringValue(), s4);
	}
	
	private void testWrite(String s0, byte[] s1, byte[] s2, byte[] s3, byte[] s4) throws IOException {
		CharacterItem.Instance i;
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		i = item1.read(new Closure(null), s(b(-1)));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s1);
		i = item2.read(new Closure(null), s(b(-1)));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s2);
		i = item3.read(new Closure(null), s(b(-1)));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s3);
		i = item4.read(new Closure(null), s(b(-1)));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s4);
	}
	
	public void run() throws Exception {
		testRead(b(), "", "", "", "");
		testRead(b('A'), "A", "A", "A", "A");
		testRead(b('A','B'), "AB", "AB", "BA", "BA");
		testRead(b('A','B','C'), "ABC", "ABC", "CBA", "CBA");
		
		testRead(b(0,0,0,0), N4, N4, N4, N4);
		testRead(b('A',0,0,0), "A"+N3, "A"+N3, N3+"A", N3+"A");
		testRead(b('A','B',0,0), "AB"+N2, "AB"+N2, N2+"BA", N2+"BA");
		testRead(b('A','B','C',0), "ABC"+N1, "ABC"+N1, N1+"CBA", N1+"CBA");
		
		testRead(b('A','B','C','D'), "ABCD", "ABCD", "DCBA", "DCBA");
		testRead(b('A','B','C','D','E'), "ABCD", "ABCD", "DCBA", "DCBA");
		testRead(b(0xA1,0xC2,'8',0xA8), "°¬8®", "°¬8®", "¨8Â¡", "¨8Â¡");
		
		testWrite("", b(0,0,0,0), b(32,32,32,32), b(0,0,0,0), b(32,32,32,32));
		testWrite("A", b('A',0,0,0), b('A',32,32,32), b(0,0,0,'A'), b(32,32,32,'A'));
		testWrite("AB", b('A','B',0,0), b('A','B',32,32), b(0,0,'B','A'), b(32,32,'B','A'));
		testWrite("ABC", b('A','B','C',0), b('A','B','C',32), b(0,'C','B','A'), b(32,'C','B','A'));
		testWrite("ABCD", b('A','B','C','D'), b('A','B','C','D'), b('D','C','B','A'), b('D','C','B','A'));
		testWrite("ABCDE", b('A','B','C','D'), b('A','B','C','D'), b('D','C','B','A'), b('D','C','B','A'));
		testWrite("°¬8®", b(0xA1,0xC2,'8',0xA8), b(0xA1,0xC2,'8',0xA8), b(0xAE,'8',0xAC,0xB0), b(0xAE,'8',0xAC,0xB0));
	}
	
	public static void main(String[] args) {
		new CharacterTest().main();
	}
}
