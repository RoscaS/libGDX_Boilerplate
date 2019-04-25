package com.modular.entities.templates;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;
import com.modular.player.Player;

public class MobileEntity extends TopDownEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public MobileEntity(float x, float y, Stage stage, Player owner) {
        super(x, y, stage);
        setOwner(owner);
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
