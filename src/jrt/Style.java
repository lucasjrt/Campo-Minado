package jrt;

import java.awt.Color;
import java.awt.Cursor;

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
		txtSelection = Color.BLUE;
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
}
