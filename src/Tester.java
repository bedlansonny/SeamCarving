import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Tester
{
    public static void main(String args[]) throws IOException
    {
        //python's a%b function = (b + a%b)%b
        Scanner in = new Scanner(System.in);

        BufferedImage image = ImageIO.read(new File("teletubbiesB3.jpg"));
        SeamCarver sc = new SeamCarver(image);

        System.out.printf("Current width: %d%nCurrent height: %d%nTarget width: ", sc.width(), sc.height());
        int x = in.nextInt();
        System.out.printf("Target height: ");
        int y = in.nextInt();

        long start = System.currentTimeMillis();

        sc.carveToRes(x, y);

        long end = System.currentTimeMillis();

        System.out.printf("Time: %f seconds%n", (0.0+end-start)/1000);

        BufferedImage imageOutput = sc.image();
        ImageIO.write(imageOutput, "jpg", new File("teletubbiesB4.jpg"));
//        Desktop.getDesktop().open(new File("ansh1.png"));

    }
}
