package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public class Metal extends Material
{
    private Vector3 albedo;
    private double fuzz;

    public Metal(Vector3 albedo) {
        this.albedo = albedo;
        this.fuzz = 0;
    }

    public Metal(Vector3 albedo, double fuzz)
    {
        super();
        this.albedo = albedo;
        if(fuzz < 1) this.fuzz = fuzz;
        else this.fuzz = 1;
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
        Vector3 reflected = reflect(Vector3.unit_vec(r_in.direction()), rec.getNormal());
        scattered.setA(rec.getP());
        scattered.setB(reflected.addVec(randomInUnitSphere().multiplyConst(fuzz)));
        attenuation.copyValue(albedo);
        return (scattered.direction().dot(rec.getNormal()) > 0);
    }
}
