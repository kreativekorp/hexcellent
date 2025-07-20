package com.kreative.templature.template.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JDialog;

public class RectangleSetWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final Marquee marquee;
	
	public RectangleSetWindow() {
		setTitle("Set Rectangle");
		setModal(true);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0,0,0,0));
		marquee = new Marquee();
		marquee.setBackground(new Color(0,0,0,0));
		marquee.setOpaque(false);
		setContentPane(marquee);
		setResizable(true);
		
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
	public Rectangle setRectangle(int left, int top, int right, int bottom) {
		accepted = false;
		setLocation(Math.min(left, right) - 10, Math.min(top, bottom) - 10);
		setSize(Math.abs(right - left) + 20, Math.abs(bottom - top) + 20);
		setVisible(true);
		if (!accepted) return null;
		Point p = getLocation();
		Dimension d = getSize();
		return new Rectangle(
			p.x + 10, p.y + 10,
			d.width - 20, d.height - 20
		);
	}
	
	public Rectangle setRectangle(Rectangle r) {
		return setRectangle(r.x, r.y, r.x + r.width, r.y + r.height);
	}
	
	private class Marquee extends JComponent {
		private static final long serialVersionUID = 1L;
		public Dimension getMinimumSize() { return new Dimension(20,20); }
		protected void paintComponent(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			if (w < 20 || h < 20) return;
			hline(g, 10, 10, w - 20);
			vline(g, 10, 10, h - 20);
			hline(g, 10, h - 11, w - 20);
			vline(g, w - 11, 10, h - 20);
			g.setColor(Color.black);
			g.fillRect(7, 7, 7, 7);
			g.fillRect((w - 7) / 2, 7, 7, 7);
			g.fillRect(w - 14, 7, 7, 7);
			g.fillRect(7, (h - 7) / 2, 7, 7);
			g.fillRect(w - 14, (h - 7) / 2, 7, 7);
			g.fillRect(7, h - 14, 7, 7);
			g.fillRect((w - 7) / 2, h - 14, 7, 7);
			g.fillRect(w - 14, h - 14, 7, 7);
			g.setColor(Color.blue);
			g.fillRect(8, 8, 5, 5);
			g.fillRect((w - 5) / 2, 8, 5, 5);
			g.fillRect(w - 13, 8, 5, 5);
			g.fillRect(8, (h - 5) / 2, 5, 5);
			g.fillRect(w - 13, (h - 5) / 2, 5, 5);
			g.fillRect(8, h - 13, 5, 5);
			g.fillRect((w - 5) / 2, h - 13, 5, 5);
			g.fillRect(w - 13, h - 13, 5, 5);
			g.setColor(Color.white);
			g.drawOval((w - 9) / 2, (h - 9) / 2, 8, 8);
			g.drawOval((w - 5) / 2, (h - 5) / 2, 4, 4);
			g.setColor(Color.black);
			g.drawOval((w - 7) / 2, (h - 7) / 2, 6, 6);
		}
	}
	
	private class Dragger implements MouseListener, MouseMotionListener {
		private DragRegion region;
		public void mousePressed(MouseEvent e) {
			Point pointerLoc = MouseInfo.getPointerInfo().getLocation();
			region = DragRegion.forPoint(e.getX(), e.getY(), getWidth(), getHeight());
			region.mousePressed(RectangleSetWindow.this, e, pointerLoc);
		}
		public void mouseDragged(MouseEvent e) {
			Point pointerLoc = MouseInfo.getPointerInfo().getLocation();
			region.mouseDragged(RectangleSetWindow.this, e, pointerLoc);
		}
		public void mouseReleased(MouseEvent e) {
			Point pointerLoc = MouseInfo.getPointerInfo().getLocation();
			region.mouseDragged(RectangleSetWindow.this, e, pointerLoc);
		}
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	private static enum DragRegion {
		TOP_LEFT {
			private int dx = 0, dy = 0, dw = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dx = p.x - rsw.getX(); dy = p.y - rsw.getY();
				dw = p.x + rsw.getWidth(); dh = p.y + rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setBounds(
					Math.min(dw - 20, p.x) - dx, Math.min(dh - 20, p.y) - dy,
					Math.max(20, dw - p.x), Math.max(20, dh - p.y)
				);
			}
		},
		TOP {
			private int x = 0, dy = 0, w = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				x = rsw.getX(); dy = p.y - rsw.getY();
				w = rsw.getWidth(); dh = p.y + rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setBounds(
					x, Math.min(dh - 20, p.y) - dy,
					w, Math.max(20, dh - p.y)
				);
			}
		},
		TOP_RIGHT {
			private int x = 0, dy = 0, dw = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				x = rsw.getX(); dy = p.y - rsw.getY();
				dw = p.x - rsw.getWidth(); dh = p.y + rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setBounds(
					x, Math.min(dh - 20, p.y) - dy,
					Math.max(20, p.x - dw), Math.max(20, dh - p.y)
				);
			}
		},
		LEFT {
			private int dx = 0, y = 0, dw = 0, h = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dx = p.x - rsw.getX(); y = rsw.getY();
				dw = p.x + rsw.getWidth(); h = rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setBounds(
					Math.min(dw - 20, p.x) - dx, y,
					Math.max(20, dw - p.x), h
				);
			}
		},
		CENTER {
			private int dx = 0, dy = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dx = p.x - rsw.getX(); dy = p.y - rsw.getY();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setLocation(p.x - dx, p.y - dy);
			}
		},
		RIGHT {
			private int dw = 0, h = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dw = p.x - rsw.getWidth(); h = rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setSize(Math.max(20, p.x - dw), h);
			}
		},
		BOTTOM_LEFT {
			private int dx = 0, y = 0, dw = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dx = p.x - rsw.getX(); y = rsw.getY();
				dw = p.x + rsw.getWidth(); dh = p.y - rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setBounds(
					Math.min(dw - 20, p.x) - dx, y,
					Math.max(20, dw - p.x), Math.max(20, p.y - dh)
				);
			}
		},
		BOTTOM {
			private int w = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				w = rsw.getWidth(); dh = p.y - rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setSize(w, Math.max(20, p.y - dh));
			}
		},
		BOTTOM_RIGHT {
			private int dw = 0, dh = 0;
			public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {
				dw = p.x - rsw.getWidth(); dh = p.y - rsw.getHeight();
			}
			public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {
				rsw.setSize(1,1);
				rsw.setSize(Math.max(20, p.x - dw), Math.max(20, p.y - dh));
			}
		};
		public static DragRegion forPoint(int x, int y, int width, int height) {
			int hw = Math.min(20, width / 3);
			int hh = Math.min(20, height / 3);
			if (y < hh) {
				if (x < hw) return TOP_LEFT;
				if (x < width - hw) return TOP;
				return TOP_RIGHT;
			} else if (y < height - hh) {
				if (x < hw) return LEFT;
				if (x < width - hw) return CENTER;
				return RIGHT;
			} else {
				if (x < hw) return BOTTOM_LEFT;
				if (x < width - hw) return BOTTOM;
				return BOTTOM_RIGHT;
			}
		}
		public void mousePressed(RectangleSetWindow rsw, MouseEvent e, Point p) {}
		public void mouseDragged(RectangleSetWindow rsw, MouseEvent e, Point p) {}
	}
	
	private static void hline(Graphics g, int x, int y, int width) {
		g.setColor(Color.black);
		for (int i = x; i < x + width; i += 8) {
			g.fillRect(i, y, Math.min(4, x + width - i), 1);
		}
		g.setColor(Color.white);
		for (int i = x + 4; i < x + width; i += 8) {
			g.fillRect(i, y, Math.min(4, x + width - i), 1);
		}
	}
	
	private static void vline(Graphics g, int x, int y, int height) {
		g.setColor(Color.black);
		for (int j = y; j < y + height; j += 8) {
			g.fillRect(x, j, 1, Math.min(4, y + height - j));
		}
		g.setColor(Color.white);
		for (int j = y + 4; j < y + height; j += 8) {
			g.fillRect(x, j, 1, Math.min(4, y + height - j));
		}
	}
}
