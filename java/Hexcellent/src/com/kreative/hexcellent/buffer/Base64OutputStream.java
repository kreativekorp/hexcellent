package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream extends OutputStream {
	public static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	public static final String BASE64URL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
	public static final String RFC3501 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+,";
	
	private final StringBuffer sb;
	private final OutputStream out;
	private final char[] alphabet;
	private final boolean padb;
	private final char padc;
	
	public Base64OutputStream(StringBuffer sb) {
		this(sb, BASE64, '=');
	}
	
	public Base64OutputStream(OutputStream out) {
		this(out, BASE64, '=');
	}
	
	public Base64OutputStream(StringBuffer sb, boolean pad) {
		this(sb, BASE64, (pad ? '=' : '\u0000'));
	}
	
	public Base64OutputStream(OutputStream out, boolean pad) {
		this(out, BASE64, (pad ? '=' : '\u0000'));
	}
	
	public Base64OutputStream(StringBuffer sb, String alphabet, char pad) {
		this.sb = sb; this.out = null; this.alphabet = alphabet.toCharArray();
		if (this.alphabet.length != 64) throw new IllegalArgumentException(alphabet);
		this.padb = (pad > '\u0000' && pad < '\uFFFD'); this.padc = pad;
	}
	
	public Base64OutputStream(OutputStream out, String alphabet, char pad) {
		this.sb = null; this.out = out; this.alphabet = alphabet.toCharArray();
		if (this.alphabet.length != 64) throw new IllegalArgumentException(alphabet);
		this.padb = (pad > '\u0000' && pad < '\uFFFD'); this.padc = pad;
	}
	
	private int word = 0;
	private int count = 0;
	
	@Override
	public void write(int b) throws IOException {
		word <<= 8;
		word |= (b & 0xFF);
		count++;
		if (count >= 3) {
			writeWord();
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
			for (int i = count; i < 3; i++) word <<= 8;
			writeWord();
		}
		word = 0;
		count = 0;
		if (out != null) out.close();
	}
	
	private void writeWord() throws IOException {
		for (int m = 18, i = 0; i <= count; m -= 6, i++) {
			char c = alphabet[(word >> m) & 0x3F];
			if (sb != null) sb.append(c);
			if (out != null) out.write(c);
		}
		if (padb) {
			for (int i = count; i < 3; i++) {
				if (sb != null) sb.append(padc);
				if (out != null) out.write(padc);
			}
		}
	}
}
