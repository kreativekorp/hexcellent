package com.kreative.templature.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import com.kreative.templature.template.TextTemplateFactory;
import com.kreative.templature.template.TextTemplateTokenStream;

public class LintTemplate {
	private final TextTemplateFactory ttf;
	
	public LintTemplate() {
		ttf = new TextTemplateFactory();
		ttf.registerDefaults();
		ttf.registerExtensions();
	}
	
	public void lintTemplate(InputStream in, OutputStream out) throws IOException {
		TextTemplateTokenStream in2 = new TextTemplateTokenStream(in);
		PrintWriter out2 = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
		ttf.createTemplate(in2).print(out2, "");
		out2.flush();
	}
	
	private void lintTemplate(File inputFile, boolean stdin, File outputFile, boolean stdout) throws IOException {
		if (stdin) {
			if (stdout) {
				lintTemplate(System.in, System.out);
			} else if (outputFile == null) {
				OutputStream out = new FileOutputStream(new File("out.tmplt"));
				lintTemplate(System.in, out);
				out.close();
			} else if (outputFile.isDirectory()) {
				OutputStream out = new FileOutputStream(new File(outputFile, "out.tmplt"));
				lintTemplate(System.in, out);
				out.close();
			} else {
				OutputStream out = new FileOutputStream(outputFile);
				lintTemplate(System.in, out);
				out.close();
			}
		} else if (inputFile.isDirectory()) {
			File outputParent = stdout ? null : createOutputParent(inputFile, outputFile);
			for (File childFile : inputFile.listFiles()) {
				String childName = childFile.getName();
				if (childName.startsWith(".")) continue;
				if (childName.endsWith("\r")) continue;
				if (childName.endsWith("\uF00D")) continue;
				File outputChild = stdout ? null : new File(outputParent,
					(childFile.isDirectory() ? childName : deriveName(childName)));
				try { lintTemplate(childFile, false, outputChild, stdout); }
				catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
			}
		} else {
			InputStream in = new FileInputStream(inputFile);
			if (stdout) {
				lintTemplate(in, System.out);
			} else if (outputFile == null) {
				OutputStream out = new FileOutputStream(deriveFile(inputFile));
				lintTemplate(in, out);
				out.close();
			} else if (outputFile.isDirectory()) {
				OutputStream out = new FileOutputStream(deriveFile(outputFile, inputFile));
				lintTemplate(in, out);
				out.close();
			} else {
				OutputStream out = new FileOutputStream(outputFile);
				lintTemplate(in, out);
				out.close();
			}
			in.close();
		}
	}
	
	private static File createOutputParent(File indir, File outdir) throws IOException {
		if (outdir == null) outdir = new File(indir.getParent(), indir.getName() + ".out");
		if (outdir.isDirectory() || outdir.mkdir()) return outdir;
		throw new IOException("Cannot create directory " + outdir);
	}
	
	private static File deriveFile(File file) {
		return deriveFile(file.getParentFile(), file);
	}
	
	private static File deriveFile(File parent, File child) {
		return new File(parent, deriveName(child.getName()));
	}
	
	private static String deriveName(String name) {
		if (name.toLowerCase().endsWith(".tmplt")) {
			// Prepend .out to the extension.
			int i = name.length() - 6;
			String root = name.substring(0, i);
			String ext = name.substring(i);
			return root + ".out" + ext;
		} else {
			return name + ".tmplt";
		}
	}
	
	public static void printHelp() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java com.kreative.templature.main.LintTemplate <options> <files>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("  -o <path>     Write to specified file or directory.");
		System.out.println("  -O            Write to standard output.");
		System.out.println("  -i <path>     Read from specified file or directory.");
		System.out.println("  -I            Read from standard input.");
		System.out.println("  --            Process remaining arguments as file names.");
		System.out.println();
	}
	
	public static void main(String[] args) {
		if (args.length == 0) { printHelp(); return; }
		LintTemplate linter = new LintTemplate();
		boolean processOptions = true;
		File outputFile = null;
		boolean stdout = true;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (processOptions && arg.startsWith("-")) {
				if (arg.equals("--")) {
					processOptions = false;
				} else if (arg.equals("-o") && argi < args.length) {
					outputFile = new File(args[argi++]);
					stdout = false;
				} else if (arg.equals("-O")) {
					outputFile = null;
					stdout = true;
				} else if (arg.equals("-i") && argi < args.length) {
					File inputFile = new File(args[argi++]);
					boolean keep = (stdout || outputFile == null || outputFile.isDirectory());
					try { linter.lintTemplate(inputFile, false, outputFile, stdout); }
					catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
					if (!keep) { outputFile = null; stdout = true; }
				} else if (arg.equals("-I")) {
					boolean keep = (stdout || outputFile == null || outputFile.isDirectory());
					try { linter.lintTemplate(null, true, outputFile, stdout); }
					catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
					if (!keep) { outputFile = null; stdout = true; }
				} else if (arg.equals("--help")) {
					printHelp();
				} else {
					System.err.println("Unknown option: " + arg);
				}
			} else {
				File inputFile = new File(arg);
				boolean keep = (stdout || outputFile == null || outputFile.isDirectory());
				try { linter.lintTemplate(inputFile, false, outputFile, stdout); }
				catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
				if (!keep) { outputFile = null; stdout = true; }
			}
		}
	}
}
