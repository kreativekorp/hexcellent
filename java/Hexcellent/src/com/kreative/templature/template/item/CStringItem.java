package com.kreative.templature.template.item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class CStringItem implements TemplateItem {
	public static final int LENGTH_MAX = Integer.MAX_VALUE;
	public static final int LENGTH_VARIABLE = -1;
	public static final int LENGTH_EVEN = -2;
	public static final int LENGTH_ODD = -3;
	
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final int length;
	private final String encoding;
	private final String lineEnding;
	
	public CStringItem(int typeConstant, String typeString, String name, int length, String encoding, String lineEnding) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.length = length;
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		String s;
		if (length >= 0) {
			// Fixed size string
			byte[] data = new byte[length];
			int read = in.read(data); int count = 0;
			while (count < read && data[count] != 0) count++;
			s = new String(data, 0, count, encoding);
		} else {
			// Variable size string
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			for (;;) { int b = in.read(); if (b <= 0) break; bs.write(b); }
			byte[] b = bs.toByteArray(); s = new String(b, encoding);
			// Logic inverted because b.length does not include terminator
			if (length == LENGTH_ODD && (b.length & 1) != 0) in.read();
			if (length == LENGTH_EVEN && (b.length & 1) == 0) in.read();
		}
		if (lineEnding != null && lineEnding.length() > 0) s = s.replace(lineEnding, "\n");
		return new Instance(closure, s);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, StringDataModel {
		private final Closure closure;
		private String value;
		private Instance(Closure closure, String value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public String getValue() {
			return value;
		}
		public String getStringValue() {
			return value;
		}
		public void setStringValue(String value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextAreaComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			String s = value;
			if (lineEnding != null && lineEnding.length() > 0) s = s.replace("\n", lineEnding);
			byte[] b = s.getBytes(encoding);
			if (length >= 0) {
				// Fixed size string
				int countMax = (length > 0) ? (length - 1) : length;
				int count = (b.length <= countMax) ? b.length : countMax;
				out.write(b, 0, count);
				out.write(new byte[length - count]);
			} else {
				// Variable size string
				out.write(b, 0, b.length); out.write(0);
				// Logic inverted because b.length does not include terminator
				if (length == LENGTH_ODD && (b.length & 1) != 0) out.write(0);
				if (length == LENGTH_EVEN && (b.length & 1) == 0) out.write(0);
			}
		}
	}
}
