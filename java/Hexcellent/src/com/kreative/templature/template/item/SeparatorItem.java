package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class SeparatorItem implements TemplateItem, PackedItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	
	public SeparatorItem(int typeConstant, String typeString, String name) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		return new Instance();
	}
	
	public PackedItemInstance read(Closure closure, BigIntegerBitInputStream in) {
		return new Instance();
	}
	
	public void measure(BigIntegerBitOutputStream out) {
		// pass
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + (
			(name == null || name.length() == 0) ? ";" :
			(" " + TemplateUtils.escapeName(name) + ";")
		));
	}
	
	public final class Instance implements TemplateItemInstance, PackedItemInstance {
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createSeparatorComponent();
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			// pass
		}
		public void write(BigIntegerBitOutputStream out) {
			// pass
		}
	}
}
