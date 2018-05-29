
package jrt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOver {
	private int width, height;
	private JButton btnNewGame, btnResizeField, btnExit;
	private JFrame frame;
	private JLabel lblYouLose;
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints c;
	private JFrame windowFrame;
	private Style style;
	
	public GameOver(int width, int height, JFrame windowFrame) {
		this.width = width;
		this.height = height;
		this.windowFrame = windowFrame;
		this.panel = new JPanel();
		this.frame = new JFrame("Game Over");
		this.lblYouLose = new JLabel("Fim de jogo, você perdeu");
		this.btnNewGame = new JButton("Novo jogo");
		this.btnResizeField = new JButton("Escolher um novo tamanho");
		this.btnExit = new JButton("Sair");
		this.style = new Style();
		this.layout = new GridBagLayout();
		this.c = new GridBagConstraints();
	}
	
	public void open() {
		windowFrame.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {
				frame.toFront();
			}
		});
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		style.preparePanel(panel);
		style.prepareLabel(lblYouLose);
		style.prepareButton(btnNewGame);
		style.prepareButton(btnResizeField);
		style.prepareButton(btnExit);
		prepareLayout(layout,c);
		frame.add(panel);
		frame.pack();
		
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				windowFrame.setVisible(false);
				frame.setVisible(false);
				frame.setAlwaysOnTop(true);
				new Window(width, height);
			}
		});
		
		btnResizeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				windowFrame.setVisible(false);
				frame.setVisible(false);
				new Game();
			}
		});
	}
	
	private void prepareLayout(GridBagLayout layout, GridBagConstraints c) {
		panel.setLayout(layout);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		layout.setConstraints(lblYouLose, c);
		panel.add(lblYouLose, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		layout.setConstraints(btnNewGame, c);
		panel.add(btnNewGame, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		layout.setConstraints(btnResizeField, c);
		panel.add(btnResizeField, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 1;
		layout.setConstraints(btnExit, c);
		panel.add(btnExit, c);
	}
}
