package binarization;

import javax.swing.*;

import Histograms.LUTimage;
import gui.ImagePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NiblackFrame extends JFrame {

	private JPanel mainPanel;
	private JPanel panelContainer;
	private JTextField lengthValue;
	private JTextField kValue;
	private JButton accept;
	private ImagePanel imagePanel;
	/*
	 * Parameter 1: is the k value. The default value is 0.2 for bright objects
	 * and -0.2 for dark objects. Any other number than 0 will change the
	 * default value. Parameter 2: is the C value. This is an offset with a
	 * default value of 0. Any other number than 0 will change its value
	 */

	public NiblackFrame(LUTimage lut, ImagePanel image) {
		super("Niblack parameters");
		this.imagePanel = image;
		mainPanel = new JPanel();
		panelContainer = new JPanel(new GridLayout(2, 1));
		mainPanel.add(new JLabel("Length:"));
		lengthValue = new JTextField();
		lengthValue.setPreferredSize(new Dimension(60, 20));
		mainPanel.add(lengthValue);
		mainPanel.add(new JLabel("K value:"));
		kValue = new JTextField();
		kValue.setPreferredSize(new Dimension(60, 20));
		accept = new JButton("Accept");
		accept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("SIEMATOJESTNICK");
				if (tryParseInt((lengthValue.getText()).replace(',', '.'))
						&& tryParseDouble((kValue.getText()).replace(',', '.'))) {
					if (!lengthValue.getText().equals("") || !kValue.getText().equals("")) {
						int length = Integer.parseInt((lengthValue.getText()).replace(',', '.'));
						double k = Double.parseDouble((kValue.getText()).replace(',', '.'));

						// if (dlugosc % 2 == 1 && k < 0) {
						System.out.println("SIEMATOJESTNICK");
						lut.grayScale(imagePanel);
						lut.localThresholdNiblack(imagePanel, k, length);
						imagePanel.repaint();
						dispose();
						// }

					}
				} /*
					 * else { int dlugosc =
					 * Integer.parseInt((lengthValue.getText()).replace(',',
					 * '.')); double k =
					 * Double.parseDouble((kValue.getText()).replace(',', '.'));
					 * System.out.println(dlugosc + " " + k);
					 * JOptionPane.showMessageDialog(processedImage,
					 * "Length > 0 and 0 > k > 1"); }
					 */
			}
		});
		mainPanel.add(kValue);
		mainPanel.add(accept);
		panelContainer.add(mainPanel);
		add(panelContainer, BorderLayout.NORTH);
		setVisible(true);
		pack();
	}

	private boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean tryParseDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
