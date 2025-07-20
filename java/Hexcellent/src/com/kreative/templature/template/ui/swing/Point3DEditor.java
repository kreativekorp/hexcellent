package com.kreative.templature.template.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class Point3DEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int xMin, yMin, zMin, xMax, yMax, zMax;
	private final SpinnerNumberModel xModel;
	private final SpinnerNumberModel yModel;
	private final SpinnerNumberModel zModel;
	
	public Point3DEditor(int x, int y, int z, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
		super(new GridLayout(1,0,8,8));
		this.xModel = new SpinnerNumberModel(x, this.xMin = xMin, this.xMax = xMax, 1);
		this.yModel = new SpinnerNumberModel(y, this.yMin = yMin, this.yMax = yMax, 1);
		this.zModel = new SpinnerNumberModel(z, this.zMin = zMin, this.zMax = zMax, 1);
		
		add(label("X", new JSpinner(xModel)));
		add(label("Y", new JSpinner(yModel)));
		add(label("Z", new JSpinner(zModel)));
	}
	
	public int getPointX() { return xModel.getNumber().intValue(); }
	public int getPointY() { return yModel.getNumber().intValue(); }
	public int getPointZ() { return zModel.getNumber().intValue(); }
	public int getXMin() { return xMin; }
	public int getYMin() { return yMin; }
	public int getZMin() { return zMin; }
	public int getXMax() { return xMax; }
	public int getYMax() { return yMax; }
	public int getZMax() { return zMax; }
	
	public int[] getPoint() {
		return new int[] {
			xModel.getNumber().intValue(),
			yModel.getNumber().intValue(),
			zModel.getNumber().intValue()
		};
	}
	
	public void setPointX(int x) { xModel.setValue(x); }
	public void setPointY(int y) { yModel.setValue(y); }
	public void setPointZ(int z) { zModel.setValue(z); }
	
	public void setPoint(int[] p) {
		if (p == null) return;
		xModel.setValue(p[0]);
		yModel.setValue(p[1]);
		zModel.setValue(p[2]);
	}
	
	public void addChangeListener(ChangeListener l) {
		xModel.addChangeListener(l);
		yModel.addChangeListener(l);
		zModel.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		xModel.removeChangeListener(l);
		yModel.removeChangeListener(l);
		zModel.removeChangeListener(l);
	}
	
	private static JPanel label(String label, Component comp) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.LINE_START);
		p.add(comp, BorderLayout.CENTER);
		return p;
	}
}
