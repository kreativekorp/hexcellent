package com.kreative.templature.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateException;

public class TemplateSelectDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String META_AUTO = "TMPL (auto)";
	private static final String META_BINARY = "TMPL (binary)";
	private static final String META_TEXT = "TMPL (text)";
	
	private TemplateLoader loader;
	private JLabel label;
	private JList list;
	private JButton okButton;
	private JButton cancelButton;
	private JButton browseButton;
	private boolean confirmed;
	private Template template;
	private Boolean metaTextFormat;
	
	public TemplateSelectDialog(Dialog parent, String name, boolean meta) {
		super(parent, "Select Template");
		setModal(true);
		make(name, meta);
	}
	
	public TemplateSelectDialog(Frame parent, String name, boolean meta) {
		super(parent, "Select Template");
		setModal(true);
		make(name, meta);
	}
	
	public TemplateSelectDialog(Window parent, String name, boolean meta) {
		super(parent, "Select Template");
		setModal(true);
		make(name, meta);
	}
	
	private static String lastOpenTemplateDirectory = null;
	private void make(String name, boolean meta) {
		loader = new TemplateLoader();
		loader.preloadTemplates();
		
		label = new JLabel(
			(name == null) ?
			"Select template for new file" :
			("Select template for “" + name + "”")
		);
		
		TreeSet<String> keys = new TreeSet<String>(loader.keySet());
		if (meta) keys.addAll(Arrays.asList(META_AUTO, META_BINARY, META_TEXT));
		list = new JList(keys.toArray(new String[keys.size()]));
		
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		browseButton = new JButton("Browse…");
		
		JScrollPane listPane = new JScrollPane(
			list,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		JPanel topButtonPanel = new JPanel(new GridLayout(0, 1, 8, 8));
		topButtonPanel.add(okButton);
		topButtonPanel.add(cancelButton);
		
		JPanel buttonPanel = new JPanel(new BorderLayout(8, 8));
		buttonPanel.add(topButtonPanel, BorderLayout.PAGE_START);
		buttonPanel.add(browseButton, BorderLayout.PAGE_END);
		
		JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
		mainPanel.add(label, BorderLayout.PAGE_START);
		mainPanel.add(listPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.LINE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object value = list.getSelectedValue();
				if (value == null) {
					getToolkit().beep();
				} else if (META_AUTO.equals(value)) {
					confirmed = true; template = null; metaTextFormat = null; dispose();
				} else if (META_TEXT.equals(value)) {
					confirmed = true; template = null; metaTextFormat = true; dispose();
				} else if (META_BINARY.equals(value)) {
					confirmed = true; template = null; metaTextFormat = false; dispose();
				} else try {
					confirmed = true;
					template = loader.loadTemplate(value.toString(), null);
					metaTextFormat = null;
					dispose();
				} catch (TemplateException te) {
					confirmed = false; template = null; metaTextFormat = null; dispose();
				} catch (IOException ioe) {
					confirmed = false; template = null; metaTextFormat = null; dispose();
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmed = false;
				template = null;
				metaTextFormat = null;
				dispose();
			}
		});
		
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(TemplateSelectDialog.this, "Select Template", FileDialog.LOAD);
				if (lastOpenTemplateDirectory != null) fd.setDirectory(lastOpenTemplateDirectory);
				fd.setVisible(true);
				String parent = fd.getDirectory();
				String name = fd.getFile();
				fd.dispose();
				if (parent == null || name == null) return;
				File file = new File((lastOpenTemplateDirectory = parent), name);
				try {
					confirmed = true;
					template = loader.loadTemplate(file, null);
					metaTextFormat = null;
					dispose();
				} catch (TemplateException te) {
					JOptionPane.showMessageDialog(
						null, "The selected template is invalid. " + te.getMessage(),
						"Select Template", JOptionPane.ERROR_MESSAGE
					);
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(
						null, "The selected template could not be read.",
						"Select Template", JOptionPane.ERROR_MESSAGE
					);
				}
			}
		});
	}
	
	public boolean isConfirmed() { return confirmed; }
	public Template getTemplate() { return template; }
	public Boolean getMetaTextFormat() { return metaTextFormat; }
	
	public boolean showDialog() {
		confirmed = false;
		template = null;
		metaTextFormat = null;
		setVisible(true);
		return confirmed;
	}
}
