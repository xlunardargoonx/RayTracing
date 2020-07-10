package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;
import tracer.material.Material;

public class Triangle extends Hitable {
    Vector3[] points = new Vector3[3];
    Material mat;

    public Triangle(Vector3 p1, Vector3 p2, Vector3 p3, Material mat){
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        //Translate vertices based on ray origin
        Vector3 p0t = points[0].subtractVec(r.origin());
        Vector3 p1t = points[1].subtractVec(r.origin());
        Vector3 p2t = points[2].subtractVec(r.origin());

        //Permute components of triangle vertices and ray direction
        Vector3 direction = new Vector3(Math.abs(r.direction().x()), Math.abs(r.direction().y()), Math.abs(r.direction().z()));
        int kz = (direction.x() > direction.y()) ? ((direction.x() > direction.z()) ? 0 : 2) :
                ((direction.y() > direction.z()) ? 1 : 2);
        int kx = kz + 1; if (kx == 3) kx = 0;
        int ky = kx + 1; if (ky == 3) ky = 0;
        Vector3 d = new Vector3(r.direction().getE(kx), r.direction().getE(ky), r.direction().getE(kz));
        p0t = new Vector3(p0t.getE(kx), p0t.getE(ky), p0t.getE(kz));
        p1t = new Vector3(p1t.getE(kx), p1t.getE(ky), p1t.getE(kz));
        p2t = new Vector3(p2t.getE(kx), p2t.getE(ky), p2t.getE(kz));

        //Apply shear transformation to translated vertex positions
        double Sx = -d.x() / d.z();
        double Sy = -d.y() / d.z();
        double Sz = 1.0 / d.z();
        p0t.set(0, p0t.x() + Sx * p0t.z());
        p0t.set(1, p0t.y() + Sy * p0t.z());
        p1t.set(0, p1t.x() + Sx * p1t.z());
        p1t.set(1, p1t.y() + Sy * p1t.z());
        p2t.set(0, p2t.x() + Sx * p2t.z());
        p2t.set(1, p2t.y() + Sy * p2t.z());

        //Compute edge function coefficients e0, e1, and e2
        double e0 = p1t.x() * p2t.y() - p1t.y() * p2t.x();
        double e1 = p2t.x() * p0t.y() - p2t.y() * p0t.x();
        double e2 = p0t.x() * p1t.y() - p0t.y() * p1t.x();

        //Perform triangle edge and determinant tests
        if ((e0 < 0 || e1 < 0 || e2 < 0) && (e0 > 0 || e1 > 0 || e2 > 0))
            return false;
        double det = e0 + e1 + e2;
        if (det == 0)
            return false;

        //Compute scaled hit distance to triangle and test against ray range
        p0t.set(2, p0t.z() * Sz);
        p1t.set(2, p1t.z() * Sz);
        p2t.set(2, p2t.z() * Sz);
        double tScaled = e0 * p0t.z() + e1 * p1t.z() + e2 * p2t.z();
        if (det < 0 && (tScaled >= 0 || tScaled < t_max * det))
            return false;
        else if (det > 0 && (tScaled <= 0 || tScaled > t_max * det))
            return false;

        //Compute barycentric coordinates and  value for triangle intersection
        double invDet = 1.0 / det;
        double b0 = e0 * invDet;
        double b1 = e1 * invDet;
        double b2 = e2 * invDet;
        double t = tScaled * invDet;

        //Compute triangle partial derivatives
//        Vector3 dpdu, dpdv;
        Vector3[] uv = new Vector3[3];
        uv[0] = new Vector3(0, 0, 0);
        uv[1] = new Vector3(1, 0, 0);
        uv[2] = new Vector3(1, 1, 0);
        //GetUVs(uv);
//        //Compute deltas for triangle partial derivatives
//        Vector3 duv02 = uv[0].subtractVec(uv[2]), duv12 = uv[1].subtractVec([2]);
        Vector3 dp02 = points[0].subtractVec(points[2]);
        Vector3 dp12 = points[1].subtractVec(points[2]);

//        double determinant = duv02.getE(0) * duv12.getE(1) - duv02.getE(1) * duv12.getE(0);
//        if (determinant == 0) {
//            //Handle zero determinant for triangle partial derivative matrix>>
//            CoordinateSystem(Normalize(Cross(p2 - p0, p1 - p0)), &dpdu, &dpdv);
//        }
//        else {
//            double invdetUV = 1.0 / determinant;
//            dpdu = ( duv12[1] * dp02 - duv02[1] * dp12) * invdetUV;
//            dpdv = (-duv12[0] * dp02 + duv02[0] * dp12) * invdetUV;
//        }

        //Interpolate  parametric coordinates and hit point>>=
        Vector3 p0 = new Vector3(points[0]), p1 = new Vector3(points[1]), p2 = new Vector3(points[2]);
        Vector3 pHit = p0.multiplyConst(b0)
                .addVec(p1.multiplyConst(b1))
                .addVec(p2.multiplyConst(b2));
        Vector3 uvHit = uv[0].multiplyConst(b0)
                .addVec(uv[1].multiplyConst(b1))
                .addVec(uv[2].multiplyConst(b2));

//        <<Test intersection against alpha texture, if present>>=
//        if (testAlphaTexture && mesh->alphaMask) {
//            SurfaceInteraction isectLocal(pHit, Vector3f(0,0,0), uvHit,
//                    Vector3f(0,0,0), dpdu, dpdv, Normal3f(0,0,0), Normal3f(0,0,0),
//                    ray.time, this);
//            if (mesh->alphaMask->Evaluate(isectLocal) == 0)
//                return false;
//        }

        //need to set t, p, u, v, mat, and normal
        rec.setMat(mat);
        rec.setNormal(Vector3.unit_vec(dp02.cross(dp12)));
        rec.setP(pHit);
        rec.setU(uvHit.getE(0));
        rec.setV(uvHit.getE(1));
        rec.setT(t);
        return true;
    }

//    private void GetUVs(Vector3[] uv){
//        if (mesh->uv) {
//            uv[0] = mesh->uv[v[0]];
//            uv[1] = mesh->uv[v[1]];
//            uv[2] = mesh->uv[v[2]];
//        } else {
//            uv[0] = Point2f(0, 0);
//            uv[1] = Point2f(1, 0);
//            uv[2] = Point2f(1, 1);
//        }
//    }
    @Override
    public boolean bounding_box(double t0, double t1, AABB box) {
        Vector3 min = new Vector3(Math.min(points[0].x(), Math.min(points[1].x(), points[2].x())),
                Math.min(points[0].y(), Math.min(points[1].y(), points[2].y())),
                Math.min(points[0].z(), Math.min(points[1].z(), points[2].z())));
        Vector3 max = new Vector3(Math.max(points[0].x(), Math.max(points[1].x(), points[2].x())),
                Math.max(points[0].y(), Math.max(points[1].y(), points[2].y())),
                Math.max(points[0].z(), Math.max(points[1].z(), points[2].z())));
        box.setMin(min);
        box.setMax(max);
        return true;
    }
}
