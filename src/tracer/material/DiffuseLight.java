package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.ScatterRecord;
import tracer.Vector3;
import tracer.texture.Texture;

public class DiffuseLight extends Material
{
    Texture emit;

    public DiffuseLight(Texture emit)
    {
        this.emit = emit;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec)
    {
        return false;
    }

    @Override
    public Vector3 emitted(double u, double v, Vector3 p)
    {
        return emit.value(u, v, p);
    }

    @Override
    public Vector3 emitted(Ray r_in, HitRecord rec, double u, double v, Vector3 p) {
        if(rec.getNormal().dot(r_in.direction()) < 0.0)
            return emit.value(u,v,p);
        return new Vector3(0, 0, 0);
    }
}
