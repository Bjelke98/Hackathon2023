package local.hackathon.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import local.hackathon.Renderable;

import static local.hackathon.util.Settings.PPM;

public class Character implements Renderable {

    protected Body body;
    protected final World world;

    public static int START_X = 16*5;
    public static int START_Y = 16*5;
    public static float CHARACTER_RADIUS = PPM;

    public Character(World world){
        this.world = world;
    }

    @Override
    public void show() {

        // Body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(START_X / PPM, START_Y / PPM);
        def.fixedRotation = true;

        body = world.createBody(def);

        // Shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(CHARACTER_RADIUS / 2 / PPM, CHARACTER_RADIUS / 2 / PPM);

        body.createFixture(shape, 1);

        // Cleanup
        shape.dispose();

    }

    @Override
    public void render(float delta) {
        update(delta);
    }

    @Override
    public void hide() {
        dispose();
    }

    private void update(float delta){

    }

    @Override
    public void dispose() {

    }

    public Vector2 getPosition(){
        if(body==null) return new Vector2(0,0);
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }
}
