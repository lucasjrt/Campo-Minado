package jrt;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Style {
	private Color btnBackground, pnlBackground, txtBackground, txtSelection, txtSelected, lblForeground;
	
	public Style() {
		pnlBackground = new Color(65, 105, 225);
		btnBackground = new Color(255, 165, 0);
		txtBackground = new Color(135, 206, 250);
		txtSelection = new Color(100, 150, 255);
		txtSelected = Color.WHITE;
		lblForeground = Color.WHITE;
	}
	
	public void preparePanel(JPanel panel) {
		panel.setBackground(pnlBackground);
	}
	
	public void prepareButton(JButton button) {
		button.setBackground(btnBackground);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	public void prepareTextField(JTextField textField) {
		textField.setBorder(null);
		textField.setBackground(txtBackground);
		textField.setSelectionColor(txtSelection);
		textField.setSelectedTextColor(txtSelected);
	}
	
	public void prepareLabel(JLabel label) {
		label.setForeground(lblForeground);
	}
	
	public void setBtnBackgroundColor(Color color) {
		btnBackground = color;
	}
	
	public void setPnlBackgroundColor(Color color) {
		btnBackground = color;
	}
	
	public void setTxtBackgroundColor(Color color) {
		txtBackground = color;
	}

	public void setTxtSelectionColor(Color color) {
		txtSelection = color;
	}

	public void setTxtSelectedColor(Color color) {
		txtSelected = color;
	}

	public void setLblForegroundColor(Color color) {
		lblForeground = color;
	}
	
	public BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
