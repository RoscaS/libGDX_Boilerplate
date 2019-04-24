package com.mygame;

import com.modular.game.BaseGame;
import com.mygame.screens.LevelScreen;

public class MyGame extends BaseGame {

	public void create() {
		super.create();
	    // setActiveScreen(new MainMenuScreen());
	    setActiveScreen(new LevelScreen());
    }
}
