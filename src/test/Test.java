package test;

import tracer.Camera;
import tracer.SceneResolution;
import tracer.Vector3;
import tracer.hitable.Hitable;
import tracer.hitable.HitableList;
import tracer.hitable.Sphere;
import tracer.hitable.XZRect;
import tracer.integrator.BasicIntegrator;
import tracer.integrator.Integrator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import static tracer.CameraInstance.cam_for_cornell_box;
import static tracer.Scenes.cornell_box;

public class Test {

    public static void main(String[] args)
    {
        SceneResolution resolution = SceneResolution.S_500X500;
        int nx = resolution.getWidth();
        int ny = resolution.getHeight();
        int ns = 200;

        Camera cam = cam_for_cornell_box(nx, ny);
        Hitable world = cornell_box();
        HitableList lights = new HitableList();
        //Hitable light_shape = new XZRect(213, 343, 227, 332, 554, null);
        //Hitable light_shape = new XZRect(113, 443, 127, 432, 554, null);
        Hitable light_shape = new XZRect(123, 423, 147, 412, 554, null);
        Hitable glass_sphere = new Sphere(new Vector3(190, 90, 190), 90, null);
        lights.addHitable(light_shape);
        //lights.addHitable(glass_sphere);
        BufferedImage bufferedImage = new BufferedImage(nx,ny,BufferedImage.TYPE_INT_ARGB);

        Integrator integrator = new BasicIntegrator(ns, nx, ny, cam);

        LocalTime start = LocalTime.now();
        System.out.println("0%; Time: " + LocalTime.now());

        integrator.integrate(world, lights);
        var buf = integrator.getImage_buf();
        for(int j = 0; j < ny; j++)
        {
            for(int i = 0; i < nx; i++)
            {
                var col = buf[i][j];

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
