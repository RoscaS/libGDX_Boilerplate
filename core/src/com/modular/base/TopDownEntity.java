package com.modular.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.Static.World;
import com.modular.base.CoreEntity;

public class TopDownEntity extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public TopDownEntity(float x, float y, Stage s) {
        super(x, y, s);
    }

    @Override
    public void initializePhysics() {
        super.initializePhysics();

        boolean isDynamic = body.getType().getValue() == 2;

        if (isDynamic) {
            FrictionJointDef fJoin = new FrictionJointDef();
            fJoin.maxForce = 2;
            fJoin.maxTorque = 2;
            fJoin.initialize(World.frictionerBox, getBody(), new Vector2(0, 0));
            World.world.createJoint(fJoin);

            // cancel top to bottom gravity
            body.setGravityScale(0);
        }
    }
}
