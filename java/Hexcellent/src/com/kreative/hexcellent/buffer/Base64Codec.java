package com.kreative.hexcellent.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Base64Codec implements ByteDecoder, ByteEncoder {
	public static final Base64Codec BASE64 = new Base64Codec() {
		protected Base64InputStream createInputStream(String data) {
			return new Base64InputStream(data, Base64InputStream.BASE64, '=');
		}
		protected Base64OutputStream createOutputStream(StringBuffer sb) {
			return new Base64OutputStream(sb, Base64OutputStream.BASE64, '=');
		}
	};
	
	public static final Base64Codec BASE64_UNPADDED = new Base64Codec() {
		protected Base64InputStream createInputStream(String data) {
			return new Base64InputStream(data, Base64InputStream.BASE64, '=');
		}
		protected Base64OutputStream createOutputStream(StringBuffer sb) {
			return new Base64OutputStream(sb, Base64OutputStream.BASE64, '\u0000');
		}
	};
	
	public static final Base64Codec BASE64URL = new Base64Codec() {
		protected Base64InputStream createInputStream(String data) {
			return new Base64InputStream(data, Base64InputStream.BASE64URL, '\u0000');
		}
		protected Base64OutputStream createOutputStream(StringBuffer sb) {
			return new Base64OutputStream(sb, Base64OutputStream.BASE64URL, '\u0000');
		}
	};
	
	public static final Base64Codec RFC3501 = new Base64Codec() {
		protected Base64InputStream createInputStream(String data) {
			return new Base64InputStream(data, Base64InputStream.RFC3501, '\u0000');
		}
		protected Base64OutputStream createOutputStream(StringBuffer sb) {
			return new Base64OutputStream(sb, Base64OutputStream.RFC3501, '\u0000');
		}
	};
	
	protected abstract Base64InputStream createInputStream(String data);
	protected abstract Base64OutputStream createOutputStream(StringBuffer sb);
	
	@Override
	public byte[] decode(String data) throws IOException {
		Base64InputStream in = createInputStream(data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int read;
		while ((read = in.read(buf)) > 0) out.write(buf, 0, read);
		in.close(); out.close(); return out.toByteArray();
	}
	
	@Override
	public String encode(byte[] data, int offset, int length) throws IOException {
		StringBuffer sb = new StringBuffer();
		Base64OutputStream out = createOutputStream(sb);
		out.write(data, offset, length);
		out.close();
		return sb.toString();
	}
}
