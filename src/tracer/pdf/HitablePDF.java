package tracer.pdf;

import tracer.Vector3;
import tracer.hitable.Hitable;

public class HitablePDF implements PDF{
    Vector3 o;
    Hitable ptr;

    public HitablePDF(Hitable p, Vector3 origin){
        ptr = p;
        o = origin;
    }

    @Override
    public double value(Vector3 direction) {
        return ptr.pdf_value(o, direction);
    }

    @Override
    public Vector3 generate() {
        return ptr.random(o);
    }
}
