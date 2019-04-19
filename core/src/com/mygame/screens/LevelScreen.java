package com.mygame.screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.framework.BaseScreen;

public class LevelScreen extends BaseScreen {

    public static final World WORLD = new World(new Vector2(0, -9.8f), true);
    public static final RayHandler HANDLER = new RayHandler(WORLD);

	/*------------------------------------------------------------------*\
	|*							Initialize  							*|
	\*------------------------------------------------------------------*/

    public void initialize() {

        HANDLER.setBlurNum(6);


    }

    /*------------------------------------------------------------------*\
   	|*							Overriden Methods 						*|
   	\*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);

        WORLD.step(1 / 60f, 6, 2);
        handlerRender();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void dispose() {
        HANDLER.dispose();
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/


    /*------------------------------------------------------------------*\
   	|*							Private methodes 						*|
   	\*------------------------------------------------------------------*/

       private void handlerRender() {
           Camera camera = mainStage.getCamera();
           Matrix4 matrix = mainStage.getCamera().combined;
           matrix.scl(100);
           HANDLER.setCombinedMatrix(matrix, 0, 0, camera.viewportWidth, camera.viewportHeight);
           HANDLER.updateAndRender();
       }
}
