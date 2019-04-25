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
import com.modular.player.Player;

public abstract class MouseScreen extends BaseScreen {

    protected OrthographicCamera mapCamera;

    /*------------------------------------------------------------------*\
   	|*							Initialize  				            *|
   	\*------------------------------------------------------------------*/

    @Override
    public void initialize() {
        mapCamera = new OrthographicCamera();

        World.initialize();
        World.lights.setAmbientLight(new Color(0x11111100));

        mouseListner();
    }

    /*------------------------------------------------------------------*\
   	|*							Update Methods 						    *|
   	\*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);

        // Physical world
        World.world.step(1 / 60f, 6, 2);

        // Debug init
        Debug.matrix = new Matrix4(mainStage.getCamera().combined);
        Debug.matrix.scl(World.SCALE);

        // Lights
        if (!debug) {
            float vpW = mapCamera.viewportWidth;
            float vpH = mapCamera.viewportHeight;
            World.lights.setCombinedMatrix(Debug.matrix, 0, 0, vpW, vpH);
            World.lights.updateAndRender();
        }

        // Debug render
        if (debug) Debug.debugRenderUpdate(mainStage);

        mainStage.getCamera().update();
        updateUi(dt);
        uiStage.draw();



    }

    @Override
    public void update(float dt) { }


    @Override
    public boolean scrolled(int amount) {
        mouseScroll(amount);
        return super.scrolled(amount);
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 				            *|
   	\*------------------------------------------------------------------*/

    private void mouseScroll(int amount) {
        OrthographicCamera mainCamera = (OrthographicCamera) mainStage.getCamera();
        if (amount == 1) {
            mapCamera.zoom += .2f;
            mainCamera.zoom += .2f;
        } else if (amount == -1) {
            mapCamera.zoom -= .2f;
            mainCamera.zoom -= .2f;
        }
    }

    public void mouseListner() {
        mainStage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player player = Store.getPlayer("A");

                // clear selected array (click on empty spot)
                if (mainStage.hit(x, y, true) == null && button == Input.Buttons.LEFT) {
                    player.getSelection().clear();
                    // Store.selected.clear();
                }
                // move selected movables to right click position

                boolean rightClick = button == Input.Buttons.RIGHT;
                boolean emptySelection = player.getSelection().isEmpty();
                if (rightClick && !emptySelection) {
                    player.getSelection().forEach(i -> i.mouseComponent().setDestination(x, y));
                    // Store.selected.forEach(i -> i.mouseComponent().setDestination(x, y));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
