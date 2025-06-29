package tracer.integrator;

import tracer.*;
import tracer.hitable.Hitable;
import tracer.pdf.HitablePDF;
import tracer.pdf.MixturePDF;

import java.time.LocalTime;
import java.util.Random;

import static tracer.HelperFunctions.*;

public class BasicIntegrator extends Integrator{
    Random rand = new Random(42);

    public BasicIntegrator(int num_of_samples, int width, int height, Camera cam) {
        super(num_of_samples, width, height, cam);
    }

    @Override
    public void integrate(Hitable world, Hitable light_shape) {
        for(int j = 0; j < this.height; j++)
        {
            for(int i = 0; i < this.width; i++)
            {
                Vector3 col = new Vector3();
                for(int s = 0; s < this.num_of_samples; s++)
                {
                    double u = ((double) i + rand.nextDouble()) / (double) this.width;
                    double v = ((double) j + rand.nextDouble()) / (double) this.height;

                    Ray r = this.cam.getRay(u, v);
                    col = col.addVec(de_nan(color(r, world, light_shape,0)));
                }
                col = col.divideConst(this.num_of_samples);
                //gamma corrected "gamma 2" = square root = raise to 1/gamma
                //better gamma 2.2
                col = new Vector3(Math.pow(col.r(),1/2.2), Math.pow(col.g(), 1/2.2), Math.pow(col.b(), 1/2.2));

                col.copyValue(boundingColor(col));
                this.image_buf[i][j] = col;

//                int ir = (int)(254.99*col.r());
//                int ig = (int)(254.99*col.g());
//                int ib = (int)(254.99*col.b());
//
//                int argb = 255;
//                argb = argb << 24;
//                argb ^= ir << 16;
//                argb ^= ig << 8;
//                argb ^= ib;
                //bufferedImage.setRGB(i, this.height-j-1, argb);

            }
        }
    }

    @Override
    public Vector3 color(Ray r, Hitable world, Hitable light_shape, int depth) {
        HitRecord rec = new HitRecord();
        //changing min from 0.0 to 0.001 to get rid of shadow acne
        if(world.hit(r, 0.001, Double.MAX_VALUE, rec))
        {
            ScatterRecord srec = new ScatterRecord();
            Vector3 emitted = rec.getMat().emitted(r, rec, rec.getU(), rec.getV(), rec.getP());
            if(depth < 50 && rec.getMat().scatter(r, rec, srec))
            {
                //version 4
                if(srec.isSpecular()) return srec.getAttenuation().multiplyVec(color(srec.getSpecular_ray(), world, light_shape, depth+1));

                HitablePDF plight = new HitablePDF(light_shape, rec.getP());
                MixturePDF p = new MixturePDF(plight, srec.getPdf_ptr());
                Ray scattered = new Ray(rec.getP(), Vector3.unit_vec(p.generate()), r.getTime());
                double pdf_val = p.value((scattered.direction()));
                return srec.getAttenuation()
                        .multiplyVec(color(scattered, world, light_shape, depth+1))
                        .multiplyConst(rec.getMat().scattering_pdf(r, rec, scattered) / pdf_val/*rec.getPDF()*/)
                        .addVec(emitted);
            }
            else
            {
                return emitted;
            }
        }
        else
        {
            return new Vector3(0,0,0);
        }
    }
}
