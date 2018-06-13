package jrt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class Window {
	public boolean questionMarkEnabled;
	private int width, height;
	Field field;
	JFrame frame;
	JPanel pnlMain, pnlTop, pnlField;
	JMenuBar menuBar;
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
		menuBar = new JMenuBar();

		lytMain.setHorizontalGroup(lytMain.createParallelGroup()
				//.addComponent(menuBar)
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		lytMain.setVerticalGroup(lytMain.createSequentialGroup()
				//.addComponent(menuBar)
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		
		prepareMenu();
		buttonsAdd(field.getButtons(), pnlField);
		field.printField();
		pnlMain.add(scorePanel);
		pnlMain.add(pnlField);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(field.bombTile);
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
	
	private void prepareMenu() {
		frame.setJMenuBar(menuBar);
		JMenu gameMenu = new JMenu("Jogo");
		JMenu helpMenu = new JMenu("Ajuda");
		JMenuItem newGame = new JMenuItem("Novo Jogo");
		JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Fácil");
		JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Intermediário");
		JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Experiente");
		JRadioButtonMenuItem custom = new JRadioButtonMenuItem("Personalizado");
		JCheckBoxMenuItem questionMark = new JCheckBoxMenuItem("Marcas (?)");
		JMenuItem exit = new JMenuItem("Sair");
		JMenuItem content = new JMenuItem("Conteúdo");
		JMenuItem about = new JMenuItem("Sobre");
		ButtonGroup buttonGroup = new ButtonGroup();
		
		buttonGroup.add(easy);
		buttonGroup.add(medium);
		buttonGroup.add(hard);
		buttonGroup.add(custom);
		easy.setSelected(true);
		questionMark.setSelected(true);
		
		gameMenu.add(newGame);
		gameMenu.addSeparator();
		gameMenu.add(easy);
		gameMenu.add(medium);
		gameMenu.add(hard);
		gameMenu.add(custom);
		gameMenu.addSeparator();
		gameMenu.add(questionMark);
		gameMenu.addSeparator();
		gameMenu.add(exit);
		
		helpMenu.add(content);
		helpMenu.addSeparator();
		helpMenu.add(about);
		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				field.resetGame();
			}
		});
		
		questionMark.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(questionMark.isSelected()) {
					questionMarkEnabled = true;
				} else {
					questionMarkEnabled = false;
				}
				System.out.println(questionMarkEnabled);
			}
		});
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
}
