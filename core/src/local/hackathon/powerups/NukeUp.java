package local.hackathon.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import static local.hackathon.util.Settings.PPM;

public class NukeUp extends PowerUp{
    Texture nuke;

    public NukeUp(World world, SpriteBatch batch, int x, int y) {
        super(world, batch, x, y);
    }

    @Override
    public void show() {
        super.show();
        nuke = new Texture("Other/Nuke.png");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.draw(nuke,
                body.getPosition().x*PPM - (float)nuke.getWidth()/2,
                body.getPosition().y*PPM - (float)nuke.getHeight()/2
        );
    }

    @Override
    public void dispose() {
        super.dispose();
        nuke.dispose();
    }
}
