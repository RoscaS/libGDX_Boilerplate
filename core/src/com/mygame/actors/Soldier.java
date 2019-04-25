package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.templates.MobileEntity;

public class Soldier extends MobileEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage stage) {
        super(x, y, stage);

        textureComponent().loadAnimationsFromSheet("soldier.png", 4, 4, .2f);
        setShapeRectangle(1.2f, 1.7f);
        setFixedRotation();
        initializePhysics();

    }
}
