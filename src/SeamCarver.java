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
    public long energy(int x, int y)
    {
        int rx = pixels[y][(width()+(x-1)%width())%width()].color.getRed() - pixels[y][(width()+(x+1)%width())%width()].color.getRed();
        int gx = pixels[y][(width()+(x-1)%width())%width()].color.getGreen() - pixels[y][(width()+(x+1)%width())%width()].color.getGreen();
        int bx = pixels[y][(width()+(x-1)%width())%width()].color.getBlue() - pixels[y][(width()+(x+1)%width())%width()].color.getBlue();
        int ry = pixels[(height()+(y-1)%height())%height()][x].color.getRed() - pixels[(height()+(y+1)%height())%height()][x].color.getRed();
        int gy = pixels[(height()+(y-1)%height())%height()][x].color.getGreen() - pixels[(height()+(y+1)%height())%height()][x].color.getGreen();
        int by = pixels[(height()+(y-1)%height())%height()][x].color.getBlue() - pixels[(height()+(y+1)%height())%height()][x].color.getBlue();

        return rx*rx+gx*gx+bx*bx+ry*ry+gy*gy+by*by;
    }

    public void updateEnergy(int x, int y)
    {
        pixels[y][x].energy = energy(x,y);
    }

/*
    public int[][] findVerticalSeams(int seamTotal)
    {
        int[][] output = new int[seamTotal][height()];
        HashSet<Pixel> used = new HashSet<>();

        //loop all this stuff below seamTotal times
        for (int seamNum = 0; seamNum < seamTotal; seamNum++)
        {
            for(Pixel[] pixelArr : pixels)
                for(Pixel pixel : pixelArr)
                    pixel.resetTraveled();

            HashMap<Pixel, Pixel> prev = new HashMap<>();
            PriorityQueue<Pixel> toCheck = new PriorityQueue<>();
            HashSet<Pixel> visited = new HashSet<>();

            //add top row as starts
            for (int x = 0; x < width(); x++) {
                if(!used.contains(pixels[0][x]))
                {
                    pixels[0][x].traveled = pixels[0][x].energy;
                    toCheck.add(pixels[0][x]);
                }
            }

            while(true)
            {
                Pixel current = toCheck.poll();
                visited.add(current);

                if(current.y == height()-1)
                {
                    Pixel seamPixel = current;
                    for(int y = height()-1; y >= 0; y--)
                    {
                        used.add(seamPixel);
                        output[seamNum][y] = seamPixel.x;
                        seamPixel = prev.get(seamPixel);
                    }
                    break;
                }

                //find true bottom left, true bottom, and true bottom right
                //if used, keep checking
                LinkedList<Pixel> children = new LinkedList<>();
                //bottom
                //TODO: find actual formula for this
                    //this is not correct, because true bottom
                    //can also be on its left
                for(int x = current.x; x < width(); x++)
                {
                    if(!used.contains(pixels[current.y+1][x]))
                    {
                        children.add(pixels[current.y+1][x]);
                        break;
                    }
                }

                //bottom right, start looking from the bottom found above
                if(current.x < width()-1)
                {

                }

                //bottom left
                if(current.x > 0)
                {

                }



            }
        }
    }
*/

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        for(Pixel[] pixelArr : pixels)
            for(Pixel pixel : pixelArr)
                pixel.resetTraveled();

        HashMap<Pixel, Pixel> prev = new HashMap<>(); //pixel, previous pixel in ideal path
        HashSet<Pixel> visited = new HashSet<>(); //pixels that have already been visited and therefore *** is this necessary???
        PriorityQueue<Pixel> toCheck = new PriorityQueue<>();

        //add top row as all possible starts
        for (int x = 0; x < width(); x++) {
            pixels[0][x].traveled = pixels[0][x].energy;
            toCheck.add(pixels[0][x]);
        }

        while(true)
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
                children = new Pixel[] {pixels[current.y+1][current.x-1], pixels[current.y+1][current.x], pixels[current.y+1][current.x+1]};

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

        for (int y = 0; y < height(); y++) {
            updateEnergy((width() + (seam[y]-1)%width())%width(), y);
            updateEnergy(seam[y]%width(), y);
        }


    }

    //sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        for(Pixel[] pixelArr : pixels)
            for(Pixel pixel : pixelArr)
                pixel.resetTraveled();

        HashMap<Pixel, Pixel> prev = new HashMap<>(); //pixel, previous pixel in ideal path
        HashSet<Pixel> visited = new HashSet<>(); //pixels that have already been visited and therefore *** is this necessary???
        PriorityQueue<Pixel> toCheck = new PriorityQueue<>();

        //add top row as all possible starts
        for (int y = 0; y < height(); y++) {
            pixels[y][0].traveled = (long)pixels[y][0].energy;
            toCheck.add(pixels[y][0]);
        }

        while(true)
        {
            Pixel current = toCheck.poll();
            visited.add(current);

            //reached bottom, shortest path
            if(current.x == width()-1)
            {
                int[] output = new int[width()];
                Pixel seamPixel = current;
                for (int x = width()-1; x >=0; x--)
                {
                    output[x] = seamPixel.y;
                    seamPixel = prev.get(seamPixel);
                }
                return output;
            }


            Pixel[] children;
            if(current.y == 0)
                children = new Pixel[] {pixels[current.y][current.x+1], pixels[current.y+1][current.x+1]};
            else if(current.y == height()-1)
                children = new Pixel[] {pixels[current.y-1][current.x+1], pixels[current.y][current.x]};
            else
                children = new Pixel[] {pixels[current.y-1][current.x+1], pixels[current.y][current.x+1], pixels[current.y+1][current.x+1]};

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
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam)
    {
        Pixel[][] output = new Pixel[height()-1][width()];

        for (int x = 0; x < output[0].length; x++) {
            for (int y = 0; y < seam[x]; y++) {
                output[y][x] = pixels[y][x];
            }
            for(int y = seam[x]; y < output.length; y++) {
                output[y][x] = new Pixel(pixels[y+1][x], x, y);
            }
        }

        pixels = output;

        for (int x = 0; x < width(); x++) {
            updateEnergy(x, (height() + (seam[x]-1)%height())%height());
            updateEnergy(x, seam[x]%height());
        }
    }

    public void removeVerticalSeams(int seamTotal)
    {
        for (int i = 0; i < seamTotal; i++) {
            removeVerticalSeam(findVerticalSeam());
        }
    }

    public void removeHorizontalSeams(int seamTotal)
    {
        for (int i = 0; i < seamTotal; i++) {
            removeHorizontalSeam(findHorizontalSeam());
        }
    }

    public void carveToRes(int x, int y)
    {
        removeVerticalSeams(width()-x);
        removeHorizontalSeams(height()-y);
    }

    class Pixel implements Comparable<Pixel>
    {
        long energy;
        Color color;
        long traveled = Long.MAX_VALUE;
        int x, y;

        public Pixel(Color color, int x, int y)
        {
            this.color = color;
            this.x = x;
            this.y = y;
        }

        public Pixel(Pixel pixel, int x, int y)
        {
            this.energy = pixel.energy;
            this.color = pixel.color;

            this.x = x;
            this.y = y;
        }

        public void resetTraveled()
        {
            traveled = Long.MAX_VALUE;
        }

        public int compareTo(Pixel otherPixel)
        {
            return (int)(this.traveled - otherPixel.traveled);
        }

    }

}
