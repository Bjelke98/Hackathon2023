package local.hackathon.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import local.hackathon.Renderable;

import static local.hackathon.util.Settings.PPM;

public class PowerUp implements Renderable {

    World world;
    SpriteBatch batch;
    Body body;
    int x,y;

    public PowerUp(World world, SpriteBatch batch, int x, int y){
        this.world = world;
        this.batch = batch;
        this.x = x;
        this.y = y;
    }

    @Override
    public void show() {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x/PPM, y/PPM);
        def.fixedRotation = true;
        def.gravityScale = 0;

        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(16f/2/PPM, 16f/2/PPM);

        FixtureDef fix = new FixtureDef();
        fix.density = 0;
        fix.shape = shape;
//        fix.isSensor = true;
        fix.restitution = 1;

        body.createFixture(fix);
        body.getFixtureList().get(0).setUserData(this);

        body.setLinearVelocity(20, 20);

        shape.dispose();

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

    }

    public Body getBody() {
        return body;
    }
}
