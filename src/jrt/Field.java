package jrt;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Field {
	BufferedImage tiles, unopenedTile, openedTile, flagTile, bombTile;
	BufferedImage oneTile, twoTile, threeTile, fourTile, fiveTile, sixTile, sevenTile, eightTile;
	BufferedImage extraTiles, clickedTile, grayFlagTile;
	JButton buttons[][];
	private int field[][];
	private int buttonSize, numBombs, width, height;
	
	public Field(int width, int height) {
		this.buttonSize = 28;
		this.numBombs = (int) (width * height * .30);
		this.width = width;
		this.height = height;
		this.field = new int[height][width]; //0 = clear unopened, 1 = bomb unopened, 2 = clear opened, 3 = bomb opened 
		this.buttons = new JButton[height][width];
		createField();
		createBombs();
		prepareButtons(buttons);
	}
	
	private void createField() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buttons[i][j] = new JButton();
				field[i][j] = 0;
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
			if(field[i][j] == 0) {
				field[i][j] = 1;
				numBombs++;
			}
		}
		System.out.println(numBombs + " bombs created");
	}
	
	private void prepareButtons(JButton[][] buttons) {
		
		try {
			tiles = ImageIO.read(getClass().getResource("minesweeper_tiles.jpg"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		unopenedTile = resize((tiles.getSubimage(0, 0, 128, 128)), buttonSize, buttonSize);	
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
				buttons[i][j].setIcon(new ImageIcon(unopenedTile));
				buttons[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {
						
					}
					@Override
					public void mouseExited(MouseEvent e) {
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						
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
	
	public JButton[][] getButtons() {
		return buttons;
	}
	
	public int[][] getField() {
		return field;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
	
	public int getButtonSize() {
		return buttonSize;
	}
	
	public void printField() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(field[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void addButtons(JPanel panel, JButton[][] buttons) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				panel.add(buttons[i][j]);
			}
		}
	}
	
	private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
