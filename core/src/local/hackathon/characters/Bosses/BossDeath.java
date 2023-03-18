package local.hackathon.characters.Bosses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import local.hackathon.animations.CharacterAnimation;
import local.hackathon.characters.Enemy;
import local.hackathon.util.TexRegion;

import static local.hackathon.util.Settings.PPM;

public class BossDeath extends Enemy {

    public enum BossDeathState{
        IDLE,
        ATTACK1,
        ATTACK2
    }

    Texture idle1;
    Texture idle2;

    CharacterAnimation idleAnimation;

    public BossDeath(World world, SpriteBatch batch) {
        super(world, batch);
    }

    @Override
    public void show() {
        super.show();

        idle1 = new Texture("Characters/death-boss/idle.png");
        idle2 = new Texture("Characters/death-boss/idle.png");

        TextureRegion[] idle1Tmp = TexRegion.tex(idle1, 4);
        TextureRegion[][] idle2Tmp = TexRegion.tex2(idle2, 2, 4);

        TextureRegion[] idleFrames = new TextureRegion[idle1Tmp.length+2*4];
        int index = 0;
        for (TextureRegion region : idle1Tmp) {
            idleFrames[index++] = region;
        }
        for (TextureRegion[] textureRegions : idle2Tmp) {
            for (TextureRegion textureRegion : textureRegions) {
                idleFrames[index++] = textureRegion;
            }
        }

        idleAnimation = new CharacterAnimation(idleFrames, .144f); // 24 frames
        idleAnimation.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        TextureRegion currentFrame;

        currentFrame = idleAnimation.getFrame(delta);

        batch.draw(currentFrame,
        body.getPosition().x * PPM - (float)idle1.getWidth()/2,
        body.getPosition().y * PPM - (float)idle2.getHeight()/2
        );

    }

    @Override
    public void dispose() {
        super.dispose();
        idle1.dispose();
        idle2.dispose();
    }
}
