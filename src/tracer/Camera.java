package tracer;

import java.util.Random;

public class Camera
{
    Vector3 origin;
    Vector3 lower_left_corner;
    Vector3 horizontal;
    Vector3 vertical;
    Vector3 u,v,w;
    double lens_radius;
    //added t0 and t1 for timing
    double time0, time1;

    static Random randG = new Random(42);

    public Camera(Vector3 origin, Vector3 lower_left_corner, Vector3 horizontal, Vector3 vertical)
    {
        this.origin = origin;
        this.lower_left_corner = lower_left_corner;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Camera()
    {
        this.origin = new Vector3(0, 0 ,0);
        this.lower_left_corner = new Vector3(-2, -1, -1);
        this.horizontal = new Vector3(4, 0, 0);
        this.vertical = new Vector3(0, 2, 0);
    }

    public Camera(Vector3 lookfrom, Vector3 lookat, Vector3 vup, double vfov, double aspect, double aperature, double focus_dist, double t0, double t1)
    {
        time0 = t0;
        time1 = t1;
        lens_radius = aperature / 2.0;
        double theta = vfov * Math.PI /180;
        double half_height = Math.tan(theta/2);
        double half_width = aspect * half_height;

        origin = lookfrom;
        w = Vector3.unit_vec(lookfrom.subtractVec(lookat));
        u = Vector3.unit_vec(vup.cross(w));
        v = w.cross(u);
        lower_left_corner = origin.subtractVec(u.multiplyConst(half_width*focus_dist))
                                  .subtractVec(v.multiplyConst(half_height*focus_dist))
                                  .subtractVec(w.multiplyConst(focus_dist));
        horizontal = u.multiplyConst(2*half_width*focus_dist);
        vertical = v.multiplyConst(2*half_height*focus_dist);
    }

    public Ray getRay(double s, double t)
    {
        Vector3 rd = random_in_unit_disk().multiplyConst(lens_radius);
        Vector3 offset = u.multiplyConst(rd.x())
                          .addVec(v.multiplyConst(rd.y()));
        double time = time0 + randG.nextDouble() * (time1 - time0);
        return new Ray(origin.addVec(offset), lower_left_corner.addVec(horizontal.multiplyConst(s))
                                                .addVec(vertical.multiplyConst(t))
                                                .subtractVec(origin)
                                                .subtractVec(offset), time);
    }

    public Vector3 random_in_unit_disk()
    {
        Vector3 p;
        do
        {
            p = new Vector3(randG.nextDouble(), randG.nextDouble(), 0).multiplyConst(2).subtractVec(new Vector3(1,1,0));
        }while(p.dot(p) >= 1.0);
        return p;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3 origin) {
        this.origin = origin;
    }

    public Vector3 getLower_left_corner() {
        return lower_left_corner;
    }

    public void setLower_left_corner(Vector3 lower_left_corner) {
        this.lower_left_corner = lower_left_corner;
    }

    public Vector3 getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Vector3 horizontal) {
        this.horizontal = horizontal;
    }

    public Vector3 getVertical() {
        return vertical;
    }

    public void setVertical(Vector3 vertical) {
        this.vertical = vertical;
    }
}
