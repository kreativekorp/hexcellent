package com.kreative.templature.template;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryTemplateOutputStream {
	private final DataOutputStream out;
	private String encoding;
	private String lineEnding;
	
	public BinaryTemplateOutputStream(OutputStream out) {
		this.out = new DataOutputStream(out);
		this.encoding = "MacRoman";
		this.lineEnding = "\r";
	}
	
	public BinaryTemplateOutputStream(OutputStream out, String encoding, String lineEnding) {
		this.out = new DataOutputStream(out);
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public void close() throws IOException {
		out.close();
	}
	
	public void flush() throws IOException {
		out.flush();
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getLineEnding() {
		return lineEnding;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setLineEnding(String lineEnding) {
		this.lineEnding = lineEnding;
	}
	
	public void write(int type, String label) throws IOException {
		if (label == null) {
			out.writeByte(0);
		} else {
			if (lineEnding != null && lineEnding.length() > 0) {
				label = label.replace("\n", lineEnding);
			}
			byte[] labelData = label.getBytes(encoding);
			int length = labelData.length;
			if (length > 255) length = 255;
			out.writeByte(length);
			out.write(labelData, 0, length);
		}
		out.writeInt(type);
	}
}
