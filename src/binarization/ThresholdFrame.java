package binarization;

import javax.swing.*;

import Histograms.LUTimage;
import gui.ImagePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThresholdFrame extends JFrame {

    public ThresholdFrame(LUTimage lut, ImagePanel image) {
        super("Amount of steps:");
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField value = new JTextField();
        JButton accpetButton = new JButton("Ok");
        accpetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tryParseInt(value.getText().replace(',', '.'))) {
                    if (!value.getText().equals("") && Integer.parseInt(value.getText()) > 0 && Integer.parseInt(value.getText()) < 256) {
                        lut.grayScale(image);
                        lut.threshold(Integer.parseInt(value.getText()));
                        lut.repaintImage(image);
                        image.repaint();
                        dispose();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(image, "Value > 0");
                }

            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(new JLabel("Steps amount"));
        panel.add(value);
        panel.add(accpetButton);
        panel.add(cancelButton);
        add(panel);
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
}
