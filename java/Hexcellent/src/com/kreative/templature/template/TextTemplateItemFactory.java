package com.kreative.templature.template;

public interface TextTemplateItemFactory {
	public abstract String[] getTemplateTypeStrings();
	public abstract String[] getPackedTypeStrings();
	
	public abstract TemplateItem createTemplateItem(
		TextTemplateFactory factory,
		TextTemplateInputState state,
		TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token,
		String maskedTypeString,
		int maskedValue,
		boolean le
	);
	
	public abstract PackedItem createPackedItem(
		TextTemplateFactory factory,
		TextTemplateInputState state,
		TextTemplateTokenStream tokens,
		TextTemplateTokenStream.Token token,
		String maskedTypeString,
		int maskedValue,
		boolean le
	);
}
