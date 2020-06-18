package tracer;

import tracer.material.Material;

public class HitRecord
{
    double t;
    Vector3 p;
    Vector3 normal;
    Material mat;
    double u,v;
    private double pdf;

    public HitRecord()
    {
        this.t = 0;
        this.p = new Vector3();
        this.normal = new Vector3();
        //init mat later
    }

    public HitRecord(double t, Vector3 p, Vector3 normal, Material mat) {
        this.t = t;
        this.p = p;
        this.normal = normal;
        this.mat = mat;
    }

    public HitRecord(HitRecord h)
    {
        this.t = h.t;
        this.p = h.p;
        this.normal = h.normal;
        this.mat = h.mat;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getT()
    {
        return t;
    }

    public void setT(double t)
    {
        this.t = t;
    }

    public Vector3 getP()
    {
        return p;
    }

    public void setP(Vector3 p)
    {
        this.p = p;
    }

    public Vector3 getNormal()
    {
        return normal;
    }

    public void setNormal(Vector3 normal)
    {
        this.normal = normal;
    }

    public Material getMat() {
        return mat;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public void setRecord(HitRecord r)
    {
        this.setP(r.p);
        this.setT(r.t);
        this.setNormal(r.normal);
        this.setMat(r.mat);
        this.setU(r.getU());
        this.setV(r.getV());
    }

    public void setPDF(double pdf) {
        this.pdf = pdf;
    }

    public double getPDF() {
        return pdf;
    }
}
