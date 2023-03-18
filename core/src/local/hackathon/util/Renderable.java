package local.hackathon.util;

public interface Renderable {
    void show ();

    void render (float delta);

    void hide ();

    void dispose ();
}
