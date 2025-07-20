package com.kreative.templature.template;

import java.io.IOException;

public interface ListDataModel {
	public abstract String getCountLabelText();
	public abstract String getCountValueText();
	public abstract int getEntryCount();
	public abstract String getEntryLabelText(int index);
	public abstract Template.Instance getEntry(int index);
	public abstract Template.Instance createNewEntry() throws IOException;
	public abstract void addEntry(int index, Template.Instance instance);
	public abstract Template.Instance removeEntry(int index);
}
