package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class RotateY extends Hitable
{
    Hitable obj;
    double sin_theta, cos_theta;
    boolean hasBox;
    AABB bbox;

    public RotateY(Hitable obj, double angle)
    {
        this.obj = obj;
        double radians = (Math.PI / 180.0) * angle;
        sin_theta = Math.sin(radians);
        cos_theta = Math.cos(radians);
        bbox = new AABB();
        hasBox = obj.bounding_box(0,1, bbox);
        Vector3 min = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Vector3 max = new Vector3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
        for(int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                for (int k = 0; k < 2; k++)
                {
                    double x = i*bbox.getMax().x() + (1-i)*bbox.min.x();
                    double y = j*bbox.getMax().y() + (1-j)*bbox.min.y();
                    double z = k*bbox.getMax().z() + (1-k)*bbox.min.z();
                    double newX = cos_theta*x + sin_theta*z;
                    double newZ = -sin_theta*x + cos_theta*z;
                    Vector3 tester = new Vector3(newX, y , newZ);
                    for(int c = 0; c < 3; c++)
                    {
                        if(tester.getE(c) > max.getE(c))
                            max.set(c, tester.getE(c));
                        if(tester.getE(c) < min.getE(c))
                            min.set(c, tester.getE(c));
                    }
                }
            }
        }
        bbox.copy(new AABB(min, max));
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        //this will modify r.origin and r.direction
        Vector3 origin = new Vector3(r.origin());
        Vector3 direction = new Vector3(r.direction());
        //0 is being modify and then used in the 2nd line not sure if this is intended
        origin.set(0, cos_theta*r.origin().getE(0) - sin_theta*r.origin().getE(2));
        origin.set(2, sin_theta*r.origin().getE(0) + cos_theta*r.origin().getE(2));
        direction.set(0, cos_theta*r.direction().getE(0) - sin_theta*r.direction().getE(2));
        direction.set(2, sin_theta*r.direction().getE(0) + cos_theta*r.direction().getE(2));
        Ray rotated_r = new Ray(origin, direction, r.getTime());
        if(obj.hit(rotated_r, t_min, t_max, rec))
        {
            Vector3 p = new Vector3(rec.getP());
            Vector3 normal = new Vector3(rec.getNormal());
            p.set(0, cos_theta*rec.getP().getE(0) + sin_theta*rec.getP().getE(2));
            p.set(2, -sin_theta*rec.getP().getE(0) + cos_theta*rec.getP().getE(2));
            normal.set(0, cos_theta*rec.getNormal().getE(0) + sin_theta*rec.getNormal().getE(2));
            normal.set(2, -sin_theta*rec.getNormal().getE(0) + cos_theta*rec.getNormal().getE(2));
            rec.getP().copyValue(p);
            rec.getNormal().copyValue(normal);
            return true;
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(bbox);
        return hasBox;
    }
}
