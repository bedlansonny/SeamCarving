import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import java.net.URL;

public class Tester
{
    public static void main(String args[]) throws IOException
    {
        //ImageIO.write(image,suffix,new File("filename"));
        //python's a%b function = (b + a%b)%b

        BufferedImage image = ImageIO.read(new File("tower.jpg"));
        SeamCarver sc = new SeamCarver(image);

        for (int i = 0; i < 200; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }

        BufferedImage imageOutput = sc.image();
        ImageIO.write(imageOutput, "jpg", new File("toweroutput3.jpg"));
    }
}
