package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.base.TopDownEntity;
import com.modular.components.MotionComponent;
import com.modular.components.MouseComponent;
import com.modular.components.TextureComponent;

public class Soldier extends TopDownEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);


        motion_c = new MotionComponent(this);
        texture_c = new TextureComponent(this);
        mouse_c = new MouseComponent(this);

        texture_c.loadAnimationsFromSheet("soldier.png", 4, 4, .2f);

        motion_c.setMaxSpeed(1.5f);
        motion_c.setAcceleration(2f);
        motion_c.setDeceleration(200f);
        motion_c.setDynamic();

        setPhysicsProperties(50, .1f, 0);
        setShapeRectangle();
        setFixedRotation();

        initializePhysics();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }
}
