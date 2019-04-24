package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;
import com.modular.entities.components.MotionComponent;
import com.modular.entities.components.MouseComponent;
import com.modular.entities.components.TextureComponent;

public class Soldier extends TopDownEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage s) {
        super(x, y, s);

        motion_c = new MotionComponent(this);
        texture_c = new TextureComponent(this);
        mouse_c = new MouseComponent(this);

        texture_c.loadAnimationsFromSheet("soldier.png", 4, 4, .2f);
        motion_c.setDynamic();

        setPhysicsProperties(50, .3f, 0f);
        setShapeRectangle(1.2f, 1.7f);
        setFixedRotation();
        initializePhysics();

        motion_c.setMaxSpeed(1.5f);
        motion_c.setAcceleration(2f);
        motion_c.setDeceleration(4f);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }
}
