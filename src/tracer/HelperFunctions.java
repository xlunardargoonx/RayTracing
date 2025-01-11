package tracer;

import tracer.hitable.Hitable;
import tracer.hitable.XZRect;
import tracer.pdf.CosinePDF;
import tracer.pdf.HitablePDF;
import tracer.pdf.MixturePDF;
import tracer.pdf.PDF;

import java.util.Random;

public class HelperFunctions
{
    public static Random randG = new Random(42);

    public static Vector3 color(Ray r, Hitable world, Hitable light_shape, int depth)
    {
        HitRecord rec = new HitRecord();
        //changing min from 0.0 to 0.001 to get rid of shadow acne
        if(world.hit(r, 0.001, Double.MAX_VALUE, rec))
        {
            ScatterRecord srec = new ScatterRecord();
            Vector3 emitted = rec.getMat().emitted(r, rec, rec.getU(), rec.getV(), rec.getP());
            if(depth < 50 && rec.getMat().scatter(r, rec, srec))
            {
                //version 4
                if(srec.isSpecular()) return srec.getAttenuation().multiplyVec(color(srec.getSpecular_ray(), world, light_shape, depth+1));

                HitablePDF plight = new HitablePDF(light_shape, rec.getP());
                MixturePDF p = new MixturePDF(plight, srec.getPdf_ptr());
                //PDF p = srec.getPdf_ptr();
                Ray scattered = new Ray(rec.getP(), Vector3.unit_vec(p.generate()), r.getTime());
                double pdf_val = p.value((scattered.direction()));
                return srec.getAttenuation()
                        .multiplyVec(color(scattered, world, light_shape, depth+1))
                        .multiplyConst(rec.getMat().scattering_pdf(r, rec, scattered) / pdf_val)
                        .addVec(emitted);
            }
            else
            {
                return emitted;
            }
        }
        else
        {
            return new Vector3(1,0,0);
        }
    }

    public static Vector3 boundingColor(Vector3 color)
    {
        Vector3 col = new Vector3(color);
        if(col.r() < 0.0) col.set(0, 0.0);
        else if(col.r() > 1.0) col.set(0, 1.0);
        if(col.g() < 0.0) col.set(1, 0.0);
        else if(col.g() > 1.0) col.set(1, 1.0);
        if(col.b() < 0.0) col.set(2, 0.0);
        else if(col.b() > 1.0) col.set(2, 1.0);

        return col;
    }

    public static Vector3 randomInUnitSphere()
    {
        Vector3 p;
        do
        {
            p = new Vector3(randG.nextDouble(), randG.nextDouble(), randG.nextDouble()).multiplyConst(2).subtractVec(new Vector3(1,1,1));
        }while(p.squared_length() >= 1.0);
        return p;
    }

    public static Vector3 random_cosine_direction(){
        double r1 = randG.nextDouble();
        double r2 = randG.nextDouble();
        double z = Math.sqrt(1-r2);
        double phi = 2*Math.PI*r1;
        double x = Math.cos(phi) * 2 * Math.sqrt(r2);
        double y = Math.sin(phi) * 2 * Math.sqrt(r2);
        return new Vector3(x, y, z);
    }

    public static Vector3 random_gloss_direction(int n){
        double r1 = randG.nextDouble();
        double r2 = randG.nextDouble();
        double z = Math.pow(r2, 1.0/(n+1));
        double phi = 2*Math.PI*r1;
        double alpha = Math.sqrt(1-Math.pow(r2, 2.0/(n+1.0)));
        double x = Math.cos(phi) * alpha;
        double y = Math.sin(phi) * alpha;
        return new Vector3(x, y, z);
    }

    public static Vector3 random_to_sphere(double radius, double distance_squared){
        double r1 = randG.nextDouble();
        double r2 = randG.nextDouble();
        double z = 1 + r2 * (Math.sqrt(1-radius*r1/distance_squared) - 1);
        double phi = 2*Math.PI*r1;
        double x = Math.cos(phi) * 2 * Math.sqrt(r2);
        double y = Math.sin(phi) * 2 * Math.sqrt(r2);
        return new Vector3(x, y, z);
    }

    public static Vector3 de_nan(Vector3 c){
        if(!(c.getE(0) == c.getE(0))) c.set(0, 0);
        if(!(c.getE(1) == c.getE(1))) c.set(1, 0);
        if(!(c.getE(2) == c.getE(2))) c.set(2, 0);
        return c;
    }
}
