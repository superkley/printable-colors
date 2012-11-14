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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to find printable colors.
 */
public final class PrintableColors {
    // grey, red, orange, yellow, ...
    private static final float[] DARKER_CONSTANTS = { 0.4f, 0.15f, 0.20f, 0.25f, 0.30f, 0.25f, 0.20f, 0.15f, 0.15f };
    private static final List<Color> PRESETS;
    static {
        PRESETS = new ArrayList<Color>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    PrintableColors.class.getResourceAsStream("/presets.res"), Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                Color c = PrintableColors.readColor(line);
                if (c != null) {
                    PrintableColors.PRESETS.add(c);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Unable to find the 'presets.res' file in the classpath!");
            e.printStackTrace();
        }
    }

    public static Color findBeautifulColor(Color color) {
        final int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };

        // System.out.println("rgb: " + Arrays.toString(rgb));
        // System.out.println("hsb: " + Arrays.toString(hsb));
        if ((rgb[0] == rgb[1]) && (rgb[1] == rgb[2])) {
            final float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);
            final float[] d = darker(hsb, true);
            return Color.getHSBColor(d[0], d[1], d[2]);
        } else {
            double min = Integer.MAX_VALUE;
            Color result = color;
            for (Color c : PrintableColors.PRESETS) {
                int dR = c.getRed() - color.getRed();
                int dB = c.getBlue() - color.getBlue();
                int dG = c.getGreen() - color.getGreen();

                // weighted difference
                double diff = Math.sqrt(dR * dR + dG * dG + dB * dB);
                if (diff < min) {
                    min = diff;
                    result = c;
                }
                if (min < 1d) {
                    break;
                }
            }
            // System.out.println("from presets: " + result + ", min: " + min);
            return result;
        }
    }

    private static float[] darker(float[] hsb) {
        return darker(hsb, false);
    }

    private static float[] darker(float[] hsb, boolean grey) {
        if (grey) {
            return new float[] { hsb[0], hsb[1], Math.max(0f, hsb[2] - DARKER_CONSTANTS[0]) };
        } else {
            return new float[] { hsb[0], Math.min(1f, hsb[1] + getDarkerConstant(hsb[0])),
                    Math.max(0f, hsb[2] - getDarkerConstant(hsb[0])) };
        }
    }

    public static Color findPrintableColor(Color color) {
        final int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };
        final float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);
        if (rgb[0] == rgb[1] && rgb[1] == rgb[2]) {
            final float[] d = darker(hsb, true);
            return Color.getHSBColor(d[0], d[1], d[2]);
        } else {
            final float[] d = darker(hsb);
            return Color.getHSBColor(d[0], d[1], d[2]);
        }
    }

    private static float getDarkerConstant(float hue) {
        final int idx = (int) (hue * 8 + 1);
        return DARKER_CONSTANTS[idx];
    }

    private static Color readColor(String line) {
        String str;
        if (line.length() > 6) {
            str = line.trim();
        } else {
            str = line;
        }
        if (str.length() != 6) {
            return null;
        }
        return new Color(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16),
                Integer.parseInt(str.substring(4, 6), 16));
    }
}
