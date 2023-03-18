package local.hackathon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import local.hackathon.Game;
import local.hackathon.characters.Bosses.BossDeath;
import local.hackathon.characters.Character;
import local.hackathon.characters.Player;
import local.hackathon.characters.PlayerStatus;
import local.hackathon.entities.LaserProjectile;
import local.hackathon.powerups.FireablePowerUp;
import local.hackathon.powerups.NukeUp;
import local.hackathon.powerups.OrangeUp;
import local.hackathon.powerups.PowerUp;
import local.hackathon.util.GameOverContainer;
import local.hackathon.util.TiledObjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static local.hackathon.Game.controllerController;
import static local.hackathon.util.Settings.*;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    public Game parent;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;

    private Box2DDebugRenderer b2dr;
    private World world;


    public ArrayList<Player> players;

    public BossDeath bossDeath;

    private ArrayList<LaserProjectile> lasers;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<FireablePowerUp> fireballs;

    private HashSet<Body> clense;

    public float timeSpent;

    public GameScreen(Game parent){
        this.parent = parent;
    }

    public Music gameSong;

    private Texture orangeIcon;
    private Texture nukeIcon;

    private MapProperties prop;

    Stage hudStage;
    Viewport hudViewport;

    ArrayList<Label> playerHp;
    Label bossHp;

    BitmapFont font;

    boolean swapAtEnd = false;
    GameOverContainer gameOverContainer;

    @Override
    public void show() {

        playerHp = new ArrayList<>();

        orangeIcon = new Texture("Other/Orange.png");
        nukeIcon = new Texture("Other/Nuke.png");

//        gameSong = Gdx.audio.newMusic(Gdx.files.internal("Music/Game.wav"));
//        gameSong.setLooping(true);
//        gameSong.play();
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

        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        hudStage = new Stage(hudViewport, batch);

        players = new ArrayList<>();
        powerUps = new ArrayList<>();

        bossDeath = new BossDeath(world, batch, this, players);
        bossDeath.show();

        for (Controller c : controllerController.getControllers()){
            Player p = new Player(world, batch, this, c);
            p.show();
            players.add(p);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GUIfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);

        for (int i = 0; i < players.size(); i++) {
            Label hpLBL = new Label("HP: "+Player.START_HP, new Label.LabelStyle(font, Color.WHITE));
            hpLBL.setPosition((PPM*2+PPM), PPM+i*PPM);
            hudStage.addActor(hpLBL);
            playerHp.add(hpLBL);
        }

        bossHp = new Label("Boss HP: "+BossDeath.START_HP, new Label.LabelStyle(font, Color.WHITE));
        bossHp.setPosition((PPM*2+PPM), PPM+ (players.size()+1)*PPM);
        hudStage.addActor(bossHp);

        lasers = new ArrayList<>();
        fireballs = new ArrayList<>();

        clense = new HashSet<>();

        // Map
        map = new TmxMapLoader().load("Maps/map_1.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);

        world.setContactListener(contactListener);

        prop = map.getProperties();

        TiledObjects.parse(world, map.getLayers().get("hitbox").getObjects());
    }

    ContactListener contactListener = new ContactListener() {
        @Override
        public void beginContact(Contact contact) {

            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            Object aData = a.getUserData();
            Object bData = b.getUserData();

            Object bBData = b.getBody().getUserData();
            Object aBData = a.getBody().getUserData();


            if(aData instanceof OrangeUp || aData instanceof NukeUp){
                if(bBData instanceof Player){
                    ((Player)bBData).givePowerUp((PowerUp) aData);
                    removePowerUp((PowerUp) aData);
                }
            }
            if(bData instanceof OrangeUp || bData instanceof NukeUp){
                if(aBData instanceof Player){
                    ((Player)aBData).givePowerUp((PowerUp) bData);
                    removePowerUp((PowerUp) bData);
                }
            }

            if(aData instanceof MapObject){
                if(bData instanceof LaserProjectile){
                    removeLaser((LaserProjectile) bData);
                }
            } else if(bData instanceof MapObject){
                if(aData instanceof LaserProjectile){
                    removeLaser((LaserProjectile) aData);
                }
            } else if(aData instanceof BossDeath){
                if(bData instanceof LaserProjectile){
                    if(((LaserProjectile) bData).getSender() instanceof Player){
                        ((BossDeath) aData).impact(LASER_DAMAGE);
                        removeLaser((LaserProjectile) bData);
                    }
                } else if (bData instanceof FireablePowerUp) {
                    if(((FireablePowerUp) bData).getSender() instanceof Player){
                        ((BossDeath) aData).impact(((FireablePowerUp)bData).getDamage());
                        removeFireBall((FireablePowerUp) bData);
                    }
                }
            } else if(bData instanceof BossDeath){
                if(aData instanceof LaserProjectile){
                    if(((LaserProjectile) aData).getSender() instanceof Player){
                        ((BossDeath) bData).impact(LASER_DAMAGE);
                        removeLaser((LaserProjectile) aData);
                    }
                } else if (aData instanceof FireablePowerUp) {
                    if(((FireablePowerUp) aData).getSender() instanceof Player){
                        ((BossDeath) bData).impact(((FireablePowerUp)aData).getDamage());
                        removeFireBall((FireablePowerUp) aData);
                    }
                }
            } else if(aBData instanceof Player){
                if(bData instanceof LaserProjectile){
                    if(((LaserProjectile) bData).getSender() instanceof BossDeath){
                        ((Player) aBData).impact(LASER_DAMAGE);
                        removeLaser((LaserProjectile) bBData);
                    }
                }
            } else if(bBData instanceof Player){
                if(aData instanceof LaserProjectile){
                    if(((LaserProjectile) aData).getSender() instanceof BossDeath){
                        ((Player) bBData).impact(LASER_DAMAGE);
                        removeLaser((LaserProjectile) aData);
                    }
                }
            }


        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    };

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0, 0, 0, 1);



        tmr.render();

        // Draw sprites
        batch.begin();

        // Numbers
        for (int i = 0; i < players.size(); i++) {
            font.draw(batch, "Player "+(i+1)+" HP:" + players.get(i).hp, PPM*2, PPM*2+PPM*i);
        }
        font.draw(batch, "Boss HP: "+bossDeath.hp, PPM*2, PPM*2+PPM*(players.size()+2));

        for (LaserProjectile l : lasers){
            l.render(delta);
        }

        for (FireablePowerUp p : fireballs){
            p.render(delta);
        }

        for (Player p : players){
            p.render(delta);
        }

        bossDeath.render(delta);

        spawnPowerups(delta);

        batch.end();

        //b2dr.render(world, camera.combined.scl(PPM));

        if(swapAtEnd){
            parent.changeScreen(gameOverContainer.i, gameOverContainer.i1, gameOverContainer.i2, gameOverContainer.timeSpent);
        }

    }
    private float powerupInterval = 0;
    public void spawnPowerups(float delta){
        powerupInterval+=delta;

        if (powerupInterval>10){
            powerupInterval = 0;
            int rand = rand(1, 10);
            MapProperties prop = map.getProperties();
            int x = rand((int)PPM, (int)(prop.get("width", Integer.class)*PPM)-(int)PPM);
            int y = rand((int)PPM, (int)(prop.get("height", Integer.class)*PPM)-(int)PPM);
            x = Math.max(x, 16);
            y = Math.max(y, 16);
            Gdx.app.log("PowerupSpawn: ", x+" - "+y);
            PowerUp p;
            if(rand>=10){
                p = new NukeUp(world, batch, x, y);
            } else {
                p = new OrangeUp(world, batch, x, y);
            }
            p.show();
            powerUps.add(p);
        }
        for (PowerUp p : powerUps){
            p.render(delta);
        }
    }

    private int rand(int min, int max){
        Random random = new Random();
        return random.nextInt(max+1-min)+min;
    }

    private void update(float delta){
        timeSpent+=delta;
        // Clean last
        for(Body c : clense) world.destroyBody(c);
        clense.clear();

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

            if(btnA || axisLY < -.75f){
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
        hudViewport.setScreenWidth(Gdx.graphics.getWidth());
        hudViewport.setScreenHeight(Gdx.graphics.getHeight());
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

    public void addLaser(Character sender, float radians){
        LaserProjectile laser = new LaserProjectile(world, batch, sender, radians);
        laser.show();
        lasers.add(laser);
    }

    public void removeLaser(LaserProjectile laser){
        lasers.remove(laser);
        if(laser!=null){
            clense.add(laser.getBody());
            laser.dispose();
        }
    }

    public void addFireBall(Character sender, float radians, int type){
        FireablePowerUp p = new FireablePowerUp(world, batch, sender, radians, type);
        p.show();
        fireballs.add(p);
    }

    public void removeFireBall(FireablePowerUp p){
        fireballs.remove(p);
        p.dispose();
        clense.add(p.getBody());
    }

    public void removePowerUp(PowerUp p){
        powerUps.remove(p);
        p.hide();
        clense.add(p.getBody());
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        tmr.dispose();
        map.dispose();
        nukeIcon.dispose();
        orangeIcon.dispose();

        for (Player p : players){
            p.hide();
        }

        bossDeath.dispose();
    }

    public void changeScreen(int i, int i1, int i2, float timeSpent) {
        gameOverContainer = new GameOverContainer(i, i1, i2, timeSpent);
        swapAtEnd = true;
    }
}
