package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class CharacterItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final String encoding;
	private final String padding;
	
	public CharacterItem(int typeConstant, String typeString, String name, boolean le, int width, String encoding, String padding) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.encoding = encoding;
		this.padding = padding;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		byte[] b = new byte[length]; int n = in.read(b); if (n < 0) n = 0;
		if (le) for (int i = 0, j = n - 1; i < j; i++, j--) { byte k = b[i]; b[i] = b[j]; b[j] = k; }
		return new Instance(closure, new String(b, 0, n, encoding));
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
			return factory.createTextFieldComponent(this, length + 1);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			byte[] da = new byte[length]; int dp = 0;
			byte[] sa = value.getBytes(encoding); int sp = 0;
			while (dp < da.length && sp < sa.length) da[dp++] = sa[sp++];
			if (padding != null && padding.length() > 0) {
				sa = padding.getBytes(encoding);
				while (dp < da.length) {
					sp = 0;
					while (dp < da.length && sp < sa.length) da[dp++] = sa[sp++];
				}
			}
			if (le) {
				for (int i = 0, j = length - 1; i < j; i++, j--) {
					byte k = da[i]; da[i] = da[j]; da[j] = k;
				}
			}
			out.write(da);
		}
	}
}
