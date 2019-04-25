package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.templates.MobileEntity;
import com.modular.player.Player;

public class Collector extends MobileEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Collector(float x, float y, Stage stage, Player owner) {
        super(x, y, stage, owner);

        textureComponent().loadAnimationsFromSheet("collector.png", 4, 3, .2f);
        setShapeRectangle(1.3f, 1.5f);
        setFixedRotation();
        initializePhysics();

    }

}
