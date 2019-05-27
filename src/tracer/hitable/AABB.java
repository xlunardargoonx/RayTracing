package tracer.hitable;

import tracer.Ray;
import tracer.Vector3;

public class AABB //extends Hitable
{
    Vector3 max, min;

    public AABB()
    {
        min = new Vector3();
        max = new Vector3();
    }

    public AABB(Vector3 min, Vector3 max)
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

    public void setMax(Vector3 max) {
        this.max = max;
    }

    public void setMin(Vector3 min) {
        this.min = min;
    }

    //@Override
    public boolean hit(Ray r, double t_min, double t_max)//, HitRecord rec)
    {
//        for(int a = 0; a < 3; a++)
//        {
//            double t0 = Math.min((min.getE(a) - r.origin().getE(a)) / r.direction().getE(a),
//                    (max.getE(a) - r.origin().getE(a)) / r.direction().getE(a));
//            double t1 = Math.max((min.getE(a) - r.origin().getE(a)) / r.direction().getE(a),
//                    (max.getE(a) - r.origin().getE(a)) / r.direction().getE(a));
//            t_min = Math.max(t0, t_min);
//            t_max = Math.min(t1, t_max);
//            if(t_max <= t_min)
//                return false;
//        }
//        return true;
        for(int a = 0; a < 3; a++)
        {
            double invD = 1.0 / r.direction().getE(a);
            double t0 = (min.getE(a) - r.origin().getE(a)) * invD;
            double t1 = (max.getE(a) - r.origin().getE(a)) * invD;

            if(invD < 0.0)
            {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }

            t_min = t0 > t_min ? t0 : t_min;
            t_max = t1 < t_max ? t1 : t_max;
            if(t_max <= t_min) return false;
        }
        return true;
    }

    public void copy(AABB box)
    {
        setMin(box.getMin());
        setMax(box.getMax());
    }

    public static void surrounding_box(AABB box0, AABB box1, AABB superBox)
    {
        Vector3 small = new Vector3(Math.min(box0.min.x(), box1.min.x()),
                Math.min(box0.min.y(), box1.min.y()),
                Math.min(box0.min.z(), box1.min.z()));
        Vector3 big = new Vector3(Math.max(box0.max.x(), box1.max.x()),
                Math.max(box0.max.y(), box1.max.y()),
                Math.max(box0.max.z(), box1.max.z()));

        superBox.setMin(small);
        superBox.setMax(big);
    }
}
