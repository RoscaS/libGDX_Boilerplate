package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.CoreEntity;

public class Solid extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Solid(float x, float y, float width, float height, Stage s) {
        super(x, y, s);
        setSize(width, height);

        addMotionComponent();
        addTextureComponent();

        setStatic();
        setPhysicsProperties(1, .5f, -1f);
        setShapeRectangle();
        setFixedRotation();

        initializePhysics();

    }
}
