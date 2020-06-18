package tracer.pdf;

import tracer.Vector3;

import java.util.Random;

public class MixturePDF implements PDF{
    PDF p[] = new PDF[2];
    static Random rand = new Random(420);
//    int p0Count = 0;
//    int p1Count = 0;

    public MixturePDF(PDF p0, PDF p1){
        p[0] = p0;
        p[1] = p1;
    }

    @Override
    public double value(Vector3 direction) {
        return 0.5 * p[0].value(direction) + 0.5 * p[1].value(direction);
        //return p[1].value(direction);
    }

    @Override
    public Vector3 generate() {
        if(rand.nextDouble() < 0.5){
//            p0Count++;
            return p[0].generate();
        }
        else{
//            p1Count++;
            return p[1].generate();
        }
//        if(rand.nextInt(2) == 0){
//            return p[0].generate();
//        }
//        else{
//            return p[1].generate();
//        }
        //return p[1].generate();
    }
}
