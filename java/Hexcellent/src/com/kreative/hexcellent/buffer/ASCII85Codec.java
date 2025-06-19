package com.kreative.hexcellent.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ASCII85Codec implements ByteDecoder, ByteEncoder {
	public static final ASCII85Codec ASCII85 = new ASCII85Codec() {
		protected ASCII85InputStream createInputStream(String data) {
			return new ASCII85InputStream(data, ASCII85InputStream.ASCII85, 'x', 'y', 'z', '~');
		}
		protected ASCII85OutputStream createOutputStream(StringBuffer sb) {
			return new ASCII85OutputStream(sb, ASCII85OutputStream.ASCII85, '\u0000', '\u0000', 'z', '~');
		}
	};
	
	public static final ASCII85Codec ASCII85XML = new ASCII85Codec() {
		protected ASCII85InputStream createInputStream(String data) {
			return new ASCII85InputStream(data, ASCII85InputStream.ASCII85XML, 'x', 'y', 'z', '~');
		}
		protected ASCII85OutputStream createOutputStream(StringBuffer sb) {
			return new ASCII85OutputStream(sb, ASCII85OutputStream.ASCII85XML, '\u0000', '\u0000', 'z', '~');
		}
	};
	
	public static final ASCII85Codec RFC1924 = new ASCII85Codec() {
		protected ASCII85InputStream createInputStream(String data) {
			return new ASCII85InputStream(data, ASCII85InputStream.RFC1924, '\u0000', '\u0000', '\u0000', '\u0000');
		}
		protected ASCII85OutputStream createOutputStream(StringBuffer sb) {
			return new ASCII85OutputStream(sb, ASCII85OutputStream.RFC1924, '\u0000', '\u0000', '\u0000', '\u0000');
		}
	};
	
	public static final ASCII85Codec Z85 = new ASCII85Codec() {
		protected ASCII85InputStream createInputStream(String data) {
			return new ASCII85InputStream(data, ASCII85InputStream.Z85, '\u0000', '\u0000', '\u0000', '\u0000');
		}
		protected ASCII85OutputStream createOutputStream(StringBuffer sb) {
			return new ASCII85OutputStream(sb, ASCII85OutputStream.Z85, '\u0000', '\u0000', '\u0000', '\u0000');
		}
	};
	
	protected abstract ASCII85InputStream createInputStream(String data);
	protected abstract ASCII85OutputStream createOutputStream(StringBuffer sb);
	
	@Override
	public byte[] decode(String data) throws IOException {
		ASCII85InputStream in = createInputStream(data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int read;
		while ((read = in.read(buf)) > 0) out.write(buf, 0, read);
		in.close(); out.close(); return out.toByteArray();
	}
	
	@Override
	public String encode(byte[] data, int offset, int length) throws IOException {
		StringBuffer sb = new StringBuffer();
		ASCII85OutputStream out = createOutputStream(sb);
		out.write(data, offset, length);
		out.close();
		return sb.toString();
	}
}
