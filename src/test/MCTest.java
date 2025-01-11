package test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.joining;

public class MCTest
{
    static Random rand = new Random(42);

    public static void main(String[] args)
    {
        List<Integer> number = Arrays.asList(2,3,4,5);
        System.out.println(number.stream()
                .map(i -> ((i%2==0) ? 'e' : 'o') + String.valueOf(i))
                .collect(joining(",")));

        int[] a = new int[]{2,4,10,2188,448066,389018,566164,5572,154,28,5338,6562,391204,389020,59050,5410,19756,59122,25012,389260,389044,531514,316,536770,177220,182476,74,76,802,82,2260,11890,19684,730,7516,395578,100,408700,6634,5356,5330,389098,531442,244,389746,5332,64378,177148,6058,389026};
        int[] b = new int[]{2,448066,5572,4,177220,389260,74,536770,10,76,182476,5330,82,5332,2260,389020,5338,154,566164,7516,28,389018,391204,5410,802,100,389026,6058,6634,5356,19756,389098,389044,11890,59122,531514,25012,389746,408700,64378,395578,316};

        Arrays.sort(a);
        Arrays.sort(b);
        for(int i : a) System.out.print(i + ", ");
        System.out.println();
        for(int i : b) System.out.print(i + ", ");
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
