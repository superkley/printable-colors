package cn.kk.printablecolors;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class PrintableColorsTest {

    @Test
    public void testFindPrintableColor() {
        float maxOriginal = Integer.MIN_VALUE;
        float maxPrint = Integer.MIN_VALUE;
        List<Color> failures = new LinkedList<Color>();
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color c = new Color(r, g, b);
            Color p = PrintableColors.findPrintableColor(c);
            float[] hsbOriginal = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), new float[3]);
            float[] hsbPrint = Color.RGBtoHSB(p.getRed(), p.getGreen(), p.getBlue(), new float[3]);
            if (hsbOriginal[2] > maxOriginal) {
                maxOriginal = hsbOriginal[2];
                System.out.println("c: " + c + ", " + Arrays.toString(hsbOriginal));
            }
            if (hsbPrint[2] > maxPrint) {
                maxPrint = hsbPrint[2];
                System.out.println("p: " + p + ", " + Arrays.toString(hsbPrint));
            }
            if (!((hsbPrint[1] > 0.16f) || (hsbPrint[2] < 0.84f))) {
                if (!failures.contains(p)) {
                    failures.add(p);
                }
            }
            // assertTrue("#" + Integer.toHexString(p.getRGB()).toUpperCase() + ", hsb: " + Arrays.toString(hsbPrint),
            // (hsbPrint[1] > 0.12f) || (hsbPrint[2] < 0.88f));
        }

        for (Color c : failures) {
            System.out.println("#" + Integer.toHexString(c.getRGB()).toUpperCase());
        }
        System.out.println("original:" + maxOriginal + ", print: " + maxPrint + ", failures: " + failures.size());

        if (!failures.isEmpty()) {
            fail();
        }
    }

}
