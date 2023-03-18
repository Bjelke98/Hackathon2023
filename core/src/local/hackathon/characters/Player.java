package local.hackathon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import local.hackathon.animations.PlayerAnimation;
import local.hackathon.entities.LaserProjectile;
import local.hackathon.screens.GameScreen;

import static local.hackathon.util.Settings.PPM;

public class Player extends Character {

    boolean spriteIsFlipped = false;

    private PlayerStatus jumpingStatus = PlayerStatus.STANDING;
    private PlayerStatus horisontalStatus = PlayerStatus.RIGHT;
    private boolean willJump = false;
    private static final int CELL_AMMOUNT = 9;

    Pixmap capySheetOG;
    Pixmap capySheetScaled;
    Texture capySheet;

    PlayerAnimation walkAnimation;
    PlayerAnimation jumpAnimation;
    PlayerAnimation downAnimation;

    SpriteBatch batch;
    GameScreen parent;

    Controller controller;

    public Player(World world, SpriteBatch batch, GameScreen parent, Controller controller) {
        super(world);
        this.batch = batch;
        this.parent = parent;
        this.controller = controller;
    }

    @Override
    public void show() {
        super.show();
        capySheetOG = new Pixmap(Gdx.files.internal(("Characters/Capy.png")));
        capySheetScaled = new Pixmap(576/4, 576/4, capySheetOG.getFormat());
        capySheetScaled.drawPixmap(capySheetOG,
            0, 0, capySheetOG.getWidth(), capySheetOG.getHeight(),
            0, 0, capySheetScaled.getWidth(), capySheetScaled.getHeight()
        );

        capySheet = new Texture(capySheetScaled);

        capySheetOG.dispose();
        capySheetScaled.dispose();

        TextureRegion[][] tmp = TextureRegion.split(capySheet,
                capySheet.getWidth() / CELL_AMMOUNT,
                capySheet.getHeight() / CELL_AMMOUNT
        );

//        TextureRegion[] capyFrames = new TextureRegion[CELL_AMMOUNT*CELL_AMMOUNT];
//        int index = 0;
//        for (int i = 0; i < CELL_AMMOUNT; i++) {
//            for (int j = 0; j < CELL_AMMOUNT; j++) {
//                capyFrames[index++] = tmp[i][j];
//            }
//        }

        TextureRegion[] walkFrames = new TextureRegion[8];
        System.arraycopy(tmp[8], 0, walkFrames, 0, walkFrames.length);
        walkAnimation = new PlayerAnimation(walkFrames);
        walkAnimation.show();

        TextureRegion[] jumpFrames = new TextureRegion[8];
        System.arraycopy(tmp[3], 0, jumpFrames, 0, jumpFrames.length);
        jumpAnimation = new PlayerAnimation(jumpFrames);
        jumpAnimation.show();

        TextureRegion[] downFrames = new TextureRegion[8];
        System.arraycopy(tmp[6], 0, downFrames, 0, downFrames.length);
        downAnimation = new PlayerAnimation(downFrames);
        downAnimation.show();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        float axisRX = controller.getAxis(controller.getMapping().axisRightX);
        float axisRY = controller.getAxis(controller.getMapping().axisRightY);

        if (axisRX<-.2f || axisRX>.2f || axisRY<-.2f || axisRY>.2f){
            parent.addLaser(this, MathUtils.atan2(axisRY, axisRX));
        }

        TextureRegion currentFrame;

        if(jumpingStatus==PlayerStatus.UP)
            currentFrame = jumpAnimation.getFrame(delta);
        else if(jumpingStatus==PlayerStatus.DOWN)
            currentFrame = downAnimation.getFrame(delta);
        else
            currentFrame = walkAnimation.getFrame(delta);

        switch (horisontalStatus){
            case LEFT:
                if(!spriteIsFlipped) {
                    walkAnimation.flip();
                    jumpAnimation.flip();
                    downAnimation.flip();
                    spriteIsFlipped = true;
                }
                break;
            case RIGHT:
                if(spriteIsFlipped) {
                    walkAnimation.flip();
                    jumpAnimation.flip();
                    downAnimation.flip();
                    spriteIsFlipped = false;
                }
                break;
        }

        batch.draw(currentFrame,
            body.getPosition().x * PPM - (float)capySheet.getWidth()/18,
            body.getPosition().y * PPM - (float)capySheet.getHeight()/18);
    }

    @Override
    public void dispose() {
        super.dispose();
        capySheet.dispose();
    }

    public void jump(){
        willJump = jumpingStatus == PlayerStatus.STANDING;
    }

    public void setJumpingStatus(PlayerStatus ps){
        jumpingStatus = ps;
    }

    public boolean tryJump() {
        if(!willJump) return false;
        if(jumpingStatus!=PlayerStatus.STANDING) return false;
        jumpingStatus = PlayerStatus.UP;
        willJump = false;
        return true;
    }

    public PlayerStatus getJumpingStatus() {
        return jumpingStatus;
    }

    public void setHorisontalStatus(PlayerStatus horisontalStatus) {
        this.horisontalStatus = horisontalStatus;
    }

    public PlayerStatus getHorisontalStatus() {
        return horisontalStatus;
    }
}
