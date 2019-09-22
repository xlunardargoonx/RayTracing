package tracer;

public class CameraInstance
{
    public static Camera cam_for_two_spheres(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(13,2,3);
        Vector3 lookat = new Vector3(0,0,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 20, nx/ny, aperture, dist_to_focus);
    }

    public static Camera cam_for_light(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(19,2,3);
        Vector3 lookat = new Vector3(0,2,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 20, nx/ny, aperture, dist_to_focus);
    }

    public static Camera cam_for_cornell_box(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(278,278,-800);
        Vector3 lookat = new Vector3(278,278,0);
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), 40, nx/ny, aperture, dist_to_focus);
    }

    public static Camera cam_for_final(double nx, double ny)
    {
        Vector3 lookfrom = new Vector3(478, 278, -600);
        Vector3 lookat = new Vector3(278,278,0);//new Vector3(400, 400, 200);//
        double dist_to_focus = 10.0;
        double aperture = 0.0;
        double vfov = 40.0;
        return new Camera(lookfrom, lookat, new Vector3(0,1,0), vfov, nx/ny, aperture, dist_to_focus, 0, 1);
    }
}
