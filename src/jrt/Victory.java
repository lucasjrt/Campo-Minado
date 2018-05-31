package jrt;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Victory {
	
	JFrame frame;
	JLabel lblWin;
	JButton btnContinue, btnResizeField, btnExit;
	Style style;
	
	public Victory(int width, int height, Window window) {
		frame = new JFrame("Vitória");
		btnContinue = new JButton();
		btnResizeField = new JButton();
		btnExit = new JButton();
		style = new Style();
		
		
		
	}
	
}
