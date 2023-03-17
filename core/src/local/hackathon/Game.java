package local.hackathon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import local.hackathon.screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {

	GameScreen gameScreen;

	@Override
	public void create () {

		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
}
