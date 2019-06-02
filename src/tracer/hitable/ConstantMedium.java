package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Isotropic;
import tracer.material.Material;
import tracer.texture.Texture;

import java.util.Random;

public class ConstantMedium extends Hitable
{
    Hitable boundary;
    double density;
    Material phase_function;
    static Random rand = new Random(42);

    public ConstantMedium(Hitable boundary, double density, Texture texture)
    {
        this.boundary = boundary;
        this.density = density;
        this.phase_function = new Isotropic(texture);
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
//        boolean db = rand.nextDouble() < 0.00001;
//        db = false;
        HitRecord rec1 = new HitRecord(), rec2 = new HitRecord();
        if(boundary.hit(r, -Double.MAX_VALUE, Double.MAX_VALUE, rec1))
        {
            if(boundary.hit(r, rec1.getT()+0.0001, Double.MAX_VALUE, rec2))
            {
                //if(db) System.out.println("\nt0 t1 " + rec1.getT() + " " + rec2.getT());
                if(rec1.getT() < t_min)
                    rec1.setT(t_min);
                if(rec2.getT() > t_max)
                    rec2.setT(t_max);
                if(rec1.getT() >= rec2.getT())
                    return false;
                if(rec1.getT() < 0)
                    rec1.setT(0);
                double distance_inside_boundary = (rec2.getT() - rec1.getT())*r.direction().length();
                double hit_distance = -(1.0/density)*Math.log(rand.nextDouble());
                if(hit_distance < distance_inside_boundary)
                {
                    //debug print statment here
                    rec.setT(rec1.getT() + hit_distance / r.direction().length());

                    rec.setP(r.point_at_parameter(rec.getT()));

                    rec.setNormal(new Vector3(1,0,0)); //arbitrary
                    rec.setMat(phase_function);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        return boundary.bounding_box(t0, t1, box);
    }
}
