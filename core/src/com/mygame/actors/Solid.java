package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.base.CoreEntity;
import com.modular.components.MotionComponent;
import com.modular.components.TextureComponent;

public class Solid extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Solid(float x, float y, float width, float height, Stage s) {
        super(x, y, s);

        setSize(width, height);
        setShapeRectangle();
        setPhysicsProperties(1, .5f, .1f);

        motion_c = new MotionComponent(this);
        texture_c = new TextureComponent(this);

        motion_c.setStatic();

        setFixedRotation();
        initializePhysics();

    }
}
