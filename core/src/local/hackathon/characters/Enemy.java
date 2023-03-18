package local.hackathon.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Character {

    protected SpriteBatch batch;

    public Enemy(World world, SpriteBatch batch) {
        super(world);
        this.batch = batch;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
