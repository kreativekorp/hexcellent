package com.kreative.templature.template;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryTemplateInputStream {
	private final DataInputStream in;
	private String encoding;
	private String lineEnding;
	
	public BinaryTemplateInputStream(InputStream in) {
		this.in = new DataInputStream(new BufferedInputStream(in));
		this.encoding = "MacRoman";
		this.lineEnding = "\r";
	}
	
	public BinaryTemplateInputStream(InputStream in, String encoding, String lineEnding) {
		this.in = new DataInputStream(new BufferedInputStream(in));
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public static final class Item {
		public final int type;
		public final String label;
		private Item(int type, String label) {
			this.type = type;
			this.label = label;
		}
	}
	
	public void close() throws IOException {
		in.close();
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getLineEnding() {
		return lineEnding;
	}
	
	public void mark(int readlimit) {
		in.mark(readlimit);
	}
	
	public Item read() throws IOException {
		int length = in.read();
		if (length < 0) return null;
		byte[] labelData = new byte[length];
		in.readFully(labelData, 0, length);
		String label = new String(labelData, encoding);
		if (lineEnding != null && lineEnding.length() > 0) {
			label = label.replace(lineEnding, "\n");
		}
		int type = in.readInt();
		return new Item(type, label);
	}
	
	public void reset() throws IOException {
		in.reset();
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setLineEnding(String lineEnding) {
		this.lineEnding = lineEnding;
	}
}
