package local.hackathon.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Character {

    private PlayerStatus jumpingStatus = PlayerStatus.STANDING;
    private boolean willJump = false;

    Texture spriteSheet;

    public Player(World world) {
        super(world);
    }

    @Override
    public void show() {
        super.show();
        spriteSheet = new Texture("Characters/Capy.png");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }

    @Override
    public void dispose() {
        super.dispose();

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
}
