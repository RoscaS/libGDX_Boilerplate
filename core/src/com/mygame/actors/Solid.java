package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.CoreEntity;
import com.modular.entities.components.MotionComponent;
import com.modular.entities.components.TextureComponent;

public class Solid extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Solid(float x, float y, float width, float height, Stage s) {
        super(x, y, s);
        setSize(width, height);

        motion_c = new MotionComponent(this);
        texture_c = new TextureComponent(this);

        motion_c.setStatic();

        setPhysicsProperties(1, .5f, -1f);
        setShapeRectangle();
        setFixedRotation();

        initializePhysics();

    }
}
