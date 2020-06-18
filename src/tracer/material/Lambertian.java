package tracer.material;

import tracer.*;
import tracer.pdf.CosinePDF;
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
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec)
    {
        //version 1
//        Vector3 target = rec.getP().addVec(rec.getNormal()).addVec(randomInUnitSphere());
//        scattered.setA(rec.getP());
//        scattered.setB(target.subtractVec(rec.getP()));
//        scattered.setTime(r_in.getTime());
//        attenuation.copyValue(albedo.value(rec.getU(), rec.getV(), rec.getP()));
//        rec.setPDF(rec.getNormal().dot(scattered.direction()) / Math.PI);
//        return true;
        //version 2
//        Onb uvw = new Onb();
//        uvw.build_from_w(rec.getNormal());
//        Vector3 direction = uvw.local(HelperFunctions.random_cosine_direction());
//        direction.make_unit_vector();
//        scattered.setA(rec.getP());
//        scattered.setB(direction);
//        scattered.setTime(r_in.getTime());
//        attenuation.copyValue(albedo.value(rec.getU(), rec.getV(), rec.getP()));
//        rec.setPDF(uvw.getW().dot(scattered.direction()) / Math.PI);
//        return true;
        srec.setIsSpecular(false);
        srec.setAttenuation(albedo.value(rec.getU(), rec.getV(), rec.getP()));
        srec.setPdf_ptr(new CosinePDF(rec.getNormal()));
        return true;
    }

    @Override
    public double scattering_pdf(Ray r_in, HitRecord rec, Ray scattered) {
        double cosine = rec.getNormal().dot(scattered.direction());
        if(cosine < 0) cosine = 0;
        return cosine / Math.PI;
    }
}
