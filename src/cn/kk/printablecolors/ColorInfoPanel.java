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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Demonstration panel showing the color as a circle and some color data.
 */
public class ColorInfoPanel extends JPanel {
  private static final long  serialVersionUID    = 5908666756253061714L;
  private static final float PADDING             = 6f;
  private static final float TXT_HEIGHT_LABEL    = 16f;
  private static final float TXT_HEIGHT_TITLE    = 18f;
  private static final float TXT_MAX_WIDTH_LABEL = 60f;
  private final String       title;
  private Color              color;

  public ColorInfoPanel(String title, Color color) {
    this.title = title;
    this.color = color;
    this.setBorder(null);
  }

  public Color getColor() {
    return this.color;
  }

  private static String pad(String str) {
    return (str.length() < 2 ? "0" + str : str).toUpperCase();
  }

  @Override
  public void paint(Graphics g) {
    super.paintComponent(g);
    final float w = this.getWidth();
    final float h = this.getHeight();
    final float d = Math.min(w, h);
    final float c = d / 2f;
    final Shape circle = new Ellipse2D.Float(0.0f, 0, d, d);
    final Shape center = new Rectangle2D.Float(c, c, 1.0f, 1.0f);
    final Font fntTitle = new Font("Serif", Font.PLAIN, 16);
    final Font fntLabel = new Font("SansSerif", Font.PLAIN, 10);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setFont(fntTitle);

    g2d.setPaint(Color.BLACK);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

    g2d.setPaint(this.color);
    g2d.fill(circle);

    g2d.setPaint(Color.LIGHT_GRAY);
    g2d.fill(center);

    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawString(this.title, ColorInfoPanel.PADDING, ColorInfoPanel.TXT_HEIGHT_TITLE);

    final int[] rgb = { this.color.getRed(), this.color.getGreen(), this.color.getBlue() };
    final int[] rgbPct = { Math.round(this.color.getRed() / 2.55f), Math.round(this.color.getGreen() / 2.55f), Math.round(this.color.getBlue() / 2.55f) };
    final float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);
    final String rgbTxt = "#" + ColorInfoPanel.pad(Integer.toHexString(rgb[0])) + ColorInfoPanel.pad(Integer.toHexString(rgb[1]))
        + ColorInfoPanel.pad(Integer.toHexString(rgb[2]));
    hsb[0] *= 360f;
    hsb[1] *= 100f;
    hsb[2] *= 100f;

    g2d.setColor(Color.WHITE);
    g2d.setFont(fntLabel);
    g2d.drawString("RGB: " + rgbTxt, ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 4f) - ColorInfoPanel.PADDING);

    g2d.drawString("H: " + Math.round(hsb[0]) + "�", ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 3f));
    g2d.drawString("S: " + Math.round(hsb[1]), ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 2f));
    g2d.drawString("B: " + Math.round(hsb[2]), ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 1f));

    g2d.drawString("R: " + rgb[0], ColorInfoPanel.TXT_MAX_WIDTH_LABEL + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 3f));
    g2d.drawString("G: " + rgb[1], ColorInfoPanel.TXT_MAX_WIDTH_LABEL + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 2f));
    g2d.drawString("B: " + rgb[2], ColorInfoPanel.TXT_MAX_WIDTH_LABEL + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 1f));

    g2d.drawString("R: " + rgbPct[0] + " %", (2f * ColorInfoPanel.TXT_MAX_WIDTH_LABEL) + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 3f));
    g2d.drawString("G: " + rgbPct[1] + " %", (2f * ColorInfoPanel.TXT_MAX_WIDTH_LABEL) + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 2f));
    g2d.drawString("B: " + rgbPct[2] + " %", (2f * ColorInfoPanel.TXT_MAX_WIDTH_LABEL) + ColorInfoPanel.PADDING, h - (ColorInfoPanel.TXT_HEIGHT_LABEL * 1f));
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
