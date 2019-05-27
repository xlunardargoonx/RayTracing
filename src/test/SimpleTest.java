package test;

import tracer.*;
import tracer.hitable.*;
import tracer.material.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Random;

public class SimpleTest {

    public static Vector3 color(Ray r, Hitable world, int depth)
    {
        HitRecord rec = new HitRecord();
        //changing min from 0.0 to 0.001 to get rid of shadow acne
        if(world.hit(r, 0.001, Double.MAX_VALUE, rec))
        {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            if(depth < 50 && rec.getMat().scatter(r, rec, attenuation, scattered))
            {
                return attenuation.multiplyVec(color(scattered,world,depth+1));
            }
            else
            {
                return new Vector3(0,0,0);
            }
        }
        else
        {
            Vector3 unit_dir = Vector3.unit_vec(r.direction());
            double t = 0.5 * (unit_dir.y() + 1.0);
            return new Vector3(1.0, 1.0, 1.0).multiplyConst(1.0 - t).addVec(new Vector3(0.5, 0.7, 1.0).multiplyConst(t));
        }
    }

    public static void main(String[] args)
    {
        System.out.println(1.0/0.0);
        int nx = 200;//3840;//1280;//3840;//1920;//200;
        int ny = 100;//2160;//720;//2160;//1080;//100;
        int ns = 100;
        Vector3 lookfrom = new Vector3(13, 2 ,3);//new Vector3(-2,2,1);//new Vector3(3,3,2);
        Vector3 lookat = new Vector3(0, 0, 0);//new Vector3(0,0,-1);
        double dist_to_focus = lookfrom.subtractVec(lookat).length();//10.0;
        double aperture = 0.25;

        Camera cam = new Camera(lookfrom, lookat, new Vector3(0,1,0), 20.0, ((double)nx)/(double)ny, aperture, dist_to_focus, 0.0, 1.0);
//        HitableList world = new HitableList();
//        world.addHitable(new MovingSphere(new Vector3(4, 1, 0), new Vector3(4, 1.0 + 0.5 /*+ rand.nextDouble()*/, 0), 1.0,
//                new Lambertian(new Vector3(0.1, 0.2, 0.5)), 0.0, 1.0));
//        world.addHitable(new MovingSphere(new Vector3(-12, 0, 2), new Vector3(-12, 1.5 /*+ rand.nextDouble()*/, 2), 0.5,
//                new Lambertian(new Vector3(0.8, 0.8, 0.0)), 0.0, 1.0));
//        world.addHitable(new Sphere(new Vector3(0, -1000, 0),1000, new Lambertian(new Vector3(0.5, 0.5, 0.5))));
//        world.addHitable(new Sphere(new Vector3(0, 0, -1), 0.5, new Lambertian(new Vector3(0.1, 0.2, 0.5))));
//        world.addHitable(new Sphere(new Vector3(0, -100.5, -1), 100, new Lambertian(new Vector3(0.8, 0.8, 0.0))));
//        world.addHitable(new Sphere(new Vector3(1,0,-1), 0.5, new Metal(new Vector3(0.8, 0.6, 0.2), 0.0)));
//        world.addHitable(new Sphere(new Vector3(-1, 0, -1), 0.5, new Dielectric(1.5)));
//        world.addHitable(new Sphere(new Vector3(-1, 0, -1), -0.45, new Dielectric(1.5)));

        Hitable world = random_scene();

//        double R = Math.cos(Math.PI/4);
//        world.addHitable(new Sphere(new Vector3(-R,0,-1), R, new Lambertian(new Vector3(0,0,1))));
//        world.addHitable(new Sphere(new Vector3(R,0,-1), R, new Lambertian(new Vector3(1,0,0))));

        BufferedImage bufferedImage = new BufferedImage(nx,ny,BufferedImage.TYPE_INT_ARGB);
        Random rand = new Random(42);

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
                    //Vector3 p = r.point_at_parameter(2.0);
                    col = col.addVec(color(r, world, 0));
                }
                col = col.divideConst(ns);
                //gamma corrected "gamma 2" = square root = raise to 1/gamma
                //better gamma 2.2
                col = new Vector3(Math.pow(col.r(),1/2.2), Math.pow(col.g(), 1/2.2), Math.pow(col.b(), 1/2.2));
                int ir = (int)(255.99*col.r());
                int ig = (int)(255.99*col.g());
                int ib = (int)(255.99*col.b());

                int argb = 255;
                argb = argb << 24;
                argb ^= ir << 16;
                argb ^= ig << 8;
                argb ^= ib;

                bufferedImage.setRGB(i, ny-j-1, argb);
            }
            double percent = ((double)j) /** ((double)i)*/ / ((double)ny) /*/ ((double)nx)*/ * 100.0;
            if(percent >= 10 && percent % 10 == 0)
                System.out.println(percent + "%; Time: " + LocalTime.now());
        }
        System.out.println("100%; Time: " + LocalTime.now());
        File file = new File("out\\production\\RayTracing\\img\\test.png");
        try
        {
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Hitable random_scene()
    {
        Random rand = new Random(42);
        int n = 500;
        HitableList list = new HitableList();
        list.addHitable(new Sphere(new Vector3(0, -1000, 0),1000, new Lambertian(new Vector3(0.5, 0.5, 0.5))));
        Vector3 temp = new Vector3(4, 0.2, 0);

        for(int a = -11; a < 11; a++)
        {
            for(int b = -11; b < 11; b++)
            {
                double choose_mat = rand.nextDouble();
                Vector3 center = new Vector3(a + 0.9 * rand.nextDouble(), 0.2, b + 0.9 * rand.nextDouble());

                if(center.subtractVec(temp).length() > 0.9)
                {
                    if(choose_mat < 0.8) //choose diffuse
                    {
//                        list.addHitable(new MovingSphere(center, center.addVec(new Vector3(0, 0.5 /*+ rand.nextDouble()*/, 0)), 0.2,
//                                new Lambertian(new Vector3(rand.nextDouble() * rand.nextDouble(),
//                                                           rand.nextDouble() * rand.nextDouble(),
//                                                           rand.nextDouble() * rand.nextDouble())), 0.0, 1.0));
                        list.addHitable(new Sphere(center, 0.2,
                                new Lambertian(new Vector3(rand.nextDouble() * rand.nextDouble(),
                                        rand.nextDouble() * rand.nextDouble(),
                                        rand.nextDouble() * rand.nextDouble()))));
                    }
                    else if(choose_mat < 0.95) //choose metal
                    {
                        list.addHitable(new Sphere(center, 0.2,
                                new Metal(new Vector3(0.5 * (1+rand.nextDouble()),
                                                      0.5 * (1+rand.nextDouble()),
                                                      0.5 * (1+rand.nextDouble())),
                                        0.5 * rand.nextDouble())));
                    }
                    else //choose glass
                    {
                        list.addHitable(new Sphere(center, 0.2, new Dielectric(1.5)));
                    }
                }
            }
        }

        list.addHitable(new Sphere(new Vector3(0, 1, 0), 1.0, new Dielectric(1.5)));
        list.addHitable(new Sphere(new Vector3(-4, 1, 0), 1.0, new Lambertian(new Vector3(0.4, 0.2, 0.1))));
        list.addHitable(new Sphere(new Vector3(4, 1, 0), 1.0, new Metal(new Vector3(0.7, 0.6, 0.5), 0.0)));
        return list;
    }
}
