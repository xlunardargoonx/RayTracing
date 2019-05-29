package tracer;

import java.util.Random;

public class Perlin
{
    static Random rand = new Random(42);
    //static double randouble[] = perlin_generate_f();
    static Vector3 ranvec[] = perlin_generate();
    static int perm_x[] =  perlin_generate_perm(),
            perm_y[] = perlin_generate_perm(),
            perm_z[] = perlin_generate_perm();

    public Perlin()
    {
    }

    public double noise(Vector3 p)
    {
//        double u = p.x() - Math.floor(p.x());
//        double v = p.y() - Math.floor(p.y());
//        double w = p.z() - Math.floor(p.z());
//        double u = 1 - (p.x() - Math.floor(p.x()));
//        double v = 1 - (p.y() - Math.floor(p.y()));
//        double w = 1 - (p.z() - Math.floor(p.z()));
        double u = rand.nextDouble(), v = rand.nextDouble(), w = rand.nextDouble();
        int i = (int) Math.floor(4*p.x());
        int j = (int) Math.floor(4*p.y());
        int k = (int) Math.floor(4*p.z());
//        int i = (int)(4*p.x());
//        int j = (int)(4*p.y());
//        int k = (int)(4*p.z());
        Vector3 c[][][] = new Vector3[2][2][2];
        //double c[][][] = new double[2][2][2];
        for(int di = 0; di < 2; di++)
            for(int dj = 0; dj < 2; dj++)
                for(int dk = 0; dk < 2; dk++)
                    c[di][dj][dk] = ranvec[perm_x[(i+di) & 255] ^ perm_y[(j+dj) & 255] ^ perm_z[(k+dk) & 255]];
                    //c[di][dj][dk] = randouble[perm_x[(i+di) & 255] ^ perm_y[(j+dj) & 255] ^ perm_z[(k+dk) & 255]];
        return trilinear_interp(c,u,v,w);
        //return c[0][0][0].length();
        //return c[0][0][0].dot(new Vector3(1, 1, 1));
        //return c[0][0][0].x();
        //return c[0][0][0];
    }

    private double trilinear_interp(Vector3 c[][][], double u, double v, double w)
    {
        double uu = u, vv = v, ww = w;
//        double uu = u * u * (3 - 2 * u);
//        double vv = v * v * (3 - 2 * v);
//        double ww = w * w * (3 - 2 * w);
        double accum = 0;
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 2; j++)
                for(int k = 0; k < 2; k++)
                {
                    Vector3 weight_v = new Vector3(u-i, v-j, w-k);//new Vector3(1,1,1);//
                    accum += (i * uu + (1.0 - i) * (1.0 - uu)) *
                             (j * vv + (1.0 - j) * (1.0 - vv)) *
                             (k * ww + (1.0 - k) * (1.0 - ww)) * c[i][j][k].dot(weight_v);
                }
        return accum;
    }

    public static double[] perlin_generate_f()
    {
        double p[] = new double[256];
        for(int i = 0; i < 256; i++)
        {
            p[i] = rand.nextDouble();
        }
        return p;
    }

    public static Vector3[] perlin_generate()
    {
        Vector3 p[] = new Vector3[256];
        for(int i = 0; i < 256; i++)
        {
            //p[i] = Vector3.unit_vec(new Vector3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
            p[i] = Vector3.unit_vec(Vector3.unit_vec(new Vector3(-1.0 + 2.0 * rand.nextDouble(), -1.0 + 2.0 * rand.nextDouble(), -1.0 + 2.0 * rand.nextDouble())));
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
