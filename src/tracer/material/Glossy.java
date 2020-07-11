package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.ScatterRecord;
import tracer.Vector3;
import tracer.pdf.GlossyPDF;
import tracer.texture.Texture;

public class Glossy extends Material{
    int specular_exponent;
    Texture albedo;

    public Glossy(Texture albedo, int exponent){
        this.albedo = albedo;
        specular_exponent = exponent;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec) {
        Vector3 reflected = reflect(Vector3.unit_vec(r_in.direction()), rec.getNormal());
        srec.setIsSpecular(false);
        srec.setAttenuation(albedo.value(rec.getU(), rec.getV(), rec.getP()));
        //srec.setPdf_ptr(new GlossyPDF(specular_exponent, rec.getNormal()));
        srec.setPdf_ptr(new GlossyPDF(specular_exponent, reflected));
        return true;
    }

    @Override
    public double scattering_pdf(Ray r_in, HitRecord rec, Ray scattered) {
        Vector3 reflected = reflect(Vector3.unit_vec(r_in.direction()), rec.getNormal());
        //double cosine = rec.getNormal().dot(scattered.direction());
        double cosine = reflected.dot(scattered.direction());
        if(cosine < 0) cosine = 0;
        return Math.pow(cosine, specular_exponent) / (2*Math.PI) * (specular_exponent+1);
    }
}
