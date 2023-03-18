package local.hackathon.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexRegion {
    public static TextureRegion[] tex(Texture sheet, int cols){
        return tex2(sheet, 1, cols)[0];
    }
    public static TextureRegion[][] tex2(Texture sheet, int rows, int cols){
        return TextureRegion.split(sheet,
        sheet.getWidth() / rows,
        sheet.getHeight() / cols);
    }
}
