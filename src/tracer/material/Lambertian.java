package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public class Lambertian extends Material
{
    private Vector3 albedo;

    public Lambertian(Vector3 albedo)
    {
        super();
        this.albedo = albedo;
    }

    public Vector3 getAlbedo()
    {
        return albedo;
    }

    public void setAlbedo(Vector3 albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered)
    {
        Vector3 target = rec.getP().addVec(rec.getNormal()).addVec(randomInUnitSphere());
        scattered.setA(rec.getP());
        scattered.setB(target.subtractVec(rec.getP()));
        attenuation.copyValue(albedo);
        return true;
    }

}
