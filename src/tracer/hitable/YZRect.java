package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class YZRect extends Hitable
{
    double y0, y1, z0, z1, k;
    Material mat;

    public YZRect(double y0, double y1, double z0, double z1, double k, Material mat)
    {
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
        this.k = k;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        double t = (k-r.origin().x()) / r.direction().x();
        if(t < t_min || t > t_max)
            return false;
        double y = r.origin().y() + t * r.direction().y();
        double z = r.origin().z() + t * r.direction().z();
        if( y < y0 || y > y1 || z < z0 || z > z1)
            return false;
        rec.setU((y-y0) / (y1-y0));
        rec.setV((z-z0) / (z1-z0));
        rec.setT(t);
        rec.setMat(mat);
        rec.setP(r.point_at_parameter(t));
        rec.setNormal(new Vector3(1,0,0));
        return true;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(new AABB(new Vector3(k-0.0001, y0, z0), new Vector3(k+0.0001, y1, z1)));
        return true;
    }
}
