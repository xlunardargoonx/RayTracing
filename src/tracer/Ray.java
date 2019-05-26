package tracer;

public class Ray
{
    Vector3 a;
    Vector3 b;
    double time;

    public Ray()
    {
        a = new Vector3();
        b = new Vector3();
        time = 0;
    }
    public Ray(Vector3 a, Vector3 b)
    {
        this.a = a;
        this.b = b;
        time = 0;
    }

    public Ray(Vector3 a, Vector3 b, double time) {
        this.a = a;
        this.b = b;
        this.time = time;
    }

    public Vector3 origin() { return a; }
    public Vector3 direction() { return b; }
    public Vector3 point_at_parameter(double t)
    {
        return a.addVec(b.multiplyConst(t));
    }

    public Vector3 getA() {
        return a;
    }

    public void setA(Vector3 a) {
        this.a = a;
    }

    public Vector3 getB() {
        return b;
    }

    public void setB(Vector3 b) {
        this.b = b;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
