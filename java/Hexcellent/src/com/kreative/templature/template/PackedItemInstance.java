package com.kreative.templature.template;

public interface PackedItemInstance {
	public abstract String getLabelText();
	public abstract <C> C createComponent(ComponentFactory<C> factory);
	public abstract void write(BigIntegerBitOutputStream out);
}
