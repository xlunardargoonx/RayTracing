package tracer.pdf;

import tracer.Onb;
import tracer.Vector3;
import static tracer.HelperFunctions.random_gloss_direction;

public class GlossyPDF implements PDF{
    Onb uvw;
    int exponent;

    public GlossyPDF(int exponent, Vector3 w){
        uvw = new Onb();
        uvw.build_from_w(w);
        this.exponent = exponent;
    }

    @Override
    public double value(Vector3 direction) {
        double cosine = Vector3.unit_vec(direction).dot(uvw.getW());
        if(cosine > 0) return Math.pow(cosine, exponent) / (2*Math.PI) * (exponent+1);
        return 0;
    }

    @Override
    public Vector3 generate() {
        return uvw.local(random_gloss_direction(exponent));
    }
}
