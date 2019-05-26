package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

public class Dielectric extends Material
{
    double ref_idx;

    public Dielectric(double ref_idx)
    {
        this.ref_idx = ref_idx;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered)
    {
        Vector3 outward_normal;
        Vector3 reflected = reflect(r_in.direction(), rec.getNormal());
        double ni_over_nt;
        attenuation.copyValue(1,1,1);
        Vector3 refracted = new Vector3();
        double reflect_prob, cosine;

        if(r_in.direction().dot(rec.getNormal()) > 0)
        {
            outward_normal = rec.getNormal().multiplyConst(-1);
            ni_over_nt = ref_idx;
            cosine = ref_idx * r_in.direction().dot(rec.getNormal()) / r_in.direction().length();
        }
        else
        {
            outward_normal = rec.getNormal();
            ni_over_nt = 1.0 / ref_idx;
            cosine = -1 * r_in.direction().dot(rec.getNormal()) / r_in.direction().length();
        }
        if(refract(r_in.direction(), outward_normal, ni_over_nt, refracted))
        {
            reflect_prob = schlick(cosine, ref_idx);
        }
        else
        {
            scattered.setA(rec.getP());
            scattered.setB(reflected);
            reflect_prob = 1.0;
            //return false;
        }
        if(randG.nextDouble() < reflect_prob)
        {
            scattered.setA(rec.getP());
            scattered.setB(reflected);
        }
        else
        {
            scattered.setA(rec.getP());
            scattered.setB(refracted);
        }
        return true;
    }

    public boolean refract(Vector3 v, Vector3 n, double ni_over_nt, Vector3 refracted)
    {
        Vector3 uv = Vector3.unit_vec(v);
        double dt = uv.dot(n);
        double discriminant = 1.0 - ni_over_nt*ni_over_nt*(1.0-dt*dt);
        if(discriminant > 0)
        {
            refracted.copyValue(uv.subtractVec(n.multiplyConst(dt))
                                  .multiplyConst(ni_over_nt)
                                  .subtractVec(n.multiplyConst(Math.sqrt(discriminant)))
            );
            return true;
        }
        return false;
    }

    public double schlick(double cosine, double ref_idx)
    {
        double r0 = (1-ref_idx) / (1+ref_idx);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1-cosine),5);
    }
}
