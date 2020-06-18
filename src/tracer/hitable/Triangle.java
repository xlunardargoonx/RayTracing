package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;

public class Triangle extends Hitable {

    public Triangle(){

    }
    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box) {
        return false;
    }
}
