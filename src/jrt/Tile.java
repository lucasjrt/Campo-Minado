package jrt;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Tile extends JButton{
	private int i, j;
	private ImageIcon icon;
	
	public Tile(int i, int j, ImageIcon icon) {
		this.icon = icon;
		this.i = i;
		this.j = j;
		this.setIcon(this.icon);
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
}
