package tracer;

import tracer.hitable.Hitable;
import tracer.hitable.XZRect;
import tracer.pdf.CosinePDF;
import tracer.pdf.HitablePDF;
import tracer.pdf.MixturePDF;

import java.util.Random;

public class HelperFunctions
{
    public static Random randG = new Random(42);

    public static Vector3 color(Ray r, Hitable world, Hitable light_shape, int depth)
    {
        HitRecord rec = new HitRecord();
        //System.out.println("depth: " + depth);
        //changing min from 0.0 to 0.001 to get rid of shadow acne
        if(world.hit(r, 0.001, Double.MAX_VALUE, rec))
        {
//            Ray scattered = new Ray();
//            Vector3 attenuation = new Vector3();
            ScatterRecord srec = new ScatterRecord();
            //Vector3 emitted = rec.getMat().emitted(rec.getU(), rec.getV(), rec.getP());
            Vector3 emitted = rec.getMat().emitted(r, rec, rec.getU(), rec.getV(), rec.getP());
            if(depth < 50 && rec.getMat().scatter(r, rec, srec))
            {
                //version 1
                //return attenuation.multiplyVec(color(scattered,world,depth+1)).addVec(emitted);

                //verson 2 with hardcoded light
//                Vector3 on_light = new Vector3(213 + randG.nextDouble() * (343-213), 554, 227 + randG.nextDouble() * (332-227));
//                Vector3 to_light = on_light.subtractVec(rec.getP());
//                double distance_squared = to_light.squared_length();
//                to_light.make_unit_vector();
//                if(to_light.dot(rec.getNormal()) < 0) return emitted;
//                double light_area = (343-213) * (332-227);
//                double light_cosine = Math.abs(to_light.y());
//                if(light_cosine < 0.000001) return emitted;
//                double pdf = distance_squared / (light_cosine * light_area);
//                scattered = new Ray(rec.getP(), to_light, r.time);
//                return attenuation.multiplyVec(color(scattered,world,depth+1)).multiplyConst(rec.getMat().scattering_pdf(r, rec, scattered)/ pdf/*rec.getPDF()*/).addVec(emitted);

                //version 3
//                CosinePDF p1 = new CosinePDF(rec.getNormal());
//                Hitable light_shape = new XZRect(213, 343, 227, 332, 554, null);
//                HitablePDF p0 = new HitablePDF(light_shape, rec.p);
//                MixturePDF p = new MixturePDF(p0, p1);
//                scattered = new Ray(rec.getP(), Vector3.unit_vec(p.generate()), r.getTime());
//                double pdf = p.value(scattered.direction());
//
//                Vector3 res = color(scattered,world,depth+1);
//                double factor = rec.getMat().scattering_pdf(r, rec, scattered)/ pdf;
//                Vector3 temp = attenuation.multiplyVec(res).multiplyConst(factor).addVec(emitted);
//                return temp;

                //version 4
                if(srec.isSpecular()) return srec.getAttenuation().multiplyVec(color(srec.getSpecular_ray(), world, light_shape, depth+1));

                HitablePDF plight = new HitablePDF(light_shape, rec.getP());
                MixturePDF p = new MixturePDF(plight, srec.getPdf_ptr());
                Ray scattered = new Ray(rec.getP(), Vector3.unit_vec(p.generate()), r.getTime());
                double pdf_val = p.value((scattered.direction()));
                //bug what if pdf_val is 0, then we have divide by 0
                return srec.getAttenuation()
                        .multiplyVec(color(scattered, world, light_shape, depth+1))
                        .multiplyConst(rec.getMat().scattering_pdf(r, rec, scattered)/ pdf_val/*rec.getPDF()*/)
                        .addVec(emitted);
            }
            else
            {
                return emitted;
            }
        }
        else
        {
//            Vector3 unit_dir = Vector3.unit_vec(r.direction());
//            double t = 0.5 * (unit_dir.y() + 1.0);
//            return new Vector3(1.0, 1.0, 1.0).multiplyConst(1.0 - t).addVec(new Vector3(0.5, 0.7, 1.0).multiplyConst(t));
            return new Vector3(0,0,0);
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
        double z = Math.pow(1-r2, 1.0/(n+1));
        double phi = 2*Math.PI*r1;

        double alpha = Math.sqrt(Math.pow(r2, 2/(n+1)));
        double x = Math.cos(phi) * alpha * 2;
        double y = Math.sin(phi) * alpha * 2;
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
