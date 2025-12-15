package com.kreative.templature.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.kreative.templature.template.BinaryTemplateFactory;
import com.kreative.templature.template.BinaryTemplateInputStream;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateException;
import com.kreative.templature.template.TextTemplateFactory;
import com.kreative.templature.template.TextTemplateTokenStream;

public class TemplateLoader {
	public static final File getTemplatesDirectory() {
		if (System.getProperty("os.name").toUpperCase().contains("MAC OS")) {
			File u = new File(System.getProperty("user.home"));
			File l = new File(u, "Library"); if (!l.exists()) l.mkdir();
			File a = new File(l, "Application Support"); if (!a.exists()) a.mkdir();
			File t = new File(a, "Templature Templates"); if (!t.exists()) t.mkdir();
			return t;
		} else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			File u = new File(System.getProperty("user.home"));
			File a = new File(u, "Application Data"); if (!a.exists()) a.mkdir();
			File k = new File(a, "Kreative"); if (!k.exists()) k.mkdir();
			File t = new File(k, "Templature Templates"); if (!t.exists()) t.mkdir();
			return t;
		} else {
			File u = new File(System.getProperty("user.home"));
			File c = new File(u, ".config"); if (!c.exists()) c.mkdir();
			File t = new File(c, "Templature Templates"); if (!t.exists()) t.mkdir();
			return t;
		}
	}
	
	private static final BinaryTemplateFactory createBinaryTemplateFactory() {
		BinaryTemplateFactory btf = new BinaryTemplateFactory();
		btf.registerDefaults();
		btf.registerExtensions();
		return btf;
	}
	
	private static final TextTemplateFactory createTextTemplateFactory() {
		TextTemplateFactory ttf = new TextTemplateFactory();
		ttf.registerDefaults();
		ttf.registerExtensions();
		return ttf;
	}
	
	private static final byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int read;
		while ((read = in.read(buf)) >= 0) out.write(buf, 0, read);
		out.flush(); out.close(); return out.toByteArray();
	}
	
	private static final byte[] read(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] out = read(in);
		in.close();
		return out;
	}
	
	private static final boolean isProbablyTextFormat(byte[] data) {
		boolean hasC0 = false;
		boolean hasCB = false;
		for (byte b : data) {
			if (b == ';' || b == '{' || b == '}') hasCB = true;
			if (b == 9 || b == 10 || b == 13) continue;
			if (b >= 0 && b < 32) hasC0 = true;
		}
		if (hasC0) return false;
		if (hasCB) return true;
		return false;
	}
	
	private final BinaryTemplateFactory btf = createBinaryTemplateFactory();
	private final TextTemplateFactory ttf = createTextTemplateFactory();
	private final Map<String,Template> btcache = new HashMap<String,Template>();
	private final Map<String,Template> ttcache = new HashMap<String,Template>();
	private final Map<String,Template> tcache = new HashMap<String,Template>();
	
	public void preloadTemplates() {
		preloadTemplates(getTemplatesDirectory(), null, true);
	}
	
	public void preloadTemplates(File file, Boolean text, boolean namesOnly) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				String childName = child.getName();
				if (childName.startsWith(".")) continue;
				if (childName.endsWith("\r")) continue;
				if (childName.endsWith("\uF00D")) continue;
				preloadTemplates(child, text, namesOnly);
			}
		} else {
			String key = (
				namesOnly ?
				file.getName().replaceAll("[.][Tt][Mm][Pp][Ll][Tt]?$", "") :
				file.getAbsolutePath()
			);
			try {
				loadTemplate(key, file, text);
			} catch (TemplateException te) {
				System.err.println("The template “" + key + "” is invalid. " + te.getMessage());
			} catch (IOException ioe) {
				System.err.println("The template “" + key + "” could not be read.");
			}
		}
	}
	
	public Template loadTemplate(String name, Boolean text) throws IOException {
		return loadTemplate(name, new File(name), text);
	}
	
	public Template loadTemplate(File file, Boolean text) throws IOException {
		return loadTemplate(file.getAbsolutePath(), file, text);
	}
	
	private Template loadTemplate(String key, File file, Boolean text) throws IOException {
		if (text == null) {
			if (tcache.containsKey(key)) return tcache.get(key);
		} else if (text.booleanValue()) {
			if (ttcache.containsKey(key)) return ttcache.get(key);
		} else {
			if (btcache.containsKey(key)) return btcache.get(key);
		}
		
		byte[] data = read(file);
		if (text == null) text = isProbablyTextFormat(data);
		
		if (text.booleanValue()) {
			String source = new String(data, "UTF-8");
			TextTemplateTokenStream in = new TextTemplateTokenStream(source);
			Template t = ttf.createTemplate(in);
			in.close();
			ttcache.put(key, t);
			tcache.put(key, t);
			return t;
		} else {
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			BinaryTemplateInputStream in = new BinaryTemplateInputStream(bin);
			Template t = btf.createTemplate(in);
			in.close();
			btcache.put(key, t);
			tcache.put(key, t);
			return t;
		}
	}
	
	public Set<String> keySet() {
		return tcache.keySet();
	}
}
