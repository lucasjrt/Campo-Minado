package jrt;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Field {
	private BufferedImage tiles, unopenedTile, openedTile, flagTile, bombTile, clickedBombTile, wrongFlagTile;
	private BufferedImage[] numberTiles;
	private Window window;
	private Tile buttons[][];
	private Property field[][];
	private Style style;
	private int buttonSize, numBombs, width, height, flaggedBombs;
	
	public Field(int width, int height, Window window) {
		this.width = width;
		this.height = height;
		this.window = window;
		flaggedBombs = 0;
		buttonSize = 28;
		//numBombs = (int) (width * height * .15);
		numBombs = 1;
		field = new Property[height][width]; 
		buttons = new Tile[height][width];
		numberTiles = new BufferedImage[9];
		style = new Style();
		createField();
		createBombs();
		prepareButtons(buttons);
	}
	
	private void createField() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buttons[i][j] = new Tile(i, j);
				field[i][j] = Property.CLEAR_UNOPENED;
			}
		}
		System.out.println("Field created");
	}
	
	public void createBombs() {
		Random random = new Random();
		int i, j, numBombs = 0;
		while (numBombs < this.numBombs) {
			i = random.nextInt(height);
			j = random.nextInt(width);
			if(field[i][j] == Property.CLEAR_UNOPENED) {
				field[i][j] = Property.BOMB_UNOPENED;
				numBombs++;
			}
		}
		System.out.println(numBombs + " bombs created");
	}
	
	private void prepareButtons(Tile[][] buttons) {
		
		try {
			tiles = ImageIO.read(new File("res/minesweeper_tiles.jpg"));
			clickedBombTile = ImageIO.read(new File("res/clicked_bomb_tile.jpg"));
			wrongFlagTile = style.resize(ImageIO.read(new File("res/extra_tiles.jpg")).getSubimage(32, 32, 32, 32), buttonSize, buttonSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		unopenedTile = style.resize(tiles.getSubimage(0, 0, 128, 128), buttonSize, buttonSize);
		flagTile = style.resize(tiles.getSubimage(128, 0, 128, 128), buttonSize, buttonSize);
		bombTile = style.resize(tiles.getSubimage(256, 0, 128, 128), buttonSize, buttonSize);
		openedTile = style.resize(tiles.getSubimage(384, 0, 128, 128), buttonSize, buttonSize);
		clickedBombTile = style.resize(clickedBombTile.getSubimage(0, 0, 128, 128), buttonSize, buttonSize);
		
		numberTiles[0] = openedTile;
		numberTiles[1] = style.resize(tiles.getSubimage(0, 128, 128, 128), buttonSize, buttonSize);
		
		for(int i = 1;  i < 8; i++) {
			numberTiles[i+1] = style.resize(tiles.getSubimage((128 * i) % 512, 128 + (128 * (int) (i / 4)), 128, 128), buttonSize, buttonSize);
		}
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				buttons[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
				buttons[i][j].setSize(new Dimension(buttonSize,buttonSize));
				buttons[i][j].setMaximumSize(new Dimension(buttonSize, buttonSize));
				buttons[i][j].setMinimumSize(new Dimension(buttonSize, buttonSize));
				buttons[i][j].setBorderPainted(false);
				buttons[i][j].setBorder(null);
				buttons[i][j].setFocusable(false);
				buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
				buttons[i][j].setContentAreaFilled(false);
				buttons[i][j].setIconImage(unopenedTile);
				buttons[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseClicked(MouseEvent e) {
						Tile button = (Tile) e.getSource();
						if(SwingUtilities.isRightMouseButton(e)) {
							if(field[button.getI()][button.getJ()] == Property.CLEAR_UNOPENED || field[button.getI()][button.getJ()] == Property.BOMB_UNOPENED) {
								if(window.scorePanel.getRemainingFlags() > 0) {
									button.setIconImage(flagTile);
									window.scorePanel.setRemainingFlags(window.scorePanel.getRemainingFlags() - 1);
									if(field[button.getI()][button.getJ()] == Property.BOMB_UNOPENED) {
										field[button.getI()][button.getJ()] = Property.BOMB_FLAG;
										flaggedBombs++;
										if(flaggedBombs == numBombs)
											victory();
									}
									else if (field[button.getI()][button.getJ()] == Property.CLEAR_UNOPENED)
										field[button.getI()][button.getJ()] = Property.CLEAR_FLAG;
								} 
							} else if (field[button.getI()][button.getJ()] == Property.BOMB_FLAG || field[button.getI()][button.getJ()] == Property.CLEAR_FLAG){
								button.setIconImage(unopenedTile);
								window.scorePanel.setRemainingFlags(window.scorePanel.getRemainingFlags() + 1);
								if(field[button.getI()][button.getJ()] == Property.BOMB_FLAG) {
									field[button.getI()][button.getJ()] = Property.BOMB_UNOPENED;
									flaggedBombs--;
								}
								else if(field[button.getI()][button.getJ()] == Property.CLEAR_FLAG)
									field[button.getI()][button.getJ()] = Property.CLEAR_UNOPENED;
							}
						} else if(SwingUtilities.isLeftMouseButton(e)) {
							if(field[button.getI()][button.getJ()] == Property.CLEAR_UNOPENED) {
								if(verifyAdjBombs(button.getI(), button.getJ()) > 0)
									button.setIconImage(numberTiles[verifyAdjBombs(button.getI(), button.getJ())]);
								else {
									recursion(button.getI(), button.getJ());
								}
								field[button.getI()][button.getJ()] = Property.CLEAR_OPENED;
							} else if (field[button.getI()][button.getJ()] == Property.BOMB_UNOPENED) {
								button.setIconImage(clickedBombTile);
								field[button.getI()][button.getJ()] = Property.BOMB_OPENED;
								gameOver();
							}
						}
					}
				});
			}
		}
	}
	
	public void addToPanel(JPanel panel) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				panel.add(buttons[i][j]);
			}
		}
	}
	
	public Tile[][] getButtons() {
		return buttons;
	}
	
	public Property[][] getField() {
		return field;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
	
	public int getButtonSize() {
		return buttonSize;
	}
	
	public int getFieldWidth() {
		return width;
	}
	
	public int getFieldHeight() {
		return height;
	}
	
	public void printField() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print((field[i][j] == Property.CLEAR_UNOPENED ? 0 : 1) + " ");
			}
			System.out.println();
		}
	}

	public void addButtons(JPanel panel, Tile[][] buttons) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				panel.add(buttons[i][j]);
			}
		}
	}

	
	private int verifyAdjBombs(int i, int j) {
		int adjBombs = 0;
		if(i - 1 >= 0) {
			if(field[i-1][j] == Property.BOMB_UNOPENED || field[i-1][j] == Property.BOMB_FLAG)
				adjBombs++;
			if(j - 1 >= 0)
				if(field[i-1][j-1] == Property.BOMB_UNOPENED || field[i-1][j-1] == Property.BOMB_FLAG)
					adjBombs++;
			if(j + 1 < width)
				if(field[i-1][j+1] == Property.BOMB_UNOPENED || field[i-1][j+1] == Property.BOMB_FLAG)
					adjBombs++;
		}
		if(i + 1 < height) {
			if(field[i+1][j] == Property.BOMB_UNOPENED || field[i+1][j] == Property.BOMB_FLAG)
				adjBombs++;
			if(j - 1 >= 0)
				if(field[i+1][j-1] == Property.BOMB_UNOPENED || field[i+1][j-1] == Property.BOMB_FLAG)
					adjBombs++;
			if(j + 1 < width)
				if(field[i+1][j+1] == Property.BOMB_UNOPENED || field[i+1][j+1] == Property.BOMB_FLAG)
					adjBombs++;
		}
		if(j - 1 >= 0) {
			if(field[i][j-1] == Property.BOMB_UNOPENED || field[i][j-1] == Property.BOMB_FLAG)
				adjBombs++;
		}
		if(j + 1 < width) {
			if(field[i][j+1] == Property.BOMB_UNOPENED || field[i][j+1] == Property.BOMB_FLAG)
				adjBombs++;
		}
		return adjBombs;
	}
	
	private void recursion(int i, int j) {
		if(field[i][j] != Property.CLEAR_OPENED) {
			buttons[i][j].setIconImage(numberTiles[0]);
			field[i][j] = Property.CLEAR_OPENED;
		}
		if(i - 1 >= 0) {
			if(field[i-1][j] == Property.CLEAR_UNOPENED) {
				field[i-1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i-1, j) > 0)
					buttons[i-1][j].setIconImage(numberTiles[verifyAdjBombs(i-1, j)]);
				else {
					buttons[i-1][j].setIconImage(numberTiles[0]);
					recursion(i-1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i-1][j-1] == Property.CLEAR_UNOPENED) {
					field[i-1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j-1) > 0)
						buttons[i-1][j-1].setIconImage(numberTiles[verifyAdjBombs(i-1, j-1)]);
					else {
						buttons[i-1][j-1].setIconImage(numberTiles[0]);
						recursion(i-1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i-1][j+1] == Property.CLEAR_UNOPENED) {
					field[i-1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j+1) > 0)
						buttons[i-1][j+1].setIconImage(numberTiles[verifyAdjBombs(i-1, j+1)]);
					else {
						buttons[i-1][j+1].setIconImage(numberTiles[0]);
						recursion(i-1, j+1);
					}
				}
			}
				
		}
		if(i + 1 < height) {
			if(field[i+1][j] == Property.CLEAR_UNOPENED) {
				field[i+1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i+1, j) > 0)
					buttons[i+1][j].setIconImage(numberTiles[verifyAdjBombs(i+1, j)]);
				else {
					buttons[i+1][j].setIconImage(numberTiles[0]);
					recursion(i+1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i+1][j-1] == Property.CLEAR_UNOPENED) {
					field[i+1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j-1) > 0)
						buttons[i+1][j-1].setIconImage(numberTiles[verifyAdjBombs(i+1, j-1)]);
					else {
						buttons[i+1][j-1].setIconImage(numberTiles[0]);
						recursion(i+1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i+1][j+1] == Property.CLEAR_UNOPENED) {
					field[i+1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j+1) > 0)
						buttons[i+1][j+1].setIconImage(numberTiles[verifyAdjBombs(i+1, j+1)]);
					else {
						buttons[i+1][j+1].setIconImage(numberTiles[0]);
						recursion(i+1, j+1);
					}
				}
			}
		}
		if(j - 1 >= 0) {
			if(field[i][j-1] == Property.CLEAR_UNOPENED) {
				field[i][j-1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j-1) > 0)
					buttons[i][j-1].setIconImage(numberTiles[verifyAdjBombs(i, j-1)]);
				else {
					buttons[i][j-1].setIconImage(numberTiles[0]);
					recursion(i, j-1);
				}
			}
		}
		if(j + 1 < width) {
			if(field[i][j+1] == Property.CLEAR_UNOPENED) {
				field[i][j+1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j+1) > 0)
					buttons[i][j+1].setIconImage(numberTiles[verifyAdjBombs(i, j+1)]);
				else {
					buttons[i][j+1].setIconImage(numberTiles[0]);
					recursion(i, j+1);
				}
			}
		}
	}
	
	private void slowRecursion(int i, int j) {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(field[i][j] != Property.CLEAR_OPENED) {
			buttons[i][j].setIconImage(numberTiles[verifyAdjBombs(i,j)]);
			field[i][j] = Property.CLEAR_OPENED;
		}
		if(i - 1 >= 0) {
			if(field[i-1][j] == Property.CLEAR_UNOPENED) {
				field[i-1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i-1, j) > 0)
					buttons[i-1][j].setIconImage(numberTiles[verifyAdjBombs(i-1, j)]);
				else {
					buttons[i-1][j].setIconImage(numberTiles[0]);
					slowRecursion(i-1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i-1][j-1] == Property.CLEAR_UNOPENED) {
					field[i-1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j-1) > 0)
						buttons[i-1][j-1].setIconImage(numberTiles[verifyAdjBombs(i-1, j-1)]);
					else {
						buttons[i-1][j-1].setIconImage(numberTiles[0]);
						slowRecursion(i-1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i-1][j+1] == Property.CLEAR_UNOPENED) {
					field[i-1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j+1) > 0)
						buttons[i-1][j+1].setIconImage(numberTiles[verifyAdjBombs(i-1, j+1)]);
					else {
						buttons[i-1][j+1].setIconImage(numberTiles[0]);
						slowRecursion(i-1, j+1);
					}
				}
			}
				
		}
		if(i + 1 < height) {
			if(field[i+1][j] == Property.CLEAR_UNOPENED) {
				field[i+1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i+1, j) > 0)
					buttons[i+1][j].setIconImage(numberTiles[verifyAdjBombs(i+1, j)]);
				else {
					buttons[i+1][j].setIconImage(numberTiles[0]);
					slowRecursion(i+1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i+1][j-1] == Property.CLEAR_UNOPENED) {
					field[i+1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j-1) > 0)
						buttons[i+1][j-1].setIconImage(numberTiles[verifyAdjBombs(i+1, j-1)]);
					else {
						buttons[i+1][j-1].setIconImage(numberTiles[0]);
						slowRecursion(i+1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i+1][j+1] == Property.CLEAR_UNOPENED) {
					field[i+1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j+1) > 0)
						buttons[i+1][j+1].setIconImage(numberTiles[verifyAdjBombs(i+1, j+1)]);
					else {
						buttons[i+1][j+1].setIconImage(numberTiles[0]);
						slowRecursion(i+1, j+1);
					}
				}
			}
		}
		if(j - 1 >= 0) {
			if(field[i][j-1] == Property.CLEAR_UNOPENED) {
				field[i][j-1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j-1) > 0)
					buttons[i][j-1].setIconImage(numberTiles[verifyAdjBombs(i, j-1)]);
				else {
					buttons[i][j-1].setIconImage(numberTiles[0]);
					slowRecursion(i, j-1);
				}
			}
		}
		if(j + 1 < width) {
			if(field[i][j+1] == Property.CLEAR_UNOPENED) {
				field[i][j+1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j+1) > 0)
					buttons[i][j+1].setIconImage(numberTiles[verifyAdjBombs(i, j+1)]);
				else {
					buttons[i][j+1].setIconImage(numberTiles[0]);
					slowRecursion(i, j+1);
				}
			}
		}
	}
	
	private void gameOver() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(field[i][j] == Property.BOMB_UNOPENED) {
					field[i][j] = Property.BOMB_OPENED;
					buttons[i][j].setIconImage(bombTile);
				} else {
					if(field[i][j] == Property.CLEAR_FLAG) {
						buttons[i][j].setIconImage(wrongFlagTile);
					}
					field[i][j] = Property.GAME_OVER;
				}
			}
		}
		
		window.scorePanel.setFace(window.scorePanel.deadFace);
		window.scorePanel.timer.setGameOver(true);
		
		GameOver gameOver = new GameOver(width, height, window.frame);
		gameOver.open();
	}
	
	private void victory() {
		window.scorePanel.timer.setGameOver(true);
		slowOpenField();
		window.scorePanel.setFace(window.scorePanel.glassFace);
	}
	
	private void slowOpenField() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						if(field[i][j] != Property.BOMB_FLAG && field[i][j] != Property.CLEAR_OPENED) {
							slowRecursion(i, j);
							field[i][j] = Property.GAME_OVER;
						}
					}
				}
			}
		});
		t.start();
	}
	
	public JFrame getWindowFrame() {
		return window.frame;
	}
	
	public void resetGame() {
		window.scorePanel.timer.setGameOver(false);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				field[i][j] = Property.CLEAR_UNOPENED;
				buttons[i][j].setIconImage(unopenedTile);
			}
		}
		
		createBombs();
		printField();
		flaggedBombs = 0;
		
		window.scorePanel.setFace(window.scorePanel.happyFace);
		window.scorePanel.resetBombsDisplay();
		window.scorePanel.resetTimer();
	}
}
