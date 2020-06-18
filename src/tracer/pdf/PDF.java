package tracer.pdf;

import tracer.Vector3;

public interface PDF {
    public double value(Vector3 direction);
    public Vector3 generate();
}
