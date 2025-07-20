package com.kreative.templature.template;

public class TextTemplateInputState {
	private String encoding = "MacRoman";
	private String lineEnding = "\r";
	private boolean le = false;
	
	public String getEncoding() { return encoding; }
	public String getLineEnding() { return lineEnding; }
	public boolean getLittleEndian() { return le; }
	
	public void setEncoding(String encoding) { this.encoding = encoding; }
	public void setLineEnding(String lineEnding) { this.lineEnding = lineEnding; }
	public void setLittleEndian(boolean le) { this.le = le; }
}
