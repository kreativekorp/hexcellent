package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class PaddedStringItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final int length;
	private final byte padding;
	private final String encoding;
	private final String lineEnding;
	
	public PaddedStringItem(int typeConstant, String typeString, String name, int length, byte padding, String encoding, String lineEnding) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.length = length;
		this.padding = padding;
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		byte[] data = new byte[length];
		int read = in.read(data); if (read < 0) read = 0;
		while (read > 0 && data[read - 1] == padding) read--;
		String s = new String(data, 0, read, encoding);
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
			int count = (b.length <= length) ? b.length : length;
			out.write(b, 0, count);
			b = new byte[length - count];
			for (int i = 0; i < b.length; i++) b[i] = padding;
			out.write(b, 0, b.length);
		}
	}
}
