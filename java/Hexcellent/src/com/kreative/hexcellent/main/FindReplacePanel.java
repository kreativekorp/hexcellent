package com.kreative.hexcellent.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorSuite;

public class FindReplacePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static FindReplacePanel instance = null;
	public static FindReplacePanel getInstance() {
		if (instance == null) instance = new FindReplacePanel();
		return instance;
	}
	
	private final JHexEditorSuite findSuite;
	private final JHexEditorSuite replaceSuite;
	private final JHexEditor findEditor;
	private final JHexEditor replaceEditor;
	private final JButton findNextButton;
	private final JButton findPreviousButton;
	private final JButton replaceAllButton;
	private final JButton closeButton;
	private JHexEditor editor = null;
	private JDialog dialog = null;
	
	public FindReplacePanel() {
		JPanel labelPanel = new JPanel(new GridLayout(0, 1, 8, 8));
		labelPanel.add(rightAlign(new JLabel("Find:")));
		labelPanel.add(rightAlign(new JLabel("Replace:")));
		
		JPanel suitePanel = new JPanel(new GridLayout(0, 1, 8, 8));
		suitePanel.add(findSuite = new JHexEditorSuite());
		suitePanel.add(replaceSuite = new JHexEditorSuite());
		findEditor = findSuite.getEditor();
		replaceEditor = replaceSuite.getEditor();
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 8, 8));
		buttonPanel.add(findNextButton = new JButton("Find Next"));
		buttonPanel.add(findPreviousButton = new JButton("Find Previous"));
		buttonPanel.add(replaceAllButton = new JButton("Replace All"));
		buttonPanel.add(closeButton = new JButton("Close"));
		
		JPanel buttonOuterPanel = new JPanel(new BorderLayout());
		buttonOuterPanel.add(buttonPanel, BorderLayout.PAGE_START);
		
		setLayout(new BorderLayout(8, 8));
		add(labelPanel, BorderLayout.LINE_START);
		add(suitePanel, BorderLayout.CENTER);
		add(buttonOuterPanel, BorderLayout.LINE_END);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		findNextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editor != null && !findNext(editor)) {
					Toolkit.getDefaultToolkit().beep();
				} else if (dialog != null) {
					dialog.dispose();
				}
			}
		});
		findPreviousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editor != null && !findPrevious(editor)) {
					Toolkit.getDefaultToolkit().beep();
				} else if (dialog != null) {
					dialog.dispose();
				}
			}
		});
		replaceAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editor != null && !replaceAll(editor)) {
					Toolkit.getDefaultToolkit().beep();
				} else if (dialog != null) {
					dialog.dispose();
				}
			}
		});
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dialog != null) dialog.dispose();
			}
		});
	}
	
	public void showDialog(EditorFrame f) {
		Options o = new Options();
		o.pull(f.getEditorSuite());
		o.push(findSuite);
		o.push(replaceSuite);
		findSuite.getInspector().setVisible(false);
		replaceSuite.getInspector().setVisible(false);
		findEditor.setPreferredRowCount(8);
		replaceEditor.setPreferredRowCount(8);
		findEditor.setEnableShortcutKeys(true);
		replaceEditor.setEnableShortcutKeys(true);
		editor = f.getEditor();
		dialog = new JDialog(f, "Find & Replace");
		dialog.setModal(true);
		dialog.setContentPane(this);
		SwingUtils.setDefaultButton(dialog.getRootPane(), findNextButton);
		SwingUtils.setCancelButton(dialog.getRootPane(), closeButton);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog = null;
		editor = null;
	}
	
	public boolean useSelectionForFind(JHexEditor editor) {
		byte[] sel = editor.getSelection();
		if (sel == null || sel.length == 0) return false;
		findEditor.selectAll();
		findEditor.replaceSelection("Use Selection For Find", sel, true);
		return true;
	}
	
	public boolean useSelectionForReplace(JHexEditor editor) {
		byte[] sel = editor.getSelection();
		if (sel == null || sel.length == 0) return false;
		replaceEditor.selectAll();
		replaceEditor.replaceSelection("Use Selection For Replace", sel, true);
		return true;
	}
	
	public byte[] getDataForFind() {
		long length = findEditor.length();
		if (length >= Integer.MAX_VALUE) return null;
		byte[] data = new byte[(int)length];
		findEditor.get(0, data, 0, (int)length);
		return data;
	}
	
	public byte[] getDataForReplace() {
		long length = replaceEditor.length();
		if (length >= Integer.MAX_VALUE) return null;
		byte[] data = new byte[(int)length];
		replaceEditor.get(0, data, 0, (int)length);
		return data;
	}
	
	public boolean findNext(JHexEditor editor) {
		byte[] dataForFind = getDataForFind();
		if (dataForFind == null || dataForFind.length == 0) return false;
		long offset = editor.getSelectionMax();
		offset = editor.indexOf(dataForFind, offset);
		if (offset < 0) offset = editor.indexOf(dataForFind);
		if (offset < 0) return false;
		editor.setSelectionRange(offset, offset + dataForFind.length);
		return true;
	}
	
	public boolean findPrevious(JHexEditor editor) {
		byte[] dataForFind = getDataForFind();
		if (dataForFind == null || dataForFind.length == 0) return false;
		long offset = editor.getSelectionMin() - dataForFind.length;
		offset = editor.lastIndexOf(dataForFind, offset);
		if (offset < 0) offset = editor.lastIndexOf(dataForFind);
		if (offset < 0) return false;
		editor.setSelectionRange(offset, offset + dataForFind.length);
		return true;
	}
	
	public boolean replace(JHexEditor editor) {
		byte[] dataForReplace = getDataForReplace();
		if (dataForReplace == null) return false;
		return editor.replaceSelection("Replace", dataForReplace, true);
	}
	
	public boolean replaceAll(JHexEditor editor) {
		byte[] dataForFind = getDataForFind();
		if (dataForFind == null || dataForFind.length == 0) return false;
		byte[] dataForReplace = getDataForReplace();
		if (dataForReplace == null) return false;
		return editor.replaceAll(dataForFind, dataForReplace);
	}
	
	private static JLabel rightAlign(JLabel label) {
		label.setHorizontalAlignment(JLabel.TRAILING);
		return label;
	}
}
