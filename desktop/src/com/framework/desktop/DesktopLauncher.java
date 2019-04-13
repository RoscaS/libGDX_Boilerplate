package com.framework.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.mygame.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		Game myGame = new MyGame();
		LwjglApplication launcher = new LwjglApplication(myGame, "My Game", 800, 600);
	}
}
