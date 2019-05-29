package tracer.texture;

import tracer.Perlin;
import tracer.Vector3;

public class NoiseTexture extends Texture
{
    Perlin noise;
    double scale;

    public NoiseTexture()
    {
        noise = new Perlin();
        scale = 1;
    }

    public NoiseTexture(double scale)
    {
        this.scale = scale;
        noise = new Perlin();
    }

    @Override
    public Vector3 value(double u, double v, Vector3 p)
    {
        double n = noise.noise(p.multiplyConst(scale));
        //System.out.println("noise : " + n);
        return new Vector3(1,1,1).multiplyConst(n);
    }
}
