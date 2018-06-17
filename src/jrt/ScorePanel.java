package jrt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {
	private int remainingFlags, faceSize;
	private BorderLayout layout;
	private Window window;
	private JButton btnFace;
	private JPanel pnlTime, pnlBombs, pnlFace;
	
	public Timer timer;
	public JLabel lblHundredsTime, lblTensTime, lblOnesTime, lblHundredsBombs, lblTensBombs, lblOnesBombs;
	
	public ScorePanel(Window window, int faceSize) {
		this.window = window;
		remainingFlags = window.field.getNumBombs();
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
		this.faceSize = faceSize;

		prepareFace();

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

	private boolean isMouseIn(MouseEvent e, Component component) {
		if(e.getXOnScreen() >= component.getLocationOnScreen().getX() &&
				e.getXOnScreen() <= component.getLocationOnScreen().getX() + component.getWidth() &&
				e.getYOnScreen() >= component.getLocationOnScreen().getY() &&
				e.getYOnScreen() <= component.getLocationOnScreen().getY() + component.getWidth())
			return true;
		return false;
	}

	public void setDisplayLabel(JLabel label, int index) {
		label.setIcon(window.images.display[index]);
	}
		
	public void setFace(ImageIcon icon) {
		btnFace.setIcon(icon);
	}
	
	public int getRemainingFlags() {
		return remainingFlags;
	}
	
	public void setRemainingFlags(int remainingFlags) {
		this.remainingFlags = remainingFlags;
		if(window.field.getNumBombs() > 100)
			setDisplayLabel(lblHundredsBombs, (int) (remainingFlags / 100));
		setDisplayLabel(lblTensBombs, (int) ((remainingFlags % 100) / 10));
		setDisplayLabel(lblOnesBombs, (int) (remainingFlags % 10));
	}
	
	public void resetTimer() {
		if(timer.isAlive())
			timer.stopCounting();
		timer = new Timer(this);
		setDisplayLabel(lblHundredsTime, 0);
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
		btnFace.setIcon(window.images.happyFace);
		btnFace.addMouseListener(new MouseListener() {
			private boolean isPressed = false;
			@Override
			public void mouseReleased(MouseEvent e) {
				isPressed = false;
				if(btnFace.getIcon() == window.images.clickedHappyFace)
					btnFace.setIcon(window.images.happyFace);
				else if(btnFace.getIcon() == window.images.clickedDeadFace)
					btnFace.setIcon(window.images.deadFace);
				else if(btnFace.getIcon() == window.images.clickedGlassFace) {
					if(isMouseIn(e,btnFace))
						btnFace.setIcon(window.images.happyFace);
					else
						btnFace.setIcon(window.images.glassFace);
				}
				if(isMouseIn(e, btnFace)) {
					window.field.resetGame();
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				isPressed = true;
				if(btnFace.getIcon() == window.images.happyFace)
					btnFace.setIcon(window.images.clickedHappyFace);
				else if(btnFace.getIcon() == window.images.deadFace)
					btnFace.setIcon(window.images.clickedDeadFace);
				else if(btnFace.getIcon() == window.images.glassFace)
					btnFace.setIcon(window.images.clickedGlassFace);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(isPressed) {
					if(btnFace.getIcon() == window.images.clickedHappyFace)
						btnFace.setIcon(window.images.happyFace);
					else if(btnFace.getIcon() == window.images.clickedDeadFace)
						btnFace.setIcon(window.images.deadFace);
					else if(btnFace.getIcon() == window.images.clickedGlassFace)
						btnFace.setIcon(window.images.glassFace);
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(isPressed) {
					if(btnFace.getIcon() == window.images.happyFace)
						btnFace.setIcon(window.images.clickedHappyFace);
					else if(btnFace.getIcon() == window.images.deadFace)
						btnFace.setIcon(window.images.clickedDeadFace);
					else if(btnFace.getIcon() == window.images.glassFace)
						btnFace.setIcon(window.images.clickedGlassFace);
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});

	}
	
	public void setTimerDisplayValue(int i) {
		setDisplayLabel(lblHundredsTime, (int) (i / 100));
		setDisplayLabel(lblTensTime, (int) (i % 100) / 10);
		setDisplayLabel(lblOnesTime, (int) (i % 10));
	}
}

class Timer extends Thread implements Runnable{
	private boolean timerRunning;
	private ScorePanel panel;
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