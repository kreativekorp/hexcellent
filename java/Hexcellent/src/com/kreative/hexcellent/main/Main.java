package com.kreative.hexcellent.main;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {
	private static final Options options = new Options();
	
	public static void main(String[] args) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hexcellent"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		try {
			Method getModule = Class.class.getMethod("getModule");
			Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
			Object allUnnamed = getModule.invoke(Main.class);
			Class<?> module = Class.forName("java.lang.Module");
			Method addOpens = module.getMethod("addOpens", String.class, module);
			addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
		} catch (Exception e) {}
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
			aacn.setAccessible(true);
			aacn.set(tk, "Hexcellent");
		} catch (Exception e) {}
		
		try { options.read(); }
		catch (Exception e) {}
		
		if (SwingUtils.IS_MAC_OS) {
			try { Class.forName("com.kreative.hexcellent.main.mac.MacDummyWindow").newInstance(); }
			catch (Throwable t) { t.printStackTrace(); }
		}
		
		if (args.length == 0) {
			newEditor();
		} else {
			for (String arg : args) {
				openEditor(new File(arg));
			}
		}
		
		if (SwingUtils.IS_MAC_OS) {
			try { Class.forName("com.kreative.hexcellent.main.mac.MyApplicationListener").newInstance(); }
			catch (Throwable t) { t.printStackTrace(); }
		}
	}
	
	public static EditorFrame newEditor() {
		EditorFrame f = new EditorFrame();
		options.push(f.getEditorSuite());
		f.pack();
		f.setVisible(true);
		return f;
	}
	
	private static String lastOpenDirectory = null;
	public static EditorFrame openEditor() {
		Frame frame = new Frame();
		FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
		if (lastOpenDirectory != null) fd.setDirectory(lastOpenDirectory);
		fd.setVisible(true);
		String parent = fd.getDirectory();
		String name = fd.getFile();
		fd.dispose();
		frame.dispose();
		if (parent == null || name == null) return null;
		File file = new File((lastOpenDirectory = parent), name);
		return openEditor(file);
	}
	
	public static EditorFrame openEditor(File file) {
		if (file == null) {
			return openEditor();
		} else try {
			EditorFrame f = new EditorFrame(file);
			options.push(f.getEditorSuite());
			f.pack();
			f.setVisible(true);
			return f;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
				null, "An error occurred while opening this file.",
				"Open", JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
	}
	
	public static void revertOptions(EditorFrame f) {
		options.push(f.getEditorSuite());
		f.pack();
	}
	
	public static void saveOptions(EditorFrame f) {
		options.pull(f.getEditorSuite());
		try { options.write(); }
		catch (Exception e) {}
	}
}
