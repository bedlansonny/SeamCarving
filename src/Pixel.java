import java.awt.Color;

public class Pixel implements Comparable<Pixel>
{
    double energy;
    Color color;
    double traveled = Double.MAX_VALUE;
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
        traveled = Double.MAX_VALUE;
    }

    public int compareTo(Pixel otherPixel)
    {
        return (int)(this.traveled - otherPixel.traveled);
    }

}
