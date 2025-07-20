package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.PascalStringItem;

public class PascalStringTest extends AbstractTest {
	private static byte[] b(int... a) {
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) b[i] = (byte)a[i];
		return b;
	}
	
	private static BufferedCountedInputStream s(byte[] b) {
		return new BufferedCountedInputStream(new ByteArrayInputStream(b));
	}
	
	private final PascalStringItem pstr = new PascalStringItem(0, "", "", false, 8, PascalStringItem.LENGTH_VARIABLE, "MacRoman", "\r");
	private final PascalStringItem wstr = new PascalStringItem(0, "", "", false, 16, PascalStringItem.LENGTH_VARIABLE, "MacRoman", "\r");
	private final PascalStringItem rtsw = new PascalStringItem(0, "", "", true, 16, PascalStringItem.LENGTH_VARIABLE, "ISO-8859-1", "\n");
	private final PascalStringItem estr = new PascalStringItem(0, "", "", false, 8, PascalStringItem.LENGTH_EVEN, "MacRoman", "\r");
	private final PascalStringItem ewst = new PascalStringItem(0, "", "", false, 16, PascalStringItem.LENGTH_EVEN, "MacRoman", "\r");
	private final PascalStringItem tswe = new PascalStringItem(0, "", "", true, 16, PascalStringItem.LENGTH_EVEN, "ISO-8859-1", "\n");
	private final PascalStringItem ostr = new PascalStringItem(0, "", "", false, 8, PascalStringItem.LENGTH_ODD, "MacRoman", "\r");
	private final PascalStringItem owst = new PascalStringItem(0, "", "", false, 16, PascalStringItem.LENGTH_ODD, "MacRoman", "\r");
	private final PascalStringItem tswo = new PascalStringItem(0, "", "", true, 16, PascalStringItem.LENGTH_ODD, "ISO-8859-1", "\n");
	private final PascalStringItem pxxx = new PascalStringItem(0, "", "", false, 8, 4, "MacRoman", "\r");
	private final PascalStringItem wxxx = new PascalStringItem(0, "", "", false, 16, 4, "MacRoman", "\r");
	private final PascalStringItem xxxw = new PascalStringItem(0, "", "", true, 16, 4, "ISO-8859-1", "\n");
	private final PascalStringItem zero = new PascalStringItem(0, "", "", false, 8, 0, "MacRoman", "\r");
	private final PascalStringItem wzro = new PascalStringItem(0, "", "", false, 16, 0, "MacRoman", "\r");
	private final PascalStringItem orzw = new PascalStringItem(0, "", "", true, 16, 0, "ISO-8859-1", "\n");
	
	private void testRead(byte[] b, PascalStringItem i, String s, int t) throws IOException {
		BufferedCountedInputStream in = s(b);
		PascalStringItem.Instance inst = i.read(new Closure(null), in);
		expectEquals(inst.getStringValue(), s);
		expectEquals(in.read(), t);
	}
	
	private void testWrite(
		String s0, byte[] s1, byte[] s2, byte[] s3, byte[] s4, byte[] s5, byte[] s6,
		byte[] s7, byte[] s8, byte[] s9, byte[] s10, byte[] s11, byte[] s12
	) throws IOException {
		PascalStringItem.Instance i;
		ByteArrayOutputStream aos;
		BufferedCountedOutputStream os;
		i = pstr.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s1);
		i = wstr.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s2);
		i = rtsw.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s3);
		i = estr.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s4);
		i = ewst.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s5);
		i = tswe.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s6);
		i = ostr.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s7);
		i = owst.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s8);
		i = tswo.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s9);
		i = pxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s10);
		i = wxxx.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s11);
		i = xxxw.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), s12);
		i = zero.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), b(0));
		i = wzro.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), b(0,0));
		i = orzw.read(new Closure(null), s(b()));
		os = new BufferedCountedOutputStream((aos = new ByteArrayOutputStream()));
		i.setStringValue(s0); i.write(os); os.close(); expectEquals(s0, aos.toByteArray(), b(0,0));
	}
	
	public void run() throws Exception {
		testRead(b(), pstr, "", -1);
		testRead(b(0), pstr, "", -1);
		testRead(b(0,'A'), pstr, "", 'A');
		testRead(b(1), pstr, "", -1);
		testRead(b(1,'A'), pstr, "A", -1);
		testRead(b(1,'A','B'), pstr, "A", 'B');
		testRead(b(2), pstr, "", -1);
		testRead(b(2,'A'), pstr, "A", -1);
		testRead(b(2,'A','B'), pstr, "AB", -1);
		testRead(b(2,'A','B','C'), pstr, "AB", 'C');
		testRead(b(3), pstr, "", -1);
		testRead(b(3,'A'), pstr, "A", -1);
		testRead(b(3,'A','B'), pstr, "AB", -1);
		testRead(b(3,'A','B','C'), pstr, "ABC", -1);
		testRead(b(3,'A','B','C','D'), pstr, "ABC", 'D');
		
		testRead(b(), wstr, "", -1);
		testRead(b(0), wstr, "", -1);
		testRead(b(0,0), wstr, "", -1);
		testRead(b(0,0,'A'), wstr, "", 'A');
		testRead(b(0,1), wstr, "", -1);
		testRead(b(0,1,'A'), wstr, "A", -1);
		testRead(b(0,1,'A','B'), wstr, "A", 'B');
		testRead(b(0,2), wstr, "", -1);
		testRead(b(0,2,'A'), wstr, "A", -1);
		testRead(b(0,2,'A','B'), wstr, "AB", -1);
		testRead(b(0,2,'A','B','C'), wstr, "AB", 'C');
		testRead(b(0,3), wstr, "", -1);
		testRead(b(0,3,'A'), wstr, "A", -1);
		testRead(b(0,3,'A','B'), wstr, "AB", -1);
		testRead(b(0,3,'A','B','C'), wstr, "ABC", -1);
		testRead(b(0,3,'A','B','C','D'), wstr, "ABC", 'D');
		
		testRead(b(), rtsw, "", -1);
		testRead(b(0), rtsw, "", -1);
		testRead(b(0,0), rtsw, "", -1);
		testRead(b(0,0,'A'), rtsw, "", 'A');
		testRead(b(1), rtsw, "", -1);
		testRead(b(1,0), rtsw, "", -1);
		testRead(b(1,0,'A'), rtsw, "A", -1);
		testRead(b(1,0,'A','B'), rtsw, "A", 'B');
		testRead(b(2), rtsw, "", -1);
		testRead(b(2,0), rtsw, "", -1);
		testRead(b(2,0,'A'), rtsw, "A", -1);
		testRead(b(2,0,'A','B'), rtsw, "AB", -1);
		testRead(b(2,0,'A','B','C'), rtsw, "AB", 'C');
		testRead(b(3), rtsw, "", -1);
		testRead(b(3,0), rtsw, "", -1);
		testRead(b(3,0,'A'), rtsw, "A", -1);
		testRead(b(3,0,'A','B'), rtsw, "AB", -1);
		testRead(b(3,0,'A','B','C'), rtsw, "ABC", -1);
		testRead(b(3,0,'A','B','C','D'), rtsw, "ABC", 'D');
		
		testRead(b(), estr, "", -1);
		testRead(b(0), estr, "", -1);
		testRead(b(0,'A'), estr, "", -1);
		testRead(b(0,'A','B'), estr, "", 'B');
		testRead(b(1), estr, "", -1);
		testRead(b(1,'A'), estr, "A", -1);
		testRead(b(1,'A','B'), estr, "A", 'B');
		testRead(b(2), estr, "", -1);
		testRead(b(2,'A'), estr, "A", -1);
		testRead(b(2,'A','B'), estr, "AB", -1);
		testRead(b(2,'A','B','C'), estr, "AB", -1);
		testRead(b(2,'A','B','C','D'), estr, "AB", 'D');
		testRead(b(3), estr, "", -1);
		testRead(b(3,'A'), estr, "A", -1);
		testRead(b(3,'A','B'), estr, "AB", -1);
		testRead(b(3,'A','B','C'), estr, "ABC", -1);
		testRead(b(3,'A','B','C','D'), estr, "ABC", 'D');
		
		testRead(b(), ewst, "", -1);
		testRead(b(0), ewst, "", -1);
		testRead(b(0,0), ewst, "", -1);
		testRead(b(0,0,'A'), ewst, "", 'A');
		testRead(b(0,1), ewst, "", -1);
		testRead(b(0,1,'A'), ewst, "A", -1);
		testRead(b(0,1,'A','B'), ewst, "A", -1);
		testRead(b(0,1,'A','B','C'), ewst, "A", 'C');
		testRead(b(0,2), ewst, "", -1);
		testRead(b(0,2,'A'), ewst, "A", -1);
		testRead(b(0,2,'A','B'), ewst, "AB", -1);
		testRead(b(0,2,'A','B','C'), ewst, "AB", 'C');
		testRead(b(0,3), ewst, "", -1);
		testRead(b(0,3,'A'), ewst, "A", -1);
		testRead(b(0,3,'A','B'), ewst, "AB", -1);
		testRead(b(0,3,'A','B','C'), ewst, "ABC", -1);
		testRead(b(0,3,'A','B','C','D'), ewst, "ABC", -1);
		testRead(b(0,3,'A','B','C','D','E'), ewst, "ABC", 'E');
		
		testRead(b(), tswe, "", -1);
		testRead(b(0), tswe, "", -1);
		testRead(b(0,0), tswe, "", -1);
		testRead(b(0,0,'A'), tswe, "", 'A');
		testRead(b(1), tswe, "", -1);
		testRead(b(1,0), tswe, "", -1);
		testRead(b(1,0,'A'), tswe, "A", -1);
		testRead(b(1,0,'A','B'), tswe, "A", -1);
		testRead(b(1,0,'A','B','C'), tswe, "A", 'C');
		testRead(b(2), tswe, "", -1);
		testRead(b(2,0), tswe, "", -1);
		testRead(b(2,0,'A'), tswe, "A", -1);
		testRead(b(2,0,'A','B'), tswe, "AB", -1);
		testRead(b(2,0,'A','B','C'), tswe, "AB", 'C');
		testRead(b(3), tswe, "", -1);
		testRead(b(3,0), tswe, "", -1);
		testRead(b(3,0,'A'), tswe, "A", -1);
		testRead(b(3,0,'A','B'), tswe, "AB", -1);
		testRead(b(3,0,'A','B','C'), tswe, "ABC", -1);
		testRead(b(3,0,'A','B','C','D'), tswe, "ABC", -1);
		testRead(b(3,0,'A','B','C','D','E'), tswe, "ABC", 'E');
		
		testRead(b(), ostr, "", -1);
		testRead(b(0), ostr, "", -1);
		testRead(b(0,'A'), ostr, "", 'A');
		testRead(b(1), ostr, "", -1);
		testRead(b(1,'A'), ostr, "A", -1);
		testRead(b(1,'A','B'), ostr, "A", -1);
		testRead(b(1,'A','B','C'), ostr, "A", 'C');
		testRead(b(2), ostr, "", -1);
		testRead(b(2,'A'), ostr, "A", -1);
		testRead(b(2,'A','B'), ostr, "AB", -1);
		testRead(b(2,'A','B','C'), ostr, "AB", 'C');
		testRead(b(3), ostr, "", -1);
		testRead(b(3,'A'), ostr, "A", -1);
		testRead(b(3,'A','B'), ostr, "AB", -1);
		testRead(b(3,'A','B','C'), ostr, "ABC", -1);
		testRead(b(3,'A','B','C','D'), ostr, "ABC", -1);
		testRead(b(3,'A','B','C','D','E'), ostr, "ABC", 'E');
		
		testRead(b(), owst, "", -1);
		testRead(b(0), owst, "", -1);
		testRead(b(0,0), owst, "", -1);
		testRead(b(0,0,'A'), owst, "", -1);
		testRead(b(0,0,'A','B'), owst, "", 'B');
		testRead(b(0,1), owst, "", -1);
		testRead(b(0,1,'A'), owst, "A", -1);
		testRead(b(0,1,'A','B'), owst, "A", 'B');
		testRead(b(0,2), owst, "", -1);
		testRead(b(0,2,'A'), owst, "A", -1);
		testRead(b(0,2,'A','B'), owst, "AB", -1);
		testRead(b(0,2,'A','B','C'), owst, "AB", -1);
		testRead(b(0,2,'A','B','C','D'), owst, "AB", 'D');
		testRead(b(0,3), owst, "", -1);
		testRead(b(0,3,'A'), owst, "A", -1);
		testRead(b(0,3,'A','B'), owst, "AB", -1);
		testRead(b(0,3,'A','B','C'), owst, "ABC", -1);
		testRead(b(0,3,'A','B','C','D'), owst, "ABC", 'D');
		
		testRead(b(), tswo, "", -1);
		testRead(b(0), tswo, "", -1);
		testRead(b(0,0), tswo, "", -1);
		testRead(b(0,0,'A'), tswo, "", -1);
		testRead(b(0,0,'A','B'), tswo, "", 'B');
		testRead(b(1), tswo, "", -1);
		testRead(b(1,0), tswo, "", -1);
		testRead(b(1,0,'A'), tswo, "A", -1);
		testRead(b(1,0,'A','B'), tswo, "A", 'B');
		testRead(b(2), tswo, "", -1);
		testRead(b(2,0), tswo, "", -1);
		testRead(b(2,0,'A'), tswo, "A", -1);
		testRead(b(2,0,'A','B'), tswo, "AB", -1);
		testRead(b(2,0,'A','B','C'), tswo, "AB", -1);
		testRead(b(2,0,'A','B','C','D'), tswo, "AB", 'D');
		testRead(b(3), tswo, "", -1);
		testRead(b(3,0), tswo, "", -1);
		testRead(b(3,0,'A'), tswo, "A", -1);
		testRead(b(3,0,'A','B'), tswo, "AB", -1);
		testRead(b(3,0,'A','B','C'), tswo, "ABC", -1);
		testRead(b(3,0,'A','B','C','D'), tswo, "ABC", 'D');
		
		testRead(b(), pxxx, "", -1);
		testRead(b(0), pxxx, "", -1);
		testRead(b(0,'A'), pxxx, "", -1);
		testRead(b(0,'A','B'), pxxx, "", -1);
		testRead(b(0,'A','B','C'), pxxx, "", -1);
		testRead(b(0,'A','B','C','D'), pxxx, "", -1);
		testRead(b(0,'A','B','C','D','E'), pxxx, "", 'E');
		testRead(b(1), pxxx, "", -1);
		testRead(b(1,'A'), pxxx, "A", -1);
		testRead(b(1,'A','B'), pxxx, "A", -1);
		testRead(b(1,'A','B','C'), pxxx, "A", -1);
		testRead(b(1,'A','B','C','D'), pxxx, "A", -1);
		testRead(b(1,'A','B','C','D','E'), pxxx, "A", 'E');
		testRead(b(2), pxxx, "", -1);
		testRead(b(2,'A'), pxxx, "A", -1);
		testRead(b(2,'A','B'), pxxx, "AB", -1);
		testRead(b(2,'A','B','C'), pxxx, "AB", -1);
		testRead(b(2,'A','B','C','D'), pxxx, "AB", -1);
		testRead(b(2,'A','B','C','D','E'), pxxx, "AB", 'E');
		testRead(b(3), pxxx, "", -1);
		testRead(b(3,'A'), pxxx, "A", -1);
		testRead(b(3,'A','B'), pxxx, "AB", -1);
		testRead(b(3,'A','B','C'), pxxx, "ABC", -1);
		testRead(b(3,'A','B','C','D'), pxxx, "ABC", -1);
		testRead(b(3,'A','B','C','D','E'), pxxx, "ABC", 'E');
		testRead(b(4), pxxx, "", -1);
		testRead(b(4,'A'), pxxx, "A", -1);
		testRead(b(4,'A','B'), pxxx, "AB", -1);
		testRead(b(4,'A','B','C'), pxxx, "ABC", -1);
		testRead(b(4,'A','B','C','D'), pxxx, "ABCD", -1);
		testRead(b(4,'A','B','C','D','E'), pxxx, "ABCD", 'E');
		testRead(b(5), pxxx, "", -1);
		testRead(b(5,'A'), pxxx, "A", -1);
		testRead(b(5,'A','B'), pxxx, "AB", -1);
		testRead(b(5,'A','B','C'), pxxx, "ABC", -1);
		testRead(b(5,'A','B','C','D'), pxxx, "ABCD", -1);
		testRead(b(5,'A','B','C','D','E'), pxxx, "ABCD", 'E');
		
		testRead(b(), wxxx, "", -1);
		testRead(b(0), wxxx, "", -1);
		testRead(b(0,0), wxxx, "", -1);
		testRead(b(0,0,'A'), wxxx, "", -1);
		testRead(b(0,0,'A','B'), wxxx, "", -1);
		testRead(b(0,0,'A','B','C'), wxxx, "", -1);
		testRead(b(0,0,'A','B','C','D'), wxxx, "", -1);
		testRead(b(0,0,'A','B','C','D','E'), wxxx, "", 'E');
		testRead(b(0,1), wxxx, "", -1);
		testRead(b(0,1,'A'), wxxx, "A", -1);
		testRead(b(0,1,'A','B'), wxxx, "A", -1);
		testRead(b(0,1,'A','B','C'), wxxx, "A", -1);
		testRead(b(0,1,'A','B','C','D'), wxxx, "A", -1);
		testRead(b(0,1,'A','B','C','D','E'), wxxx, "A", 'E');
		testRead(b(0,2), wxxx, "", -1);
		testRead(b(0,2,'A'), wxxx, "A", -1);
		testRead(b(0,2,'A','B'), wxxx, "AB", -1);
		testRead(b(0,2,'A','B','C'), wxxx, "AB", -1);
		testRead(b(0,2,'A','B','C','D'), wxxx, "AB", -1);
		testRead(b(0,2,'A','B','C','D','E'), wxxx, "AB", 'E');
		testRead(b(0,3), wxxx, "", -1);
		testRead(b(0,3,'A'), wxxx, "A", -1);
		testRead(b(0,3,'A','B'), wxxx, "AB", -1);
		testRead(b(0,3,'A','B','C'), wxxx, "ABC", -1);
		testRead(b(0,3,'A','B','C','D'), wxxx, "ABC", -1);
		testRead(b(0,3,'A','B','C','D','E'), wxxx, "ABC", 'E');
		testRead(b(0,4), wxxx, "", -1);
		testRead(b(0,4,'A'), wxxx, "A", -1);
		testRead(b(0,4,'A','B'), wxxx, "AB", -1);
		testRead(b(0,4,'A','B','C'), wxxx, "ABC", -1);
		testRead(b(0,4,'A','B','C','D'), wxxx, "ABCD", -1);
		testRead(b(0,4,'A','B','C','D','E'), wxxx, "ABCD", 'E');
		testRead(b(0,5), wxxx, "", -1);
		testRead(b(0,5,'A'), wxxx, "A", -1);
		testRead(b(0,5,'A','B'), wxxx, "AB", -1);
		testRead(b(0,5,'A','B','C'), wxxx, "ABC", -1);
		testRead(b(0,5,'A','B','C','D'), wxxx, "ABCD", -1);
		testRead(b(0,5,'A','B','C','D','E'), wxxx, "ABCD", 'E');
		
		testRead(b(), xxxw, "", -1);
		testRead(b(0), xxxw, "", -1);
		testRead(b(0,0), xxxw, "", -1);
		testRead(b(0,0,'A'), xxxw, "", -1);
		testRead(b(0,0,'A','B'), xxxw, "", -1);
		testRead(b(0,0,'A','B','C'), xxxw, "", -1);
		testRead(b(0,0,'A','B','C','D'), xxxw, "", -1);
		testRead(b(0,0,'A','B','C','D','E'), xxxw, "", 'E');
		testRead(b(1), xxxw, "", -1);
		testRead(b(1,0), xxxw, "", -1);
		testRead(b(1,0,'A'), xxxw, "A", -1);
		testRead(b(1,0,'A','B'), xxxw, "A", -1);
		testRead(b(1,0,'A','B','C'), xxxw, "A", -1);
		testRead(b(1,0,'A','B','C','D'), xxxw, "A", -1);
		testRead(b(1,0,'A','B','C','D','E'), xxxw, "A", 'E');
		testRead(b(2), xxxw, "", -1);
		testRead(b(2,0), xxxw, "", -1);
		testRead(b(2,0,'A'), xxxw, "A", -1);
		testRead(b(2,0,'A','B'), xxxw, "AB", -1);
		testRead(b(2,0,'A','B','C'), xxxw, "AB", -1);
		testRead(b(2,0,'A','B','C','D'), xxxw, "AB", -1);
		testRead(b(2,0,'A','B','C','D','E'), xxxw, "AB", 'E');
		testRead(b(3), xxxw, "", -1);
		testRead(b(3,0), xxxw, "", -1);
		testRead(b(3,0,'A'), xxxw, "A", -1);
		testRead(b(3,0,'A','B'), xxxw, "AB", -1);
		testRead(b(3,0,'A','B','C'), xxxw, "ABC", -1);
		testRead(b(3,0,'A','B','C','D'), xxxw, "ABC", -1);
		testRead(b(3,0,'A','B','C','D','E'), xxxw, "ABC", 'E');
		testRead(b(4), xxxw, "", -1);
		testRead(b(4,0), xxxw, "", -1);
		testRead(b(4,0,'A'), xxxw, "A", -1);
		testRead(b(4,0,'A','B'), xxxw, "AB", -1);
		testRead(b(4,0,'A','B','C'), xxxw, "ABC", -1);
		testRead(b(4,0,'A','B','C','D'), xxxw, "ABCD", -1);
		testRead(b(4,0,'A','B','C','D','E'), xxxw, "ABCD", 'E');
		testRead(b(5), xxxw, "", -1);
		testRead(b(5,0), xxxw, "", -1);
		testRead(b(5,0,'A'), xxxw, "A", -1);
		testRead(b(5,0,'A','B'), xxxw, "AB", -1);
		testRead(b(5,0,'A','B','C'), xxxw, "ABC", -1);
		testRead(b(5,0,'A','B','C','D'), xxxw, "ABCD", -1);
		testRead(b(5,0,'A','B','C','D','E'), xxxw, "ABCD", 'E');
		
		testRead(b(), zero, "", -1);
		testRead(b(0), zero, "", -1);
		testRead(b(1), zero, "", -1);
		testRead(b(0,0), zero, "", 0);
		testRead(b(0,1), zero, "", 1);
		testRead(b(1,0), zero, "", 0);
		testRead(b(1,1), zero, "", 1);
		
		testRead(b(), wzro, "", -1);
		testRead(b(0), wzro, "", -1);
		testRead(b(1), wzro, "", -1);
		testRead(b(0,0), wzro, "", -1);
		testRead(b(0,1), wzro, "", -1);
		testRead(b(1,0), wzro, "", -1);
		testRead(b(1,1), wzro, "", -1);
		testRead(b(0,0,0), wzro, "", 0);
		testRead(b(0,0,1), wzro, "", 1);
		testRead(b(0,1,0), wzro, "", 0);
		testRead(b(0,1,1), wzro, "", 1);
		testRead(b(1,0,0), wzro, "", 0);
		testRead(b(1,0,1), wzro, "", 1);
		testRead(b(1,1,0), wzro, "", 0);
		testRead(b(1,1,1), wzro, "", 1);
		
		testRead(b(), orzw, "", -1);
		testRead(b(0), orzw, "", -1);
		testRead(b(1), orzw, "", -1);
		testRead(b(0,0), orzw, "", -1);
		testRead(b(0,1), orzw, "", -1);
		testRead(b(1,0), orzw, "", -1);
		testRead(b(1,1), orzw, "", -1);
		testRead(b(0,0,0), orzw, "", 0);
		testRead(b(0,0,1), orzw, "", 1);
		testRead(b(0,1,0), orzw, "", 0);
		testRead(b(0,1,1), orzw, "", 1);
		testRead(b(1,0,0), orzw, "", 0);
		testRead(b(1,0,1), orzw, "", 1);
		testRead(b(1,1,0), orzw, "", 0);
		testRead(b(1,1,1), orzw, "", 1);
		
		testRead(b(0,13,0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\n',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC), wstr, "°≤√‘Âˆ\nß∏…⁄Î¸", -1);
		testRead(b(0,13,0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\r',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC), wstr, "°≤√‘Âˆ\nß∏…⁄Î¸", -1);
		testRead(b(13,0,0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\n',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC), rtsw, "¡²ÃÔåö\n§¸ÉÚëü", -1);
		testRead(b(13,0,0xA1,0xB2,0xC3,0xD4,0xE5,0xF6,'\r',0xA7,0xB8,0xC9,0xDA,0xEB,0xFC), rtsw, "¡²ÃÔåö\r§¸ÉÚëü", -1);
		
		testWrite(
			"",
			b(0), b(0,0), b(0,0), // variable length
			b(0,0), b(0,0), b(0,0), // even length
			b(0), b(0,0,0), b(0,0,0), // odd length
			b(0,0,0,0,0), b(0,0,0,0,0,0), b(0,0,0,0,0,0) // fixed length
		);
		testWrite(
			"A",
			b(1,'A'), b(0,1,'A'), b(1,0,'A'), // variable length
			b(1,'A'), b(0,1,'A',0), b(1,0,'A',0), // even length
			b(1,'A',0), b(0,1,'A'), b(1,0,'A'), // odd length
			b(1,'A',0,0,0), b(0,1,'A',0,0,0), b(1,0,'A',0,0,0) // fixed length
		);
		testWrite(
			"AB",
			b(2,'A','B'), b(0,2,'A','B'), b(2,0,'A','B'), // variable length
			b(2,'A','B',0), b(0,2,'A','B'), b(2,0,'A','B'), // even length
			b(2,'A','B'), b(0,2,'A','B',0), b(2,0,'A','B',0), // odd length
			b(2,'A','B',0,0), b(0,2,'A','B',0,0), b(2,0,'A','B',0,0) // fixed length
		);
		testWrite(
			"ABC",
			b(3,'A','B','C'), b(0,3,'A','B','C'), b(3,0,'A','B','C'), // variable length
			b(3,'A','B','C'), b(0,3,'A','B','C',0), b(3,0,'A','B','C',0), // even length
			b(3,'A','B','C',0), b(0,3,'A','B','C'), b(3,0,'A','B','C'), // odd length
			b(3,'A','B','C',0), b(0,3,'A','B','C',0), b(3,0,'A','B','C',0) // fixed length
		);
		testWrite(
			"ABCD",
			b(4,'A','B','C','D'), b(0,4,'A','B','C','D'), b(4,0,'A','B','C','D'), // variable length
			b(4,'A','B','C','D',0), b(0,4,'A','B','C','D'), b(4,0,'A','B','C','D'), // even length
			b(4,'A','B','C','D'), b(0,4,'A','B','C','D',0), b(4,0,'A','B','C','D',0), // odd length
			b(4,'A','B','C','D'), b(0,4,'A','B','C','D'), b(4,0,'A','B','C','D') // fixed length
		);
		testWrite(
			"ABCDE",
			b(5,'A','B','C','D','E'), b(0,5,'A','B','C','D','E'), b(5,0,'A','B','C','D','E'), // variable length
			b(5,'A','B','C','D','E'), b(0,5,'A','B','C','D','E',0), b(5,0,'A','B','C','D','E',0), // even length
			b(5,'A','B','C','D','E',0), b(0,5,'A','B','C','D','E'), b(5,0,'A','B','C','D','E'), // odd length
			b(4,'A','B','C','D'), b(0,4,'A','B','C','D'), b(4,0,'A','B','C','D') // fixed length
		);
		testWrite(
			"ABCDEF",
			b(6,'A','B','C','D','E','F'), b(0,6,'A','B','C','D','E','F'), b(6,0,'A','B','C','D','E','F'), // variable length
			b(6,'A','B','C','D','E','F',0), b(0,6,'A','B','C','D','E','F'), b(6,0,'A','B','C','D','E','F'), // even length
			b(6,'A','B','C','D','E','F'), b(0,6,'A','B','C','D','E','F',0), b(6,0,'A','B','C','D','E','F',0), // odd length
			b(4,'A','B','C','D'), b(0,4,'A','B','C','D'), b(4,0,'A','B','C','D') // fixed length
		);
		testWrite(
			"¡Hêllø¸\nWörld!",
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\n','W',0xF6,'r','l','d','!'),
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\n','W',0xF6,'r','l','d','!'),
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\n','W',0xF6,'r','l','d','!',0),
			b(4,0xC1,'H',0x90,'l'), b(0,4,0xC1,'H',0x90,'l'), b(4,0,0xA1,'H',0xEA,'l')
		);
		testWrite(
			"¡Hêllø¸\rWörld!",
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\r','W',0xF6,'r','l','d','!'),
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\r','W',0xF6,'r','l','d','!'),
			b(14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!'),
			b(0,14,0xC1,'H',0x90,'l','l',0xBF,0xFC,'\r','W',0x9A,'r','l','d','!',0),
			b(14,0,0xA1,'H',0xEA,'l','l',0xF8,0xB8,'\r','W',0xF6,'r','l','d','!',0),
			b(4,0xC1,'H',0x90,'l'), b(0,4,0xC1,'H',0x90,'l'), b(4,0,0xA1,'H',0xEA,'l')
		);
		testWrite(
			"We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.",
			"ÿWe the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish ".getBytes("ISO-8859-1"),
			"\u00012We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"2\u0001We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"ÿWe the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish ".getBytes("ISO-8859-1"),
			"\u00012We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"2\u0001We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"ÿWe the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish \u0000".getBytes("ISO-8859-1"),
			"\u00012We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.\u0000".getBytes("ISO-8859-1"),
			"2\u0001We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.\u0000".getBytes("ISO-8859-1"),
			b(4,'W','e',' ','t'), b(0,4,'W','e',' ','t'), b(4,0,'W','e',' ','t')
		);
		testWrite(
			"We the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.",
			"ÿWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ".getBytes("ISO-8859-1"),
			"\u0001GWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"G\u0001We the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"ÿWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ".getBytes("ISO-8859-1"),
			"\u0001GWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.\u0000".getBytes("ISO-8859-1"),
			"G\u0001We the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.\u0000".getBytes("ISO-8859-1"),
			"ÿWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do \u0000".getBytes("ISO-8859-1"),
			"\u0001GWe the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			"G\u0001We the People of the United States, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defense, promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution for the United States of America.".getBytes("ISO-8859-1"),
			b(4,'W','e',' ','t'), b(0,4,'W','e',' ','t'), b(4,0,'W','e',' ','t')
		);
	}
	
	public static void main(String[] args) {
		new PascalStringTest().main();
	}
}
