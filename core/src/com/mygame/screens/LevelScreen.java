package com.mygame.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.framework.BaseScreen;
import com.modular.Static.Store;
import com.modular.Static.World;
import com.modular.base.CoreEntity;
import com.modular.base.TilemapEntities;
import com.mygame.actors.Soldier;
import com.mygame.actors.Solid;

public class LevelScreen extends BaseScreen {

    private OrthographicCamera mapCamera;
    private TilemapEntities tme;

	/*------------------------------------------------------------------*\
	|*							Initialize  						  *|
	\*------------------------------------------------------------------*/

    public void initialize() {

        World.initialize();
        mapCamera = new OrthographicCamera();
        tme = new TilemapEntities("tests/map.tmx", mainStage, mapCamera);

        World.setTopdownWorld(tme, 1, 1);

        Soldier s1 =  new Soldier(100, 300, mainStage);
        // new Torch(50, 300, mainStage);

        // for (MapObject obj : tme.getRectangleList("Solid")) {
        //     MapProperties props = obj.getProperties();
        //     new Solid(
        //             (float) props.get("x"), (float) props.get("y"),
        //             (float) props.get("width"), (float) props.get("height"), mainStage
        //     );
        // }

        mouseListner();
    }

    /*------------------------------------------------------------------*\
   	|*							Overriden Methods 						*|
   	\*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);

        World.world.step(1 / 60f, 6, 2);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void dispose() {

    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 						  *|
   	\*------------------------------------------------------------------*/


    /*------------------------------------------------------------------*\
   	|*							Private methodes 					  *|
   	\*------------------------------------------------------------------*/

    private void mouseListner() {
        mainStage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // System.out.println("click \t x: " + x + "\ty: " + y);
                // lastClick = new Vector3(
                //         (float) (Math.round(x * 100.0) / 100.0),
                //         (float) (Math.round(y * 100.0) / 100.0),
                //         button
                // );
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
