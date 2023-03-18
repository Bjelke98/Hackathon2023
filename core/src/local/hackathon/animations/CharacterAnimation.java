package local.hackathon.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterAnimation {

    Animation<TextureRegion> animation;
    float stateTime;
    TextureRegion[] tr;
    float fr;

    public CharacterAnimation(TextureRegion[] tr, float fr){
        this.tr = tr;
        this.fr = fr;
    }

    public void show(){
        animation = new Animation<>(fr, tr);
        stateTime = 0f;
    }

    public TextureRegion getFrame(float delta){
        stateTime+=delta;
        return animation.getKeyFrame(stateTime, true);
    }

    public void flip(){
        for (TextureRegion tr : animation.getKeyFrames()){
            tr.flip(true, false);
        }
    }
}
