package jrt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
	//TODO    v    remove static from here
	private static final long serialVersionUID = 1L;
	int displayWidth, displayHeight, faceSize, remainingFlags, time;
	double scale;
	BorderLayout layout;
	BufferedImage displayImage, faces;
	BufferedImage display[];
	Window window;
	Timer timer;
	public ImageIcon happyFace, glassFace, deadFace;
	JLabel lblHundredsTime, lblTensTime, lblOnesTime, lblHundredsBombs, lblTensBombs, lblOnesBombs;
	JButton btnFace;
	JPanel pnlTime, pnlBombs, pnlFace;
	Style style;
	
	public ScorePanel(Window window) {
		this.window = window;
		time = 0;
		scale = 1.1;
		displayWidth = (int) (26/scale);
		displayHeight = (int) (46/scale);
		faceSize = 32;
		remainingFlags = window.field.getNumBombs();
		display = new BufferedImage[10];
		style = new Style();
		pnlTime = new JPanel();
		pnlBombs = new JPanel();
		pnlFace = new JPanel();
		btnFace = new JButton();
		lblHundredsTime = new JLabel();
		lblTensTime = new JLabel();
		lblOnesTime = new JLabel();
		lblHundredsBombs = new JLabel();
		lblTensBombs = new JLabel();
		lblOnesBombs = new JLabel();
		timer = new Timer(this);
		layout = new BorderLayout();
		
		try {
			displayImage = ImageIO.read(new File("res/seven_segment_display.jpg"));
			faces = ImageIO.read(new File("res/extra_tiles.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		for(int i = 0; i < 10; i++) {
			display[i] = style.resize(displayImage.getSubimage(i * 26, 0, 26, 46), displayHeight, displayWidth);
		}
		
		happyFace = new ImageIcon(style.resize(faces.getSubimage(0, 0, 32, 32), faceSize, faceSize));
		glassFace = new ImageIcon(style.resize(faces.getSubimage(32, 0, 32, 32), faceSize, faceSize));
		deadFace = new ImageIcon(style.resize(faces.getSubimage(0, 32, 32, 32), faceSize, faceSize));
		
		prepareFace();
		
		btnFace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!timer.isGameOver()) {
					confirmDialog();
				} else {
					window.field.resetGame();
				}
			}
		});
		
		setRemainingFlags(remainingFlags);
		
		setDisplayLabel(lblHundredsTime, 0);
		setDisplayLabel(lblTensTime, 0);
		setDisplayLabel(lblOnesTime, 0);
		
		this.setLayout(layout);
		pnlTime.setLayout(new GridLayout(1,3));
		pnlBombs.setLayout(new GridLayout(1, 3));
		
		pnlBombs.add(lblHundredsBombs);
		pnlBombs.add(lblTensBombs);
		pnlBombs.add(lblOnesBombs);
		
		pnlFace.add(btnFace);
		
		pnlTime.add(lblHundredsTime);
		pnlTime.add(lblTensTime);
		pnlTime.add(lblOnesTime);
		
		updateTimer();
		
		this.add(pnlBombs, BorderLayout.WEST);
		this.add(pnlFace, BorderLayout.CENTER);
		this.add(pnlTime, BorderLayout.EAST);
	}
	
	public void setDisplayLabel(JLabel label, int index) {
		label.setIcon(new ImageIcon(display[index]));
	}
	
	public void increaseTimer() {
		time++;
		setDisplayLabel(lblHundredsTime, (int) (time / 100));
		setDisplayLabel(lblTensTime, (int) (time % 100) / 10);
		setDisplayLabel(lblOnesTime, (int) (time % 10));
	}
	
	public int getTime() {
		return time;
	}
	
	public void setFace(ImageIcon icon) {
		btnFace.setIcon(icon);
	}
	
	public int getRemainingFlags() {
		return remainingFlags;
	}
	
	public void setRemainingFlags(int remainingFlags) {
		this.remainingFlags = remainingFlags;
		if((int) (remainingFlags / 100) == 1) {
			setDisplayLabel(lblHundredsBombs, 1);
			pnlBombs.add(lblHundredsBombs);
		}
		setDisplayLabel(lblTensBombs, (int) ((remainingFlags % 100) / 10));
		setDisplayLabel(lblOnesBombs, (int) (remainingFlags % 10));
	}
	
	private void updateTimer() {
		timer.begin();
	}
	
	public void resetTimer() {
		time = 0;
		timer = new Timer(this);
		timer.setGameOver(false);
		setDisplayLabel(lblHundredsBombs, 0);
		setDisplayLabel(lblTensTime, 0);
		setDisplayLabel(lblOnesTime, 0);
		updateTimer();
	}
	
	public void resetBombsDisplay() { 
		setRemainingFlags(window.field.getNumBombs());
	}
	
	private void prepareFace() {
		btnFace.setPreferredSize(new Dimension(faceSize, faceSize));
		btnFace.setSize(new Dimension(faceSize, faceSize));
		btnFace.setMaximumSize(new Dimension(faceSize, faceSize));
		btnFace.setMinimumSize(new Dimension(faceSize, faceSize));
		btnFace.setBorderPainted(false);
		btnFace.setBorder(null);
		btnFace.setFocusable(false);
		btnFace.setMargin(new Insets(0, 0, 0, 0));
		btnFace.setContentAreaFilled(false);
		btnFace.setIcon(happyFace);
	}
	
	private void confirmDialog() {
		JFrame frame = new JFrame("Aviso");
		JLabel lblWarning = new JLabel("Você tem certeza que deseja iniciar um novo jogo?");
		JPanel panel = new JPanel();
		JButton btnConfirm, btnCancel;
		btnConfirm = new JButton("Confirmar");
		btnCancel = new JButton("Cancelar");
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(lblWarning)
				.addGroup(layout.createSequentialGroup()
						.addComponent(btnCancel)
						.addComponent(btnConfirm)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(lblWarning)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(btnCancel)
						.addComponent(btnConfirm)));
		
		style.prepareButton(btnCancel);
		style.prepareButton(btnConfirm);
		style.prepareLabel(lblWarning);
		style.preparePanel(panel);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel.add(lblWarning);
		panel.add(btnCancel);
		panel.add(btnConfirm);
		frame.add(panel);
		frame.pack();
		
		btnConfirm.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				window.scorePanel.timer.setGameOver(true);
				window.field.resetGame();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
	}
	
	public void setTimerDisplayValue(int i) {
		setDisplayLabel(lblHundredsTime, (int) (i / 100));
		setDisplayLabel(lblTensTime, (int) (i % 100) / 10);
		setDisplayLabel(lblOnesTime, (int) (i % 10));
	}
	
	public int getTimerDisplayValue() {
		return time;
	}
}

class Timer extends Thread implements Runnable{
	boolean gameOver;
	ScorePanel panel;
	
	public Timer(ScorePanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void run() {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		long last = startTime;
		while(!gameOver) {
			long delta = System.currentTimeMillis() - startTime;
			long seconds = delta / 1000;
			long secondsDisplay = seconds % 60;
			if(delta % 1000 == 0 && secondsDisplay != last) {
				last = secondsDisplay;
				panel.increaseTimer();
			}
		}
	}
	
	public void begin() {
		this.start();
	}
	
	public void setGameOver(boolean isGameOver) {
		gameOver = isGameOver;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
}