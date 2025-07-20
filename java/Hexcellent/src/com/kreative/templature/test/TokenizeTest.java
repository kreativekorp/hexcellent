package com.kreative.templature.test;

import java.util.ArrayList;
import com.kreative.templature.template.TextTemplateTokenStream;

public class TokenizeTest {
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) sb.append(" ");
			sb.append(args[i]);
		}
		System.out.println(sb.toString());
		TextTemplateTokenStream ts = new TextTemplateTokenStream(sb.toString());
		ArrayList<TextTemplateTokenStream.Token> tokens = new ArrayList<TextTemplateTokenStream.Token>();
		while (ts.hasNext()) {
			TextTemplateTokenStream.Token token = ts.next();
			System.out.println("\t" + token.image);
			tokens.add(token);
		}
		ts.close();
		System.out.println(TextTemplateTokenStream.join(tokens, false));
		System.out.println(TextTemplateTokenStream.join(tokens, true));
	}
}
