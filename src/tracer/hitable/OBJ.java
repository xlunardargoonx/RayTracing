package tracer.hitable;

import tracer.*;
import tracer.material.Material;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJ extends Hitable {
    BVH_node obj;
    Material mat;

    public OBJ(String filePath, Material mat, int scale){
        this.mat = mat;
        loadFile(filePath, scale);
    }

    private void loadFile(String filePath, int scale) {
        try
        {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            List<Vector3> points = new ArrayList<>();
            List<Vector3> colors = new ArrayList<>();
            List<Hitable> hitList = new ArrayList<>();
            while(sc.hasNext()){
                String string = sc.next();
                if(string.equals("v")){
                    points.add(new Vector3(sc.nextDouble()*scale, sc.nextDouble()*scale, sc.nextDouble()*scale));
                    if(sc.hasNextDouble())
                        colors.add(new Vector3(sc.nextDouble(), sc.nextDouble(), sc.nextDouble()));
                }
                else if(string.equals("f")){
                    String p0 = sc.next();
                    String p1 = sc.next();
                    String p2 = sc.next();

                    hitList.add(new Triangle(points.get(Integer.parseInt(p0.substring(0, p0.indexOf('/')))-1),
                            points.get(Integer.parseInt(p1.substring(0, p1.indexOf('/')))-1),
                            points.get(Integer.parseInt(p2.substring(0, p2.indexOf('/')))-1),
                            mat));
                }
            }
            System.out.println(points.size());
            System.out.println(colors.size());
            System.out.println(hitList.size());
            obj = new BVH_node(hitList, 0 ,1);
            AABB box = new AABB();
            obj.bounding_box(0, 1, box);
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("no such file");
            e.printStackTrace();
        }
    }

    @Override
    public boolean hit(Ray r, double t_min, double t_max, HitRecord rec) {
        return obj.hit(r, t_min, t_max, rec);
    }

    @Override
    public boolean bounding_box(double t0, double t1, AABB box) {
        return obj.bounding_box(t0, t1, box);
    }
}
