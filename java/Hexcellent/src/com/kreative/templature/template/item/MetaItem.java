package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class MetaItem implements TemplateItem, PackedItem {
	private final int typeConstant;
	private final String typeString;
	private final String directive;
	
	public MetaItem(int typeConstant, String typeString, String directive) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.directive = directive;
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
		out.write(typeConstant, directive);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.quote(directive, '"') + ";");
	}
	
	public final class Instance implements TemplateItemInstance, PackedItemInstance {
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return null;
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			// pass
		}
		public void write(BigIntegerBitOutputStream out) {
			// pass
		}
	}
}
