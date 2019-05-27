package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public class AABB extends Hitable
{
    Vector3 max, min;

    public AABB(Vector3 max, Vector3 min)
    {
        this.max = max;
        this.min = min;
    }

    public Vector3 getMax()
    {
        return max;
    }

    public Vector3 getMin()
    {
        return min;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        for(int a = 0; a < 3; a++)
        {
            double t0 = Math.min((min.getE(a) - r.origin().getE(a)) / r.direction().getE(a),
                    (max.getE(a) - r.origin().getE(a)) / r.direction().getE(a));
            double t1 = Math.max((min.getE(a) - r.origin().getE(a)) / r.direction().getE(a),
                    (max.getE(a) - r.origin().getE(a)) / r.direction().getE(a));;
            t_min = Math.min(t0, t_min);
            t_max = Math.max(t1, t_max);
            if(t_max <= t_min)
                return false;
        }
        return true;
    }
}
