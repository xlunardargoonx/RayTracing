package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
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
    public boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered)
    {
        return false;
    }

    @Override
    public Vector3 emitted(double u, double v, Vector3 p)
    {
        return emit.value(u, v, p);
    }
}
