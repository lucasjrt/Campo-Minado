package jrt;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
	//TODO    v    remove static from here
	private static final long serialVersionUID = 1L;
	int displayWidth, displayHeight, faceSize, remainingFlags, time;
	double scale;
	BufferedImage displayImage, faces;
	BufferedImage display[];
	Field field;
	Timer timer;
	public ImageIcon happyFace, glassFace, deadFace;
	JLabel lblHundredsTime, lblTensTime, lblOnesTime, lblFace, lblHundredsBombs, lblTensBombs, lblOnesBombs;
	JPanel pnlTime, pnlBombs, pnlFace;
	Style style;
	
	public ScorePanel(Field field) {
		this.field = field;
		time = 0;
		scale = 1.1;
		displayWidth = (int) (26/scale);
		displayHeight = (int) (46/scale);
		faceSize = 32;
		remainingFlags = field.getNumBombs();
		display = new BufferedImage[10];
		style = new Style();
		pnlTime = new JPanel();
		pnlBombs = new JPanel();
		pnlFace = new JPanel();
		lblHundredsTime = new JLabel();
		lblTensTime = new JLabel();
		lblOnesTime = new JLabel();
		lblFace = new JLabel();
		lblHundredsBombs = new JLabel();
		lblTensBombs = new JLabel();
		lblOnesBombs = new JLabel();
		timer = new Timer(this);
		
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
		
		setRemainingFlags(remainingFlags);
		
		setDisplayLabel(lblHundredsTime, 0);
		setDisplayLabel(lblTensTime, 0);
		setDisplayLabel(lblOnesTime, 0);
		
		lblFace.setIcon(happyFace);
		
		this.setLayout(new BorderLayout());
		pnlTime.setLayout(new GridLayout(1,3));
		pnlBombs.setLayout(new GridLayout(1, 3));
		
		pnlBombs.add(lblHundredsBombs);
		pnlBombs.add(lblTensBombs);
		pnlBombs.add(lblOnesBombs);
		
		pnlFace.add(lblFace);
		
		pnlTime.add(lblHundredsTime);
		pnlTime.add(lblTensTime);
		pnlTime.add(lblOnesTime);
		
		updateTimer();
		
		this.add(pnlBombs, BorderLayout.LINE_START);
		this.add(pnlFace, BorderLayout.CENTER);
		this.add(pnlTime, BorderLayout.LINE_END);
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
	
	public void setFace(ImageIcon icon) {
		lblFace.setIcon(icon);
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
}