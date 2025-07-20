package com.kreative.templature.template.ui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JDialog;

public class PointSetWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final Reticle reticle;
	
	public PointSetWindow() {
		setTitle("Set Point");
		setModal(true);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0,0,0,0));
		reticle = new Reticle();
		reticle.setBackground(new Color(0,0,0,0));
		reticle.setOpaque(false);
		setContentPane(reticle);
		setSize(41,41);
		setResizable(false);
		
		Dragger dragger = new Dragger();
		addMouseListener(dragger);
		addMouseMotionListener(dragger);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_ENTER:
					case KeyEvent.VK_SPACE:
					case KeyEvent.VK_INSERT:
						accepted = true;
						setVisible(false);
						break;
					case KeyEvent.VK_ESCAPE:
					case KeyEvent.VK_BACK_SPACE:
					case KeyEvent.VK_DELETE:
						accepted = false;
						setVisible(false);
						break;
				}
			}
		});
	}
	
	private boolean accepted;
	public Point setPoint(int x, int y) {
		accepted = false;
		setLocation(x - 20, y - 20);
		setVisible(true);
		if (!accepted) return null;
		Point p = getLocation();
		p.x += 20; p.y += 20;
		return p;
	}
	
	public Point setPoint(Point p) {
		return setPoint(p.x, p.y);
	}
	
	private class Reticle extends JComponent {
		private static final long serialVersionUID = 1L;
		public Dimension getMinimumSize() { return new Dimension(41,41); }
		public Dimension getPreferredSize() { return new Dimension(41,41); }
		public Dimension getMaximumSize() { return new Dimension(41,41); }
		protected void paintComponent(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(19, 0, 3, 19);
			g.fillRect(0, 19, 19, 3);
			g.fillRect(22, 19, 19, 3);
			g.fillRect(19, 22, 3, 19);
			if (g instanceof Graphics2D) ((Graphics2D)g).setStroke(new BasicStroke(3));
			g.drawOval(10, 10, 20, 20);
			g.setColor(Color.black);
			g.fillRect(20, 0, 1, 19);
			g.fillRect(0, 20, 19, 1);
			g.fillRect(22, 20, 19, 1);
			g.fillRect(20, 22, 1, 19);
			if (g instanceof Graphics2D) ((Graphics2D)g).setStroke(new BasicStroke(1));
			g.drawOval(10, 10, 20, 20);
		}
	}
	
	private class Dragger implements MouseListener, MouseMotionListener {
		private int dx = 0, dy = 0;
		public void mousePressed(MouseEvent e) {
			Point pointerLoc = MouseInfo.getPointerInfo().getLocation();
			Point windowLoc = getLocation();
			dx = pointerLoc.x - windowLoc.x;
			dy = pointerLoc.y - windowLoc.y;
		}
		public void mouseDragged(MouseEvent e) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			setLocation(p.x - dx, p.y - dy);
		}
		public void mouseReleased(MouseEvent e) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			setLocation(p.x - dx, p.y - dy);
		}
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
}
