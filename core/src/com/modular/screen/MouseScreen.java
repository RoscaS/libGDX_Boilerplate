package com.modular.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.modular.Static.Debug;
import com.modular.Static.Store;
import com.modular.Static.World;

public class MouseScreen extends BaseScreen {

    protected OrthographicCamera mapCamera;

    /*------------------------------------------------------------------*\
   	|*							Initialize  				            *|
   	\*------------------------------------------------------------------*/

    @Override
    public void initialize() {
        World.initialize();
        mapCamera = new OrthographicCamera();
        mouseListner();

        World.lights.setAmbientLight(new Color(0x11111100));
    }

    /*------------------------------------------------------------------*\
   	|*							Update Methods 						    *|
   	\*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);

        // Physical world
        World.world.step(1 / 60f, 6, 2);

        // Debug
        Debug.matrix = new Matrix4(mainStage.getCamera().combined);
        Debug.matrix.scl(World.SCALE);

        World.lights.setCombinedMatrix(Debug.matrix, 0, 0, mapCamera.viewportWidth, mapCamera.viewportHeight);
        World.lights.updateAndRender();

        // Debug.debugRenderUpdate(mainStage);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public boolean scrolled(int amount) {
        OrthographicCamera mainCamera = (OrthographicCamera) mainStage.getCamera();
        if (amount == 1) {
            mapCamera.zoom += .2f;
            mainCamera.zoom += .2f;
        } else if (amount == -1) {
            mapCamera.zoom -= .2f;
            mainCamera.zoom -= .2f;
        }
        return super.scrolled(amount);
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 				        *|
	\*------------------------------------------------------------------*/

    private void mouseListner() {
        mainStage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // clear selected array (click on empty spot)
                if (mainStage.hit(x, y, true) == null && button == Input.Buttons.LEFT) {
                    Store.selected.clear();
                }
                // move selected movables to right click position
                if (button == Input.Buttons.RIGHT && !Store.selected.isEmpty()) {
                    Store.selected.forEach(i -> i.mouseComponent().setDestination(x, y));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
