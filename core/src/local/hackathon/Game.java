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
import local.hackathon.screens.MenuScreen;

import java.util.ArrayList;
import java.util.HashSet;

public class Game extends com.badlogic.gdx.Game {

	GameScreen gameScreen;
	MenuScreen menuScreen;

	public static ControllerController controllerController;

	@Override
	public void create () {
		controllerController = new ControllerController();
		changeScreen(1);
	}

	public void changeScreen(int screen){
		switch (screen){
			case 1:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case 2:
				if(gameScreen != null) gameScreen.hide();
				gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
		}
	}

	@Override
	public void render() {
		super.render();
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
}
