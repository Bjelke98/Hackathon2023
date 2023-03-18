package local.hackathon.characters.Bosses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import local.hackathon.animations.CharacterAnimation;
import local.hackathon.characters.Enemy;
import local.hackathon.characters.Player;
import local.hackathon.screens.GameScreen;
import local.hackathon.util.TexRegion;

import java.util.ArrayList;

import static local.hackathon.util.Settings.PPM;

public class BossDeath extends Enemy {

    public enum BossDeathState{
        IDLE,
        ATTACK1,
        ATTACK2,
    }

    public static final int START_HP = 100_000;
    public static final int HP_ATTACK_INTERVAL = 1_000;
    public static final float TIME_ATTACK_INTERVAL = 3;

    float timeInterval = 0;


    public int hp = 100;
    float hpInterval = hp;

    Texture idle1;
    Texture idle2;

    Texture attack;

    BossDeathState bossState = BossDeathState.IDLE;

    CharacterAnimation idleAnimation;

    CharacterAnimation attack1Animation;
    CharacterAnimation attack2Animation;

    float attackInterval = 0;

    GameScreen parent;

    ArrayList<Player> players;

    public BossDeath(World world, SpriteBatch batch, GameScreen parent, ArrayList<Player> players) {
        super(world, batch);
        this.parent = parent;
        this.players = players;
    }

    @Override
    public void show() {
        idle1 = new Texture("Characters/death-boss/idle.png");
        idle2 = new Texture("Characters/death-boss/idle2.png");

        attack = new Texture("Characters/death-boss/attacking.png");

        CHARACTER_WIDTH = (float)idle2.getWidth()/8;
        CHARACTER_HEIGHT = (float)idle2.getHeight()/4;
        START_X = 16*27;
        START_Y = 16*6;

        super.show();

        body.getFixtureList().get(0).setUserData(this);

        TextureRegion[] idle1Tmp = TexRegion.tex(idle1, 4);
        TextureRegion[][] idle2Tmp = TexRegion.tex2(idle2, 4, 2);

        TextureRegion[] idleFrames = new TextureRegion[2*4+idle1Tmp.length-1];
        int index = 0;
        for (int i = 0; i < idle1Tmp.length - 1; i++) {
            idleFrames[index++] = idle1Tmp[i];
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                idleFrames[index++] = idle2Tmp[i][j];
            }
        }

        idleAnimation = new CharacterAnimation(idleFrames, .144f); // 24 FPS
        idleAnimation.show();
        idleAnimation.flip();

        TextureRegion[][] attackTmp = TexRegion.tex2(attack, 6, 3);

        TextureRegion[] attack1Frames = new TextureRegion[6];
        System.arraycopy(attackTmp[0], 0, attack1Frames, 0, attack1Frames.length);

        TextureRegion[] attack2Frames = new TextureRegion[7];
        for (int i = 0; i < attack2Frames.length - 1; i++) {
            attack2Frames[i] = attackTmp[1][i];
        }
        attack2Frames[attack2Frames.length-1] = attackTmp[1][0];

        attack1Animation = new CharacterAnimation(attack1Frames, .144f, true); // 24 FPS
        attack1Animation.show();
        attack1Animation.flip();

        attack2Animation = new CharacterAnimation(attack2Frames, .144f, true); // 24 FPS
        attack2Animation.show();
        attack2Animation.flip();

    }

    @Override
    public void render(float delta) {
        update(delta);

        super.render(delta);

        TextureRegion currentFrame;

        if(bossState == BossDeathState.ATTACK1) {
            currentFrame = attack1Animation.getFrame(delta);
        } else if(bossState == BossDeathState.ATTACK2) {
            currentFrame = attack2Animation.getFrame(delta);
        } else {
            currentFrame = idleAnimation.getFrame(delta);
        }

        batch.draw(currentFrame,
            body.getPosition().x * PPM - (float)idle2.getWidth()/8,
            body.getPosition().y * PPM - (float)idle2.getHeight()/4
        );
    }

    private void update(float delta){

        attackInterval+=delta;
        if(bossState == BossDeathState.ATTACK1){
            for (float i = 0f+attackInterval; i <=6.2+attackInterval ; i+=.8f) {
                parent.addLaser(this, i);
            }
            if(attackInterval>=4){
                attackInterval = 0;
                bossState = BossDeathState.IDLE;
            }
        }

        if(bossState == BossDeathState.ATTACK2){
            for (Player p : players){
                Vector2 pPos = p.getBody().getPosition();
                Vector2 bPos = body.getPosition();

                float angleRadians = MathUtils.atan2(pPos.y - bPos.y, pPos.x - bPos.x);

                parent.addLaser(this, -angleRadians);
            }
            if(attackInterval>=4.7){
                attackInterval = 0;
                bossState = BossDeathState.IDLE;
            }
        }

        timeInterval+=delta;

        if(timeInterval>TIME_ATTACK_INTERVAL){
            if(bossState != BossDeathState.ATTACK2){
                bossState = BossDeathState.ATTACK1;
                timeInterval=0;
            }
        }
        if(hp<hpInterval-HP_ATTACK_INTERVAL){
            if(bossState != BossDeathState.ATTACK1){
                bossState = BossDeathState.ATTACK2;
                hpInterval = hp;
            }
        }
    }

    public void impact(int damage){
        hp-=damage;
        if(hp<=0){
            int avgHP = 0;
            for (Player p : players) {
                avgHP+=p.hp;
            }
            parent.changeScreen(3, (int)((float)avgHP/players.size()), 0, parent.timeSpent);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        idle1.dispose();
        idle2.dispose();
        attack.dispose();
    }
}
