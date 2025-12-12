package com.kreative.templature.main;

import java.util.Arrays;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp();
		} else {
			String arg0 = args[0].toLowerCase();
			List<String> arga = Arrays.asList(args);
			arga = arga.subList(1, arga.size());
			args = arga.toArray(new String[arga.size()]);
			if (arg0.equals("edit")) {
				// TODO
				printHelp();
			} else if (arg0.equals("compile")) {
				CompileTemplate.main(args);
			} else if (arg0.equals("decompile")) {
				DecompileTemplate.main(args);
			} else if (arg0.equals("lint")) {
				LintTemplate.main(args);
			} else {
				printHelp();
			}
		}
	}
	
	private static void printHelp() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java com.kreative.templature.main.Main compile <options> <files>");
		System.out.println("  java com.kreative.templature.main.Main decompile <options> <files>");
		System.out.println("  java com.kreative.templature.main.Main lint <options> <files>");
		System.out.println();
	}
}
