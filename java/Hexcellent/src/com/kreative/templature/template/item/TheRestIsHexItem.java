package com.kreative.templature.template.item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class TheRestIsHexItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final String encoding;
	private final boolean le;
	
	public TheRestIsHexItem(int typeConstant, String typeString, String name, String encoding, boolean le) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.encoding = encoding;
		this.le = le;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int n;
		while ((n = in.read(buf)) >= 0) bs.write(buf, 0, n);
		return new Instance(closure, bs.toByteArray());
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, HexDataModel {
		private final Closure closure;
		private byte[] value;
		private Instance(Closure closure, byte[] value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public byte[] getValue() {
			return value;
		}
		public byte[] getHexValue() {
			return value;
		}
		public String getHexEncoding() {
			return encoding;
		}
		public boolean getHexLittleEndian() {
			return le;
		}
		public void setHexValue(byte[] value) {
			if (value == null) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createHexAreaComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.write(value);
		}
	}
}
