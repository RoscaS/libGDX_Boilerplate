package com.mygame.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.modular.Static.Store;
import com.modular.Static.World;
import com.modular.game.BaseGame;
import com.modular.map.TilemapEntities;
import com.modular.player.Player;
import com.modular.screen.MouseScreen;
import com.mygame.actors.Collector;
import com.mygame.actors.Soldier;
import com.mygame.actors.Torch;

public class MapScreen extends MouseScreen {

    private TilemapEntities tme;

    // ui
    protected Label selectionLabel;

	/*------------------------------------------------------------------*\
	|*							Initialization                          *|
	\*------------------------------------------------------------------*/

	@Override
    public void initialize() {
        super.initialize();

        initMap();
        initPlayers();
        initActors();
        initUi();
    }

    @Override
    public void initMap() {
	    Store.map = new TilemapEntities("tests/map.tmx", mainStage, mapCamera);
	    Store.map.extractDecorations(mainStage);
        World.setTopdownWorld(Store.map, 1, 1);
    }

    @Override
    public void initPlayers() {
        Store.players.add(new Player("A"));
        Store.players.add(new Player("Computer"));
	}


    @Override
    public void initActors() {
	    Player a = Store.getPlayer("A");
	    Player computer = Store.getPlayer("Computer");

        new Soldier(100, 300, mainStage, a);
        new Collector(900, 250, mainStage, computer);

        new Torch(1100, 150, .4f,mainStage);
        new Torch(630, 700, .4f,mainStage);
        new Torch(100, 150, .4f,mainStage);

        for (int i = 3; i < 6; i++) {
            new Soldier(100 * i, 800, mainStage, a);
        }
    }


    @Override
    public void initUi() {
        selectionAreaUi();
    }

    /*------------------------------------------------------------------*\
   	|*							Update Methods 						    *|
   	\*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        Store.map.alignCameraMouse();
    }

    /*------------------------------------------------------------------*\
   	|*							    Ui                                  *|
   	\*------------------------------------------------------------------*/

    public void selectionAreaUi() {
   	    selectionLabel = new Label("", BaseGame.labelStyle);
   	    selectionLabel.setColor(Color.WHITE);
   	    selectionLabel.setFontScale(0.7f);

   	    uiTable.add().expandY();
   	    uiTable.add(selectionLabel).bottom();
   	    uiTable.pad(20);
       }

    @Override
    public void updateUi(float dt) {
        selectionLabel.setText("Selected: " + Store.getPlayer("A").getSelection());
    }
    /*------------------------------------------------------------------*\
   	|*							Public Methods 						    *|
   	\*------------------------------------------------------------------*/

    @Override
    public void dispose() {

    }


}
