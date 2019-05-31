package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;

public class FlipNormals extends Hitable
{
    Hitable hit;

    public FlipNormals(Hitable hit)
    {
        this.hit = hit;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        if(hit.hit(r, t_min, t_max, rec))
        {
            rec.setNormal(rec.getNormal().multiplyConst(-1));
            return true;
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        return hit.bounding_box(t0, t1, box);
    }
}
