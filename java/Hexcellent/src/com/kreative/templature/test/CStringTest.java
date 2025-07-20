package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.CStringItem;

public class CStringTest extends AbstractTest {
	private static byte[] b(int... a) {
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) b[i] = (byte)a[i];
		return b;
	}
	
	private static BufferedCountedInputStream s(byte[] b) {
		return new BufferedCountedInputStream(new ByteArrayInputStream(b));
	}
	
	private final CStringItem cstr = new CStringItem(0, "", "", CStringItem.LENGTH_VARIABLE, "MacRoman", "\r");
	private final CStringItem ecst = new CStringItem(0, "", "", CStringItem.LENGTH_EVEN, "MacRoman", "\r");
	private final CStringItem ocst = new CStringItem(0, "", "", CStringItem.LENGTH_ODD, "MacRoman", "\r");
	private final CStringItem cxxx = new CStringItem(0, "", "", 4, "MacRoman", "\r");
	private final CStringItem zero = new CStringItem(0, "", "", 0, "MacRoman", "\r");
	
	private void testRead(byte[] b, CStringItem i, String s, int t) throws IOException {
		BufferedCountedInputStream in = s(b);
		CStringItem.Instance inst = i.read(new Closure(null), in);
		expectEquals(inst.getStringValue(), s);
		expectEquals(in.read(), t);
	}
	
	private void testWrite(String s0, byte[] s1, byte[] s2, byte[] s3, byte[] s4) throws IOException {
		CStringItem.Instance i;
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		i = cstr.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s1);
		i = ecst.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s2);
		i = ocst.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s3);
		i = cxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s4);
		i = zero.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), b());
	}
	
	public void run() throws Exception {
		testRead(b(), cstr, "", -1);
		testRead(b(0), cstr, "", -1);
		testRead(b(0,'A'), cstr, "", 'A');
		testRead(b('A'), cstr, "A", -1);
		testRead(b('A',0), cstr, "A", -1);
		testRead(b('A',0,'B'), cstr, "A", 'B');
		testRead(b('A','B'), cstr, "AB", -1);
		testRead(b('A','B',0), cstr, "AB", -1);
		testRead(b('A','B',0,'C'), cstr, "AB", 'C');
		testRead(b('A','B','C'), cstr, "ABC", -1);
		testRead(b('A','B','C',0), cstr, "ABC", -1);
		testRead(b('A','B','C',0,'D'), cstr, "ABC", 'D');
		
		testRead(b(), ecst, "", -1);
		testRead(b(0), ecst, "", -1);
		testRead(b(0,'A'), ecst, "", -1);
		testRead(b(0,'A','B'), ecst, "", 'B');
		testRead(b('A'), ecst, "A", -1);
		testRead(b('A',0), ecst, "A", -1);
		testRead(b('A',0,'B'), ecst, "A", 'B');
		testRead(b('A','B'), ecst, "AB", -1);
		testRead(b('A','B',0), ecst, "AB", -1);
		testRead(b('A','B',0,'C'), ecst, "AB", -1);
		testRead(b('A','B',0,'C','D'), ecst, "AB", 'D');
		testRead(b('A','B','C'), ecst, "ABC", -1);
		testRead(b('A','B','C',0), ecst, "ABC", -1);
		testRead(b('A','B','C',0,'D'), ecst, "ABC", 'D');
		
		testRead(b(), ocst, "", -1);
		testRead(b(0), ocst, "", -1);
		testRead(b(0,'A'), ocst, "", 'A');
		testRead(b('A'), ocst, "A", -1);
		testRead(b('A',0), ocst, "A", -1);
		testRead(b('A',0,'B'), ocst, "A", -1);
		testRead(b('A',0,'B','C'), ocst, "A", 'C');
		testRead(b('A','B'), ocst, "AB", -1);
		testRead(b('A','B',0), ocst, "AB", -1);
		testRead(b('A','B',0,'C'), ocst, "AB", 'C');
		testRead(b('A','B','C'), ocst, "ABC", -1);
		testRead(b('A','B','C',0), ocst, "ABC", -1);
		testRead(b('A','B','C',0,'D'), ocst, "ABC", -1);
		testRead(b('A','B','C',0,'D','E'), ocst, "ABC", 'E');
		
		testRead(b(), cxxx, "", -1);
		testRead(b(0), cxxx, "", -1);
		testRead(b(0,'A'), cxxx, "", -1);
		testRead(b(0,'A','B'), cxxx, "", -1);
		testRead(b(0,'A','B','C'), cxxx, "", -1);
		testRead(b(0,'A','B','C','D'), cxxx, "", 'D');
		testRead(b('A'), cxxx, "A", -1);
		testRead(b('A',0), cxxx, "A", -1);
		testRead(b('A',0,'B'), cxxx, "A", -1);
		testRead(b('A',0,'B','C'), cxxx, "A", -1);
		testRead(b('A',0,'B','C','D'), cxxx, "A", 'D');
		testRead(b('A','B'), cxxx, "AB", -1);
		testRead(b('A','B',0), cxxx, "AB", -1);
		testRead(b('A','B',0,'C'), cxxx, "AB", -1);
		testRead(b('A','B',0,'C','D'), cxxx, "AB", 'D');
		testRead(b('A','B','C'), cxxx, "ABC", -1);
		testRead(b('A','B','C',0), cxxx, "ABC", -1);
		testRead(b('A','B','C',0,'D'), cxxx, "ABC", 'D');
		testRead(b('A','B','C','D'), cxxx, "ABCD", -1);
		testRead(b('A','B','C','D',0), cxxx, "ABCD", 0);
		testRead(b('A','B','C','D','E'), cxxx, "ABCD", 'E');
		
		testRead(b(), zero, "", -1);
		testRead(b(0), zero, "", 0);
		testRead(b(1), zero, "", 1);
		
		testRead(b(0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\n',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC,0), cstr, "°≤√‘Âˆ\nß∏…⁄Î¸", -1);
		testRead(b(0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\r',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC,0), cstr, "°≤√‘Âˆ\nß∏…⁄Î¸", -1);
		
		testWrite(
			"",
			b(0), // variable length
			b(0,0), // even length
			b(0), // odd length
			b(0,0,0,0) // fixed length
		);
		testWrite(
			"A",
			b('A',0), // variable length
			b('A',0), // even length
			b('A',0,0), // odd length
			b('A',0,0,0) // fixed length
		);
		testWrite(
			"AB",
			b('A','B',0), // variable length
			b('A','B',0,0), // even length
			b('A','B',0), // odd length
			b('A','B',0,0) // fixed length
		);
		testWrite(
			"ABC",
			b('A','B','C',0), // variable length
			b('A','B','C',0), // even length
			b('A','B','C',0,0), // odd length
			b('A','B','C',0) // fixed length
		);
		testWrite(
			"ABCD",
			b('A','B','C','D',0), // variable length
			b('A','B','C','D',0,0), // even length
			b('A','B','C','D',0), // odd length
			b('A','B','C',0) // fixed length
		);
		testWrite(
			"ABCDE",
			b('A','B','C','D','E',0), // variable length
			b('A','B','C','D','E',0), // even length
			b('A','B','C','D','E',0,0), // odd length
			b('A','B','C',0) // fixed length
		);
		testWrite(
			"ABCDEF",
			b('A','B','C','D','E','F',0), // variable length
			b('A','B','C','D','E','F',0,0), // even length
			b('A','B','C','D','E','F',0), // odd length
			b('A','B','C',0) // fixed length
		);
		testWrite(
			"¡Hêllø¸\nWörld!",
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0), // variable length
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0,0), // even length
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0), // odd length
			b(0xC1,'H',0x90,0) // fixed length
		);
		testWrite(
			"¡Hêllø¸\rWörld!",
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0), // variable length
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0,0), // even length
			b(0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0), // odd length
			b(0xC1,'H',0x90,0) // fixed length
		);
	}
	
	public static void main(String[] args) {
		new CStringTest().main();
	}
}
