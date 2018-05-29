package jrt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOver {
	private JButton btnNewGame, btnResizeField, btnExit;
	private JFrame frame;
	private JLabel lblYouLose;
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints c;
	private Style style;
	
	public GameOver() {
		panel = new JPanel();
		frame = new JFrame("Game Over");
		lblYouLose = new JLabel("Fim de jogo, você perdeu");
		btnNewGame = new JButton("Novo jogo");
		btnResizeField = new JButton("Escolher um novo tamanho");
		btnExit = new JButton("Sair");
		style = new Style();
	}
	
	public void open() {
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
	}
	
	private void prepareLayout(GridBagLayout layout, GridBagConstraints c) {
		
	}
}
