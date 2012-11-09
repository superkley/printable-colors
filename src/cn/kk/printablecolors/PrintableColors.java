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
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to find printable colors.
 */
public final class PrintableColors {
    private static final List<Color> PRESETS;
    static {
        PRESETS = new ArrayList<Color>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    PrintableColors.class.getResourceAsStream("/presets.res"), Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                Color c = readColor(line);
                if (c != null) {
                    PRESETS.add(c);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Unable to find the 'presets.res' file in the classpath!");
            e.printStackTrace();
        }
    }

    public static Color findPrintableColor(Color color) {
        final int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };
        final float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], new float[3]);

        // System.out.println("rgb: " + Arrays.toString(rgb));
        // System.out.println("hsb: " + Arrays.toString(hsb));
        if (rgb[0] == rgb[1] && rgb[1] == rgb[2]) {
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2] * 0.7f);
        } else {
            double min = Integer.MAX_VALUE;
            Color result = color;
            for (Color c : PRESETS) {
                int dR = c.getRed() - color.getRed();
                int dB = c.getBlue() - color.getBlue();
                int dG = c.getGreen() - color.getGreen();

                // weighted difference
                double diff = Math.sqrt(dR * dR * 0.241 + dG * dG * 0.691 + dB * dB * 0.068);
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