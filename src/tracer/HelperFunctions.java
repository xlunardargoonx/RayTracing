package tracer;

import tracer.hitable.Hitable;

public class HelperFunctions
{
    public static Vector3 color(Ray r, Hitable world, int depth)
    {
        HitRecord rec = new HitRecord();
        //System.out.println("depth: " + depth);
        //changing min from 0.0 to 0.001 to get rid of shadow acne
        if(world.hit(r, 0.001, Double.MAX_VALUE, rec))
        {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            Vector3 emitted = rec.getMat().emitted(rec.getU(), rec.getV(), rec.getP());
            if(depth < 50 && rec.getMat().scatter(r, rec, attenuation, scattered))
            {
                return attenuation.multiplyVec(color(scattered,world,depth+1)).addVec(emitted);
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
}
