package com.modular.entities.templates;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;

public class MobileEntity extends TopDownEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public MobileEntity(float x, float y, Stage stage) {
        super(x, y, stage);
        addMotionComponent();
        addTextureComponent();
        addMouseComponent();

        setDynamic();

        setPhysicsProperties(50, 0f, 0f);
        motionComponent().setMaxSpeed(1.5f);
        motionComponent().setAcceleration(2f);
        motionComponent().setDeceleration(4f);
    }
}
