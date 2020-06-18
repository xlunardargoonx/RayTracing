package tracer.hitable;

import tracer.HitRecord;
import tracer.Onb;
import tracer.Ray;
import tracer.Vector3;
import tracer.hitable.Hitable;
import tracer.material.Material;

import static tracer.HelperFunctions.random_to_sphere;

public class Sphere extends Hitable
{
    Vector3 center;
    double radius;
    Material mat;

    public Sphere(Vector3 center, double radius, Material mat) {
        this.center = center;
        this.radius = radius;
        this.mat = mat;
    }

    public Vector3 getCenter() {
        return center;
    }

    public void setCenter(Vector3 center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Material getMat() {
        return mat;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        Vector3 oc = r.origin().subtractVec(center);
        double a = r.direction().dot(r.direction());
        double b = oc.dot(r.direction());
        double c = oc.dot(oc) - radius*radius;
        double discriminant = b*b - a*c;

        if(discriminant > 0)
        {
            double possibleRoots[] = {(-b - Math.sqrt(discriminant))/a, (-b + Math.sqrt(discriminant))/a};

            for(double temp : possibleRoots)
            {
                if(temp < t_max && temp > t_min)
                {
                    rec.setT(temp);
                    rec.setP(r.point_at_parameter(rec.getT()));
                    rec.setNormal(rec.getP().subtractVec(center).divideConst(radius));
                    rec.setMat(mat);
                    Vector3 uv = get_sphere_uv(rec.getP().subtractVec(center).divideConst(radius));
                    rec.setU(uv.x());
                    rec.setV(uv.y());
                    return true;
                }
            }
//            double temp = (-b - Math.sqrt(discriminant)) / a;
//            if(temp < t_max && temp > t_min)
//            {
//                rec.setT(temp);
//                rec.setP(r.point_at_parameter(rec.getT()));
//                rec.setNormal(rec.getP().subtractVec(center).divideConst(radius));
//                rec.setMat(mat);
//                Vector3 uv = get_sphere_uv(rec.getP().subtractVec(center).divideConst(radius));
//                rec.setU(uv.x());
//                rec.setV(uv.y());
//                return true;
//            }
//            temp = (-b + Math.sqrt(discriminant))/a;
//            if(temp < t_max && temp > t_min)
//            {
//                rec.setT(temp);
//                rec.setP(r.point_at_parameter(rec.getT()));
//                rec.setNormal(rec.getP().subtractVec(center).divideConst(radius));
//                rec.setMat(mat);
//                Vector3 uv = get_sphere_uv(rec.getP().subtractVec(center).divideConst(radius));
//                rec.setU(uv.x());
//                rec.setV(uv.y());
//                return true;
//            }
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        Vector3 v = new Vector3(radius, radius, radius);
        box.setMin(center.subtractVec(v));
        box.setMax(center.addVec(v));
        return true;
    }

    /* return a vector that contain value of u and v
     *  u will be the first and v will be the 2nd
     * */
    public Vector3 get_sphere_uv(Vector3 p)
    {
        double phi = Math.atan2(p.z(),p.x());
        double theta = Math.asin(p.y());
        double u = 1-(phi + Math.PI) / (2*Math.PI);
        double v = (theta + Math.PI/2) / Math.PI;
        return new Vector3(u, v, 0);
    }

    @Override
    public double pdf_value(Vector3 o, Vector3 v) {
        HitRecord rec = new HitRecord();
        if(this.hit(new Ray(o,v), 0.001, Double.MAX_VALUE, rec)){
            double cos_theta_max = Math.sqrt(1 - radius*radius / (center.subtractVec(o).squared_length()));
            double solid_angle = 2*Math.PI*(1-cos_theta_max);
            return 1 / solid_angle;
        }
        return 0;
    }

    @Override
    public Vector3 random(Vector3 o) {
        Vector3 direction = center.subtractVec(o);
        double distance_squared = direction.squared_length();
        Onb uvw = new Onb();
        uvw.build_from_w(direction);
        return  uvw.local(random_to_sphere(radius, distance_squared));
    }
}
