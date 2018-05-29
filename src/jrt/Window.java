package jrt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {
	private int time, bombsRemaining, width, height;
	Field field;
	JFrame frame;
	JPanel pnlMain, pnlContainer1, pnlField;
	GridBagLayout layout;
	GridBagConstraints c;
	
	public Window(int width, int height) {
		this.width         = width;
		this.height        = height;
		this.time          = 0;
		this.field         = new Field(width, height);
		this.frame         = new JFrame("Campo minado");
		this.pnlMain       = new JPanel();
		this.pnlContainer1 = new JPanel();
		this.layout        = new GridBagLayout();
		this.c             = new GridBagConstraints();
		this.pnlField      = new JPanel(layout);

		buttonsAdd(field.getButtons(), pnlField);
		field.printField();
		pnlMain.add(pnlField);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(pnlField);
		frame.pack();
	}
	
	private void buttonsAdd(JButton[][] buttons, JPanel panel) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				c.gridx = j;
				c.gridy = i;
				layout.setConstraints(buttons[i][j], c);
				panel.add(buttons[i][j]);
			}
		}
	}
	
}
