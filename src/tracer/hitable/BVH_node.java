package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;

import java.util.List;
import java.util.Random;

public class BVH_node extends Hitable
{
    Hitable left, right;
    AABB box;

    public BVH_node()
    {
    }

    public BVH_node(List<Hitable> l, double time0, double time1)
    {
        Random rand = new Random(42);
        int axis = (int)(3*rand.nextDouble());

        //sort based on axis
        if(axis == 0)
            l.sort(BVH_node::boxXCompare);
        else if(axis == 1)
            l.sort(BVH_node::boxYCompare);
        else
            l.sort(BVH_node::boxZCompare);

        if(l.size() == 1)
        {
            left = right = l.get(0);
        }
        else if(l.size() ==2)
        {
            left = l.get(0);
            right = l.get(1);
        }
        else
        {
            //divide the list in half
            left = new BVH_node(l.subList(0, l.size()/2), time0, time1);
            right = new BVH_node(l.subList(l.size()/2, l.size()), time0, time1);
        }

        AABB box_left = new AABB(), box_right = new AABB();
        if(!left.bounding_box(time0, time1, box_left) || !right.bounding_box(time0, time1, box_right))
            System.out.println("NO bounding box in bvh_node constructer");
        box = new AABB();
        AABB.surrounding_box(box_left, box_right, box);
//        System.out.println("List size = " + l.size());
//        System.out.println("Distance = " + box.getMax().subtractVec(box.getMin()).length());
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        if(box.hit(r, t_min, t_max))
        {
            HitRecord left_rec = new HitRecord(), right_rec = new HitRecord();
            boolean hit_left = left.hit(r, t_min, t_max, left_rec);
            boolean hit_right = right.hit(r, t_min, t_max, right_rec);

            if(hit_left && hit_right)
            {
                if(left_rec.getT() < right_rec.getT())
                    rec.setRecord(left_rec);
                else
                    rec.setRecord(right_rec);
                return true;
            }
            else if(hit_left)
            {
                rec.setRecord(left_rec);
                return true;
            }
            else if(hit_right)
            {
                rec.setRecord(right_rec);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        box.copy(this.box);
        return true;
    }

    private static int boxCompare(Hitable a, Hitable b, int i)
    {
        AABB box_left = new AABB(), box_right = new AABB();
        if(!a.bounding_box(0,0, box_left) || !b.bounding_box(0, 0, box_right))
        {
            System.out.println("NO bounding box in bvh_node constructer");
        }

        if(box_left.min.getE(i) - box_right.min.getE(i) < 0.0) return -1;
        return 1;
    }

    private static int boxXCompare(Hitable a, Hitable b)
    {
        return boxCompare(a, b, 0);
    }

    private static int boxYCompare(Hitable a, Hitable b)
    {
        return boxCompare(a, b, 1);
    }

    private static int boxZCompare(Hitable a, Hitable b)
    {
        return boxCompare(a, b, 2);
    }
}
