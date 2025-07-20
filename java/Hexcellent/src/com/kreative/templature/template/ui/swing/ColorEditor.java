package com.kreative.templature.template.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int rMax, gMax, bMax, aMax;
	private final SpinnerNumberModel rModel;
	private final SpinnerNumberModel gModel;
	private final SpinnerNumberModel bModel;
	private final SpinnerNumberModel aModel;
	private final ColorWell colorWell;
	private final JButton setButton;
	
	public ColorEditor(Color c, boolean alpha) {
		this(
			c.getRed(), c.getGreen(), c.getBlue(),
			(alpha ? c.getAlpha() : 0),
			255, 255, 255,
			(alpha ? 255 : 0)
		);
	}
	
	public ColorEditor(int r, int g, int b, int a, int rMax, int gMax, int bMax, int aMax) {
		super(new GridLayout(1,0,8,8));
		this.rModel = new SpinnerNumberModel(r, 0, this.rMax = rMax, 1);
		this.gModel = new SpinnerNumberModel(g, 0, this.gMax = gMax, 1);
		this.bModel = new SpinnerNumberModel(b, 0, this.bMax = bMax, 1);
		this.aModel = new SpinnerNumberModel(a, 0, this.aMax = aMax, 1);
		this.colorWell = new ColorWell();
		this.setButton = new JButton("Set");
		
		add(colorWell);
		if (rMax > 0) add(label("R", new JSpinner(rModel)));
		if (gMax > 0) add(label("G", new JSpinner(gModel)));
		if (bMax > 0) add(label("B", new JSpinner(bModel)));
		if (aMax > 0) add(label("A", new JSpinner(aModel)));
		add(setButton);
		
		rModel.addChangeListener(colorWell);
		gModel.addChangeListener(colorWell);
		bModel.addChangeListener(colorWell);
		aModel.addChangeListener(colorWell);
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setColor(JColorChooser.showDialog(ColorEditor.this, "Set Color", getColor()));
			}
		});
	}
	
	public int getRed() { return rModel.getNumber().intValue(); }
	public int getGreen() { return gModel.getNumber().intValue(); }
	public int getBlue() { return bModel.getNumber().intValue(); }
	public int getAlpha() { return aModel.getNumber().intValue(); }
	public int getRedMax() { return rMax; }
	public int getGreenMax() { return gMax; }
	public int getBlueMax() { return bMax; }
	public int getAlphaMax() { return aMax; }
	
	public Color getColor() {
		float r = (rMax > 0) ? (rModel.getNumber().floatValue() / rMax) : 0.0f;
		float g = (gMax > 0) ? (gModel.getNumber().floatValue() / gMax) : 0.0f;
		float b = (bMax > 0) ? (bModel.getNumber().floatValue() / bMax) : 0.0f;
		float a = (aMax > 0) ? (aModel.getNumber().floatValue() / aMax) : 1.0f;
		return new Color(r, g, b, a);
	}
	
	public void setRed(int r) { rModel.setValue(r); }
	public void setGreen(int g) { gModel.setValue(g); }
	public void setBlue(int b) { bModel.setValue(b); }
	public void setAlpha(int a) { aModel.setValue(a); }
	
	public void setColor(Color c) {
		if (c == null) return;
		float[] rgba = c.getRGBComponents(new float[4]);
		rModel.setValue(Math.round(rgba[0] * rMax));
		gModel.setValue(Math.round(rgba[1] * gMax));
		bModel.setValue(Math.round(rgba[2] * bMax));
		aModel.setValue(Math.round(rgba[3] * aMax));
	}
	
	public void addChangeListener(ChangeListener l) {
		rModel.addChangeListener(l);
		gModel.addChangeListener(l);
		bModel.addChangeListener(l);
		aModel.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		rModel.removeChangeListener(l);
		gModel.removeChangeListener(l);
		bModel.removeChangeListener(l);
		aModel.removeChangeListener(l);
	}
	
	private class ColorWell extends JComponent implements ChangeListener {
		private static final long serialVersionUID = 1L;
		protected void paintComponent(Graphics g) {
			Insets i = getInsets();
			int w = getWidth() - i.left - i.right;
			int h = getHeight() - i.top - i.bottom;
			Color c = getColor();
			g.setColor(c);
			g.fillRect(i.left, i.top, w, h);
			g.setColor(c.darker());
			g.fillRect(i.left, i.top, w, 1);
			g.fillRect(i.left, i.top + h - 1, w, 1);
			g.fillRect(i.left, i.top + 1, 1, h - 2);
			g.fillRect(i.left + w - 1, i.top + 1, 1, h - 2);
		}
		public void stateChanged(ChangeEvent e) {
			repaint();
		}
	}
	
	private static JPanel label(String label, Component comp) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.LINE_START);
		p.add(comp, BorderLayout.CENTER);
		return p;
	}
}
