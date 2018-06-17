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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class Window {
	public int buttonSize, displayWidth, displayHeight, faceSize;
	public Field field;
	public ScorePanel scorePanel;
	public Images images;
	private int width, height;
	private JFrame frame;
	private JPanel pnlMain, pnlField;
	private JMenuBar menuBar;
	private GridBagLayout lytField;
	private GridBagConstraints c;
	private GroupLayout lytMain;
	private float scale;
	
	public Window(int width, int height, int bombs, int difficulty, boolean questionMark) {
		buttonSize = 28;
		scale = 1.1f;
		displayWidth = (int) (26/scale);
		displayHeight = (int) (46/scale);
		faceSize = 32;
		images = new Images(this);
		this.width = width;
		this.height = height;
		frame = new JFrame("Campo minado");
		field = new Field(width, height, this, bombs);
		pnlMain = new JPanel();
		lytField = new GridBagLayout();
		lytMain = new GroupLayout(pnlMain);
		c = new GridBagConstraints();
		pnlField = new JPanel(lytField);
		scorePanel = new ScorePanel(this, faceSize);
		pnlMain.setLayout(lytMain);
		menuBar = new JMenuBar();
		field.setQuestionMarkTile(questionMark);

		lytMain.setHorizontalGroup(lytMain.createParallelGroup()
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		lytMain.setVerticalGroup(lytMain.createSequentialGroup()
				.addComponent(scorePanel)
				.addComponent(pnlField));
		
		
		prepareMenu(difficulty);
		buttonsAdd(field.getButtons(), pnlField);
		field.printField();
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(images.happyFace.getImage());
		frame.add(pnlMain);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
	
	private void prepareMenu(int difficulty) {
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
		if(field.isQuestionMarkEnabled())
			questionMark.setSelected(true);;
		switch(difficulty) {
		case 1:
			easy.setSelected(true);
			break;
		case 2:
			medium.setSelected(true);
			break;
		case 3:
			hard.setSelected(true);
			break;
		case 4:
			custom.setSelected(true);
			break;
		}
		
		
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
		
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					this.finalize();
					frame.setVisible(false);
					new Window(9,9,10, 1, field.isQuestionMarkEnabled());
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		});
		
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					this.finalize();
					frame.setVisible(false);
					new Window(16, 16, 40, 2, field.isQuestionMarkEnabled());
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		});
		
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					this.finalize();
					frame.setVisible(false);
					new Window(30, 16, 99, 3, field.isQuestionMarkEnabled());
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		});
		
		custom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					this.finalize();
					frame.setVisible(false);
					new Game();
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		});
		
		questionMark.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(questionMark.isSelected()) {
					field.setQuestionMarkTile(true);
				} else {
					field.setQuestionMarkTile(false);
					for(int i = 0; i < height; i++) {
						for(int j = 0; j < width; j++) {
							if(field.getField()[i][j] == Property.BOMB_QUESTION) {
								field.getField()[i][j] = Property.BOMB_UNOPENED;
								field.getButtons()[i][j].setIcon(images.unopenedTile);
							} else if (field.getField()[i][j] == Property.CLEAR_QUESTION) {
								field.getField()[i][j] = Property.CLEAR_UNOPENED;
								field.getButtons()[i][j].setIcon(images.unopenedTile);
							}
						}
					}
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		content.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						if(field.getField()[i][j] == Property.BOMB_UNOPENED || field.getField()[i][j] == Property.CLEAR_UNOPENED)
							field.getButtons()[i][j].setIcon(images.happyFace);
					}
				}
			}
		});
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, 
						"Desenvolvido por: \n"
						+ "             *Lucas Justino\n"
						+ "             *Tarcisio Junio\n"
						+ "Alunos de Ciência da computação na\n"
						+ "Universidade Federal de Uberlândia - UFU\n",
						"Sobre", JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
}
