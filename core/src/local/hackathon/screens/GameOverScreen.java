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

import static local.hackathon.util.Settings.SKIN;

public class GameOverScreen implements Screen {
    Game parent;
    Stage stage;

    TextButton confirm;
    TextButton back;
    Label text;
    Label scoreText;
    TextField name;

    int playerHP, bossHP;
    float duration;

    int score;

    public GameOverScreen(Game parent, int playerHP, int bossHP, float duration){
        this.parent = parent;
        this.playerHP = playerHP;
        this.bossHP = bossHP;
        this.duration = duration;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        String output;
        score = (int) (playerHP/(duration*.1f)-bossHP+BossDeath.START_HP);
        if(playerHP>0){
            output = "Congratulations! You WON!";
        } else {
            output = "Sorry, you lost :(";
        }

        text = new Label(output, SKIN);
        scoreText = new Label("Score: "+score, SKIN);

        confirm = new TextButton("Submit Score", SKIN);
        back = new TextButton("Back", SKIN);

        name = new TextField("", SKIN);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(text).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(scoreText).fillX().uniformX();
        table.row();
        table.add(name).fillX().uniformX();
        table.row();
        table.add(confirm).fillX().uniformX();
        table.row();
        table.add(back).fillX().uniformX();

        confirm.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Scoreboard.scores.put(name.getText(), score);
                Scoreboard.serialize();
                parent.changeScreen(1, 0, 0, 0);
            }
        });

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
