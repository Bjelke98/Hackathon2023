package local.hackathon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import local.hackathon.input.ControllerController;
import local.hackathon.screens.GameOverScreen;
import local.hackathon.screens.GameScreen;
import local.hackathon.screens.MenuScreen;
import local.hackathon.screens.ScoreboardScreen;

import java.util.ArrayList;
import java.util.HashSet;

public class Game extends com.badlogic.gdx.Game {

	GameScreen gameScreen;
	MenuScreen menuScreen;
	GameOverScreen gameOverScreen;
	ScoreboardScreen scoreboardScreen;
	public Music menuSong;
	public static ControllerController controllerController;
	@Override
	public void create () {
//		Gdx.app.log("---", Gdx.files.internal("Music/Menu.wav").exists() ? "True": "False");
//		menuSong = Gdx.audio.newMusic(Gdx.files.internal("Music/Menu.wav"));
//		menuSong.setLooping(true);
//		menuSong.play();
		controllerController = new ControllerController();
		changeScreen(1, 0, 0, 0);
	}

	public void changeScreen(int screen, int playerHP, int bossHP, float timeSpent){
		switch (screen){
			case 1:
				menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case 2:
				if(gameScreen != null) gameScreen.hide();
				gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
			case 3:
				gameOverScreen = new GameOverScreen(this, playerHP, bossHP, timeSpent);
				this.setScreen(gameOverScreen);
				break;
			case 4:
				scoreboardScreen = new ScoreboardScreen(this, 0, 0, 0);
				this.setScreen(scoreboardScreen);
				break;
		}
	}

	@Override
	public void render() {
		super.render();
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}
}
