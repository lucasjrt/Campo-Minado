package jrt;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Tile extends JButton{
	//TODO    v    remove static from here
	private static final long serialVersionUID = 1L;
	private int i, j;
	private ImageIcon icon;
	private BufferedImage bufferedIcon;
	
	public Tile(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public void setIconImage(BufferedImage icon) {
		this.bufferedIcon = icon;
		this.icon = new ImageIcon(icon);
		this.setIcon(this.icon);
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	public BufferedImage getBufferedIcon() {
		return bufferedIcon;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
}
