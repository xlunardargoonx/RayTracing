package tracer;

import java.util.Random;

public class Perlin
{
    static Random rand = new Random(42);
    static double randouble[] = perlin_generate();
    static int perm_x[] =  perlin_generate_perm(),
            perm_y[] = perlin_generate_perm(),
            perm_z[] = perlin_generate_perm();

    public Perlin()
    {
    }

    public double noise(Vector3 p)
    {
        double u = p.x() - Math.floor(p.x());
        double v = p.y() - Math.floor(p.y());
        double w = p.z() - Math.floor(p.z());
        u = u * u * (3 - 2 * u);
        v = v * v * (3 - 2 * v);
        w = w * w * (3 - 2 * w);
//        int i = (int)(4*p.x()) & 255;
//        int j = (int)(4*p.y()) & 255;
//        int k = (int)(4*p.z()) & 255;
//        return randouble[perm_x[i] ^ perm_y[j] ^ perm_z[k]];
        int i = (int) Math.floor(p.x());
        int j = (int) Math.floor(p.y());
        int k = (int) Math.floor(p.z());
        double c[][][] = new double[2][2][2];
        for(int di = 0; di < 2; di++)
            for(int dj = 0; dj < 2; dj++)
                for(int dk = 0; dk < 2; dk++)
                    c[di][dj][dk] = randouble[perm_x[(i+di) & 255] ^ perm_y[(j+dj) & 255] ^ perm_z[(k+dk) & 255]];
        return trilinear_interp(c,u,v,w);
    }

    private double trilinear_interp(double c[][][], double u, double v, double w)
    {
        double accum = 0;
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 2; j++)
                for(int k = 0; k < 2; k++)
                    accum += (i * u + (1.0 - i) * (1.0 - u)) *
                             (j * v + (1.0 - j) * (1.0 - v)) *
                             (k * w + (1.0 - k) * (1.0 - w)) * c[i][j][k];
        return accum;
    }

    public static double[] perlin_generate()
    {
        double p[] = new double[256];
        for(int i = 0; i < 256; i++)
        {
            p[i] = rand.nextDouble();
        }
        return p;
    }

    public static void permute(int p[])
    {
        for(int i = p.length-1; i >0; i--)
        {
            int target = (int)(rand.nextDouble()*(i+1));
            int temp = p[i];
            p[i] = p[target];
            p[target] = temp;
        }
    }

    public static int[] perlin_generate_perm()
    {
        int p[] = new int[256];
        for(int i = 0; i < 256; i++)
            p[i] = i;
        permute(p);
        return p;
    }
}
