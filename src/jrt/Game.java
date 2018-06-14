package jrt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Game {
	EmptyBorder border;
	JFrame frame;
	JPanel panel;
	JLabel lblSize, lblWidth, lblHeight, lblBombs;
	JTextField txtWidth, txtHeight, txtBombs;
	JButton btnContinue, btnExit;
	GroupLayout layout;
	Style style;
	Window window;
	public static void main(String[] args) {
		new Window(9, 9, 10, 1);
//		new Game();
	}
	
	public Game() {
		txtWidth = new JTextField("9", 3);
		txtHeight = new JTextField("9", 3);
		txtBombs = new JTextField("10", 3);
		lblSize = new JLabel("Defina o tamanho do campo");
		lblWidth = new JLabel("Largura: ");
		lblHeight = new JLabel("Altura: ");
		lblBombs = new JLabel("Minas: ");
		btnContinue = new JButton("Continuar");
		btnExit = new JButton("Sair");
		frame = new JFrame("Campo minado");
		panel = new JPanel();
		layout = new GroupLayout(panel);
		style = new Style();
		
		border = new EmptyBorder(0, 5, 10, 5);
		lblSize.setBorder(border);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(lblSize)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(lblWidth)
								.addComponent(lblHeight)
								.addComponent(lblBombs)
								.addComponent(btnExit))
						.addGroup(layout.createParallelGroup()
								.addComponent(txtWidth)
								.addComponent(txtHeight)
								.addComponent(txtBombs)
								.addComponent(btnContinue))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(lblSize)
					.addGroup(layout.createParallelGroup()
							.addComponent(lblWidth)
							.addComponent(txtWidth))
					.addGroup(layout.createParallelGroup()
							.addComponent(lblHeight)
							.addComponent(txtHeight))
					.addGroup(layout.createParallelGroup()
							.addComponent(lblBombs)
							.addComponent(txtBombs))
					.addGroup(layout.createParallelGroup()
							.addComponent(btnExit)
							.addComponent(btnContinue)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, btnExit, btnContinue);
		
		style.preparePanel(panel);
		style.prepareButton(btnContinue);
		style.prepareButton(btnExit);
		style.prepareTextField(txtHeight);
		style.prepareTextField(txtWidth);
		style.prepareTextField(txtBombs);
		style.prepareLabel(lblSize);
		style.prepareLabel(lblHeight);
		style.prepareLabel(lblWidth);
		style.prepareLabel(lblBombs);
		
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		
		txtWidth.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {
				txtWidth.selectAll();
			}
		});
		
		txtHeight.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {
				txtHeight.selectAll();
			}
		});
		
		txtBombs.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {
				txtBombs.selectAll();
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int w, h, b;
				if(isInteger(txtWidth.getText()) && isInteger(txtHeight.getText()) && isInteger(txtBombs.getText())) {
					w = Integer.parseInt(txtWidth.getText());
					h = Integer.parseInt(txtHeight.getText());
					b = Integer.parseInt(txtBombs.getText());
					if(w >= 5 && h >= 5 && w <= 50 && h <= 30 && b > 0 && b < w * h) {
						frame.setVisible(false);
						window = new Window(Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtBombs.getText()), 4);
					}
					else if(w < 5 || w > 50)
						JOptionPane.showMessageDialog(frame, "A largura deve estar entre 5 e 50", "ERRO", JOptionPane.ERROR_MESSAGE);
					else if(h < 5 || h > 30)
						JOptionPane.showMessageDialog(frame, "A altura deve estar entre 5 e 30", "ERRO", JOptionPane.ERROR_MESSAGE);
					else if(b < 1 || b > w * h - 1)
						JOptionPane.showMessageDialog(null, "O númeor de bombas deve estar entre 1 e " + (w * h - 1), "ERRO", JOptionPane.ERROR_MESSAGE);
					else
						JOptionPane.showMessageDialog(frame, "Erro inesperado", "ERRO", JOptionPane.ERROR_MESSAGE);
					}	
			}
		});
	}
	
	boolean isInteger(String text) {
		try  {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
			return false;
		}
	}
}
