package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class XZRect extends Hitable
{
    double x0, x1, z0, z1, k;
    Material mat;

    public XZRect(double x0, double x1, double z0, double z1, double k, Material mat)
    {
        this.x0 = x0;
        this.x1 = x1;
        this.z0 = z0;
        this.z1 = z1;
        this.k = k;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        double t = (k-r.origin().y()) / r.direction().y();
        if(t < t_min || t > t_max)
            return false;
        double x = r.origin().x() + t * r.direction().x();
        double z = r.origin().z() + t * r.direction().z();
        if(x < x0 || x > x1 || z < z0 || z > z1)
            return false;
        rec.setU((x-x0) / (x1-x0));
        rec.setV((z-z0) / (z1-z0));
        rec.setT(t);
        rec.setMat(mat);
        rec.setP(r.point_at_parameter(t));
        rec.setNormal(new Vector3(0,1,0));
        return true;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(new AABB(new Vector3(x0, k-0.0001, z0), new Vector3(x1, k+0.0001, z1)));
        return true;
    }
}
