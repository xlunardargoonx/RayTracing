package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;

public abstract class Hitable
{
    public abstract boolean hit(Ray r, double t_min, double t_max, HitRecord rec);
}
