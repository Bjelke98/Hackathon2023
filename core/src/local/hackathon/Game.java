package local.hackathon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import local.hackathon.input.ControllerController;
import local.hackathon.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashSet;

public class Game extends com.badlogic.gdx.Game {

	GameScreen gameScreen;

	public static ControllerController controllerController;

	@Override
	public void create () {
		controllerController = new ControllerController();
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
}
