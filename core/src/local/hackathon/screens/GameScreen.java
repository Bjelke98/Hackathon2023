package local.hackathon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import local.hackathon.Game;
import local.hackathon.characters.Character;
import local.hackathon.characters.Player;
import local.hackathon.characters.PlayerStatus;
import local.hackathon.entities.LaserProjectile;

import java.util.ArrayList;
import java.util.List;

import static local.hackathon.Game.controllerController;
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


    private ArrayList<Player> players;

    private ArrayList<LaserProjectile> lasers;

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
        world = new World(new Vector2(0, -10f), false);
        b2dr = new Box2DDebugRenderer();

        // Textures
        batch = new SpriteBatch();

        players = new ArrayList<>();

        for (Controller c : controllerController.getControllers()){
            Player p = new Player(world, batch, this, c);
            p.show();
            players.add(p);
        }

        lasers = new ArrayList<>();

        // Map
        map = new TmxMapLoader().load("Maps/map_1.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);

        TiledObjects.parse(world, map.getLayers().get("hitbox").getObjects());
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);



        tmr.render();

        // Draw sprites
        batch.begin();

        for (LaserProjectile l : lasers){
            l.render(delta);
        }

        for (Player p : players){
            p.render(delta);
        }


        batch.end();

        b2dr.render(world, camera.combined.scl(PPM));
    }

    private void update(float delta){
        world.step(1/60f, 6, 2);

        updateInput(delta);
        updateCamera(delta);

        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    private void updateCamera(float delta){

        float playersPosX = 0;
        float playersPosY = 0;

        Vector3 position = camera.position;

        for (Player p : players){
            playersPosX+=p.getPosition().x;
            playersPosY+=p.getPosition().y;
        }

        position.x = (playersPosX/players.size()) * PPM;
        position.y = (playersPosY/players.size()) * PPM;
        camera.position.set(position);

        camera.update();
    }

    private void updateInput(float delta){

        // Gamepad

        List<Controller> controllers = controllerController.getControllers();

        for (int i = 0; i < controllers.size(); i++) {

            Controller controller = controllers.get(i);

            Player player = players.get(i);

            float axisLX = controller.getAxis(controller.getMapping().axisLeftX);
            float axisLY = controller.getAxis(controller.getMapping().axisLeftY);
            boolean btnA = controller.getButton(controller.getMapping().buttonA);

            float horizontalForce = 0;

            if(axisLX>=.2f || axisLX<=-.2f) {
                horizontalForce+=Math.min(axisLX, 1f);
            }

            Body playerBody = player.getBody();

            if(btnA){
                player.jump();
            }

            if(player.tryJump()){
                playerBody.applyForceToCenter(0, 500, false);
            }

            float xVel = playerBody.getLinearVelocity().x;

            if(xVel > 2){
                player.setHorisontalStatus(PlayerStatus.RIGHT);
            }
            if(xVel < -2){
                player.setHorisontalStatus(PlayerStatus.LEFT);
            }

//        Gdx.app.log("XVEL: ", ""+xVel);

            float yVel = playerBody.getLinearVelocity().y;

            if(yVel >=-2 && yVel <=2 && player.getJumpingStatus() != PlayerStatus.UP){
                player.setJumpingStatus(PlayerStatus.STANDING);
            } else if(yVel>2){
                player.setJumpingStatus(PlayerStatus.UP);
            } else if(yVel<-2){
                player.setJumpingStatus(PlayerStatus.DOWN);
            }

            playerBody.setLinearVelocity(horizontalForce*10, playerBody.getLinearVelocity().y);

        }
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

    public LaserProjectile addLaser(Player sender, float radians){
        LaserProjectile laser = new LaserProjectile(world, batch, sender, radians);
        laser.show();
        lasers.add(laser);
        return laser;
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        tmr.dispose();
        map.dispose();

        for (Player p : players){
            p.hide();
        }
    }
}
