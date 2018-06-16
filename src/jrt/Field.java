package jrt;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Field {
	private Window window;
	private Tile buttons[][];
	private Property field[][];
	private int numBombs, width, height, flaggedBombs, openedTiles;	
	private boolean gameStarted, questionMark;
	
	public Field(int width, int height, Window window, int numBombs) {
		this.width = width;
		this.height = height;
		this.window = window;
		flaggedBombs = 0;
		openedTiles = 0;
		gameStarted = false;
		questionMark = true;
		this.numBombs = numBombs;
		field = new Property[height][width];
		buttons = new Tile[height][width];
		createField();
		createBombs();
		prepareButtons(buttons);
	}
	
	private void createField() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buttons[i][j] = new Tile(i, j, window.images.unopenedTile);
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
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				buttons[i][j].setPreferredSize(new Dimension(window.buttonSize, window.buttonSize));
				buttons[i][j].setSize(new Dimension(window.buttonSize,window.buttonSize));
				buttons[i][j].setMaximumSize(new Dimension(window.buttonSize, window.buttonSize));
				buttons[i][j].setMinimumSize(new Dimension(window.buttonSize, window.buttonSize));
				buttons[i][j].setBorderPainted(false);
				buttons[i][j].setBorder(null);
				buttons[i][j].setFocusable(false);
				buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
				buttons[i][j].setContentAreaFilled(false);
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
						if(!gameStarted && field[button.getI()][button.getJ()] != Property.BOMB_UNOPENED) {
							gameStarted = true;
							window.scorePanel.timer.begin();
						}
						
						if(SwingUtilities.isRightMouseButton(e)) {
							if(field[button.getI()][button.getJ()] == Property.CLEAR_UNOPENED || field[button.getI()][button.getJ()] == Property.BOMB_UNOPENED) {
								if(window.scorePanel.getRemainingFlags() > 0) {
									button.setIcon(window.images.flagTile);
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
								if(!questionMark) {
									button.setIcon(window.images.unopenedTile);
									window.scorePanel.setRemainingFlags(window.scorePanel.getRemainingFlags() + 1);
									if(field[button.getI()][button.getJ()] == Property.BOMB_FLAG) {
										flaggedBombs--;
										field[button.getI()][button.getJ()] = Property.BOMB_UNOPENED;
									}
									else if(field[button.getI()][button.getJ()] == Property.CLEAR_FLAG)
										field[button.getI()][button.getJ()] = Property.CLEAR_UNOPENED;
								} else {
									button.setIcon(window.images.questionTile);
									window.scorePanel.setRemainingFlags(window.scorePanel.getRemainingFlags() + 1);
									if(field[button.getI()][button.getJ()] == Property.BOMB_FLAG) {
										field[button.getI()][button.getJ()] = Property.BOMB_QUESTION;
										flaggedBombs--;
									} else if (field[button.getI()][button.getJ()] == Property.CLEAR_FLAG) 
										field[button.getI()][button.getJ()] = Property.CLEAR_QUESTION;
								}
							} else if(field[button.getI()][button.getJ()] == Property.BOMB_QUESTION || field[button.getI()][button.getJ()] == Property.CLEAR_QUESTION) {
								buttons[button.getI()][button.getJ()].setIcon(window.images.unopenedTile);
								
								if(field[button.getI()][button.getJ()] == Property.BOMB_QUESTION) {
									field[button.getI()][button.getJ()] = Property.BOMB_UNOPENED;
								} else if(field[button.getI()][button.getJ()] == Property.CLEAR_QUESTION) {
									field[button.getI()][button.getJ()] = Property.CLEAR_UNOPENED;
								}
							}
						} 
						
						else if(SwingUtilities.isLeftMouseButton(e)) {
							if(field[button.getI()][button.getJ()] == Property.CLEAR_UNOPENED) {
								if(verifyAdjBombs(button.getI(), button.getJ()) > 0) {
									button.setIcon(window.images.numberTiles[verifyAdjBombs(button.getI(), button.getJ())]);
									openedTiles++;
								} else {
									recursion(button.getI(), button.getJ());
								}
								if(openedTiles >= width * height - numBombs)
									victory();
								field[button.getI()][button.getJ()] = Property.CLEAR_OPENED;
							} else if (field[button.getI()][button.getJ()] == Property.BOMB_UNOPENED) {
								button.setIcon(window.images.clickedBombTile);
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
	
	public int getNumBombs() {
		return numBombs;
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
			buttons[i][j].setIcon(window.images.numberTiles[0]);
			openedTiles++;
			field[i][j] = Property.CLEAR_OPENED;
		}
		if(i - 1 >= 0) {
			if(field[i-1][j] == Property.CLEAR_UNOPENED) {
				openedTiles++;
				field[i-1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i-1, j) > 0)
					buttons[i-1][j].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j)]);
				else {
					buttons[i-1][j].setIcon(window.images.numberTiles[0]);
					recursion(i-1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i-1][j-1] == Property.CLEAR_UNOPENED) {
					openedTiles++;
					field[i-1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j-1) > 0)
						buttons[i-1][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j-1)]);
					else {
						buttons[i-1][j-1].setIcon(window.images.numberTiles[0]);
						recursion(i-1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i-1][j+1] == Property.CLEAR_UNOPENED) {
					openedTiles++;
					field[i-1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j+1) > 0)
						buttons[i-1][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j+1)]);
					else {
						buttons[i-1][j+1].setIcon(window.images.numberTiles[0]);
						recursion(i-1, j+1);
					}
				}
			}
				
		}
		if(i + 1 < height) {
			if(field[i+1][j] == Property.CLEAR_UNOPENED) {
				openedTiles++;
				field[i+1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i+1, j) > 0)
					buttons[i+1][j].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j)]);
				else {
					buttons[i+1][j].setIcon(window.images.numberTiles[0]);
					recursion(i+1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i+1][j-1] == Property.CLEAR_UNOPENED) {
					openedTiles++;
					field[i+1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j-1) > 0)
						buttons[i+1][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j-1)]);
					else {
						buttons[i+1][j-1].setIcon(window.images.numberTiles[0]);
						recursion(i+1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i+1][j+1] == Property.CLEAR_UNOPENED) {
					openedTiles++;
					field[i+1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j+1) > 0)
						buttons[i+1][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j+1)]);
					else {
						buttons[i+1][j+1].setIcon(window.images.numberTiles[0]);
						recursion(i+1, j+1);
					}
				}
			}
		}
		if(j - 1 >= 0) {
			if(field[i][j-1] == Property.CLEAR_UNOPENED) {
				openedTiles++;
				field[i][j-1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j-1) > 0)
					buttons[i][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i, j-1)]);
				else {
					buttons[i][j-1].setIcon(window.images.numberTiles[0]);
					recursion(i, j-1);
				}
			}
		}
		if(j + 1 < width) {
			if(field[i][j+1] == Property.CLEAR_UNOPENED) {
				openedTiles++;
				field[i][j+1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j+1) > 0)
					buttons[i][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i, j+1)]);
				else {
					buttons[i][j+1].setIcon(window.images.numberTiles[0]);
					recursion(i, j+1);
				}
			}
		}
	}
	
	private void slowRecursion(int i, int j) {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(field[i][j] != Property.CLEAR_OPENED) {
			buttons[i][j].setIcon(window.images.numberTiles[verifyAdjBombs(i,j)]);
			field[i][j] = Property.CLEAR_OPENED;
		}
		if(i - 1 >= 0) {
			if(field[i-1][j] == Property.CLEAR_UNOPENED) {
				field[i-1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i-1, j) > 0)
					buttons[i-1][j].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j)]);
				else {
					buttons[i-1][j].setIcon(window.images.numberTiles[0]);
					slowRecursion(i-1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i-1][j-1] == Property.CLEAR_UNOPENED) {
					field[i-1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j-1) > 0)
						buttons[i-1][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j-1)]);
					else {
						buttons[i-1][j-1].setIcon(window.images.numberTiles[0]);
						slowRecursion(i-1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i-1][j+1] == Property.CLEAR_UNOPENED) {
					field[i-1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i-1, j+1) > 0)
						buttons[i-1][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i-1, j+1)]);
					else {
						buttons[i-1][j+1].setIcon(window.images.numberTiles[0]);
						slowRecursion(i-1, j+1);
					}
				}
			}
				
		}
		if(i + 1 < height) {
			if(field[i+1][j] == Property.CLEAR_UNOPENED) {
				field[i+1][j] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i+1, j) > 0)
					buttons[i+1][j].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j)]);
				else {
					buttons[i+1][j].setIcon(window.images.numberTiles[0]);
					slowRecursion(i+1, j);
				}
			}
			if(j - 1 >= 0) {
				if(field[i+1][j-1] == Property.CLEAR_UNOPENED) {
					field[i+1][j-1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j-1) > 0)
						buttons[i+1][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j-1)]);
					else {
						buttons[i+1][j-1].setIcon(window.images.numberTiles[0]);
						slowRecursion(i+1, j-1);
					}
				}
			}
			if(j + 1 < width) {
				if(field[i+1][j+1] == Property.CLEAR_UNOPENED) {
					field[i+1][j+1] = Property.CLEAR_OPENED;
					if(verifyAdjBombs(i+1, j+1) > 0)
						buttons[i+1][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i+1, j+1)]);
					else {
						buttons[i+1][j+1].setIcon(window.images.numberTiles[0]);
						slowRecursion(i+1, j+1);
					}
				}
			}
		}
		if(j - 1 >= 0) {
			if(field[i][j-1] == Property.CLEAR_UNOPENED) {
				field[i][j-1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j-1) > 0)
					buttons[i][j-1].setIcon(window.images.numberTiles[verifyAdjBombs(i, j-1)]);
				else {
					buttons[i][j-1].setIcon(window.images.numberTiles[0]);
					slowRecursion(i, j-1);
				}
			}
		}
		if(j + 1 < width) {
			if(field[i][j+1] == Property.CLEAR_UNOPENED) {
				field[i][j+1] = Property.CLEAR_OPENED;
				if(verifyAdjBombs(i, j+1) > 0)
					buttons[i][j+1].setIcon(window.images.numberTiles[verifyAdjBombs(i, j+1)]);
				else {
					buttons[i][j+1].setIcon(window.images.numberTiles[0]);
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
					buttons[i][j].setIcon(window.images.bombTile);
				} else {
					if(field[i][j] == Property.CLEAR_FLAG) {
						buttons[i][j].setIcon(window.images.wrongFlagTile);
					}
					field[i][j] = Property.GAME_OVER;
				}
			}
		}
		
		window.scorePanel.setFace(window.images.deadFace);
		window.scorePanel.timer.stopCounting();
	}
	
	private void victory() {
		window.scorePanel.timer.stopCounting();
		slowOpenField();
		window.scorePanel.setFace(window.images.glassFace);
	}
	
	private void slowOpenField() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						if(field[i][j] != Property.BOMB_FLAG && field[i][j] != Property.CLEAR_OPENED && field[i][j] != Property.BOMB_UNOPENED) {
							slowRecursion(i, j);
							field[i][j] = Property.GAME_OVER;
						} else if(field[i][j] == Property.BOMB_UNOPENED){
							buttons[i][j].setIcon(window.images.flagTile);
						}
					}
				}
			}
		});
		t.start();
	}
	
	public void resetGame() {
		window.scorePanel.timer.stopCounting();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				field[i][j] = Property.CLEAR_UNOPENED;
				buttons[i][j].setIcon(window.images.unopenedTile);
			}
		}
		
		createBombs();
		printField();
		flaggedBombs = 0;
		openedTiles = 0;
		gameStarted = false;
		
		window.scorePanel.setFace(window.images.happyFace);
		window.scorePanel.resetBombsDisplay();
		window.scorePanel.resetTimer();
	}
	
	public void setQuestionMarkTile(boolean isQuestionMarkEnabled) {
		questionMark = isQuestionMarkEnabled;
	}
	
	public boolean isQuestionMarkEnabled() {
		return questionMark;
	}
}
