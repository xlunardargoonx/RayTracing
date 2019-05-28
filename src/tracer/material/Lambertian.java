package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.texture.Texture;

public class Lambertian extends Material
{
    private Texture albedo;

    public Lambertian(Texture albedo)
    {
        super();
        this.albedo = albedo;
    }

    public Texture getAlbedo()
    {
        return albedo;
    }

    public void setAlbedo(Texture albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered)
    {
        Vector3 target = rec.getP().addVec(rec.getNormal()).addVec(randomInUnitSphere());
        scattered.setA(rec.getP());
        scattered.setB(target.subtractVec(rec.getP()));
        scattered.setTime(r_in.getTime());
        attenuation.copyValue(albedo.value(0,0, rec.getP()));
        return true;
    }

}
