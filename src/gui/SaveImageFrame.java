package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SaveImageFrame extends JFrame {

	public JPanel saveName;
	public JTextField givenName;
	public JButton saveImages;
	public String textGivenName;

	public SaveImageFrame(ImagePanel image) {

		super("Save image");
		saveImages = new JButton("Save");
		saveName = new JPanel();
		givenName = new JTextField(20);

		saveName.setLayout(new GridLayout(1, 2, 1, 3));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 300);
		setVisible(true);

		System.out.println("SaveImg...");

		saveName.add(saveImages);
		saveName.add(givenName);

		givenName.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				System.out.println();
				if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
					if (givenName.getText() != null) {
						textGivenName = givenName.getText();
					}
				}
			}

			public void keyReleased(KeyEvent ke) {
				if (givenName.getText() != null) {

					textGivenName = givenName.getText();
				}
			}
		});

		saveImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					image.saveImage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		setLayout(new BorderLayout(4, 4));
		add("Center", saveName);
	}
}
