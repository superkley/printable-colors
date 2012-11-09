/*
 * Copyright (c) 2012 Xiaoyun Zhu
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.kk.printablecolors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Demo class
 */
public class Main extends JPanel {
  private static final long serialVersionUID = -7485098391743276874L;

  class ColorTracker implements ActionListener {
    JColorChooser chooser;
    Color         color;

    public ColorTracker(JColorChooser c) {
      this.chooser = c;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.color = this.chooser.getColor();
    }

    public Color getColor() {
      return this.color;
    }
  }

  private static final int PADDING = 6;

  public static void main(String[] arguments) throws IOException {
    JFrame frame = new JFrame("Printable Color Test");
    frame.setIconImage(ImageIO.read(Main.class.getResource("/print.png")));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Main finder = new Main();
    frame.setContentPane(finder);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    finder.chooseColor();
  }

  ColorInfoPanel actualColorPanel;

  ColorInfoPanel printableColorPanel;

  public Main() {
    this.setBackground(new Color(45, 45, 45));
    GridLayout lyt = new GridLayout(1, 2, Main.PADDING, Main.PADDING);
    this.setLayout(lyt);
    this.setPreferredSize(new Dimension(600, 300));
    this.actualColorPanel = new ColorInfoPanel("actual", new Color(255, 255, 0));
    this.printableColorPanel = new ColorInfoPanel("printable", PrintableColors.findPrintableColor(this.actualColorPanel.getColor()));
    this.add(this.actualColorPanel);
    this.add(this.printableColorPanel);
    this.actualColorPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Main.this.chooseColor();
      }
    });
  }

  void chooseColor() {
    final JColorChooser chooser = new JColorChooser(this.actualColorPanel.getColor());
    chooser.getSelectionModel().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Main.this.updateColor(chooser.getColor());
      }
    });

    final ColorTracker okListener = new ColorTracker(chooser);
    final JDialog dialog = JColorChooser.createDialog(this, "Choose Color", true, chooser, okListener, null);
    dialog.setVisible(true);

    final Color c = okListener.getColor();
    this.updateColor(c);
  }

  @Override
  public Insets getInsets() {
    return new Insets(Main.PADDING, Main.PADDING, Main.PADDING, Main.PADDING);
  }

  protected void updateColor(final Color c) {
    if (c != null) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Main.this.actualColorPanel.setColor(c);
          Main.this.printableColorPanel.setColor(PrintableColors.findPrintableColor(c));
          Main.this.actualColorPanel.repaint();
          Main.this.printableColorPanel.repaint();
        }
      });
    }
  }
}
