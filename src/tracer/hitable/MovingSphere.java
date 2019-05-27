package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class MovingSphere extends Sphere {
    double time0, time1;
    Vector3 center0, center1;

    public MovingSphere(Vector3 center, Vector3 center1, double radius, Material mat, double time0, double time1)
    {
        super(center, radius, mat);
        this.time0 = time0;
        this.time1 = time1;
        this.center0 = center;
        this.center1 = center1;
    }

    public Vector3 moving_center(double time)
    {
        return center0.addVec(center1.subtractVec(center0)
                                     .multiplyConst((time - time0) / (time1 - time0))
                             );
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        setCenter(moving_center(r.getTime()));
        return super.hit(r, t_min, t_max, rec);
    }
}
