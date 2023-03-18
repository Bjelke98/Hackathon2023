package local.hackathon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import local.hackathon.Game;
import local.hackathon.characters.Bosses.BossDeath;
import local.hackathon.util.Scoreboard;

import java.util.Set;

import static local.hackathon.util.Settings.SKIN;

public class ScoreboardScreen implements Screen {
    Game parent;
    Stage stage;

    Label header;
    Label scoresText;
    Button back;

    public ScoreboardScreen(Game parent, int playerHP, int bossHP, float duration){
        this.parent = parent;
    }

    StringBuilder sb;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        header = new Label("Scoreboard!", SKIN);
        back = new TextButton("Back", SKIN);
        sb = new StringBuilder();

        Scoreboard.deSerialize();
        Set<String> keys = Scoreboard.scores.keySet();
        for (String k : keys){
            sb.append(k).append(": ").append(Scoreboard.scores.get(k)).append("\n");
        }

        scoresText = new Label(sb.toString(), SKIN);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(header).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(scoresText).fillX().uniformX();
        table.row();
        table.add(back).fillX().uniformX();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(1, 0, 0, 0);
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
