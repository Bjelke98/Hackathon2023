package local.hackathon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import local.hackathon.Renderable;
import local.hackathon.characters.Player;
import local.hackathon.screens.GameScreen;

import static local.hackathon.util.Settings.PPM;

public class LaserProjectile implements Renderable {

    private static final float SPEED = 20f;

    private SpriteBatch batch;
    private World world;
    private Body body;
    TextureRegion region;
    Sprite sprite;

    float x, y, radians;
    Player sender;

    private Texture texture;

    public LaserProjectile(World world, SpriteBatch batch, Player sender, float radians) {
        this.world = world;
        this.batch = batch;
        this.x = sender.getPosition().x;
        this.y = sender.getPosition().y;
        this.sender = sender;
        this.radians = -radians;
    }

    @Override
    public void show() {

        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.KinematicBody;
        def.fixedRotation = true;
        def.position.set(x, y);

        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(16f/2/PPM, 8f/2/PPM);

        FixtureDef fix = new FixtureDef();
        fix.density = 0;
        fix.shape = shape;
        fix.isSensor = true;

        body.createFixture(fix);

        Vector2 position = body.getPosition();

        body.setTransform(position, radians);

        // Calculate velocity of projectile
        float vx = SPEED * MathUtils.cos(radians);
        float vy = SPEED * MathUtils.sin(radians);
        body.setLinearVelocity(vx,vy);

        shape.dispose();

        texture = new Texture("Other/Laser.png");
        region = new TextureRegion(texture);
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

        sender.getBody().applyForceToCenter(-vx*20, -vy*.75f, false);

    }

    @Override
    public void render(float delta) {
        sprite.setRotation(MathUtils.radiansToDegrees * radians);
        sprite.setPosition(body.getPosition().x*PPM-(float)texture.getWidth()/2, body.getPosition().y*PPM-(float)texture.getHeight()/2);
        sprite.draw(batch);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
