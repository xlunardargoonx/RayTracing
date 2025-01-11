package tracer.material;

import tracer.HitRecord;
import tracer.Ray;
import tracer.ScatterRecord;
import tracer.Vector3;

import java.util.Random;

public class MixMaterial  extends Material{
    Material mat[] = new Material[2];
    static Random rand = new Random(420);

    public MixMaterial(Material m1, Material m2){
        mat[0] = m1;
        mat[1] = m2;
    }

    @Override
    public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec) {
        if(rand.nextDouble() < 0.5){
            return mat[0].scatter(r_in, rec, srec);
        }
        else{
            return mat[1].scatter(r_in, rec, srec);
        }
    }

    @Override
    public double scattering_pdf(Ray r_in, HitRecord rec, Ray scattered) {
        return mat[0].scattering_pdf(r_in, rec, scattered) * 0.5 + mat[1].scattering_pdf(r_in, rec, scattered) * 0.5;
    }

    @Override
    public Vector3 emitted(double u, double v, Vector3 p) {
        if(rand.nextDouble() < 0.5){
            return mat[0].emitted(u, v, p);
        }
        else{
            return mat[1].emitted(u, v, p);
        }
    }

    @Override
    public Vector3 emitted(Ray r_in, HitRecord rec, double u, double v, Vector3 p) {
        if(rand.nextDouble() < 0.5){
            return mat[0].emitted(r_in, rec, u, v, p);
        }
        else{
            return mat[1].emitted(r_in, rec, u ,v ,p);
        }
    }

    @Override
    public Vector3 reflect(Vector3 v, Vector3 n) {
        if(rand.nextDouble() < 0.5){
            return mat[0].reflect(v, n);
        }
        else{
            return mat[1].reflect(v, n);
        }
    }
}
