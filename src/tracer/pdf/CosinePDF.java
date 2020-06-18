package tracer.pdf;

import tracer.Onb;
import tracer.Vector3;

import static tracer.HelperFunctions.random_cosine_direction;

public class CosinePDF implements PDF{
    Onb uvw;
    public CosinePDF(Vector3 w){
        uvw = new Onb();
        uvw.build_from_w(w);
    }

    @Override
    public double value(Vector3 direction) {
        double cosine = Vector3.unit_vec(direction).dot(uvw.getW());
        if(cosine > 0) return cosine / Math.PI;
        return 0;
    }

    @Override
    public Vector3 generate() {
        return uvw.local(random_cosine_direction());
    }
}
