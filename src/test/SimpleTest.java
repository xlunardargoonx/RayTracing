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

class Tracing{
    static Random rand = new Random(42);
    Hitable world;
    HitableList lights;
    BufferedImage render;
    Camera cam;
    boolean workOn[][];
    int x, y;
    int sample;
    int total;

    Tracing(Hitable world, HitableList lights, BufferedImage render, Camera cam, int x, int y, int sample){
        this.world = world;
        this.lights = lights;
        this.render = render;
        this.cam = cam;
        workOn = new boolean[x][y];
        this.sample = sample;
        total = x * y;
        this.x = x;
        this.y = y;
    }

    public boolean isDone(){
        return total == 0;
    }

    public void trace(int location[]){
        Vector3 col = new Vector3();
        for(int s = 0; s < sample; s++)
        {
            double u = ((double) location[0] + rand.nextDouble()) / (double) x;
            double v = ((double) location[1] + rand.nextDouble()) / (double) y;

            Ray r = cam.getRay(u, v);
            col = col.addVec(de_nan(color(r, world, lights,0)));
        }
        col = col.divideConst(sample);
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
        render.setRGB(location[0], y-location[1]-1, argb);
    }

    public int[] nextPixel(){
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                if(workOn[i][j]) continue;
                workOn[i][j] = true;
                total--;
                return new int[]{i,j};
            }
        }
        return new int[]{0,0};
    }
}

class RenderThread extends Thread {
    Tracing tracer;
    String threadName;

    RenderThread(Tracing tracer, String name){
        this.tracer = tracer;
        threadName = name;
    }

    public void run(){
        System.out.println(threadName + " is running");
        while(!tracer.isDone()){
            //grab next pixel
            int location[];
            synchronized(tracer){
                location = tracer.nextPixel();
            }
            //trace pixel
            tracer.trace(location);
        }
    }
}

public class SimpleTest {

    static Random rand = new Random(42);

    public static void main(String[] args)
    {
        SceneResolution resolution = SceneResolution.S_500X500;
        int nx = resolution.getWidth();
        int ny = resolution.getHeight();
        int ns = 500;

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

        LocalTime start = LocalTime.now();
        System.out.println("0%; Time: " + LocalTime.now());

        Tracing tracer = new Tracing(world, lights, bufferedImage, cam, nx, ny, ns);
//        while(!tracer.isDone()){
//            tracer.trace(tracer.nextPixel());
//        }
        int nt = 3;
        Thread threads[] = new Thread[nt];
        for(int i = 0; i < nt; i++){
            threads[i] = new RenderThread(tracer, "Thread " + i);
        }

        for(Thread t : threads){
            t.start();
        }

        try {
            for(Thread t : threads){ t.join(); }
        } catch(Exception e){
            System.out.println("Interrupted");
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
