package tracer.texture;

import tracer.Vector3;

public class ConstantTexture extends Texture
{
    Vector3 color;

    public ConstantTexture()
    {
    }

    public ConstantTexture(Vector3 color)
    {
        this.color = color;
    }

    @Override
    public Vector3 value(double u, double v, Vector3 p)
    {
        return color;
    }
}
