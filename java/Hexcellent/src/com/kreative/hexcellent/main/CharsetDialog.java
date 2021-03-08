package com.kreative.hexcellent.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CharsetDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JList charsetList;
	private JButton okButton;
	private JButton cancelButton;
	private String charset;
	
	public CharsetDialog(Dialog parent, String charset) {
		super(parent, "Encoding");
		setModal(true);
		make(charset);
	}
	
	public CharsetDialog(Frame parent, String charset) {
		super(parent, "Encoding");
		setModal(true);
		make(charset);
	}
	
	public CharsetDialog(Window parent, String charset) {
		super(parent, "Encoding");
		setModal(true);
		make(charset);
	}
	
	private void make(String charset) {
		Collection<Charset> cs = Charset.availableCharsets().values();
		Charset[] csa = cs.toArray(new Charset[cs.size()]);
		Arrays.sort(csa, charsetComparator);
		charsetList = new JList(csa);
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		JScrollPane charsetPane = new JScrollPane(charsetList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel buttonInnerPanel = new JPanel(new GridLayout(1, 0, 8, 8));
		buttonInnerPanel.add(okButton);
		buttonInnerPanel.add(cancelButton);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonInnerPanel);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(charsetPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(mainPanel);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		try { charsetList.setSelectedValue(Charset.forName(charset), true); }
		catch (Exception e) {}
		okButton.requestFocusInWindow();
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CharsetDialog.this.charset = charsetList.getSelectedValue().toString();
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CharsetDialog.this.charset = null;
				dispose();
			}
		});
	}
	
	public String showDialog() {
		charset = null;
		setVisible(true);
		return charset;
	}
	
	private static int naturalCompare(String a, String b) {
		List<String> na = naturalTokenize(a.trim());
		List<String> nb = naturalTokenize(b.trim());
		for (int i = 0; i < na.size() && i < nb.size(); i++) {
			try {
				double va = Double.parseDouble(na.get(i));
				double vb = Double.parseDouble(nb.get(i));
				int cmp = Double.compare(va, vb);
				if (cmp != 0) return cmp;
			} catch (NumberFormatException e) {
				int cmp = na.get(i).compareToIgnoreCase(nb.get(i));
				if (cmp != 0) return cmp;
			}
		}
		return na.size() - nb.size();
	}
	
	private static List<String> naturalTokenize(String s) {
		List<String> tokens = new ArrayList<String>();
		StringBuffer token = new StringBuffer();
		int tokenType = 0;
		CharacterIterator iter = new StringCharacterIterator(s);
		for (char ch = iter.first(); ch != CharacterIterator.DONE; ch = iter.next()) {
			int tt = Character.isDigit(ch) ? 1 : Character.isLetter(ch) ? 2 : 3;
			if (tt != tokenType) {
				if (token.length() > 0) {
					tokens.add(token.toString());
					token = new StringBuffer();
				}
				tokenType = tt;
			}
			token.append(ch);
		}
		if (token.length() > 0) tokens.add(token.toString());
		return tokens;
	}
	
	private static final Comparator<Charset> charsetComparator = new Comparator<Charset>() {
		@Override
		public int compare(Charset a, Charset b) {
			return naturalCompare(a.displayName(), b.displayName());
		}
	};
}
