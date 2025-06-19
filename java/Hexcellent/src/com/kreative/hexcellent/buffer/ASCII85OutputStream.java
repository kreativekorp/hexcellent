package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.OutputStream;

public class ASCII85OutputStream extends OutputStream {
	public static final String ASCII85 = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu";
	public static final String ASCII85XML = "!w#$%|v()*+,-./0123456789:;{=}?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu";
	public static final String RFC1924 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&()*+-;<=>?@^_`{|}~";
	public static final String Z85 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-:+=^!/*?&<>()[]{}@%$#";
	
	private final StringBuffer sb;
	private final OutputStream out;
	private final char[] alphabet;
	private final boolean xb, yb, zb, tb;
	private final char xc, yc, zc, tc;
	
	public ASCII85OutputStream(StringBuffer sb) {
		this(sb, ASCII85, '\u0000', '\u0000', 'z', '~');
	}
	
	public ASCII85OutputStream(OutputStream out) {
		this(out, ASCII85, '\u0000', '\u0000', 'z', '~');
	}
	
	public ASCII85OutputStream(StringBuffer sb, boolean xml, boolean x, boolean y, boolean z) {
		this(sb, (xml ? ASCII85XML : ASCII85), (x ? 'x' : '\u0000'), (y ? 'y' : '\u0000'), (z ? 'z' : '\u0000'), '~');
	}
	
	public ASCII85OutputStream(OutputStream out, boolean xml, boolean x, boolean y, boolean z) {
		this(out, (xml ? ASCII85XML : ASCII85), (x ? 'x' : '\u0000'), (y ? 'y' : '\u0000'), (z ? 'z' : '\u0000'), '~');
	}
	
	public ASCII85OutputStream(StringBuffer sb, String alphabet) {
		this(sb, alphabet, '\u0000', '\u0000', '\u0000', '\u0000');
	}
	
	public ASCII85OutputStream(OutputStream out, String alphabet) {
		this(out, alphabet, '\u0000', '\u0000', '\u0000', '\u0000');
	}
	
	public ASCII85OutputStream(StringBuffer sb, String alphabet, char x, char y, char z, char t) {
		this.sb = sb; this.out = null; this.alphabet = alphabet.toCharArray();
		if (this.alphabet.length != 85) throw new IllegalArgumentException(alphabet);
		this.xb = (x > '\u0000' && x < '\uFFFD'); this.xc = x;
		this.yb = (y > '\u0000' && y < '\uFFFD'); this.yc = y;
		this.zb = (z > '\u0000' && z < '\uFFFD'); this.zc = z;
		this.tb = (t > '\u0000' && t < '\uFFFD'); this.tc = t;
	}
	
	public ASCII85OutputStream(OutputStream out, String alphabet, char x, char y, char z, char t) {
		this.sb = null; this.out = out; this.alphabet = alphabet.toCharArray();
		if (this.alphabet.length != 85) throw new IllegalArgumentException(alphabet);
		this.xb = (x > '\u0000' && x < '\uFFFD'); this.xc = x;
		this.yb = (y > '\u0000' && y < '\uFFFD'); this.yc = y;
		this.zb = (z > '\u0000' && z < '\uFFFD'); this.zc = z;
		this.tb = (t > '\u0000' && t < '\uFFFD'); this.tc = t;
	}
	
	private long word = 0;
	private int count = 0;
	
	@Override
	public void write(int b) throws IOException {
		if (count < 0) {
			if (tb) {
				if (sb != null) sb.append(tc);
				if (out != null) out.write(tc);
			}
			word = 0;
			count = 0;
		}
		word <<= 8;
		word |= (b & 0xFF);
		count++;
		if (count >= 4) {
			if (word == 0x00000000L && zb) {
				if (sb != null) sb.append(zc);
				if (out != null) out.write(zc);
			} else if (word == 0x20202020L && yb) {
				if (sb != null) sb.append(yc);
				if (out != null) out.write(yc);
			} else if (word == 0xFFFFFFFFL && xb) {
				if (sb != null) sb.append(xc);
				if (out != null) out.write(xc);
			} else {
				writeWord();
			}
			word = 0;
			count = 0;
		}
	}
	
	@Override
	public void flush() throws IOException {
		if (out != null) out.flush();
	}
	
	@Override
	public void close() throws IOException {
		if (count > 0) {
			for (int i = count; i < 4; i++) word <<= 8;
			writeWord();
		}
		word = -1;
		count = -1;
		if (out != null) out.close();
	}
	
	private void writeWord() throws IOException {
		for (int d = 52200625, i = 0; i <= count; d /= 85, i++) {
			char c = alphabet[(int)((word / d) % 85)];
			if (sb != null) sb.append(c);
			if (out != null) out.write(c);
		}
	}
}
