package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;

public class Soldier extends TopDownEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage s) {
        super(x, y, s);

        addMotionComponent();
        addTextureComponent();
        addMouseComponent();

        textureComponent().loadAnimationsFromSheet("soldier.png", 4, 4, .2f);
        setDynamic();

        setPhysicsProperties(50, .3f, -1f);
        setShapeRectangle(1.2f, 1.7f);
        setFixedRotation();
        initializePhysics();

        motionComponent().setMaxSpeed(1.5f);
        motionComponent().setAcceleration(2f);
        motionComponent().setDeceleration(4f);

    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }
}
