package es.tid.pdg.gdx.core;

public class Size {
    public static final float FULLSCREEN = Float.MIN_VALUE;
    private float width = FULLSCREEN;
    private float height = FULLSCREEN;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
