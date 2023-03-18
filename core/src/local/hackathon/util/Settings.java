package local.hackathon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Settings {
    public static final boolean DEBUG = true;
    public static final float SCALE = 4;
    public static final float PPM = 16;

    public static final int LASER_DAMAGE = 10;
    public static final int ORANGE_DAMAGE = 1000;
    public static final int BANANA_DAMAGE = 5000;

    public static final Skin SKIN = new Skin(Gdx.files.internal("Skins/tracer/skin/tracer-ui.json"));

}
