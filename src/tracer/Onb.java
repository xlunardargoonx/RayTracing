package tracer;

public class Onb {
    Vector3 axis[];
    public Onb(){
        axis = new Vector3[3];
    }

    public Vector3 getU(){
        return axis[0];
    }

    public Vector3 getV(){
        return axis[1];
    }

    public Vector3 getW(){
        return axis[2];
    }

    public Vector3 local(double a, double b, double c){
        return axis[0].multiplyConst(a)
                .addVec(axis[1].multiplyConst(b))
                .addVec(axis[2].multiplyConst(c));
    }

    public Vector3 local(Vector3 a){
        return axis[0].multiplyConst(a.x())
                .addVec(axis[1].multiplyConst(a.y()))
                .addVec(axis[2].multiplyConst(a.z()));
    }

    public void build_from_w(Vector3 n){
        n.make_unit_vector();
        axis[2] = n;
        Vector3 a;
        if(Math.abs(getW().x()) > 0.9){
            a = new Vector3(0, 1, 0);
        }
        else{
            a = new Vector3(1, 0, 0);
        }
        axis[1] = getW().cross(a);
        axis[1].make_unit_vector();
        axis[0] = getW().cross(getV());
    }
}
