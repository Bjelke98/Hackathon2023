package local.hackathon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import local.hackathon.Game;

import static local.hackathon.util.Settings.SKIN;

public class MenuScreen implements Screen {

    Game parent;
    Stage stage;

    TextButton newGame;
    TextButton scoreBoard;
    TextButton exit;

    Label header;

    private BitmapFont font;

    public MenuScreen(Game parent){
        this.parent = parent;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GUIfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);

        header = new Label("Capyblasta", new Label.LabelStyle(font, Color.WHITE));

        newGame = new TextButton("New Game", SKIN);
        scoreBoard = new TextButton("Scoreboard", SKIN);
        exit = new TextButton("Exit", SKIN);



        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(header).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(newGame).fillX().uniformX();
        table.row();
        table.add(scoreBoard).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(2, 0, 0, 0);
            }
        });

        scoreBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(4, 0, 0, 0);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(delta, 1f/30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
