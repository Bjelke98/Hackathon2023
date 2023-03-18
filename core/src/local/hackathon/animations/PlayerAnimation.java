package local.hackathon.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimation {

    Animation<TextureRegion> animation;
    float stateTime;
    TextureRegion[] tr;

    public PlayerAnimation(TextureRegion[] tr){
        this.tr = tr;
    }

    public void show(){
        animation = new Animation<>(.144f, tr);
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
