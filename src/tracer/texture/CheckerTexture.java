package tracer.texture;

import tracer.Vector3;
import tracer.texture.Texture;

import static java.lang.Math.sin;

public class CheckerTexture extends Texture
{
    Texture odd, even;

    public CheckerTexture()
    {
    }

    public CheckerTexture(Texture even, Texture odd)
    {
        this.odd = odd;
        this.even = even;
    }

    @Override
    public Vector3 value(double u, double v, Vector3 p)
    {
        double sines = sin(10*p.x()) * sin(10*p.y()) * sin(10*p.z());
        if(sines < 0)
            return odd.value(u,v,p);
        else
            return even.value(u,v,p);
    }
}
