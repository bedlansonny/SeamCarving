import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;

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
                pixels[y][x] = new Pixel(new Color(image.getRGB(x, y)), x, y);
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
        double rx = pixels[y][(width()+(x-1)%width())%width()].color.getRed() - pixels[y][(width()+(x+1)%width())%width()].color.getRed();
        double gx = pixels[y][(width()+(x-1)%width())%width()].color.getGreen() - pixels[y][(width()+(x+1)%width())%width()].color.getGreen();
        double bx = pixels[y][(width()+(x-1)%width())%width()].color.getBlue() - pixels[y][(width()+(x+1)%width())%width()].color.getBlue();
        double ry = pixels[(height()+(y-1)%height())%height()][x].color.getRed() - pixels[(height()+(y+1)%height())%height()][x].color.getRed();
        double gy = pixels[(height()+(y-1)%height())%height()][x].color.getGreen() - pixels[(height()+(y+1)%height())%height()][x].color.getGreen();
        double by = pixels[(height()+(y-1)%height())%height()][x].color.getBlue() - pixels[(height()+(y+1)%height())%height()][x].color.getBlue();

        return rx*rx+gx*gx+bx*bx+ry*ry+gy*gy+by*by;
    }

    public void updateEnergy(int x, int y)
    {
        pixels[y][x].energy = energy(x,y);
    }

    //unsure if this works yet
    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        for(Pixel[] pixelArr : pixels)
            for(Pixel pixel : pixelArr)
                pixel.resetTraveled();

        //HashMap<Pixel, Double> traveled = new HashMap<>(); //pixel, traveled distance of pixel in ideal path
        HashMap<Pixel, Pixel> prev = new HashMap<>(); //pixel, previous pixel in ideal path
        HashSet<Pixel> visited = new HashSet<>(); //pixels that have already been visited and therefore *** is this necessary???
        PriorityQueue<Pixel> toCheck = new PriorityQueue<>();

        //add top row as all possible starts
        for (int x = 0; x < width(); x++) {
            pixels[0][x].traveled = (long)pixels[0][x].energy;
            toCheck.add(pixels[0][x]);
        }

        while(toCheck.size() > 0)
        {
            Pixel current = toCheck.poll();
            visited.add(current);

            //reached bottom, shortest path
            if(current.y == height()-1)
            {
                int[] output = new int[height()];
                Pixel seamPixel = current;
                for (int y = height()-1; y >=0; y--)
                {
                    output[y] = seamPixel.x;
                    seamPixel = prev.get(seamPixel);
                }
                return output;
            }

            Pixel[] children;
            if(current.x == 0)
                children = new Pixel[] {pixels[current.y+1][current.x], pixels[current.y+1][current.x+1]};
            else if(current.x == width()-1)
                children = new Pixel[] {pixels[current.y+1][current.x-1], pixels[current.y+1][current.x]};
            else
            {
                try
                {
                    //TODO: FIX THIS
                    children = new Pixel[] {pixels[current.y+1][current.x-1], pixels[current.y+1][current.x], pixels[current.y+1][current.x+1]};
                }
                catch(Exception e)
                {
                    System.out.printf("%d%n%d%n%d%n%d%n",width(),height(),current.x,current.y);
                    children = null;
                }
            }

            for(Pixel child : children)
            {
                if(!visited.contains(child) && child.traveled > current.traveled + child.energy)
                {
                    prev.put(child, current);
                    child.traveled = current.traveled + child.energy;
                    toCheck.add(child);
                }
            }
        }
        return null;    //if it fails
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam)
    {
        Pixel[][] output = new Pixel[height()][width()-1];

        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < seam[y]; x++) {
                output[y][x] = pixels[y][x];
            }
            for(int x = seam[y]; x < output[y].length; x++) {
                output[y][x] = new Pixel(pixels[y][x+1], x, y);
            }
        }

        pixels = output;

        //TODO: update energy around the seam, fix this
        for (int y = 0; y < height(); y++) {
            updateEnergy((width() + (seam[y]-1)%width())%width(), y);
            updateEnergy(seam[y]%width(), y);
        }


    }

//    public void carve(char dimension, int amount)   //dimension x or y, amount negative for shrinking positive for growing

//    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
//    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
}
