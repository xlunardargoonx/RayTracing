package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.hitable.Hitable;
import tracer.material.Material;

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
            double temp = (-b - Math.sqrt(discriminant)) / a;
            if(temp < t_max && temp > t_min)
            {
                rec.setT(temp);
                rec.setP(r.point_at_parameter(rec.getT()));
                rec.setNormal(rec.getP().subtractVec(center).divideConst(radius));
                rec.setMat(mat);
                return true;
            }
            temp = (-b + Math.sqrt(discriminant))/a;
            if(temp < t_max && temp > t_min)
            {
                rec.setT(temp);
                rec.setP(r.point_at_parameter(rec.getT()));
                rec.setNormal(rec.getP().subtractVec(center).divideConst(radius));
                rec.setMat(mat);
                return true;
            }
        }
        return false;
    }
}
