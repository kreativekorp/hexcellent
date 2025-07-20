package com.kreative.templature.template.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class RectangleEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int lMin, tMin, rMin, bMin;
	private final int lMax, tMax, rMax, bMax;
	private final SpinnerNumberModel lModel;
	private final SpinnerNumberModel tModel;
	private final SpinnerNumberModel rModel;
	private final SpinnerNumberModel bModel;
	private final JButton setButton;
	
	public RectangleEditor(
		int l, int t, int r, int b,
		int lMin, int tMin, int rMin, int bMin,
		int lMax, int tMax, int rMax, int bMax
	) {
		super(new GridLayout(1,0,8,8));
		this.lModel = new SpinnerNumberModel(l, this.lMin = lMin, this.lMax = lMax, 1);
		this.tModel = new SpinnerNumberModel(t, this.tMin = tMin, this.tMax = tMax, 1);
		this.rModel = new SpinnerNumberModel(r, this.rMin = rMin, this.rMax = rMax, 1);
		this.bModel = new SpinnerNumberModel(b, this.bMin = bMin, this.bMax = bMax, 1);
		this.setButton = new JButton("Set");
		
		add(label("L", new JSpinner(lModel)));
		add(label("T", new JSpinner(tModel)));
		add(label("R", new JSpinner(rModel)));
		add(label("B", new JSpinner(bModel)));
		add(setButton);
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RectangleSetWindow rsw = new RectangleSetWindow();
				setRectangle(rsw.setRectangle(getRectangle()));
				rsw.dispose();
			}
		});
	}
	
	public int getRectangleLeft() { return lModel.getNumber().intValue(); }
	public int getRectangleTop() { return tModel.getNumber().intValue(); }
	public int getRectangleRight() { return rModel.getNumber().intValue(); }
	public int getRectangleBottom() { return bModel.getNumber().intValue(); }
	public int getLeftMin() { return lMin; }
	public int getTopMin() { return tMin; }
	public int getRightMin() { return rMin; }
	public int getBottomMin() { return bMin; }
	public int getLeftMax() { return lMax; }
	public int getTopMax() { return tMax; }
	public int getRightMax() { return rMax; }
	public int getBottomMax() { return bMax; }
	
	public Rectangle getRectangle() {
		int l = lModel.getNumber().intValue();
		int t = tModel.getNumber().intValue();
		int r = rModel.getNumber().intValue();
		int b = bModel.getNumber().intValue();
		return new Rectangle(l, t, r - l, b - t);
	}
	
	public void setRectangleLeft(int left) { lModel.setValue(left); }
	public void setRectangleTop(int top) { tModel.setValue(top); }
	public void setRectangleRight(int right) { rModel.setValue(right); }
	public void setRectangleBottom(int bottom) { bModel.setValue(bottom); }
	
	public void setRectangle(Rectangle r) {
		if (r == null) return;
		lModel.setValue(r.x);
		tModel.setValue(r.y);
		rModel.setValue(r.x + r.width);
		bModel.setValue(r.y + r.height);
	}
	
	public void addChangeListener(ChangeListener l) {
		lModel.addChangeListener(l);
		tModel.addChangeListener(l);
		rModel.addChangeListener(l);
		bModel.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		lModel.removeChangeListener(l);
		tModel.removeChangeListener(l);
		rModel.removeChangeListener(l);
		bModel.removeChangeListener(l);
	}
	
	private static JPanel label(String label, Component comp) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.LINE_START);
		p.add(comp, BorderLayout.CENTER);
		return p;
	}
}
