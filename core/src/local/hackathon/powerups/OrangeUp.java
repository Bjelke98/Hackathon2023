package local.hackathon.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import static local.hackathon.util.Settings.PPM;

public class OrangeUp extends PowerUp{

    Texture orange;

    public OrangeUp(World world, SpriteBatch batch, int x, int y) {
        super(world, batch, x, y);
    }

    @Override
    public void show() {
        super.show();
        orange = new Texture("Other/Orange");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.draw(orange,
            body.getPosition().x*PPM - (float)orange.getWidth()/2,
            body.getPosition().y*PPM - (float)orange.getHeight()/2
        );
    }

    @Override
    public void dispose() {
        super.dispose();
        orange.dispose();
    }
}
