package com.modular.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

    }

    /*------------------------------------------------------------------*\
   	|*							Update Methods 						    *|
   	\*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
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
                    Store.selected.forEach(i -> i.getMouseComponent().setDestination(x, y));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
