package tracer.material;

import tracer.HelperFunctions;
import tracer.HitRecord;
import tracer.Ray;
import tracer.ScatterRecord;
import tracer.texture.Texture;

public class Isotropic extends Material
{
    Texture albedo;

    public Isotropic(Texture albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec)
    {
        srec.setPdf_ptr(null);
        srec.setIsSpecular(true);
        srec.getSpecular_ray().setA(rec.getP());
        srec.getSpecular_ray().setB(HelperFunctions.randomInUnitSphere());
        srec.getAttenuation().copyValue(albedo.value(rec.getU(), rec.getV(), rec.getP()));
        return true;
    }
}
