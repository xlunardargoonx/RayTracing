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
        //basic perlin noise
        //double n = noise.noise(p.multiplyConst(scale));
        //System.out.println("noise : " + n);
        //return new Vector3(1,1,1).multiplyConst(n);
        //perlin noise shifted up
        //return new Vector3(1,1,1).multiplyConst((n+1)*0.5);
        //camo turb
        //return new Vector3(1,1,1).multiplyConst(noise.turb(p.multiplyConst(scale), 7));
        //marble turb
        //return new Vector3(1,1,1).multiplyConst((Math.sin(noise.turb(p, 7)*10 + scale*p.z())+1)*0.5);
        //marble for final picture in book 2
        return new Vector3(1,1,1).multiplyConst((Math.sin(noise.turb(p.multiplyConst(scale), 7)*5 + scale*p.x())+1)*0.5);
    }
}
