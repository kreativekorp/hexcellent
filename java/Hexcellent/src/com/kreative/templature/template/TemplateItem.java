package com.kreative.templature.template;

import java.io.IOException;
import java.io.PrintWriter;

public interface TemplateItem {
	public abstract TemplateItemInstance read(Closure closure, BufferedCountedInputStream in) throws IOException;
	public abstract void write(BinaryTemplateOutputStream out) throws IOException;
	public abstract void print(PrintWriter out, String prefix);
}
