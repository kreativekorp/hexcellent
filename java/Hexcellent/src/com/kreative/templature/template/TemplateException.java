package com.kreative.templature.template;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TemplateException(String format, Object... items) {
		super(format(format, items));
	}
	
	private static final Pattern SPEC = Pattern.compile("\"\\^([0-9]+)\"|'\\^([0-9]+)'|\\^([0-9]+)");
	
	public static String format(String format, Object... items) {
		StringBuffer sb = new StringBuffer();
		Matcher m = SPEC.matcher(format);
		while (m.find()) {
			if (m.group(1) != null && m.group(1).length() > 0) {
				Object item = items[Integer.parseInt(m.group(1))];
				m.appendReplacement(sb, "“" + item + "”");
			}
			if (m.group(2) != null && m.group(2).length() > 0) {
				Object item = items[Integer.parseInt(m.group(2))];
				if (item instanceof Integer) item = toTypeConstantString((Integer)item);
				m.appendReplacement(sb, "‘" + item + "’");
			}
			if (m.group(3) != null && m.group(3).length() > 0) {
				Object item = items[Integer.parseInt(m.group(3))];
				m.appendReplacement(sb, item.toString());
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	private static String toTypeConstantString(int typeConstant) {
		try {
			return new String(new byte[] {
				(byte)(typeConstant >> 24),
				(byte)(typeConstant >> 16),
				(byte)(typeConstant >> 8),
				(byte)typeConstant
			}, "MacRoman");
		} catch (UnsupportedEncodingException e) {
			return Integer.toHexString(typeConstant).toUpperCase();
		}
	}
}
