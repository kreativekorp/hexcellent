package com.kreative.templature.template.item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class TheRestIsTextItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final String encoding;
	private final String lineEnding;
	
	public TheRestIsTextItem(int typeConstant, String typeString, String name, String encoding, String lineEnding) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int n;
		while ((n = in.read(buf)) >= 0) bs.write(buf, 0, n);
		String s = new String(bs.toByteArray(), encoding);
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
			out.write(s.getBytes(encoding));
		}
	}
}
