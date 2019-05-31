package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class XYRect extends Hitable
{
    double x0, x1, y0, y1, k;
    Material mat;

    public XYRect(double x0, double x1, double y0, double y1, double k, Material mat)
    {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.k = k;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        double t = (k-r.origin().z()) / r.direction().z();
        if(t < t_min || t > t_max)
            return false;
        double x = r.origin().x() + t * r.direction().x();
        double y = r.origin().y() + t * r.direction().y();
        if(x < x0 || x >x1 || y < y0 || y >y1)
            return false;
        rec.setU((x-x0) / (x1-x0));
        rec.setV((y-y0) / (y1-y0));
        rec.setT(t);
        rec.setMat(mat);
        rec.setP(r.point_at_parameter(t));
        rec.setNormal(new Vector3(0,0,1));
        return true;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(new AABB(new Vector3(x0,y0, k-0.0001), new Vector3(x1, y1, k+0.0001)));
        return true;
    }
}
