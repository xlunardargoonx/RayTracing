package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Lambertian;
import tracer.material.Material;
import tracer.texture.ConstantTexture;

public class Box extends Hitable
{
    Vector3 pmin, pmax;
    HitableList list;

    public Box(Vector3 pmin, Vector3 pmax, Material mat)
    {
        this.pmin = pmin;
        this.pmax = pmax;
        list = new HitableList();
        Material blue = new Lambertian(new ConstantTexture(new Vector3(0.05, 0.05, 0.65)));
        list.addHitable(new XYRect(pmin.x(), pmax.x(), pmin.y(), pmax.y(), pmax.z(), mat));
        list.addHitable(new FlipNormals(new XYRect(pmin.x(), pmax.x(), pmin.y(), pmax.y(), pmin.z(), mat)));
        list.addHitable(new XZRect(pmin.x(), pmax.x(), pmin.z(), pmax.z(), pmax.y(), mat));
        list.addHitable(new FlipNormals(new XZRect(pmin.x(), pmax.x(), pmin.z(), pmax.z(), pmin.y(), mat)));
        //list.addHitable(new XZRect(pmin.x(), pmax.x(), pmin.z(), pmax.z(), pmin.y(), mat));
        list.addHitable(new YZRect(pmin.y(), pmax.y(), pmin.z(), pmax.z(), pmax.x(), mat));
        list.addHitable(new FlipNormals(new YZRect(pmin.y(), pmax.y(), pmin.z(), pmax.z(), pmin.x(), mat)));
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        return list.hit(r, t_min, t_max, rec);
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(new AABB(pmin, pmax));
        return true;
    }
}
