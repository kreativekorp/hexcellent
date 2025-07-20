package com.kreative.templature.test;

import com.kreative.templature.template.TemplateConstants;
import com.kreative.templature.template.TemplateException;

public class TemplateExceptionTest {
	public static void main(String[] args) {
		System.out.println(new TemplateException(
			"The template used for displaying resources of type '^0' is invalid. " +
			"It contains a hex dump ('^1') field that is not the last field in the template.\n" +
			"A Møøse once bit my sister.\n" +
			"Nø realli! She was Karving her initials øn the møøse with the sharpened end " +
			"of an interspace tøøthbrush given her by ^2 — her brother-in-law — an Oslo " +
			"dentist and star of many Norwegian møvies: \"^3\", \"^4\", \"^5\".",
			"WTF?", TemplateConstants.$HEXD,
			"Svenge", "The Høt Hands of an Oslo Dentist",
			"Fillings of Passion", "The Huge Mølars of Horst Nordfink"
		).getMessage());
	}
}
