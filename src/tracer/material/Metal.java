package tracer.material;

import tracer.*;
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
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec)
    {
        Vector3 reflected = reflect(Vector3.unit_vec(r_in.direction()), rec.getNormal());
        srec.setSpecular_ray(new Ray(rec.getP(), reflected.addVec(HelperFunctions.randomInUnitSphere().multiplyConst(fuzz))));
        srec.setAttenuation(albedo.value(rec.getU(), rec.getV(), rec.getP()));
        srec.setIsSpecular(true);
        srec.setPdf_ptr(null);
        return true;
//        scattered.setA(rec.getP());
//        scattered.setB(reflected.addVec(HelperFunctions.randomInUnitSphere().multiplyConst(fuzz)));
//        scattered.setTime(r_in.getTime());
//        attenuation.copyValue(albedo.value(0,0, rec.getP()));
//        return (scattered.direction().dot(rec.getNormal()) > 0);
    }
}
