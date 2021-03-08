package com.kreative.hexcellent.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.kreative.hexcellent.editor.JHexEditorColors;

public class ColorSchemeDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JList colorsList;
	private ColorPreviewPanel previewPanel;
	private JButton okButton;
	private JButton cancelButton;
	private JHexEditorColors colors;
	
	public ColorSchemeDialog(Dialog parent, JHexEditorColors colors) {
		super(parent, "Color Scheme");
		setModal(true);
		make(colors);
	}
	
	public ColorSchemeDialog(Frame parent, JHexEditorColors colors) {
		super(parent, "Color Scheme");
		setModal(true);
		make(colors);
	}
	
	public ColorSchemeDialog(Window parent, JHexEditorColors colors) {
		super(parent, "Color Scheme");
		setModal(true);
		make(colors);
	}
	
	private void make(JHexEditorColors colors) {
		colorsList = new JList(JHexEditorColors.BUILTINS);
		previewPanel = new ColorPreviewPanel(colors);
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		JScrollPane colorsPane = new JScrollPane(colorsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel colorsPanel = new JPanel(new BorderLayout(12, 12));
		colorsPanel.add(colorsPane, BorderLayout.CENTER);
		colorsPanel.add(previewPanel, BorderLayout.PAGE_END);
		
		JPanel buttonInnerPanel = new JPanel(new GridLayout(1, 0, 8, 8));
		buttonInnerPanel.add(okButton);
		buttonInnerPanel.add(cancelButton);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonInnerPanel);
		
		JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
		mainPanel.add(colorsPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		colorsList.setSelectedValue(colors, true);
		okButton.requestFocusInWindow();
		
		colorsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				previewPanel.setColors(getSelectedColors());
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorSchemeDialog.this.colors = getSelectedColors();
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColorSchemeDialog.this.colors = null;
				dispose();
			}
		});
	}
	
	private JHexEditorColors getSelectedColors() {
		return (JHexEditorColors)colorsList.getSelectedValue();
	}
	
	public JHexEditorColors showDialog() {
		colors = null;
		setVisible(true);
		return colors;
	}
}
