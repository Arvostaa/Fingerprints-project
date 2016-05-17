package Histograms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import gui.ImagePanel;
import gui.MainPanel;

public class LUTimage {

	private int[] lutR;
	private int[] lutG;
	private int[] lutB;
	private int[] lutRo;
	private int[] lutGo;
	private int[] lutBo;
	private MainPanel mainPanel;
	private LookupTable lookupTable;

	public LUTimage(MainPanel mainWindow) {
		this.mainPanel = mainWindow;
	}

	public void expLUT(double x) { // darken/lightne
		int[] lut = new int[256];
		double xx = x;
		if (x > 3) {
			xx = 3;
		}
		if (x < 0.7) {
			xx = 0.7;
		}
		for (int i = 0; i < 256; i++) {
			lut[i] = (int) Math.round(Math.pow(i, xx));
		}
		normalizeLUT(lut);

		lutR = lutG = lutB = lut;

	}

	public void repaintImage(ImagePanel image) {
		Color temp;
		Color c;
		for (int i = 0; i < image.img.getWidth(); i++) {
			for (int j = 0; j < image.img.getHeight(); j++) {
				temp = new Color(image.img.getRGB(i, j));
				c = new Color(lutR[temp.getRed()], lutG[temp.getGreen()], lutB[temp.getBlue()]);
				image.img.setRGB(i, j, c.getRGB());
				image.img.setRGB(i, j, c.getRGB());
			}
		}
	}

	public void repaintImageo(ImagePanel image) {
		Color temp;
		Color c;

		BufferedImage tempImg = image.img;
		for (int i = 0; i < image.img.getWidth(); i++) {
			for (int j = 0; j < image.img.getHeight(); j++) {
				temp = new Color(image.img.getRGB(i, j));
				c = new Color(lutRo[temp.getRed()], lutGo[temp.getGreen()], lutBo[temp.getBlue()]);
				tempImg.setRGB(i, j, c.getRGB());
				tempImg.setRGB(i, j, c.getRGB());
			}
		}

		image.img = tempImg;
	}

	// stretch + equalize//
	public void stretchingHistogram(int a, int b) {
		int[] lut = new int[256];
		if (a > b) {
			int temp = b;
			b = a;
			a = temp;
		}

		for (int i = 0; i < lut.length; i++) {
			if (i <= a) {
				lut[i] = 0;
			} else if (i >= b) {
				lut[i] = 255;
			} else {
				lut[i] = (int) ((255.0 / (b - a)) * (i - a));
			}
		}
		lutR = lutG = lutB = lut;
	}

	public void histogramEqualization() {
		lutR = new int[256];
		lutG = new int[256];
		lutB = new int[256];
		BufferedImage bf = mainPanel.imagePanel.img;
		for (int i = 0; i < bf.getWidth(); i++) {
			for (int j = 0; j < bf.getHeight(); j++) {
				Color c = new Color(bf.getRGB(i, j));
				lutR[Math.round(c.getRed())]++;
				lutG[Math.round(c.getGreen())]++;
				lutB[Math.round(c.getBlue())]++;
			}
		}
		for (int i = 0; i < 255; i++) {
			lutR[i + 1] += lutR[i];
			lutG[i + 1] += lutG[i];
			lutB[i + 1] += lutB[i];
		}
		int R = 0, G = 0, B = 0;
		for (int i = 0; i < 256; i++) {
			if (lutR[i] > 0 && R == 0) {
				R = lutR[i];
			}
			if (lutG[i] > 0 && G == 0) {
				G = lutG[i];
			}
			if (lutB[i] > 0 && B == 0) {
				B = lutB[i];
			}
			if (R != 0 && G != 0 && B != 0) {
				break;
			}
		}
		for (int i = 0; i < 256; i++) {
			lutR[i] = (int) (255.0 * (lutR[i] - R)) / (bf.getHeight() * bf.getWidth() - R);
			lutG[i] = (int) (255.0 * (lutG[i] - G)) / (bf.getHeight() * bf.getWidth() - G);
			lutB[i] = (int) (255.0 * (lutB[i] - B)) / (bf.getHeight() * bf.getWidth() - B);
		}
	}

	public void normalizeLUT(int[] lut) {
		double minValue = 0;
		for (int i = 0; i < lut.length; i++) {
			if (lut[i] < minValue) {
				minValue = lut[i];
			}
		}
		for (int i = 0; i < lut.length; i++) {
			lut[i] -= minValue;
		}
		double maxValue = 0;
		for (int i = 0; i < lut.length; i++) {
			if (lut[i] > maxValue) {
				maxValue = lut[i];
			}
		}
		if (maxValue == 0) {
			return;
		}
		double scale = 255.0 / maxValue;
		for (int i = 0; i < lut.length; i++) {
			lut[i] = (int) Math.round(lut[i] * scale);
			if (lut[i] > 255) {
				lut[i] = 255;
			}
			if (lut[i] < 0) {
				lut[i] = 0;
			}
		}
	}

	// BINARIZATION//

	public void grayScale(ImagePanel image) {
		for (int i = 0; i < image.img.getWidth(); i++) {
			for (int j = 0; j < image.img.getHeight(); j++) {
				Color temp = new Color(image.img.getRGB(i, j));
				int intColor = Math.round((temp.getRed() + temp.getGreen() + temp.getBlue()) / 3);
				Color c = new Color(intColor, intColor, intColor);
				image.img.setRGB(i, j, c.getRGB());
				image.img.setRGB(i, j, c.getRGB());
			}
		}
	}

	public void grayScale3(BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				Color temp = new Color(image.getRGB(i, j));
				int intColor = Math.round((temp.getRed() + temp.getGreen() + temp.getBlue()) / 3);
				Color c = new Color(intColor, intColor, intColor);
				image.setRGB(i, j, c.getRGB());
				image.setRGB(i, j, c.getRGB());
			}
		}
	}

	public void threshold(int steps) {
		int[] lut = new int[256];
		for (int i = 0; i < lut.length; i++) {
			if (i < steps) {
				lut[i] = 0;
			}
			if (i >= steps) {
				lut[i] = 255;
			}
		}
		lutR = lutG = lutB = lut;
	}

	public void thresholdOtsu(int steps) {
		int[] lut = new int[256];
		for (int i = 0; i < lut.length; i++) {
			if (i < steps) {
				lut[i] = 0;
			}
			if (i >= steps) {
				lut[i] = 255;
			}
		}
		lutRo = lutGo = lutBo = lut;
	}

	public void otsu() {

		BufferedImage tempImage = mainPanel.imagePanel.img;
		grayScale(mainPanel.imagePanel);
		int width = mainPanel.imagePanel.img.getWidth();
		int height = mainPanel.imagePanel.img.getHeight();
		int amountOfPixels = width * height;
		int threshold;

		int[] histogram = new int[256];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(tempImage.getRGB(i, j));
				histogram[c.getRed()]++;
			}
		}

		float sum = 0;
		for (int t = 0; t < 256; t++)
			sum += t * histogram[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		threshold = 0;

		for (int t = 0; t < 256; t++) {
			wB += histogram[t]; // w background/f
			if (wB == 0)
				continue;
			wF = amountOfPixels - wB;
			if (wF == 0)
				break;

			sumB += (float) (t * histogram[t]);

			float mB = sumB / wB; // m background / f
			float mF = (sum - sumB) / wF;

			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF); // Between
																				// Class
																				// Variance

			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}
		System.out.println(threshold + "SSVSFDDFFNJHNWEJMKBE W");
		thresholdOtsu(threshold);
		repaintImageo(mainPanel.imagePanel);
	}

	public void localThresholdNiblack(ImagePanel image, double k, int length) {

		BufferedImage tempImage = new BufferedImage(image.img.getWidth(), image.img.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		int height = image.img.getHeight();
		int width = image.img.getWidth();
		int[][] niblacklut = new int[width][height];
		int square = 0;
		for (int i = 1; i < length; i = i + 2) {
			square++;
		}
		int[] tempSquare = new int[(int) Math.pow(length, 2)];
		double sum = 0;
		double average = 0;
		double variance = 0;
		int addToTable = 0;
		double th = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				sum = 0;
				average = 0;
				variance = 0;
				for (int x = i - square; x < i + length - square; x++) {
					for (int y = j - square; y < j + length - square; y++) {
						if (x < 0 || y < 0 || x > (width - 1) || y > (height - 1)) {
							addToTable++;
						} else {
							sum += (image.img.getRGB(x, y) >> 16) & 0xFF;
							tempSquare[addToTable] = (image.img.getRGB(x, y) >> 16) & 0xFF;
							addToTable++;
						}
					}
				}
				average = sum / Math.pow(length, 2);
				for (int z = 0; z < Math.pow(length, 2); z++) {
					variance += Math.pow((tempSquare[z] - average), 2);
				}
				variance = variance / Math.pow(length, 2);
				th = average + k * Math.sqrt(variance); // wart progu
				if (((image.img.getRGB(i, j) >> 16) & 0xFF) < th) {
					tempImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
					addToTable = 0;
				} else {
					tempImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
					addToTable = 0;
				}
			}
		}
		image.img = tempImage;
		image.repaint();
	}

	public void sobel() {

		double A[][], B[][], Ar[][], Br[][], Ag[][], Bg[][], Ab[][], Bb[][], G[][], Gr[][], Gg[][], Gb[][];
		BufferedImage inImg = mainPanel.imagePanel.img;
		int width = inImg.getWidth();
		int height = inImg.getHeight();
		int[] pixels = new int[width * height];

		// RGB channels of the image
		int[][] red = new int[height][width];
		int[][] green = new int[height][width];
		int[][] blue = new int[height][width];

		try {
			inImg.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int counter = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// get color of each pixel and separate it in the RGB components
				Color c = new Color(pixels[counter]);
				red[i][j] = c.getRed();
				green[i][j] = c.getGreen();
				blue[i][j] = c.getBlue();
				counter = counter + 1;
			}
		}

		// Arrays for RGB values (Ar, Br, Ag, Bg, Ab, Bb) which are than
		// combined into final array for generating processed image
		A = new double[height][width];
		B = new double[height][width];
		Ar = new double[height][width];
		Br = new double[height][width];
		Ag = new double[height][width];
		Bg = new double[height][width];
		Ab = new double[height][width];
		Bb = new double[height][width];
		G = new double[height][width];
		Gr = new double[height][width];
		Gg = new double[height][width];
		Gb = new double[height][width];

		/**
		 * Transform pixel p of each RGB channel p = sqrt(A^2 + B^2), where A =
		 * (p3 + 2*p4 + p5) - (p1 + 2*p8 + p7) and B = (p1 + 2*p2 + p3) - (p7 +
		 * 2*p6 + p5)
		 * 
		 * Pixel p
		 * 
		 * p1 p2 p3 p8 p p4 p7 p6 p5
		 */
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i == 0 || i == height - 1 || j == 0 || j == width - 1)
					A[i][j] = B[i][j] = G[i][j] = Ar[i][j] = Br[i][j] = Gr[i][j] = Ag[i][j] = Bg[i][j] = Gg[i][j] = Ab[i][j] = Bb[i][j] = Gb[i][j] = 0; // Image
																																						// boundary
																																						// cleared
				else {
					// RED CHANNEL
					Ar[i][j] = red[i - 1][j + 1] + 2 * red[i][j + 1] + red[i + 1][j + 1] - red[i - 1][j - 1]
							- 2 * red[i][j - 1] - red[i + 1][j - 1];
					Br[i][j] = red[i - 1][j - 1] + 2 * red[i - 1][j] + red[i - 1][j + 1] - red[i + 1][j - 1]
							- 2 * red[i + 1][j] - red[i + 1][j + 1];

					// Gr[i][j] = Math.sqrt(Ar[i][j] * Ar[i][j] + Br[i][j] *
					// Br[i][j]);
					Gr[i][j] = Ar[i][j];

					// GREEN CHANNEL
					Ag[i][j] = green[i - 1][j + 1] + 2 * green[i][j + 1] + green[i + 1][j + 1] - green[i - 1][j - 1]
							- 2 * green[i][j - 1] - green[i + 1][j - 1];
					Bg[i][j] = green[i - 1][j - 1] + 2 * green[i - 1][j] + green[i - 1][j + 1] - green[i + 1][j - 1]
							- 2 * green[i + 1][j] - green[i + 1][j + 1];

					// Gg[i][j] = Math.sqrt(Ag[i][j] * Ag[i][j] + Bg[i][j] *
					// Bg[i][j]);
					Gg[i][j] = Ag[i][j];

					// BLUE CHANNEL
					Ab[i][j] = blue[i - 1][j + 1] + 2 * blue[i][j + 1] + blue[i + 1][j + 1] - blue[i - 1][j - 1]
							- 2 * blue[i][j - 1] - blue[i + 1][j - 1];
					Bb[i][j] = blue[i - 1][j - 1] + 2 * blue[i - 1][j] + blue[i - 1][j + 1] - blue[i + 1][j - 1]
							- 2 * blue[i + 1][j] - blue[i + 1][j + 1];
							// System.out.println(output[i][j]);

					// Gb[i][j] = Math.sqrt(Ab[i][j] * Ab[i][j] + Bb[i][j] *
					// Bb[i][j]);
					Gb[i][j] = Ab[i][j];

					if ((int) Gg[i][j] > 255) {
						Gg[i][j] = 255;
					}
					if ((int) Gb[i][j] > 255) {
						Gb[i][j] = 255;
					}
					if ((int) Gr[i][j] > 255) {
						Gr[i][j] = 255;
					}

					if ((int) Gg[i][j] < 0) {
						Gg[i][j] = 0;
					}
					if ((int) Gb[i][j] < 0) {
						Gb[i][j] = 0;
					}
					if ((int) Gr[i][j] < 0) {
						Gr[i][j] = 0;
					}

					G[i][j] = new Color((int) Gr[i][j], (int) Gg[i][j], (int) Gb[i][j]).getRGB();

				}
			}
		}
		counter = 0;
		for (int ii = 0; ii < height; ii++) {
			for (int jj = 0; jj < width; jj++) {
				pixels[counter] = (int) G[ii][jj];
				counter = counter + 1;
			}
		}

		BufferedImage outImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		System.out.println(
				"pixels.length = " + pixels.length + "; Image size: " + outImg.getHeight() + "x" + outImg.getWidth());

		// outImg.getRaster().setPixels(0,0,width,height,pixels);
		outImg.setRGB(0, 0, width, height, pixels, 0, width);

		mainPanel.imagePanel.img = outImg;
		mainPanel.imagePanel.repaint();

	}
	
	

	public void normalizeMatrix(double[][] matrix) {
		int sum = 0;

		for (int i = 0; i <= 2; i++) {

			for (int j = 0; j <= 2; j++) {
				sum += matrix[i][j];
			}
		}

		if (sum > 0) {

			for (int i = 0; i <= 2; i++) {

				for (int j = 0; j <= 2; j++) {
					matrix[i][j] = matrix[i][j] / sum;
				}
			}
		}

	}

	public void sobel2() {

		// double[][] sobel_y = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };

		//double[][] sobel_y = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		double[][] sobel_y = { { 1, 1, 1 }, { 1,1, 1 }, { 1, 1, 1 } };

		/*
		 * int [][] sobel_y = {{-1,-2,-1}, {0,0,0}, {1,2,1}};
		 */
		normalizeMatrix(sobel_y);

		for (int i = 0; i <= 2; i++) {

			for (int j = 0; j <= 2; j++) {
				System.out.println(sobel_y[i][j]);
			}
		}
		double A[][], B[][], xR[][], yR[][], xG[][], yG[][], xB[][], yB[][], G[][], Gr[][], Gg[][], Gb[][];
		BufferedImage inImg = mainPanel.imagePanel.img;
		int width = inImg.getWidth();
		int height = inImg.getHeight();
		int[] pixels = new int[width * height];

		// RGB channels of the image
		int[][] red = new int[height][width];
		int[][] green = new int[height][width];
		int[][] blue = new int[height][width];

		try {
			inImg.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int counter = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color c = new Color(pixels[counter]);
				red[i][j] = c.getRed() + 128;
				green[i][j] = c.getGreen() + 128;
				blue[i][j] = c.getBlue() + 128;
				counter = counter + 1;
			}
		}

		yR = new double[height][width];
		yG = new double[height][width];
		yB = new double[height][width];
		G = new double[height][width];
		Gr = new double[height][width];
		Gg = new double[height][width];
		Gb = new double[height][width];

		for (int i = 0; i < height; i++) { // i = y, j = x
			for (int j = 0; j < width; j++) {
				if (i == 0 || i == height - 1 || j == 0 || j == width - 1)
					G[i][j] = yR[i][j] = Gr[i][j] = yG[i][j] = Gg[i][j] = yB[i][j] = Gb[i][j] = 0; // Image

				else {
					// R

					yR[i][j] = sobel_y[0][0] * red[i - 1][j - 1] + sobel_y[0][1] * red[i - 1][j]
							+ sobel_y[0][2] * red[i - 1][j + 1] + sobel_y[1][0] * red[i][j - 1]
							+ sobel_y[1][1] * red[i][j] + sobel_y[1][2] * red[i][j + 1] +

					+sobel_y[2][0] * red[i + 1][j - 1] + sobel_y[2][1] * red[i + 1][j]
							+ sobel_y[2][2] * red[i + 1][j + 1];

					Gr[i][j] = yR[i][j];

					// G

					yG[i][j] = sobel_y[0][0] * green[i - 1][j - 1] + sobel_y[0][1] * green[i - 1][j]
							+ sobel_y[0][2] * green[i - 1][j + 1] + sobel_y[1][0] * green[i][j - 1]
							+ sobel_y[1][1] * green[i][j] + sobel_y[1][2] * green[i][j + 1] +

					+sobel_y[2][0] * green[i + 1][j - 1] + sobel_y[2][1] * green[i + 1][j]
							+ sobel_y[2][2] * green[i + 1][j + 1];
					Gg[i][j] = yG[i][j];

					// B

					yB[i][j] = sobel_y[0][0] * blue[i - 1][j - 1] + sobel_y[0][1] * blue[i - 1][j]
							+ sobel_y[0][2] * blue[i - 1][j + 1] + sobel_y[1][0] * blue[i][j - 1]
							+ sobel_y[1][1] * blue[i][j] + sobel_y[1][2] * blue[i][j + 1] +

					+sobel_y[2][0] * blue[i + 1][j - 1] + sobel_y[2][1] * blue[i + 1][j]
							+ sobel_y[2][2] * blue[i + 1][j + 1];
					Gb[i][j] = yB[i][j];

					if ((int) Gg[i][j] > 255) {
						Gg[i][j] = 255;
					}
					if ((int) Gb[i][j] > 255) {
						Gb[i][j] = 255;
					}
					if ((int) Gr[i][j] > 255) {
						Gr[i][j] = 255;
					}

					if ((int) Gg[i][j] < 0) {
						Gg[i][j] = 0;
					}
					if ((int) Gb[i][j] < 0) {
						Gb[i][j] = 0;
					}
					if ((int) Gr[i][j] < 0) {
						Gr[i][j] = 0;
					}

					G[i][j] = new Color((int) Gr[i][j], (int) Gg[i][j], (int) Gb[i][j]).getRGB();
				}
			}
		}
		counter = 0;
		for (int ii = 0; ii < height; ii++) {
			for (int jj = 0; jj < width; jj++) {
				pixels[counter] = (int) G[ii][jj];
				counter = counter + 1;
			}
		}

		BufferedImage outImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		System.out.println(
				"pixels.length = " + pixels.length + "; Image size: " + outImg.getHeight() + "x" + outImg.getWidth());

		// outImg.getRaster().setPixels(0,0,width,height,pixels);
		outImg.setRGB(0, 0, width, height, pixels, 0, width);

		mainPanel.imagePanel.img = outImg;
		mainPanel.imagePanel.repaint();

	}

	public Color grayScalePix(Color c) {

		int intColor = Math.round((c.getRed() + c.getGreen() + c.getBlue()) / 3);
		Color c2 = new Color(intColor, intColor, intColor);
		/*
		 * image.setRGB(i, j, c.getRGB()); image.setRGB(i, j, c.getRGB());
		 */
		return c2;

	}
}
