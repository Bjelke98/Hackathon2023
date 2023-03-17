package local.hackathon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import local.hackathon.Game;
import local.hackathon.characters.Character;

import static local.hackathon.util.Settings.PPM;
import static local.hackathon.util.Settings.SCALE;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Game parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;

    private Box2DDebugRenderer b2dr;
    private World world;

    private Character player;

    public GameScreen(Game parent){
        this.parent = parent;
    }

    @Override
    public void show() {
        // Screen size
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/SCALE, h/SCALE);

        // Creates a world with real earth-like gravity.
        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        // Player
        player = new Character(world);
        player.show();

        // Textures
        batch = new SpriteBatch();

        // Map
        map = new TmxMapLoader().load("Maps/map_0.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);

        TiledObjects.parse(world, map.getLayers().get("hitbox").getObjects());
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);

        // Draw sprites
        batch.begin();

        batch.end();

        tmr.render();

        b2dr.render(world, camera.combined.scl(PPM));
    }

    private void update(float delta){
        world.step(1/60f, 6, 2);

        updateCamera(delta);

        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    private void updateCamera(float delta){
        Vector3 position = camera.position;
        position.x = player.getPosition().x * PPM;
        position.y = player.getPosition().y * PPM;
        camera.position.set(position);

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width/SCALE, height/SCALE);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        tmr.dispose();
        map.dispose();

        player.hide();
    }
}
