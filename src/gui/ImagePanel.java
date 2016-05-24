package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	Image displayImage, startImage;

	String path;
	Graphics2D big;
	LookupTable lookupTable;

	public BufferedImage img; // bi
	public String sname;
	// public JLabel imgContainer;
	public File selectedFile;
	public String selectedPath;
	public Dimension d;
	public int imageH;
	public int imageW;

	ImagePanel() {
		this.setSize(550, 550);
		createBufferedImage();
		// this.setSize(imageW, imageH);
	}

	public void resizeImagePanel() {
		this.setSize(imageW, imageH);
	}

	public void imageLoad() throws IOException {

		//JFileChooser fc = new JFileChooser("C:\\Users\\Anna\\Documents\\sem6\\biometria\\BiometriaObrazki\\histogramy");
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			String sname = selectedFile.getAbsolutePath();
			img = ImageIO.read(selectedFile);
			imageH = img.getHeight();
			imageW = img.getWidth();
			int w = img.getWidth();
			int h = img.getHeight();

			double scale = imageH / (double) h;

			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(img, after);

			loadImage(after);
		}
	}

	public void saveImage() throws IOException {
		JFileChooser chooser = new JFileChooser(
				"C:\\Users\\Anna\\Documents\\sem6\\biometria\\BiometriaObrazki\\histogramy");
				// int result = chooser.showOpenDialog(null);

		// chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showSaveDialog(null);
		String filename = chooser.getSelectedFile().toString();
		File file = new File(filename);
		ImageIO.write(img, "jpg", file);
		ImageIO.write(img, "gif", file);
		ImageIO.write(img, "tif", file);
	}

	public void createBufferedImage() {
		img = new BufferedImage(550, 550, BufferedImage.TYPE_INT_ARGB);

	}

	public void loadImage(BufferedImage image) {

		int w = image.getWidth();
		int h = image.getHeight();

		img = image;
		imageW = image.getWidth();
		imageH = image.getHeight();
		big = image.createGraphics();
		big.drawImage(image, 0, 0, this);

		// startImage = image;
		// displayImage = image;
		repaint();
	}

	public void update(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(img, 0, 0, this);
	}
}
