package test;

import tracer.*;
import tracer.hitable.*;
import tracer.material.*;
import tracer.texture.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
            Vector3 emitted = rec.getMat().emitted(rec.getU(), rec.getV(), rec.getP());
            if(depth < 50 && rec.getMat().scatter(r, rec, attenuation, scattered))
            {
                return attenuation.multiplyVec(color(scattered,world,depth+1)).addVec(emitted);
            }
            else
            {
                return emitted;
            }
        }
        else
        {
//            Vector3 unit_dir = Vector3.unit_vec(r.direction());
//            double t = 0.5 * (unit_dir.y() + 1.0);
//            return new Vector3(1.0, 1.0, 1.0).multiplyConst(1.0 - t).addVec(new Vector3(0.5, 0.7, 1.0).multiplyConst(t));
            return new Vector3(0,0,0);
        }
    }

    public static void main(String[] args)
    {
        //System.out.println(1.0/0.0);
        int nx = 1280;//3840;//1280;//3840;//1920;//200;
        int ny = 720;//2160;//720;//2160;//1080;//100;
        int ns = 100;

        BufferedImage img = null;
        try
        {
            //img = ImageIO.read(new File("data/Penguins.jpg"));
            img = ImageIO.read(new File("data/earth.jpg"));
            //img = ImageIO.read(new File("data/blue.jpg"));
            BufferedImage bufferedImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < img.getWidth(); i++)
                for(int j = 0; j < img.getHeight(); j++)
                    bufferedImage.setRGB(i, j, img.getRGB(i,j));
            ImageIO.write(bufferedImage, "png", new File("img\\abc.png"));
        }
        catch(IOException e)
        {
            System.out.println("no such image");
        }

        Texture earth_img = new ImageTexture(img);

        Vector3 lookfrom = new Vector3(13, 2 ,3);//new Vector3(-2,2,1);//new Vector3(3,3,2);
        Vector3 lookat = new Vector3(0, 0, 0);//new Vector3(0,0,-1);
        double dist_to_focus = lookfrom.subtractVec(lookat).length();//10.0;
        double aperture = 0.25;

        //Camera cam = new Camera(lookfrom, lookat, new Vector3(0,1,0), 20.0, ((double)nx)/(double)ny, aperture, dist_to_focus, 0.0, 1.0);
        //Camera cam = cam_for_two_spheres(nx, ny);
        //Camera cam = cam_for_light(nx, ny);
        Camera cam = cam_for_cornell_box(nx, ny);
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

        //Hitable world = random_scene();
        //world = new BVH_node(((HitableList)world).getHitList(), 0, 1);
        //Hitable world = two_spheres();
        //Hitable world = two_perlin_spheres();
        //Hitable world = simple_light();
        //Hitable world = cornell_box();
        Hitable world = smoke_cornell_box();
        //Hitable world = new Sphere(new Vector3(0, 2, 0), 2, new Lambertian(earth_img));

//        List<Hitable> list = new ArrayList<>();
//        list.add(new Sphere(new Vector3(0, 0, 0), 1, new Dielectric(1.5)));
//        list.add(new Sphere(new Vector3(0, 0, 0), 1, new Dielectric(1.5)));
//        list.add(new Sphere(new Vector3(0, 0, 2), 1, new Dielectric(1.5)));
//        Hitable world = new BVH_node(list, 0, 1);
//        double R = Math.cos(Math.PI/4);
//        world.addHitable(new Sphere(new Vector3(-R,0,-1), R, new Lambertian(new Vector3(0,0,1))));
//        world.addHitable(new Sphere(new Vector3(R,0,-1), R, new Lambertian(new Vector3(1,0,0))));

        BufferedImage bufferedImage = new BufferedImage(nx,ny,BufferedImage.TYPE_INT_ARGB);
        Random rand = new Random(42);

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

    public static Hitable random_scene()
    {
        Random rand = new Random(42);
        int n = 500;
        HitableList list = new HitableList();
        Texture checker = new CheckerTexture(new ConstantTexture(new Vector3(0.2, 0.3, 0.1)), new ConstantTexture(new Vector3(0.9, 0.9, 0.9)));
        list.addHitable(new Sphere(new Vector3(0, -1000, 0),1000, new Lambertian(checker)));//new ConstantTexture(new Vector3(0.5, 0.5, 0.5)))));
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
                                new Lambertian(new ConstantTexture(new Vector3(rand.nextDouble() * rand.nextDouble(),
                                                                               rand.nextDouble() * rand.nextDouble(),
                                                                               rand.nextDouble() * rand.nextDouble())))));
                    }
                    else if(choose_mat < 0.95) //choose metal
                    {
                        list.addHitable(new Sphere(center, 0.2,
                                new Metal(new ConstantTexture(new Vector3(0.5 * (1+rand.nextDouble()),
                                                                          0.5 * (1+rand.nextDouble()),
                                                                          0.5 * (1+rand.nextDouble()))),
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
        list.addHitable(new Sphere(new Vector3(-4, 1, 0), 1.0, new Lambertian(new ConstantTexture(new Vector3(0.4, 0.2, 0.1)))));
        list.addHitable(new Sphere(new Vector3(4, 1, 0), 1.0, new Metal(new ConstantTexture(new Vector3(0.7, 0.6, 0.5)), 0.0)));
        return list;
    }

    public static Hitable two_spheres()
    {
        Texture checker = new CheckerTexture(new ConstantTexture(new Vector3(0.2, 0.3, 0.1)),
                                             new ConstantTexture(new Vector3(0.9, 0.9, 0.9)));
        HitableList list = new HitableList();
        list.addHitable(new Sphere(new Vector3(0, -10, 0), 10, new Lambertian(checker)));
        list.addHitable(new Sphere(new Vector3(0, 10, 0), 10, new Lambertian(checker)));
        return list;
    }

    public static Hitable two_perlin_spheres()
    {
        //Texture pertext = new NoiseTexture();
        Texture pertext = new NoiseTexture(6.66);
        HitableList list = new HitableList();
        list.addHitable(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(pertext)));
        list.addHitable(new Sphere(new Vector3(0, 2, 0), 2, new Lambertian(pertext)));
        return list;
    }

    public static Hitable simple_light()
    {
        //Texture pertext = new NoiseTexture();
        Texture pertext = new NoiseTexture(6.66);
        HitableList list = new HitableList();
        list.addHitable(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(pertext)));
        list.addHitable(new Sphere(new Vector3(0, 2, 0), 2, new Lambertian(pertext)));
        list.addHitable(new Sphere(new Vector3(0, 7, 0), 2, new DiffuseLight(new ConstantTexture(new Vector3(4,4,4)))));
        list.addHitable(new XYRect(3,5,1,3,-2, new DiffuseLight(new ConstantTexture(new Vector3(4,4,4)))));
        return list;
    }

    public static Hitable cornell_box()
    {
        HitableList list = new HitableList();
        Material red = new Lambertian(new ConstantTexture(new Vector3(0.65, 0.05, 0.05)));
        Material white = new Lambertian(new ConstantTexture(new Vector3(0.73, 0.73, 0.73)));
        Material green = new Lambertian(new ConstantTexture(new Vector3(0.12, 0.45, 0.15)));
        Material light = new DiffuseLight(new ConstantTexture(new Vector3(15, 15, 15)));
        list.addHitable(new FlipNormals(new YZRect(0, 555, 0, 555, 555, green)));
        list.addHitable(new YZRect(0, 555, 0, 555, 0, red));
        list.addHitable(new XZRect(213, 343, 227, 332, 554, light));
        //list.addHitable(new FlipNormals(new XZRect(213, 343, 227, 332, 554, light)));
        list.addHitable(new FlipNormals(new XZRect(0, 555, 0, 555, 555, white)));
        list.addHitable(new XZRect(0, 555, 0, 555, 0, white));
        list.addHitable(new FlipNormals(new XYRect(0, 555, 0, 555, 555, white)));
//        list.addHitable(new Box(new Vector3(130, 0, 65), new Vector3(295, 165, 230), white));
//        list.addHitable(new Box(new Vector3(265, 0 , 295), new Vector3(430, 330, 460), white));
        list.addHitable(new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 165, 165), white), -18), new Vector3(130, 0, 65)));
        list.addHitable(new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 330, 165), white), 15), new Vector3(265, 0, 295)));
        return list;
    }

    public static Hitable smoke_cornell_box()
    {
        HitableList list = new HitableList();
        Material red = new Lambertian(new ConstantTexture(new Vector3(0.65, 0.05, 0.05)));
        Material white = new Lambertian(new ConstantTexture(new Vector3(0.73, 0.73, 0.73)));
        Material green = new Lambertian(new ConstantTexture(new Vector3(0.12, 0.45, 0.15)));
        Material light = new DiffuseLight(new ConstantTexture(new Vector3(15, 15, 15)));
        list.addHitable(new FlipNormals(new YZRect(0, 555, 0, 555, 555, green)));
        list.addHitable(new YZRect(0, 555, 0, 555, 0, red));
        list.addHitable(new XZRect(213, 343, 227, 332, 554, light));
        list.addHitable(new FlipNormals(new XZRect(0, 555, 0, 555, 555, white)));
        list.addHitable(new XZRect(0, 555, 0, 555, 0, white));
        list.addHitable(new FlipNormals(new XYRect(0, 555, 0, 555, 555, white)));
        Hitable box1 = new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 165, 165), white), -18), new Vector3(130, 0, 65));
        Hitable box2 = new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 330, 165), white), 15), new Vector3(265, 0, 295));
        list.addHitable(new ConstantMedium(box1, 0.01, new ConstantTexture(new Vector3(1,1,1))));
        list.addHitable(new ConstantMedium(box2, 0.01, new ConstantTexture(new Vector3(0,0,0))));
        return list;
    }

    public static Camera cam_for_two_spheres(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(13,2,3);
        Vector3 lookat = new Vector3(0,0,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 20, nx/ny, aperture, dist_to_focus);
    }

    public static Camera cam_for_light(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(19,2,3);
        Vector3 lookat = new Vector3(0,2,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 20, nx/ny, aperture, dist_to_focus);
    }

    public static Camera cam_for_cornell_box(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(278,278,-800);
        Vector3 lookat = new Vector3(278,278,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 40, nx/ny, aperture, dist_to_focus);
    }
}
