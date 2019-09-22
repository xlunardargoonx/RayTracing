package test;

import java.util.Random;

public class MCTest
{
    static Random rand = new Random(42);

    public static void main(String[] args)
    {

    }

    public static void simpleMC()
    {
        int N = 1000;
        int inside_circle = 0;
        for(int i = 0; i < N; i++)
        {
            double x = 2*rand.nextDouble() - 1;
            double y = 2*rand.nextDouble() - 1;

            if(x*x + y*y < 1)
                inside_circle++;
            System.out.println("Estimate of Pi = " + 4*(double)inside_circle / N);
        }
    }

    public static void simpleMCInfinite()
    {
        int inside_circle = 0;
        int runs = 0;
        while(true)
        {
            double x = 2*rand.nextDouble() - 1;
            double y = 2*rand.nextDouble() - 1;

            if(x*x + y*y < 1)
                inside_circle++;
            if(runs % 100000 == 0)
                System.out.println("Estimate of Pi = " + 4*(double)inside_circle / runs);
        }
    }

    public static void simpleMCStratified()
    {
        int inside_circle = 0;
        int inside_circle_stratified = 0;
        int sqrt_N = 10000;

        for(int i = 0; i < sqrt_N; i++)
        {
            for(int j = 0; j < sqrt_N; j++)
            {
                double x = 2 * rand.nextDouble() - 1;
                double y = 2 * rand.nextDouble() - 1;
                if (x * x + y * y < 1)
                    inside_circle++;
                x = 2 * ((i+rand.nextDouble()) / sqrt_N) - 1;
                y = 2 * ((j+rand.nextDouble()) / sqrt_N) - 1;
                if(x * x + y * y < 1)
                    inside_circle_stratified++;
            }
        }

        // percentage inside circle = Pi / 4;
        System.out.println("Regular    Estimate of PI = " + 4 * (double)inside_circle / (double)(sqrt_N*sqrt_N));
        System.out.println("Stratified Estimate of PI = " + 4 * (double)inside_circle_stratified / (double)(sqrt_N*sqrt_N));
    }

    public static void oneDimensionalMC()
    {
        //integral of x^2 over 0 to 2
        int N = 1000000;
        double sum = 0;
        for(int i = 0; i < N; i++)
        {
            double x = 2 * rand.nextDouble();
            sum += x * x;
        }
        //expect close to 8/3
        System.out.println("I = "  + 2 * sum / (double)N);
    }
}
