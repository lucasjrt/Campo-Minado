package jrt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {
	private int displayWidth, displayHeight, faceSize, remainingFlags, time;
	private double scale;
	private BorderLayout layout;
	private BufferedImage displayImage, faces, clickedFaces;
	private BufferedImage display[];
	private Window window;
	private JButton btnFace;
	private JPanel pnlTime, pnlBombs, pnlFace;
	
	
	private Style style;
	public Timer timer;
	public ImageIcon happyFace, glassFace, deadFace, wowFace;
	public ImageIcon clickedHappyFace, clickedDeadFace, clickedGlassFace;
	public JLabel lblHundredsTime, lblTensTime, lblOnesTime, lblHundredsBombs, lblTensBombs, lblOnesBombs;
	
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
			clickedFaces = ImageIO.read(new File("res/clicked_tiles.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		for(int i = 0; i < 10; i++) {
			display[i] = style.resize(displayImage.getSubimage(i * 26, 0, 26, 46), displayHeight, displayWidth);
		}
		
		happyFace = new ImageIcon(style.resize(faces.getSubimage(0, 0, 32, 32), faceSize, faceSize));
		glassFace = new ImageIcon(style.resize(faces.getSubimage(32, 0, 32, 32), faceSize, faceSize));
		deadFace = new ImageIcon(style.resize(faces.getSubimage(0, 32, 32, 32), faceSize, faceSize));
		clickedHappyFace = new ImageIcon(style.resize(clickedFaces.getSubimage(32, 0, 32, 32), faceSize, faceSize));
		clickedDeadFace = new ImageIcon(style.resize(clickedFaces.getSubimage(0, 32, 32, 32), faceSize, faceSize));
		clickedGlassFace = new ImageIcon(style.resize(clickedFaces.getSubimage(32, 32, 32, 32), faceSize, faceSize));
		prepareFace();
		
		btnFace.addMouseListener(new MouseListener() {
			private boolean isPressed = false;
			@Override
			public void mouseReleased(MouseEvent e) {
				isPressed = false;
				if(btnFace.getIcon() == clickedHappyFace)
					btnFace.setIcon(happyFace);
				else if(btnFace.getIcon() == clickedDeadFace)
					btnFace.setIcon(deadFace);
				else if(btnFace.getIcon() == clickedGlassFace) {
					if(isMouseIn(e,btnFace))
						btnFace.setIcon(happyFace);
					else
						btnFace.setIcon(glassFace);
				}
				if(isMouseIn(e, btnFace)) {
					window.field.resetGame();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				isPressed = true;
				if(btnFace.getIcon() == happyFace)
					btnFace.setIcon(clickedHappyFace);
				else if(btnFace.getIcon() == deadFace)
					btnFace.setIcon(clickedDeadFace);
				else if(btnFace.getIcon() == glassFace)
					btnFace.setIcon(clickedGlassFace);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(isPressed) {
					if(btnFace.getIcon() == clickedHappyFace) 
						btnFace.setIcon(happyFace);
					else if(btnFace.getIcon() == clickedDeadFace)
						btnFace.setIcon(deadFace);
					else if(btnFace.getIcon() == clickedGlassFace)
						btnFace.setIcon(glassFace);
				}
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(isPressed) {
					if(btnFace.getIcon() == happyFace)
						btnFace.setIcon(clickedHappyFace);
					else if(btnFace.getIcon() == deadFace)
						btnFace.setIcon(clickedDeadFace);
					else if(btnFace.getIcon() == glassFace)
						btnFace.setIcon(clickedGlassFace);
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
		
		this.add(pnlBombs, BorderLayout.WEST);
		this.add(pnlFace, BorderLayout.CENTER);
		this.add(pnlTime, BorderLayout.EAST);
	}
	
	protected boolean isMouseIn(MouseEvent e, Component component) {
		if(e.getXOnScreen() >= component.getLocationOnScreen().getX() &&
				e.getXOnScreen() <= component.getLocationOnScreen().getX() + component.getWidth() &&
				e.getYOnScreen() >= component.getLocationOnScreen().getY() &&
				e.getYOnScreen() <= component.getLocationOnScreen().getY() + component.getWidth())
			return true;
		return false;
	}

	public void setDisplayLabel(JLabel label, int index) {
		label.setIcon(new ImageIcon(display[index]));
	}
	
	public void increaseTimer() {
		
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
	
	public void resetTimer() {
		if(timer.isAlive())
			timer.stopCounting();
		time = 0;
		timer = new Timer(this);
		setDisplayLabel(lblHundredsBombs, 0);
		setDisplayLabel(lblTensTime, 0);
		setDisplayLabel(lblOnesTime, 0);
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
	private boolean timerRunning;
	ScorePanel panel;
	int time;
	
	public Timer(ScorePanel panel) {
		this.panel = panel;
		timerRunning = false;
		time = 0;
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
		while(timerRunning) {
			long delta = System.currentTimeMillis() - startTime;
			long seconds = delta / 1000;
			long secondsDisplay = seconds % 60;
			if(delta % 1000 == 0 && secondsDisplay != last) {
				last = secondsDisplay;
				time++;
				panel.setDisplayLabel(panel.lblHundredsTime, (int) (time / 100));
				panel.setDisplayLabel(panel.lblTensTime, (int) (time % 100) / 10);
				panel.setDisplayLabel(panel.lblOnesTime, (int) (time % 10));
			}
		}
	}
	
	public void begin() {
		timerRunning = true;
		this.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stopCounting() {
		this.stop();
		timerRunning = false;
	}
}