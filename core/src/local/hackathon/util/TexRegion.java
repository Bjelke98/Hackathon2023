package local.hackathon.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexRegion {
    public static TextureRegion[] tex(Texture sheet, int cols){
        return tex2(sheet, cols, 1)[0];
    }
    public static TextureRegion[][] tex2(Texture sheet, int cols, int rows){
        return TextureRegion.split(sheet,
        100,
        100);
    }
}
