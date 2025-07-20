package com.kreative.templature.test;

import com.kreative.templature.template.TemplateUtils;

public class EscapeNameTest {
	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println(TemplateUtils.escapeName(arg));
		}
	}
}
