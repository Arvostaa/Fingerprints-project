
package gui;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Histograms.LUTimage;
import binarization.NiblackFrame;
import binarization.ThresholdFrame;
import fingerprint.Thinning;

public class MainPanel extends JPanel {

	public JMenuBar menu;
	private LUTimage lut;
	public ImagePanel imagePanel;

	public MainPanel() {
		menu = new JMenuBar();
		lut = new LUTimage(this);

		// FILE//
		JMenu fileMenu = new JMenu("File");
		JMenuItem save = new JMenuItem("Export image");
		JMenuItem loadImage = new JMenuItem("Load image");
		fileMenu.add(loadImage);
		fileMenu.add(save);

		// BINARIZATION//

		JMenu binarization = new JMenu("Binarization");
		JMenuItem threshold = new JMenuItem("Threshold");
		JMenuItem otsu = new JMenuItem("Otsu");
		JMenuItem niblack = new JMenuItem("Niblack");
		binarization.add(threshold);
		binarization.add(otsu);
		binarization.add(niblack);

		// FINGERPRINT METHODS//

		JMenuItem thinning = new JMenuItem("Thinning");

		// ***MENU***//
		menu.add(fileMenu);
		menu.add(binarization);
		menu.add(thinning);

		imagePanel = new ImagePanel(); // MAIN IMAGE

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// add(menu);
		add(imagePanel);

		// load image...
		loadImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					imagePanel.imageLoad(); // LOAD RESIZED IMAGE
					imagePanel.resizeImagePanel();

				} catch (IOException ex) {
					ex.printStackTrace();
				}
				// pack();
			}

		});

		save.addActionListener(new ActionListener() { // change //

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					imagePanel.saveImage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		// BINARIZATION METHODS//

		niblack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NiblackFrame(lut, imagePanel);
			}
		});

		threshold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ThresholdFrame(lut, imagePanel);
			}
		});

		otsu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lut.otsu();
				imagePanel.repaint();
			}
		});

		// FINGERPRINTS METHODS//

		thinning.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Thinning t = new Thinning(imagePanel.img);
				try {
					t.thin();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				imagePanel.img = t.image;
				imagePanel.repaint();

			}
		});
	}
}