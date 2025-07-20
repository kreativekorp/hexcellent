package com.kreative.templature.test;

import javax.swing.SwingUtilities;
import com.kreative.templature.template.ui.swing.PointSetWindow;

public class PointSetWindowTest {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PointSetWindow psw = new PointSetWindow();
				System.out.println(psw.setPoint(100, 100));
				psw.dispose();
			}
		});
	}
}
