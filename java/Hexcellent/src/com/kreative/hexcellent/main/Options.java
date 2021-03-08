package com.kreative.hexcellent.main;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorColors;

public class Options {
	private Font font = new Font("Monospaced", Font.PLAIN, 12);
	private JHexEditorColors colors = JHexEditorColors.AQUA;
	private String charset = "ISO-8859-1";
	private boolean decimalAddresses = false;
	private boolean overtype = false;
	private boolean enableTransformKeys = true;
	private boolean littleEndian = false;
	
	public void push(EditorFrame f) {
		JHexEditor editor = f.getEditor();
		editor.setFont(font);
		editor.setColors(colors);
		editor.setCharset(charset);
		editor.setDecimalAddresses(decimalAddresses);
		editor.setOvertype(overtype);
		editor.setEnableTransformKeys(enableTransformKeys);
		editor.setLittleEndian(littleEndian);
		f.pack();
	}
	
	public void pull(EditorFrame f) {
		JHexEditor editor = f.getEditor();
		font = editor.getFont();
		colors = editor.getColors();
		charset = editor.getCharset();
		decimalAddresses = editor.getDecimalAddresses();
		overtype = editor.getOvertype();
		enableTransformKeys = editor.getEnableTransformKeys();
		littleEndian = editor.isLittleEndian();
	}
	
	public void read() throws IOException {
		read(getPreferencesFile());
	}
	
	public void read(File file) throws IOException {
		Scanner in = new Scanner(file, "UTF-8");
		read(in);
		in.close();
	}
	
	public void read(Scanner in) {
		while (in.hasNextLine()) {
			String line = in.nextLine().trim();
			if (line.startsWith(";")) continue;
			int o = line.indexOf("=");
			if (o < 0) continue;
			String key = line.substring(0, o).trim();
			String value = line.substring(o + 1).trim();
			
			if (key.equalsIgnoreCase("FontName")) {
				font = new Font(value, font.getStyle(), font.getSize());
			}
			if (key.equalsIgnoreCase("FontStyle")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				font = new Font(font.getName(), i, font.getSize());
			}
			if (key.equalsIgnoreCase("FontSize")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				font = new Font(font.getName(), font.getStyle(), i);
			}
			if (key.equalsIgnoreCase("ColorScheme")) {
				for (JHexEditorColors preset : JHexEditorColors.BUILTINS) {
					if (preset.name.equalsIgnoreCase(value)) {
						colors = preset;
					}
				}
			}
			if (key.equalsIgnoreCase("Charset")) {
				charset = value;
			}
			if (key.equalsIgnoreCase("DecimalAddresses")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				decimalAddresses = (i != 0);
			}
			if (key.equalsIgnoreCase("OvertypeMode")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				overtype = (i != 0);
			}
			if (key.equalsIgnoreCase("EnableTransformKeys")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				enableTransformKeys = (i != 0);
			}
			if (key.equalsIgnoreCase("LittleEndian")) {
				int i; try { i = Integer.parseInt(value); }
				catch (NumberFormatException e) { continue; }
				littleEndian = (i != 0);
			}
		}
	}
	
	public void write() throws IOException {
		write(getPreferencesFile());
	}
	
	public void write(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		PrintWriter pw = new PrintWriter(osw, true);
		write(pw);
		pw.flush();
		pw.close();
	}
	
	public void write(PrintWriter out) {
		out.println("FontName=" + font.getName());
		out.println("FontStyle=" + font.getStyle());
		out.println("FontSize=" + font.getSize());
		out.println("ColorScheme=" + colors.name);
		out.println("Charset=" + charset);
		out.println("DecimalAddresses=" + (decimalAddresses ? 1 : 0));
		out.println("OvertypeMode=" + (overtype ? 1 : 0));
		out.println("EnableTransformKeys=" + (enableTransformKeys ? 1 : 0));
		out.println("LittleEndian=" + (littleEndian ? 1 : 0));
	}
	
	private static File getPreferencesFile() {
		if (System.getProperty("os.name").toUpperCase().contains("MAC OS")) {
			File u = new File(System.getProperty("user.home"));
			File l = new File(u, "Library"); if (!l.exists()) l.mkdir();
			File p = new File(l, "Preferences"); if (!p.exists()) p.mkdir();
			return new File(p, "com.kreative.hexcellent.ini");
		} else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			File u = new File(System.getProperty("user.home"));
			File a = new File(u, "Application Data"); if (!a.exists()) a.mkdir();
			File k = new File(a, "Kreative"); if (!k.exists()) k.mkdir();
			return new File(k, "Hexcellent.ini");
		} else {
			File u = new File(System.getProperty("user.home"));
			File c = new File(u, ".config"); if (!c.exists()) c.mkdir();
			return new File(c, "hexcellent.ini");
		}
	}
}
