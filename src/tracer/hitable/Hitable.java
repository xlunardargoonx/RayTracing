package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public abstract class Hitable
{
    public abstract boolean hit(Ray r, double t_min, double t_max, HitRecord rec);
    public abstract boolean bounding_box(double t0, double t1, AABB box);

    public double pdf_value(Vector3 o, Vector3 v)
    {
        return 0.0;
    }
    public Vector3 random(Vector3 o)
    {
        return new Vector3(1,0,0);
    }
}
