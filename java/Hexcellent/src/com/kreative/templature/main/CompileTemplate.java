package com.kreative.templature.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.kreative.templature.template.BinaryTemplateOutputStream;
import com.kreative.templature.template.TextTemplateFactory;
import com.kreative.templature.template.TextTemplateTokenStream;

public class CompileTemplate {
	private final TextTemplateFactory ttf;
	
	public CompileTemplate() {
		ttf = new TextTemplateFactory();
		ttf.registerDefaults();
		ttf.registerExtensions();
	}
	
	public void compileTemplate(InputStream in, OutputStream out) throws IOException {
		TextTemplateTokenStream in2 = new TextTemplateTokenStream(in);
		BinaryTemplateOutputStream out2 = new BinaryTemplateOutputStream(out);
		ttf.createTemplate(in2).write(out2);
		out2.flush();
	}
	
	private void compileTemplate(File inputFile, boolean stdin, File outputFile, boolean stdout) throws IOException {
		if (stdin) {
			if (stdout) {
				compileTemplate(System.in, System.out);
			} else if (outputFile == null) {
				OutputStream out = new FileOutputStream(new File("out.tmpl"));
				compileTemplate(System.in, out);
				out.close();
			} else if (outputFile.isDirectory()) {
				OutputStream out = new FileOutputStream(new File(outputFile, "out.tmpl"));
				compileTemplate(System.in, out);
				out.close();
			} else {
				OutputStream out = new FileOutputStream(outputFile);
				compileTemplate(System.in, out);
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
				try { compileTemplate(childFile, false, outputChild, stdout); }
				catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
			}
		} else {
			InputStream in = new FileInputStream(inputFile);
			if (stdout) {
				compileTemplate(in, System.out);
			} else if (outputFile == null) {
				OutputStream out = new FileOutputStream(deriveFile(inputFile));
				compileTemplate(in, out);
				out.close();
			} else if (outputFile.isDirectory()) {
				OutputStream out = new FileOutputStream(deriveFile(outputFile, inputFile));
				compileTemplate(in, out);
				out.close();
			} else {
				OutputStream out = new FileOutputStream(outputFile);
				compileTemplate(in, out);
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
			// Just strip off the final letter t.
			return name.substring(0, name.length() - 1);
		} else {
			return name + ".tmpl";
		}
	}
	
	public static void printHelp() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java com.kreative.templature.main.CompileTemplate <options> <files>");
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
		CompileTemplate compiler = new CompileTemplate();
		boolean processOptions = true;
		File outputFile = null;
		boolean stdout = false;
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
					try { compiler.compileTemplate(inputFile, false, outputFile, stdout); }
					catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
					if (!keep) { outputFile = null; stdout = false; }
				} else if (arg.equals("-I")) {
					boolean keep = (stdout || outputFile == null || outputFile.isDirectory());
					try { compiler.compileTemplate(null, true, outputFile, stdout); }
					catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
					if (!keep) { outputFile = null; stdout = false; }
				} else if (arg.equals("--help")) {
					printHelp();
				} else {
					System.err.println("Unknown option: " + arg);
				}
			} else {
				File inputFile = new File(arg);
				boolean keep = (stdout || outputFile == null || outputFile.isDirectory());
				try { compiler.compileTemplate(inputFile, false, outputFile, stdout); }
				catch (IOException ioe) { System.err.println("I/O Error: " + ioe); }
				if (!keep) { outputFile = null; stdout = false; }
			}
		}
	}
}
