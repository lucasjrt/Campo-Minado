package jrt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	JLabel lblSize, lblWidth, lblHeight;
	JTextField txtWidth, txtHeight;
	JButton btnContinue, btnExit;
	GroupLayout layout;
	Style style;
	Window window;
	public static void main(String[] args) {
		new Game();
	}
	
	public Game() {
		txtWidth    = new JTextField("10", 3);
		txtHeight   = new JTextField("10", 3);
		lblSize     = new JLabel("Defina o tamanho do campo");
		lblWidth    = new JLabel("Largura: ");
		lblHeight   = new JLabel("Altura: ");
		btnContinue = new JButton("Continuar");
		btnExit     = new JButton("Sair");
		frame       = new JFrame("Campo minado");
		panel       = new JPanel();
		layout      = new GroupLayout(panel);
		style       = new Style();
		
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
								.addComponent(btnExit))
						.addGroup(layout.createParallelGroup()
								.addComponent(txtWidth)
								.addComponent(txtHeight)
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
							.addComponent(btnExit)
							.addComponent(btnContinue)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, btnExit, btnContinue);
		
		style.preparePanel(panel);
		style.prepareButton(btnContinue);
		style.prepareButton(btnExit);
		style.prepareTextField(txtHeight);
		style.prepareTextField(txtWidth);
		style.prepareLabel(lblSize);
		style.prepareLabel(lblHeight);
		style.prepareLabel(lblWidth);
		
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isInteger(txtWidth.getText()) && isInteger(txtHeight.getText()) && Integer.parseInt(txtWidth.getText()) >= 5 && Integer.parseInt(txtHeight.getText()) >= 5 && Integer.parseInt(txtWidth.getText()) <= 30 && Integer.parseInt(txtHeight.getText()) <= 30) {
					frame.setVisible(false);
					window = new Window(Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText()));
				} else {
					JOptionPane.showMessageDialog(null, "Altura ou largura inválido, o valor deve estar entre 5 e 30", "ERRO", JOptionPane.ERROR_MESSAGE);
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
