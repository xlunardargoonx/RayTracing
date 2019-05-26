package tracer;

public class Vector3
{
    final double e[] = new double[3];

    public Vector3()
    {
        e[0] = 0;
        e[1] = 0;
        e[2] = 0;
    }

    public Vector3(double e0, double e1, double e2)
    {
        e[0] = e0;
        e[1] = e1;
        e[2] = e2;
    }

    public Vector3 (Vector3 v)
    {
        e[0] = v.e[0];
        e[1] = v.e[1];
        e[2] = v.e[2];
    }

    public double x() { return e[0]; }
    public double y() { return e[1]; }
    public double z() { return e[2]; }
    public double r() { return e[0]; }
    public double g() { return e[1]; }
    public double b() { return e[2]; }

    public void copyValue(Vector3 v)
    {
        e[0] = v.e[0];
        e[1] = v.e[1];
        e[2] = v.e[2];
    }

    public void copyValue(double e0, double e1, double e2)
    {
        e[0] = e0;
        e[1] = e1;
        e[2] = e2;
    }

    public Vector3 addVec(Vector3 v)
    {
        Vector3 sum = new Vector3();
        sum.e[0] = this.e[0] + v.e[0];
        sum.e[1] = this.e[1] + v.e[1];
        sum.e[2] = this.e[2] + v.e[2];
        return sum;
    }

    public Vector3 subtractVec(Vector3 v)
    {
        Vector3 diff = new Vector3();
        diff.e[0] = this.e[0] - v.e[0];
        diff.e[1] = this.e[1] - v.e[1];
        diff.e[2] = this.e[2] - v.e[2];
        return diff;
    }

    public Vector3 multiplyVec(Vector3 v)
    {
        Vector3 diff = new Vector3();
        diff.e[0] = this.e[0] * v.e[0];
        diff.e[1] = this.e[1] * v.e[1];
        diff.e[2] = this.e[2] * v.e[2];
        return diff;
    }

    public Vector3 divideVec(Vector3 v)
    {
        Vector3 diff = new Vector3();
        diff.e[0] = this.e[0] / v.e[0];
        diff.e[1] = this.e[1] / v.e[1];
        diff.e[2] = this.e[2] / v.e[2];
        return diff;
    }

    public Vector3 multiplyConst(double value)
    {
        Vector3 diff = new Vector3();
        diff.e[0] = this.e[0] * value;
        diff.e[1] = this.e[1] * value;
        diff.e[2] = this.e[2] * value;
        return diff;
    }

    public Vector3 divideConst(double value)
    {
        Vector3 diff = new Vector3();
        diff.e[0] = this.e[0] / value;
        diff.e[1] = this.e[1] / value;
        diff.e[2] = this.e[2] / value;
        return diff;
    }

    public double getE(int index) {
        return e[index];
    }

    public double length()
    {
        return Math.sqrt(e[0]*e[0] + e[1]*e[1] + e[2]*e[2]);
    }

    public double squared_length()
    {
        return e[0]*e[0] + e[1]*e[1] + e[2]*e[2];
    }

    public void make_unit_vector()
    {
        double length = this.length();
        e[0] /= length;
        e[1] /= length;
        e[2] /= length;
    }

    public double dot(Vector3 v)
    {
        return e[0] * v.e[0] + e[1] * v.e[1] + e[2] * v.e[2];
    }

    public Vector3 cross(Vector3 v)
    {
        return new Vector3( (e[1]*v.e[2] - e[2]*v.e[1]),
                            -1*(e[0]*v.e[2] - e[2]*v.e[0]),
                            (e[0]*v.e[1] - e[1]*v.e[0]));
    }

    public static Vector3 unit_vec(Vector3 v)
    {
        return v.divideConst(v.length());
    }
}