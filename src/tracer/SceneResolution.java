package tracer;

public enum SceneResolution
{
    S_200X100(200, 100),
    S_400X300(400, 300),
    S_500X500(500, 500),
    S_BOX_800(800, 800),
    S_720(1280, 720),
    S_1080(1920, 1080),
    S_4K(3840, 2160),
    S_8K(7680, 4320);

    int width;
    int height;

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    private SceneResolution(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
}
