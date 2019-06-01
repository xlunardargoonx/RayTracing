package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.texture.Texture;

public class Isotropic extends Material
{
    Texture albedo;

    public Isotropic(Texture albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered)
    {
        scattered.setA(rec.getP());
        scattered.setB(randomInUnitSphere());
        attenuation.copyValue(albedo.value(rec.getU(), rec.getV(), rec.getP()));
        return true;
    }
}
