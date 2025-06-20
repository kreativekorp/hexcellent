package com.kreative.hexcellent.main;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GenerateDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JRadioButton randomButton;
	private JRadioButton beButton;
	private JRadioButton leButton;
	private JRadioButton byteButton;
	private JRadioButton shortButton;
	private JRadioButton intButton;
	private JTextField startValue;
	private JTextField incValue;
	private JTextField count;
	private JButton okButton;
	private JButton cancelButton;
	private byte[] data;
	
	public GenerateDialog(Dialog parent) {
		super(parent, "Generate");
		setModal(true);
		make();
	}
	
	public GenerateDialog(Frame parent) {
		super(parent, "Generate");
		setModal(true);
		make();
	}
	
	public GenerateDialog(Window parent) {
		super(parent, "Generate");
		setModal(true);
		make();
	}
	
	private void make() {
		randomButton = new JRadioButton("Random");
		beButton = new JRadioButton("Sequential (Big-Endian)");
		leButton = new JRadioButton("Sequential (Little-Endian)");
		byteButton = new JRadioButton("Bytes");
		shortButton = new JRadioButton("Words (16-Bit)");
		intButton = new JRadioButton("Words (32-Bit)");
		startValue = new JTextField("0");
		incValue = new JTextField("0");
		count = new JTextField("0x100");
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		randomButton.setSelected(true);
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(randomButton);
		bg1.add(beButton);
		bg1.add(leButton);
		JPanel bp1 = new JPanel(new GridLayout(0, 1));
		bp1.add(randomButton);
		bp1.add(beButton);
		bp1.add(leButton);
		
		byteButton.setSelected(true);
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(byteButton);
		bg2.add(shortButton);
		bg2.add(intButton);
		JPanel bp2 = new JPanel(new GridLayout(0, 1));
		bp2.add(byteButton);
		bp2.add(shortButton);
		bp2.add(intButton);
		
		startValue.setEnabled(false);
		incValue.setEnabled(false);
		JPanel lp = new JPanel(new GridLayout(0, 1, 4, 4));
		lp.add(new JLabel("Start:"));
		lp.add(new JLabel("Increment:"));
		lp.add(new JLabel("Count:"));
		JPanel fp = new JPanel(new GridLayout(0, 1, 4, 4));
		fp.add(startValue);
		fp.add(incValue);
		fp.add(count);
		JPanel ip = new JPanel(new BorderLayout(4, 4));
		ip.add(lp, BorderLayout.LINE_START);
		ip.add(fp, BorderLayout.CENTER);
		
		JPanel mp = new JPanel(new GridLayout(1, 0, 8, 8));
		mp.add(bp1);
		mp.add(bp2);
		mp.add(ip);
		
		JPanel bpi = new JPanel(new GridLayout(1, 0, 8, 8));
		bpi.add(okButton);
		bpi.add(cancelButton);
		JPanel bp = new JPanel(new FlowLayout());
		bp.add(bpi);
		
		JPanel cp = new JPanel(new BorderLayout(8, 8));
		cp.add(mp, BorderLayout.CENTER);
		cp.add(bp, BorderLayout.PAGE_END);
		cp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(cp);
		SwingUtils.setDefaultButton(getRootPane(), okButton);
		SwingUtils.setCancelButton(getRootPane(), cancelButton);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		okButton.requestFocusInWindow();
		
		randomButton.addActionListener(new StartIncListener());
		beButton.addActionListener(new StartIncListener());
		leButton.addActionListener(new StartIncListener());
		startValue.addKeyListener(new ArrowListener(startValue, 0L));
		incValue.addKeyListener(new ArrowListener(incValue, 0L));
		count.addKeyListener(new ArrowListener(count, 0xFFFFFFFFL));
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try { GenerateDialog.this.data = generateData(); }
				catch (IOException ioe) { GenerateDialog.this.data = null; }
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GenerateDialog.this.data = null;
				dispose();
			}
		});
	}
	
	public byte[] showDialog() {
		data = null;
		setVisible(true);
		return data;
	}
	
	private byte[] generateData() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(out);
		if (randomButton.isSelected()) {
			Random r = new Random();
			if (intButton.isSelected()) {
				for (long n = parseValue(count.getText()); n > 0; n--) {
					int value = r.nextInt();
					dout.writeInt(value);
				}
			} else if (shortButton.isSelected()) {
				for (long n = parseValue(count.getText()); n > 0; n--) {
					short value = (short)r.nextInt(65536);
					dout.writeShort(value);
				}
			} else {
				for (long n = parseValue(count.getText()); n > 0; n--) {
					byte value = (byte)r.nextInt(256);
					dout.writeByte(value);
				}
			}
		} else {
			if (intButton.isSelected()) {
				int value = (int)parseValue(startValue.getText());
				int inc = (int)parseValue(incValue.getText());
				boolean le = leButton.isSelected();
				for (long n = parseValue(count.getText()); n > 0; n--) {
					dout.writeInt(le ? Integer.reverseBytes(value) : value);
					value += inc;
				}
			} else if (shortButton.isSelected()) {
				short value = (short)parseValue(startValue.getText());
				short inc = (short)parseValue(incValue.getText());
				boolean le = leButton.isSelected();
				for (long n = parseValue(count.getText()); n > 0; n--) {
					dout.writeShort(le ? Short.reverseBytes(value) : value);
					value += inc;
				}
			} else  {
				byte value = (byte)parseValue(startValue.getText());
				byte inc = (byte)parseValue(incValue.getText());
				for (long n = parseValue(count.getText()); n > 0; n--) {
					dout.writeByte(value);
					value += inc;
				}
			}
		}
		dout.close();
		out.close();
		return out.toByteArray();
	}
	
	private class StartIncListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			startValue.setEnabled(!randomButton.isSelected());
			incValue.setEnabled(!randomButton.isSelected());
		}
	}
	
	private class ArrowListener extends KeyAdapter {
		private final JTextField field;
		private final long mask;
		private ArrowListener(JTextField field, long mask) {
			this.field = field;
			this.mask = mask;
		}
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					String u = incrementValue(
						field.getText(), 1L,
						(mask != 0L) ? mask :
						intButton.isSelected() ? 0xFFFFFFFFL :
						shortButton.isSelected() ? 0xFFFFL : 0xFFL
					);
					if (u != null) field.setText(u);
					break;
				case KeyEvent.VK_DOWN:
					String v = incrementValue(
						field.getText(), -1L,
						(mask != 0L) ? mask :
						intButton.isSelected() ? 0xFFFFFFFFL :
						shortButton.isSelected() ? 0xFFFFL : 0xFFL
					);
					if (v != null) field.setText(v);
					break;
			}
		}
	}
	
	private static String incrementValue(String s, long inc, long mask) {
		try {
			s = s.trim().toLowerCase();
			if (s.startsWith("0x")) return "0x" + Long.toHexString((Long.parseLong(s.substring(2), 16) + inc) & mask).toUpperCase();
			if (s.startsWith("0h")) return "0h" + Long.toHexString((Long.parseLong(s.substring(2), 16) + inc) & mask).toUpperCase();
			if (s.startsWith("0d")) return "0d" + Long.toString((Long.parseLong(s.substring(2), 10) + inc) & mask);
			if (s.startsWith("0b")) return "0b" + Long.toBinaryString((Long.parseLong(s.substring(2), 2) + inc) & mask);
			if (s.startsWith("0o")) return "0o" + Long.toOctalString((Long.parseLong(s.substring(2), 8) + inc) & mask);
			if (s.startsWith("#")) return "#" + Long.toHexString((Long.parseLong(s.substring(1), 16) + inc) & mask).toUpperCase();
			if (s.startsWith("$")) return "$" + Long.toHexString((Long.parseLong(s.substring(1), 16) + inc) & mask).toUpperCase();
			if (s.startsWith("%")) return "%" + Long.toBinaryString((Long.parseLong(s.substring(1), 2) + inc) & mask);
			if (s.startsWith("0") && s.length() > 1) return "0" + Long.toOctalString((Long.parseLong(s.substring(1), 8) + inc) & mask);
			return Long.toString((Long.parseLong(s) + inc) & mask);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private static long parseValue(String s) {
		try {
			s = s.trim().toLowerCase();
			if (s.startsWith("0x")) return Long.parseLong(s.substring(2), 16);
			if (s.startsWith("0h")) return Long.parseLong(s.substring(2), 16);
			if (s.startsWith("0d")) return Long.parseLong(s.substring(2), 10);
			if (s.startsWith("0b")) return Long.parseLong(s.substring(2), 2);
			if (s.startsWith("0o")) return Long.parseLong(s.substring(2), 8);
			if (s.startsWith("#")) return Long.parseLong(s.substring(1), 16);
			if (s.startsWith("$")) return Long.parseLong(s.substring(1), 16);
			if (s.startsWith("%")) return Long.parseLong(s.substring(1), 2);
			if (s.startsWith("0") && s.length() > 1) return Long.parseLong(s.substring(1), 8);
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
