package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.templates.MobileEntity;

public class Collector extends MobileEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Collector(float x, float y, Stage stage) {
        super(x, y, stage);

        textureComponent().loadAnimationsFromSheet("collector.png", 4, 3, .2f);
        setShapeRectangle(1.3f, 1.5f);
        setFixedRotation();
        initializePhysics();

    }

}
