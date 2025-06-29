package tracer.integrator;

import tracer.Camera;
import tracer.Ray;
import tracer.Vector3;
import tracer.hitable.Hitable;

public abstract class Integrator {

    int num_of_samples, width, height;
    Camera cam;
    Vector3[][] image_buf;

    public Vector3[][] getImage_buf() {
        return image_buf;
    }

    public Integrator(int num_of_samples, int width, int height, Camera cam){
        this.num_of_samples = num_of_samples;
        this.height = height;
        this.width = width;
        this.image_buf = new Vector3[height][width];
        this.cam = cam;
    }

    public abstract void integrate(Hitable world, Hitable light_shape);

    public abstract Vector3 color(Ray r, Hitable world, Hitable light_shape, int depth);
}
