package tracer.integrator;

import tracer.Camera;

public class BasicThreadIntegrator extends BasicIntegrator{
    public BasicThreadIntegrator(int num_of_samples, int width, int height, Camera cam) {
        super(num_of_samples, width, height, cam);
    }

//    class Tracing{
//        //static Random rand = new Random(42);
//        Hitable world;
//        HitableList lights;
//        BufferedImage render;
//        JLabel label;
//        Camera cam;
//        boolean workOn[][];
//        int x, y;
//        int sample;
//        int total;
//
//        Tracing(Hitable world, HitableList lights, BufferedImage render, JLabel label, Camera cam, int x, int y, int sample){
//            this.world = world;
//            this.lights = lights;
//            this.render = render;
//            this.label = label;
//            this.cam = cam;
//            workOn = new boolean[x][y];
//            this.sample = sample;
//            total = x * y;
//            this.x = x;
//            this.y = y;
//        }
//
//        public boolean isDone(){
//            return total == 0;
//        }
//
//        public void trace(int location[]){
//            Vector3 col = new Vector3();
//            for(int s = 0; s < sample; s++)
//            {
//                double u = ((double) location[0] + ThreadLocalRandom.current().nextDouble()) / (double) x;
//                double v = ((double) location[1] + ThreadLocalRandom.current().nextDouble()) / (double) y;
//
//                Ray r = cam.getRay(u, v);
//                col = col.addVec(de_nan(color(r, world, lights,0)));
//            }
//            col = col.divideConst(sample);
//            //gamma corrected "gamma 2" = square root = raise to 1/gamma
//            //better gamma 2.2
//            col = new Vector3(Math.pow(col.r(),1/2.2), Math.pow(col.g(), 1/2.2), Math.pow(col.b(), 1/2.2));
//
//            col.copyValue(boundingColor(col));
//            int ir = (int)(254.99*col.r());
//            int ig = (int)(254.99*col.g());
//            int ib = (int)(254.99*col.b());
//
//            int argb = 255;
//            argb = argb << 24;
//            argb ^= ir << 16;
//            argb ^= ig << 8;
//            argb ^= ib;
//            render.setRGB(location[0], y-location[1]-1, argb);
//        }
//
//        int i = 0, j = 0;
//        public int[] nextPixel(){
//            for(; i < x; i++){
//                for(; j < y; j++){
//                    if(workOn[i][j]) continue;
//                    workOn[i][j] = true;
//                    total--;
//                    return new int[]{i,j};
//                }
//                j = 0;
//            }
//            return new int[]{0,0};
//        }
//
//        public void update(){
//            label.setIcon(new ImageIcon(render));
//        }
//    }
//
//    class RenderThread extends Thread {
//        Tracing tracer;
//        String threadName;
//
//        RenderThread(Tracing tracer, String name){
//            this.tracer = tracer;
//            threadName = name;
//        }
//
//        public void run(){
//            System.out.println(threadName + " is running");
//            while(true){
//                //grab next pixel
//                int location[];
//                synchronized(tracer){
//                    tracer.update();
//                    if(tracer.isDone()) break;
//                    location = tracer.nextPixel();
//                }
//                //trace pixel
//                tracer.trace(location);
//            }
//        }
//    }
}
