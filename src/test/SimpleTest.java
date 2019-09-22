package test;

import tracer.*;
import tracer.hitable.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

import static tracer.CameraInstance.*;
import static tracer.HelperFunctions.*;
import static tracer.Scenes.*;

public class SimpleTest {

    static Random rand = new Random(42);

    public static void main(String[] args)
    {
        SceneResolution resolution = SceneResolution.S_200X100;
        int nx = resolution.getWidth();
        int ny = resolution.getHeight();
        int ns = 100;

        Camera cam = cam_for_final(nx, ny);
        Hitable world = final_scene();
        BufferedImage bufferedImage = new BufferedImage(nx,ny,BufferedImage.TYPE_INT_ARGB);

        LocalTime start = LocalTime.now();
        System.out.println("0%; Time: " + LocalTime.now());
        for(int j = 0; j < ny; j++)
        {
            for(int i = 0; i < nx; i++)
            {
                Vector3 col = new Vector3();
                for(int s = 0; s < ns; s++)
                {
                    double u = ((double) i + rand.nextDouble()) / (double) nx;
                    double v = ((double) j + rand.nextDouble()) / (double) ny;

                    Ray r = cam.getRay(u, v);
                    col = col.addVec(color(r, world, 0));
                }
                col = col.divideConst(ns);
                //gamma corrected "gamma 2" = square root = raise to 1/gamma
                //better gamma 2.2
                col = new Vector3(Math.pow(col.r(),1/2.2), Math.pow(col.g(), 1/2.2), Math.pow(col.b(), 1/2.2));

                col.copyValue(boundingColor(col));
                int ir = (int)(254.99*col.r());
                int ig = (int)(254.99*col.g());
                int ib = (int)(254.99*col.b());

                int argb = 255;
                argb = argb << 24;
                argb ^= ir << 16;
                argb ^= ig << 8;
                argb ^= ib;
                bufferedImage.setRGB(i, ny-j-1, argb);
            }
            double percent = ((double)j) / ((double)ny) * 100.0;
            if(percent >= 10 && percent % 10 == 0)
                System.out.println(percent + "%; Time: " + LocalTime.now());
        }
        LocalTime end = LocalTime.now();
        System.out.println("100%; Time: " + end);
        System.out.println("Time to finish: " + (end.toSecondOfDay() - start.toSecondOfDay()) + " second(s)");
        File file = new File("img\\test.png");
        try
        {
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
