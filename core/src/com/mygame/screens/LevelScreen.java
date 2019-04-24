package com.mygame.screens;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import com.modular.Static.World;
import com.modular.entities.base.CoreEntity;
import com.modular.entities.base.TilemapEntities;
import com.modular.screen.MouseScreen;
import com.mygame.actors.Soldier;
import com.mygame.actors.Solid;
import com.mygame.actors.Torch;

public class LevelScreen extends MouseScreen {

    private TilemapEntities tme;

    Soldier soldier;
    Torch torch;

	/*------------------------------------------------------------------*\
	|*							Initialize  						  *|
	\*------------------------------------------------------------------*/

    public void initialize() {
        super.initialize();

        tme = new TilemapEntities("tests/map.tmx", mainStage, mapCamera);

        World.setTopdownWorld(tme, 1, 1);

        soldier = new Soldier(100, 300, mainStage);
        torch = new Torch(100, 150, mainStage);
        // torch.grabedBy(soldier);

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

        // if (CoreEntity.isContactBetween(soldier, torch))

    }

    @Override
    public void dispose() {

    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 						     *|
   	\*------------------------------------------------------------------*/


    /*------------------------------------------------------------------*\
   	|*							Private methodes 					    *|
   	\*------------------------------------------------------------------*/
}
