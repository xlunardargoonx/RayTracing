package tracer.texture;

import tracer.Vector3;

public abstract class Texture
{
    public abstract Vector3 value(double u, double v, Vector3 p);
}
