package com.kreative.hexcellent.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FontDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JList fontNameList;
	private JCheckBox boldCheckbox;
	private JCheckBox italicCheckbox;
	private SpinnerNumberModel fontSizeModel;
	private JLabel previewLabel;
	private JButton okButton;
	private JButton cancelButton;
	private Font font;
	
	public FontDialog(Dialog parent, Font font) {
		super(parent, "Font");
		setModal(true);
		make(font);
	}
	
	public FontDialog(Frame parent, Font font) {
		super(parent, "Font");
		setModal(true);
		make(font);
	}
	
	public FontDialog(Window parent, Font font) {
		super(parent, "Font");
		setModal(true);
		make(font);
	}
	
	private void make(Font font) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontNameList = new JList(ge.getAvailableFontFamilyNames());
		boldCheckbox = new JCheckBox("Bold", (font.getStyle() & Font.BOLD) != 0);
		italicCheckbox = new JCheckBox("Italic", (font.getStyle() & Font.ITALIC) != 0);
		fontSizeModel = new SpinnerNumberModel(font.getSize(), 1, 255, 1);
		previewLabel = new JLabel("01234567: 89 AB CD EF The quick brown fox jumps over the lazy dog.");
		previewLabel.setMinimumSize(new Dimension(400, 50));
		previewLabel.setPreferredSize(new Dimension(400, 50));
		previewLabel.setMaximumSize(new Dimension(400, 50));
		previewLabel.setHorizontalAlignment(JLabel.LEFT);
		previewLabel.setVerticalAlignment(JLabel.CENTER);
		previewLabel.setFont(font);
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		JScrollPane fontNamePane = new JScrollPane(fontNameList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JSpinner fontSizeSpinner = new JSpinner(fontSizeModel);
		
		JPanel stylePanel = new JPanel(new GridLayout(1, 0, 8, 8));
		stylePanel.add(boldCheckbox);
		stylePanel.add(italicCheckbox);
		stylePanel.add(fontSizeSpinner);
		
		JPanel fontPanel = new JPanel(new BorderLayout(8, 8));
		fontPanel.add(fontNamePane, BorderLayout.CENTER);
		fontPanel.add(stylePanel, BorderLayout.PAGE_END);
		
		JPanel buttonInnerPanel = new JPanel(new GridLayout(1, 0, 8, 8));
		buttonInnerPanel.add(okButton);
		buttonInnerPanel.add(cancelButton);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonInnerPanel);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(fontPanel, BorderLayout.PAGE_START);
		mainPanel.add(previewLabel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		fontNameList.setSelectedValue(font.getFamily(), true);
		okButton.requestFocusInWindow();
		
		fontNameList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				previewLabel.setFont(getSelectedFont());
			}
		});
		boldCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previewLabel.setFont(getSelectedFont());
			}
		});
		italicCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previewLabel.setFont(getSelectedFont());
			}
		});
		fontSizeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				previewLabel.setFont(getSelectedFont());
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FontDialog.this.font = getSelectedFont();
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FontDialog.this.font = null;
				dispose();
			}
		});
	}
	
	private Font getSelectedFont() {
		String fontName = fontNameList.getSelectedValue().toString();
		int fontStyle = (boldCheckbox.isSelected() ? Font.BOLD : 0) | (italicCheckbox.isSelected() ? Font.ITALIC : 0);
		int fontSize = fontSizeModel.getNumber().intValue();
		return new Font(fontName, fontStyle, fontSize);
	}
	
	public Font showDialog() {
		font = null;
		setVisible(true);
		return font;
	}
}
