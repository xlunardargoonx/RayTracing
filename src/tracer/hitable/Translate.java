package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public class Translate extends Hitable
{
    Hitable obj;
    Vector3 offset;

    public Translate(Hitable obj, Vector3 offset)
    {
        this.obj = obj;
        this.offset = offset;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        Ray moved_r = new Ray(r.origin().subtractVec(offset), r.direction(), r.getTime());
        if(obj.hit(moved_r, t_min, t_max, rec))
        {
            rec.setP(rec.getP().addVec(offset));
            return true;
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        if(obj.bounding_box(t0, t1, box))
        {
            box.copy(new AABB(box.getMin().addVec(offset), box.getMax().addVec(offset)));
            return true;
        }
        return false;
    }
}
