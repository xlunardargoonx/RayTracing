package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.ScatterRecord;
import tracer.Vector3;

public abstract class Material
{

    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec)
    {
        return false;
    }

    public double scattering_pdf(Ray r_in, HitRecord rec, Ray scattered){
        return 0.0;
    }

    public Vector3 emitted(double u, double v, Vector3 p)
    {
        return new Vector3(0, 0, 0);
    }

    public Vector3 emitted(Ray r_in, HitRecord rec, double u, double v, Vector3 p)
    {
        return new Vector3(0, 0, 0);
    }

    public Vector3 reflect(Vector3 v, Vector3 n)
    {
        return v.subtractVec(n.multiplyConst(2*v.dot(n)));
    }
}
