package com.kreative.templature.test;

import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import com.kreative.templature.template.ui.swing.RectangleSetWindow;

public class RectangleSetWindowTest {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				RectangleSetWindow rsw = new RectangleSetWindow();
				System.out.println(rsw.setRectangle(new Rectangle(100,100,100,100)));
				rsw.dispose();
			}
		});
	}
}
