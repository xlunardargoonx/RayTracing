package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.texture.Texture;

public class Metal extends Material
{
    private Texture albedo;
    private double fuzz;

    public Metal(Texture albedo) {
        this.albedo = albedo;
        this.fuzz = 0;
    }

    public Metal(Texture albedo, double fuzz)
    {
        super();
        this.albedo = albedo;
        if(fuzz < 1) this.fuzz = fuzz;
        else this.fuzz = 1;
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
        Vector3 reflected = reflect(Vector3.unit_vec(r_in.direction()), rec.getNormal());
        scattered.setA(rec.getP());
        scattered.setB(reflected.addVec(randomInUnitSphere().multiplyConst(fuzz)));
        scattered.setTime(r_in.getTime());
        attenuation.copyValue(albedo.value(0,0, rec.getP()));
        return (scattered.direction().dot(rec.getNormal()) > 0);
    }
}
