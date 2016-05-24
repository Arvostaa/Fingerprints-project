/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint;

import java.util.List;
import java.util.ArrayList;
import fingerprint.Points;
import gui.ImagePanel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class Minutiae {

    private int X;
    private int Y;
    private int counterCrossLine = 0;

    List<Points> listofTargetPoints = new ArrayList<Points>();

    List<Points> listOfEndsPoints = new ArrayList<Points>();

    public void MinutiaeRun(ImagePanel img) throws IOException {

        checkMinutiae(img);
        makeColorPoint(listofTargetPoints, img);
        checkMinutieaEndLine(img);
        makeColorEndLine(listOfEndsPoints, img);
//        repaint(img, panelek, labelek);

    }

    public void checkMinutiae(ImagePanel img) {

        for (int x = 10; x <= img.img.getWidth() - 10; x++) {

            for (int y = 10; y <= img.img.getHeight() - 10; y++) {

                {
                    int clr = img.img.getRGB(x, y);
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;

                    if (red == 0 && green == 0 && blue == 0) {

                        if (checkWallSmallSquare(x, y, img) == true) {
                            if (checkWallBigSquare(x, y, img) == true) {
                                Points point = new Points(x, y);
                                listofTargetPoints.add(point);
                            }
                        }
                    }
                }

            }

        }

    }
/*
    public void repaint(ImagePanel imgPanel, JPanel panelek, JLabel labelek) { // ?????????????????
        labelek.setIcon(new ImageIcon(imgPanel.img));
        panelek.add(labelek);
        panelek.validate();
        panelek.repaint();

    }*/

    public void checkMinutieaEndLine(ImagePanel imgPanel) {

        for (int y = 5; y <= imgPanel.img.getHeight() - 5; y++) {

            for (int x = 5; x <= imgPanel.img.getWidth() - 5; x++) {

                {
                    int clr = imgPanel.img.getRGB(x, y);
                    //  System.out.println("1 if = " + clr);
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;

                    if (red == 0 && green == 0 && blue == 0) {

                        if (checkEndLineSmall(x, y, imgPanel) == true) {
                            //           System.out.println("wchodze z pkt x "+x +" y "+y);
                            if (ignoreEndOfPrintsLine(x, y, imgPanel) == true) {

                                Points point = new Points(x, y);
//                            point.setX(x);
//                            point.setY(y);
                                listOfEndsPoints.add(point);
                            }
                        }
                    }
                }
            }

        }

//        for(int f=0;f<=listOfEndsPoints.size();f++){
//        System.out.println("punkt "+listOfEndsPoints.get(f).getX()+" "+"punkt "+listOfEndsPoints.get(f).getY()+" "+img.getImage().getWidth());
//        }
    }

    public boolean checkEndLineSmall(int i, int j, ImagePanel imgPanel) {
        int m = 3;
        int c = 2;
        counterCrossLine = 0;
//System.out.println("Jestem w check small square");
        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB((i - c) + k, j + c);
            //  System.out.println("1 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0||red==255 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB(i + c, (j + c) - k);
            //  System.out.println("2 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 || red==255 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }
        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB((i + c) - k, j - c);
            //  System.out.println("3 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0|| red==255 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= m; k++) {
            int g = i - c;
            int h = (j - c) + k;
            int clr = imgPanel.img.getRGB(g, h);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0|| red==255 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        if (counterCrossLine == 1) {
            return true;
        }

        return false;

    }

    public boolean ignoreEndOfPrintsLine(int i, int j, ImagePanel img) {
      int  counterCrossLinePrawa = 0;
int  counterCrossLineLewa = 0;
        for (int x = i+2; x <= img.img.getWidth() - 5; x++) {
            //System.out.println("Zawiesilem sie na x "+x +" i pkt "+i+" "+j+" "+img.getImage().getWidth());
            int clr = img.img.getRGB(x, j);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLinePrawa++;

            }
        }
        for (int x = i-2; x >=5; x--) {

            int clr = img.img.getRGB(x, j);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLineLewa++;

            }
        }
        
        if (counterCrossLinePrawa == 0) {
            return false;
        }
          if (counterCrossLineLewa == 0) {
            return false;
        }

        return true;
    }

    public boolean checkWallSmallSquare(int i, int j, ImagePanel imgPanel) {
        int m = 3;
        counterCrossLine = 0;
//System.out.println("Jestem w check small square");
        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB((i - 2) + k, j + 2);
            //  System.out.println("1 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB(i + 2, (j + 2) - k);
            //  System.out.println("2 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }
        for (int k = 0; k <= m; k++) {

            int clr = imgPanel.img.getRGB((i + 2) - k, j - 2);
            //  System.out.println("3 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= m; k++) {
            int g = i - 2;
            int h = (j - 2) + k;
            int clr = imgPanel.img.getRGB(g, h);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        if (counterCrossLine == 3) {
            return true;
        }

        return false;

    }

    public void makeColorPoint(List<Points> points, ImagePanel imgPanel) throws IOException { ///// ????
        int red = 255;
        System.out.println("Punkt " + points.size());
//        System.out.println("Jestem w make point");
        Color newColor = new Color(red, 0, 0);
        int colorek = newColor.getRGB();

        for (int k = 0; k < points.size(); k++) {

            imgPanel.img.setRGB(points.get(k).getX(), points.get(k).getY(), colorek);
            //System.out.println("Jestem w make point");

        }
        imgPanel.repaint();

      //  labelek.setIcon(new ImageIcon(imgPanel.img));
      //  panelek.add(labelek);
     //   panelek.validate();
      //  panelek.repaint();

    }

    public void makeColorEndLine(List<Points> points, ImagePanel imgPanel) throws IOException {
        int green = 255;
        System.out.println("Punkt " + points.size());
//        System.out.println("Jestem w make point");
        Color newColor = new Color(0, green, 0);
        int colorek = newColor.getRGB();

        for (int k = 0; k < points.size(); k++) {

            imgPanel.img.setRGB(points.get(k).getX(), points.get(k).getY(), colorek);
            //System.out.println("Jestem w make point");

        }
        imgPanel.repaint();
       /* labelek.setIcon(new ImageIcon(imgPanel.img));
        panelek.add(labelek);
        panelek.validate();
        panelek.repaint();
*/
    }

    public boolean checkWallBigSquare(int i, int j, ImagePanel imgPanel) {
        int n = 5;
        counterCrossLine = 0;
//System.out.println("Jestem w check small square");
        for (int k = 0; k <= n; k++) {

            int clr = imgPanel.img.getRGB((i - 4) + k, j + 4);
            //  System.out.println("1 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= n; k++) {

            int clr = imgPanel.img.getRGB(i + 4, (j + 4) - k);
            //  System.out.println("2 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }
        for (int k = 0; k <= n; k++) {

            int clr = imgPanel.img.getRGB((i + 4) - k, j - 4);
            //  System.out.println("3 if = " + clr);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        for (int k = 0; k <= n; k++) {
            int g = i - 4;
            int h = (j - 4) + k;
            int clr = imgPanel.img.getRGB(g, h);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;

            if (red == 0 && green == 0 && blue == 0) {
                counterCrossLine++;

            }
        }

        if (counterCrossLine == 3) {
            return true;
        }

        return false;

    }
}
