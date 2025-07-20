package com.kreative.templature.template.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class Point2DEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int xMin, yMin, xMax, yMax;
	private final SpinnerNumberModel xModel;
	private final SpinnerNumberModel yModel;
	private final JButton setButton;
	
	public Point2DEditor(int x, int y, int xMin, int yMin, int xMax, int yMax) {
		super(new GridLayout(1,0,8,8));
		this.xModel = new SpinnerNumberModel(x, this.xMin = xMin, this.xMax = xMax, 1);
		this.yModel = new SpinnerNumberModel(y, this.yMin = yMin, this.yMax = yMax, 1);
		this.setButton = new JButton("Set");
		
		add(label("X", new JSpinner(xModel)));
		add(label("Y", new JSpinner(yModel)));
		add(setButton);
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PointSetWindow psw = new PointSetWindow();
				setPoint(psw.setPoint(getPoint()));
				psw.dispose();
			}
		});
	}
	
	public int getPointX() { return xModel.getNumber().intValue(); }
	public int getPointY() { return yModel.getNumber().intValue(); }
	public int getXMin() { return xMin; }
	public int getYMin() { return yMin; }
	public int getXMax() { return xMax; }
	public int getYMax() { return yMax; }
	
	public Point getPoint() {
		return new Point(xModel.getNumber().intValue(), yModel.getNumber().intValue());
	}
	
	public void setPointX(int x) { xModel.setValue(x); }
	public void setPointY(int y) { yModel.setValue(y); }
	
	public void setPoint(Point p) {
		if (p == null) return;
		xModel.setValue(p.x);
		yModel.setValue(p.y);
	}
	
	public void addChangeListener(ChangeListener l) {
		xModel.addChangeListener(l);
		yModel.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		xModel.removeChangeListener(l);
		yModel.removeChangeListener(l);
	}
	
	private static JPanel label(String label, Component comp) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.LINE_START);
		p.add(comp, BorderLayout.CENTER);
		return p;
	}
}
