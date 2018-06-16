package jrt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Images {
	private Style style;
	private int buttonSize, displayWidth, displayHeight, faceSize;
	
	
	private BufferedImage tiles, displayImage, faces, clickedFaces;
	
	public ImageIcon unopenedTile, openedTile, flagTile, bombTile, clickedBombTile, wrongFlagTile, questionTile;
	public ImageIcon[] numberTiles;
	
	public ImageIcon display[];
	public ImageIcon happyFace, glassFace, deadFace, clickedHappyFace, clickedGlassFace, clickedDeadFace;
	
	public Images(Window window) {
		this.buttonSize = window.buttonSize;
		this.displayWidth = window.displayWidth;
		this.displayHeight = window.displayHeight;
		this.faceSize = window.faceSize;
		style = new Style();
		numberTiles = new ImageIcon[9];
		display = new ImageIcon[10];
		
		loadImages();
	}
	
	private void loadImages() {
		try {
			tiles =  ImageIO.read(new File("res/minesweeper_tiles.jpg"));
			clickedBombTile = new ImageIcon(style.resize(ImageIO.read(new File("res/clicked_bomb_tile.jpg")), buttonSize, buttonSize));
			wrongFlagTile = new ImageIcon(style.resize(ImageIO.read(new File("res/extra_tiles.jpg")).getSubimage(32, 32, 32, 32), buttonSize, buttonSize));
			questionTile = new ImageIcon(style.resize(ImageIO.read(new File("res/question_mark.jpg")), buttonSize, buttonSize));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		unopenedTile = new ImageIcon(style.resize(tiles.getSubimage(0, 0, 128, 128), buttonSize, buttonSize));
		System.out.println("unopenedTile loaded.");
		flagTile = new ImageIcon(style.resize(tiles.getSubimage(128, 0, 128, 128), buttonSize, buttonSize));
		bombTile = new ImageIcon(style.resize(tiles.getSubimage(256, 0, 128, 128), buttonSize, buttonSize));
		openedTile = new ImageIcon(style.resize(tiles.getSubimage(384, 0, 128, 128), buttonSize, buttonSize));
		
		numberTiles[0] = openedTile;
		numberTiles[1] = new ImageIcon(style.resize(tiles.getSubimage(0, 128, 128, 128), buttonSize, buttonSize));
		
		for(int i = 1;  i < 8; i++) {
			numberTiles[i+1] = new ImageIcon(style.resize(tiles.getSubimage((128 * i) % 512, 128 + (128 * (int) (i / 4)), 128, 128), buttonSize, buttonSize));
		}
		try {
			displayImage = ImageIO.read(new File("res/seven_segment_display.jpg"));
			faces = ImageIO.read(new File("res/extra_tiles.jpg"));
			clickedFaces = ImageIO.read(new File("res/clicked_tiles.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		for(int i = 0; i < 10; i++) {
			display[i] = new ImageIcon(style.resize(displayImage.getSubimage(i * 26, 0, 26, 46), displayHeight, displayWidth));
		}

		happyFace = new ImageIcon(style.resize(faces.getSubimage(0, 0, 32, 32), faceSize, faceSize));
		glassFace = new ImageIcon(style.resize(faces.getSubimage(32, 0, 32, 32), faceSize, faceSize));
		deadFace = new ImageIcon(style.resize(faces.getSubimage(0, 32, 32, 32), faceSize, faceSize));
		clickedHappyFace = new ImageIcon(style.resize(clickedFaces.getSubimage(32, 0, 32, 32), faceSize, faceSize));
		clickedGlassFace = new ImageIcon(style.resize(clickedFaces.getSubimage(32, 32, 32, 32), faceSize, faceSize));
		clickedDeadFace = new ImageIcon(style.resize(clickedFaces.getSubimage(0, 32, 32, 32), faceSize, faceSize));

	}
}
