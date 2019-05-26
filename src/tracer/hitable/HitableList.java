package tracer.hitable;

import tracer.HitRecord;
import tracer.Ray;

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
}
