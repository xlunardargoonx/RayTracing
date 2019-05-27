package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;
import tracer.Vector3;

import java.util.ArrayList;
import java.util.List;

public class HitableList extends Hitable
{
    List<Hitable> hitList;

    public HitableList()
    {
        this.hitList = new ArrayList<>();
    }

    public void addHitable(Hitable h)
    {
        hitList.add(h);
    }

    public List<Hitable> getHitList() {
        return hitList;
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec)
    {
        HitRecord temp_rec = new HitRecord();
        boolean hit_anything = false;
        double closest_so_far = t_max;
        for(int i = 0; i < hitList.size(); i++)
        {
            if(hitList.get(i).hit(r, t_min, closest_so_far, temp_rec))
            {
                hit_anything = true;
                closest_so_far = temp_rec.getT();
                rec.setRecord(temp_rec);
            }
        }
        return hit_anything;
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box)
    {
        if(hitList.size() < 1) return false;
        AABB temp_box = new AABB();

        boolean first_true = hitList.get(0).bounding_box(t0, t1, temp_box);
        if(!first_true) return false;
        else box.copy(temp_box);

        for(int i = 1; i < hitList.size(); i++)
        {
            if(hitList.get(i).bounding_box(t0, t1, temp_box)) AABB.surrounding_box(box, temp_box, box);
            else return false;
        }
        return true;
    }
}
