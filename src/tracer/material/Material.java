package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

import java.util.Random;

public abstract class Material
{
    protected static Random randG = new Random(42);

    public abstract boolean scatter(Ray r_in, HitRecord rec, Vector3 attenuation, Ray scattered);
    public Vector3 emitted(double u, double v, Vector3 p)
    {
        return new Vector3(0, 0, 0);
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
    public Vector3 reflect(Vector3 v, Vector3 n)
    {
        return v.subtractVec(n.multiplyConst(2*v.dot(n)));
    }
}
