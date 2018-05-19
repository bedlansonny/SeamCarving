import java.awt.Color;
import java.awt.image.BufferedImage;

public class SeamCarver
{
    Pixel[][] pixels;

    //duplicates image in pixels array and calculates energy of all pixels
    public SeamCarver(BufferedImage image)
    {
        pixels = new Pixel[image.getHeight()][image.getWidth()];

        for(int y = 0; y < height(); y++)
        {
            for(int x = 0; x < width(); x++)
            {
                pixels[y][x] = new Pixel(new Color(image.getRGB(x, y)));
            }
        }

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                updateEnergy(x,y);
            }
        }
    }

    //displays image from the array of pixels
    public BufferedImage image()
    {
        BufferedImage output = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                output.setRGB(x, y, pixels[y][x].color.getRGB());
            }
        }

        return output;
    }

    // width of current picture
    public int width()
    {
        return pixels[0].length;
    }

    // height of current picture
    public int height()
    {
        return pixels.length;
    }

    // energy of pixel at row y and column x: Rx2+Gx2+Bx2+Ry2+Gy2+By2
    public double energy(int x, int y)
    {
        double rx = pixels[y][(x-1)%width()].color.getRed() - pixels[y][(x+1)%width()].color.getRed();
        double gx = pixels[y][(x-1)%width()].color.getGreen() - pixels[y][(x+1)%width()].color.getGreen();
        double bx = pixels[y][(x-1)%width()].color.getBlue() - pixels[y][(x+1)%width()].color.getBlue();
        double ry = pixels[(y-1)%height()][x].color.getRed() - pixels[(y+1)%height()][x].color.getRed();
        double gy = pixels[(y-1)%height()][x].color.getGreen() - pixels[(y+1)%height()][x].color.getGreen();
        double by = pixels[(y-1)%height()][x].color.getBlue() - pixels[(y+1)%height()][x].color.getBlue();

        return rx*rx+gx*gx+bx*bx+ry*ry+gy*gy+by*by;
    }

    public void updateEnergy(int x, int y)
    {
        pixels[y][x].energy = energy(x,y);
    }

    //TODO: use Dijkstra's to find vertical seam
    //    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam

//    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from picture

//    public void carve(char dimension, int amount)   //dimension x or y, amount negative for shrinking positive for growing

//    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
//    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
}
