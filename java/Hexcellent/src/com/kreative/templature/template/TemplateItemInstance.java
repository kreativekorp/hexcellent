package com.kreative.templature.template;

import java.io.IOException;

public interface TemplateItemInstance {
	public abstract String getLabelText();
	public abstract <C> C createComponent(ComponentFactory<C> factory);
	public abstract void write(BufferedCountedOutputStream out) throws IOException;
}
