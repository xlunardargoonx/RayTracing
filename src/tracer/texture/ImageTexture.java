package tracer.texture;

import tracer.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTexture extends Texture
{
//    char data[];
//    int nx, ny;
    BufferedImage img;

    public ImageTexture()
    {
        try
        {
            img = ImageIO.read(new File("data/earth.jpg"));
        }
        catch(IOException e)
        {

        }
    }

    public ImageTexture(BufferedImage img)
    {
        this.img = img;
    }

    //    public ImageTexture(char[] data, int nx, int ny)
//    {
//        this.data = data;
//        this.nx = nx;
//        this.ny = ny;
//    }

    @Override
    public Vector3 value(double u, double v, Vector3 p)
    {
//        int i = (int)((  u) * nx);
//        int j = (int)((1-v) * ny - 0.001);
//        if(i < 0) i = 0;
//        if(j < 0) j = 0;
//        if(i > nx-1) i = nx-1;
//        if(j > ny-1) j = ny-1;
//        double r = (int)(data[3*i + 3*nx*j]) / 255.0;
//        double g = (int)(data[3*i + 3*nx*j+1]) / 255.0;
//        double b = (int)(data[3*i + 3*nx*j+2]) / 255.0;
//        return new Vector3(r,g,b);
        int nx = img.getWidth();
        int ny = img.getHeight();

        int i = (int)(u * nx);
        int j = (int)((1-v) * ny - 0.001);
        if(i < 0) i = 0;
        if(j < 0) j = 0;
        if(i > nx-1) i = nx-1;
        if(j > ny-1) j = ny-1;
       // System.out.println(i + " " + j);
//        double r = (int)(data[3*i + 3*nx*j]) / 255.0;
//        double g = (int)(data[3*i + 3*nx*j+1]) / 255.0;
//        double b = (int)(data[3*i + 3*nx*j+2]) / 255.0;
        int argb = img.getRGB(i,j);
        double r,g,b;
        b = (argb & 0xFF)/256.0;
        argb = argb >> 8;
        g = (argb & 0xFF)/256.0;
        argb = argb >> 8;
        r = (argb & 0xFF)/256.0;
        //System.out.printf("%x, %x, %x, %x\n", img.getRGB(i,j), r, g, b);
        return new Vector3(r,g,b);
    }
}
