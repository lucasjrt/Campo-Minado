package jrt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {
	private int width, height;
	Field field;
	JFrame frame;
	JPanel pnlMain, pnlTop, pnlField;
	ScorePanel scorePanel;
	GridBagLayout lytField;
	GridBagConstraints c;
	GroupLayout lytMain;
	
	public Window(int width, int height) {
		this.width = width;
		this.height = height;
		frame = new JFrame("Campo minado");
		field = new Field(width, height, this);
		pnlMain = new JPanel();
		pnlTop = new JPanel();
		lytField = new GridBagLayout();
		lytMain = new GroupLayout(pnlMain);
		c = new GridBagConstraints();
		pnlField = new JPanel(lytField);
		scorePanel = new ScorePanel(this);
		pnlMain.setLayout(lytMain);

		lytMain.setHorizontalGroup(lytMain.createParallelGroup()
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		lytMain.setVerticalGroup(lytMain.createSequentialGroup()
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		
		buttonsAdd(field.getButtons(), pnlField);
		field.printField();
		pnlMain.add(scorePanel);
		pnlMain.add(pnlField);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(pnlMain);
		frame.pack();
	}
	
	private void buttonsAdd(JButton[][] buttons, JPanel panel) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				c.gridx = j;
				c.gridy = i;
				lytField.setConstraints(buttons[i][j], c);
				panel.add(buttons[i][j]);
			}
		}
	}
}
