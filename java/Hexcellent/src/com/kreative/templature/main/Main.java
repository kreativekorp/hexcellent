package com.kreative.templature.main;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateException;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			List<OpenRequest> list = parseOpenRequests(args);
			if (list == null) printMainHelp();
			else executeOpenRequests(list);
		} else {
			String arg0 = args[0].toLowerCase();
			List<String> arga = Arrays.asList(args);
			arga = arga.subList(1, arga.size());
			String[] argz = arga.toArray(new String[arga.size()]);
			if (arg0.equals("edit")) {
				List<OpenRequest> list = parseOpenRequests(argz);
				if (list == null) printEditHelp();
				else executeOpenRequests(list);
			} else if (arg0.equals("compile")) {
				CompileTemplate.main(argz);
			} else if (arg0.equals("decompile")) {
				DecompileTemplate.main(argz);
			} else if (arg0.equals("lint")) {
				LintTemplate.main(argz);
			} else {
				List<OpenRequest> list = parseOpenRequests(args);
				if (list == null) printMainHelp();
				else executeOpenRequests(list);
			}
		}
	}
	
	public static void printMainHelp() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java com.kreative.templature.main.Main [edit] <options> <files>");
		System.out.println("  java com.kreative.templature.main.Main compile <options> <files>");
		System.out.println("  java com.kreative.templature.main.Main decompile <options> <files>");
		System.out.println("  java com.kreative.templature.main.Main lint <options> <files>");
		System.out.println();
	}
	
	public static void printEditHelp() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java com.kreative.templature.main.Main [edit] <options> <files>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("  -A            Edit a template in text format.");
		System.out.println("  -B            Edit a template in binary format.");
		System.out.println("  -T            Edit a template in unknown format.");
		System.out.println("  -a <template> Edit a file using a template in text format.");
		System.out.println("  -b <template> Edit a file using a template in binary format.");
		System.out.println("  -t <template> Edit a file using a template in unknown format.");
		System.out.println("  -p, --prompt  Edit a file using a template chosen from a dialog.");
		System.out.println("  -n, --new     Edit a new template or file.");
		System.out.println("  --            Process remaining arguments as file names.");
		System.out.println();
	}
	
	private static enum OpenRequestType {
		EDIT_TEMPLATE_TEXT, EDIT_TEMPLATE_BINARY, EDIT_TEMPLATE_GUESS,
		EDIT_INSTANCE_TEXT, EDIT_INSTANCE_BINARY, EDIT_INSTANCE_GUESS,
		EDIT_INSTANCE_PROMPT
	}
	
	private static class OpenRequest {
		public final OpenRequestType type;
		public final String templateSpec;
		public final String instanceSpec;
		public OpenRequest(OpenRequestType type, String templateSpec, String instanceSpec) {
			this.type = type;
			this.templateSpec = templateSpec;
			this.instanceSpec = instanceSpec;
		}
	}
	
	private static List<OpenRequest> parseOpenRequests(String[] args) {
		List<OpenRequest> list = new ArrayList<OpenRequest>();
		boolean processOptions = true;
		OpenRequestType type = null;
		String templateSpec = null;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (processOptions && arg.startsWith("-")) {
				if (arg.equals("--")) {
					processOptions = false;
				} else if (arg.equals("-A")) {
					type = OpenRequestType.EDIT_TEMPLATE_TEXT;
					templateSpec = null;
				} else if (arg.equals("-B")) {
					type = OpenRequestType.EDIT_TEMPLATE_BINARY;
					templateSpec = null;
				} else if (arg.equals("-T")) {
					type = OpenRequestType.EDIT_TEMPLATE_GUESS;
					templateSpec = null;
				} else if (arg.equals("-a") && argi < args.length) {
					type = OpenRequestType.EDIT_INSTANCE_TEXT;
					templateSpec = args[argi++];
				} else if (arg.equals("-b") && argi < args.length) {
					type = OpenRequestType.EDIT_INSTANCE_BINARY;
					templateSpec = args[argi++];
				} else if (arg.equals("-t") && argi < args.length) {
					type = OpenRequestType.EDIT_INSTANCE_GUESS;
					templateSpec = args[argi++];
				} else if (arg.equals("-p") || arg.equalsIgnoreCase("--prompt")) {
					type = OpenRequestType.EDIT_INSTANCE_PROMPT;
					templateSpec = null;
				} else if (arg.equals("-n") || arg.equalsIgnoreCase("--new")) {
					list.add(new OpenRequest(type, templateSpec, null));
				} else {
					return null;
				}
			} else {
				list.add(new OpenRequest(type, templateSpec, arg));
			}
		}
		if (list.isEmpty()) {
			list.add(new OpenRequest(type, templateSpec, null));
		}
		return list;
	}
	
	private static void executeOpenRequests(List<OpenRequest> list) {
		initGUI();
		TemplateLoader loader = new TemplateLoader();
		loader.preloadTemplates();
		for (OpenRequest o : list) {
			if (o.type == null) {
				if (o.instanceSpec == null) newTemplateInstanceEditor(null);
				else openTemplateInstanceEditor(null, new File(o.instanceSpec));
			} else switch (o.type) {
				case EDIT_TEMPLATE_TEXT:
					if (o.instanceSpec == null) newTemplateEditor(true);
					else openTemplateEditor(new File(o.instanceSpec), true);
					break;
				case EDIT_TEMPLATE_BINARY:
					if (o.instanceSpec == null) newTemplateEditor(false);
					else openTemplateEditor(new File(o.instanceSpec), false);
					break;
				case EDIT_TEMPLATE_GUESS:
					if (o.instanceSpec == null) newTemplateEditor(null);
					else openTemplateEditor(new File(o.instanceSpec), null);
					break;
				case EDIT_INSTANCE_TEXT:
					openTemplateInstanceEditor(loader, o.templateSpec, true, o.instanceSpec);
					break;
				case EDIT_INSTANCE_BINARY:
					openTemplateInstanceEditor(loader, o.templateSpec, false, o.instanceSpec);
					break;
				case EDIT_INSTANCE_GUESS:
					openTemplateInstanceEditor(loader, o.templateSpec, null, o.instanceSpec);
					break;
				case EDIT_INSTANCE_PROMPT:
					if (o.instanceSpec == null) newTemplateInstanceEditor(null);
					else openTemplateInstanceEditor(null, new File(o.instanceSpec));
					break;
			}
		}
	}
	
	private static void openTemplateInstanceEditor(TemplateLoader loader, String templateSpec, Boolean text, String instanceSpec) {
		try {
			Template t = loader.loadTemplate(templateSpec, text);
			if (instanceSpec == null) newTemplateInstanceEditor(t);
			else openTemplateInstanceEditor(t, new File(instanceSpec));
		} catch (TemplateException te) {
			JOptionPane.showMessageDialog(
				null, "The template “" + templateSpec + "” is invalid. " + te.getMessage(),
				"Open", JOptionPane.ERROR_MESSAGE
			);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				null, "The template “" + templateSpec + "” could not be read.",
				"Open", JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	private static void initGUI() {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Templature"); } catch (Exception e) {}
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
			aacn.set(tk, "Templature");
		} catch (Exception e) {}
		
		if (SwingUtils.IS_MAC_OS) {
			try { Class.forName("com.kreative.templature.main.mac.MyApplicationListener").newInstance(); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public static TemplateEditorFrame newTemplateEditor(Boolean text) {
		try {
			TemplateEditorFrame f = new TemplateEditorFrame(text);
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
	
	private static String lastOpenTemplateDirectory = null;
	public static TemplateEditorFrame openTemplateEditor(File file, Boolean text) {
		if (file == null) {
			Frame frame = new Frame();
			FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
			if (lastOpenTemplateDirectory != null) fd.setDirectory(lastOpenTemplateDirectory);
			fd.setVisible(true);
			String parent = fd.getDirectory();
			String name = fd.getFile();
			fd.dispose();
			frame.dispose();
			if (parent == null || name == null) return null;
			file = new File((lastOpenTemplateDirectory = parent), name);
		}
		try {
			TemplateEditorFrame f = new TemplateEditorFrame(file, text);
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
	
	public static TemplateInstanceEditorFrame newTemplateInstanceEditor(Template t) {
		if (t == null) {
			TemplateSelectDialog tsd = new TemplateSelectDialog((Frame)null, null, true);
			if (!tsd.showDialog()) return null;
			t = tsd.getTemplate();
			if (t == null) {
				newTemplateEditor(tsd.getMetaTextFormat());
				return null;
			}
		}
		try {
			TemplateInstanceEditorFrame f = new TemplateInstanceEditorFrame(t);
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
	
	private static String lastOpenTemplateInstanceDirectory = null;
	public static TemplateInstanceEditorFrame openTemplateInstanceEditor(Template t, File file) {
		if (file == null) {
			Frame frame = new Frame();
			FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
			if (lastOpenTemplateInstanceDirectory != null) fd.setDirectory(lastOpenTemplateInstanceDirectory);
			fd.setVisible(true);
			String parent = fd.getDirectory();
			String name = fd.getFile();
			fd.dispose();
			frame.dispose();
			if (parent == null || name == null) return null;
			file = new File((lastOpenTemplateInstanceDirectory = parent), name);
		}
		if (t == null) {
			TemplateSelectDialog tsd = new TemplateSelectDialog((Frame)null, file.getName(), true);
			if (!tsd.showDialog()) return null;
			t = tsd.getTemplate();
			if (t == null) {
				openTemplateEditor(file, tsd.getMetaTextFormat());
				return null;
			}
		}
		try {
			TemplateInstanceEditorFrame f = new TemplateInstanceEditorFrame(t, file);
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
}
