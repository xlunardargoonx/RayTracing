package tracer;

import tracer.hitable.*;
import tracer.material.*;
import tracer.texture.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Scenes
{
    static Random rand = new Random(42);

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
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File("data/colors.jpg"));
        }
        catch(IOException e)
        {
            System.out.println("no such image ... final");
        }
        Material emat = new Lambertian(new ImageTexture(img));
        Texture pertext = new NoiseTexture(6.66);
        HitableList list = new HitableList();
        list.addHitable(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(pertext)));
        list.addHitable(new Sphere(new Vector3(0, 2, 0), 2, emat));
        list.addHitable(new Sphere(new Vector3(0, 7, 0), 2, new DiffuseLight(new ConstantTexture(new Vector3(4,4,4)))));
        list.addHitable(new XYRect(3,5,1,3,-2, new DiffuseLight(new ConstantTexture(new Vector3(4,4,4)))));
        //list.addHitable(new XZRect(4,6,0,2,5, new DiffuseLight(new ConstantTexture(new Vector3(4,4,4)))));
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
        Material light = new DiffuseLight(new ConstantTexture(new Vector3(7, 7, 7)));
        list.addHitable(new FlipNormals(new YZRect(0, 555, 0, 555, 555, green)));
        list.addHitable(new YZRect(0, 555, 0, 555, 0, red));
        list.addHitable(new XZRect(113, 443, 127, 432, 554, light));
        list.addHitable(new FlipNormals(new XZRect(0, 555, 0, 555, 555, white)));
        list.addHitable(new XZRect(0, 555, 0, 555, 0, white));
        list.addHitable(new FlipNormals(new XYRect(0, 555, 0, 555, 555, white)));
        Hitable box1 = new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 165, 165), white), -18), new Vector3(130, 0, 65));
        Hitable box2 = new Translate(new RotateY(new Box(new Vector3(0,0,0), new Vector3(165, 330, 165), white), 15), new Vector3(265, 0, 295));
//        Hitable box1 = new Box(new Vector3(130, 0, 65), new Vector3(295, 165, 230), white);
//        Hitable box2 = new Box(new Vector3(265, 0 , 295), new Vector3(430, 330, 460), white);
//        Hitable box1 = new FlipNormals(new XZRect(130.0, 295.0, 65.0, 230.0, 0.0, white));
//        Hitable box2 = new FlipNormals(new XZRect(265.0, 430.0, 295.0, 460.0, 0.0, white));
        list.addHitable(new ConstantMedium(box1, 0.01, new ConstantTexture(new Vector3(1,1,1))));
        list.addHitable(new ConstantMedium(box2, 0.01, new ConstantTexture(new Vector3(0,0,0))));
//        list.addHitable(new FlipNormals(new XZRect(130.0, 295.0, 65.0, 230.0, 0.0, white)));
//        list.addHitable(new FlipNormals(new XZRect(265.0, 430.0, 295.0, 460.0, 0.0, white)));
        //list.addHitable(new FlipNormals(new XZRect(0, 165, 0, 165, 0, white)));
        return list;
    }

    public static Hitable final_scene()
    {
        int nb = 20;
        HitableList list = new HitableList();
        HitableList boxList = new HitableList();
        HitableList boxList2 = new HitableList();
        Material white = new Lambertian(new ConstantTexture(new Vector3(0.73, 0.73, 0.73)));
        Material ground = new Lambertian(new ConstantTexture(new Vector3(0.48, 0.83, 0.53)));

        /* ground blocks */
        for(int i = 0; i < nb; i++)
        {
            for(int j = 0; j < nb; j++)
            {
                double w = 100;
                double x0 = -1000 + i*w;
                double z0 = -1000 + j*w;
                double y0 = 0;
                double x1 = x0 + w;
                double y1 = 100 * (rand.nextDouble() + 0.01);
                double z1 = z0 + w;
                boxList.addHitable(new Box(new Vector3(x0, y0, z0), new Vector3(x1, y1, z1), ground));
            }
        }

        list.addHitable(new BVH_node(boxList.getHitList(), 0 ,1));

        Material light = new DiffuseLight(new ConstantTexture(new Vector3(7,7,7)));
        list.addHitable(new XZRect(123, 423, 147, 412, 554, light));

        Vector3 center = new Vector3(400, 400, 200);
        /* moving ball in the upper left */
        list.addHitable(new MovingSphere(center, center.addVec(new Vector3(30,0,0)),  50, new Lambertian(new ConstantTexture(new Vector3(0.7, 0.3, 0.1))), 0, 1));
        /* lower center glass ball */
        list.addHitable(new Sphere(new Vector3(260, 150, 45), 50, new Dielectric(1.5)));
        /* Metal ball on the lower right*/
        list.addHitable(new Sphere(new Vector3(0, 150, 145), 50, new Metal(new ConstantTexture(new Vector3(0.8, 0.8, 0.9)), 10)));

        /* Blue glossy ball on the lower left infront of the earth */
        Hitable boundary = new Sphere(new Vector3(360, 150, 145), 70, new Dielectric(1.5));
        list.addHitable(boundary);
        list.addHitable(new ConstantMedium(boundary, 0.2, new ConstantTexture(new Vector3(0.2, 0.4, 0.9))));
        boundary = new Sphere(new Vector3(0,0,0), 5000, new Dielectric(1.5));
        list.addHitable(new ConstantMedium(boundary, 0.0001, new ConstantTexture(new Vector3(1.0, 1.0, 1.0))));

        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File("data/earth.jpg"));
        }
        catch(IOException e)
        {
            System.out.println("no such image ... final");
        }
        Material emat = new Lambertian(new ImageTexture(img));
        list.addHitable(new Sphere(new Vector3(400, 200, 400), 100, emat));

        Texture pertext = new NoiseTexture(0.1);
        list.addHitable(new Sphere(new Vector3(220, 280, 300), 80, new Lambertian(pertext)));
        //list.addHitable(new Sphere(new Vector3(220, 280, 300), 80, emat));
        //list.addHitable(new XZRect(123, 203, 147, 227, 150, light));
        //list.addHitable(new Sphere(new Vector3(0, 2, 0), 200, new Lambertian(pertext)));
        //center = new Vector3(220, 280, 300);
        //list.addHitable(new MovingSphere(center, center.addVec(new Vector3(30,0,0)),  50, new Lambertian(new ConstantTexture(new Vector3(0.7, 0.3, 0.1))), 0, 1));

        int ns = 1000;
        for(int j = 0; j < ns; j++)
        {
            boxList2.addHitable(new Sphere(new Vector3(165*rand.nextDouble(), 165*rand.nextDouble(), 165*rand.nextDouble()), 10, white));
        }
        list.addHitable(new Translate(new RotateY(new BVH_node(boxList2.getHitList(), 0.0, 1.0), 15), new Vector3(-100, 270, 395)));
        return list;
        //return new BVH_node(list.getHitList(),0.0, 1.0);
    }
}
