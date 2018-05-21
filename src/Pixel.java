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

    public int compareTo(Pixel otherPixel)
    {
        return (int)(this.traveled - otherPixel.traveled);
    }

}
