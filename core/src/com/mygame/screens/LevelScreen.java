package com.mygame.screens;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import com.modular.Static.World;
import com.modular.entities.base.TilemapEntities;
import com.modular.screen.MouseScreen;
import com.mygame.actors.Collector;
import com.mygame.actors.Soldier;
import com.mygame.actors.Solid;
import com.mygame.actors.Torch;

public class LevelScreen extends MouseScreen {

    private TilemapEntities tme;

	/*------------------------------------------------------------------*\
	|*							Initialize  						  *|
	\*------------------------------------------------------------------*/

    public void initialize() {
        super.initialize();

        tme = new TilemapEntities("tests/map.tmx", mainStage, mapCamera);

        World.setTopdownWorld(tme, 1, 1);

        new Soldier(100, 300, mainStage);
        new Collector(900, 250, mainStage);

        new Torch(1100, 150, .4f,mainStage);
        new Torch(100, 150, .4f,mainStage);

        // Torch fire = new Torch(630, 920, 1,mainStage);



        for (MapObject obj : tme.getRectangleList("Solid")) {
            MapProperties props = obj.getProperties();
            new Solid(
                    (float) props.get("x"), (float) props.get("y"),
                    (float) props.get("width"), (float) props.get("height"), mainStage
            );
        }
    }

    /*------------------------------------------------------------------*\
   	|*							Overriden Methods 						*|
   	\*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        tme.alignCameraMouse();
    }

    @Override
    public void dispose() {

    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 						     *|
   	\*------------------------------------------------------------------*/
}
