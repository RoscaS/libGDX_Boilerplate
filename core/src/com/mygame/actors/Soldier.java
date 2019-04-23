package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.base.CoreEntity;
import com.modular.components.MotionComponent;
import com.modular.components.SelectionComponent;
import com.modular.components.TextureComponent;

public class Soldier extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);

        texture = new TextureComponent(this);
        motion = new MotionComponent(this);
        selection = new SelectionComponent(this);


        texture.loadTexture("tests/spaceship.png");
        motion.setMaxSpeed(1.5f);
        motion.setDynamic();

        setPhysicsProperties(50, .1f, 0);
        setShapeRectangle();
        setFixedRotation();
        initializePhysics();

        getBody().setGravityScale(0);

    }
}
