package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.CoreEntity;
import com.modular.entities.components.MotionComponent;
import com.modular.entities.components.ParticlesComponent;
import com.modular.entities.components.TextureComponent;

public class Torch extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Torch(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);

        motion_c = new MotionComponent(this);
        texture_c = new TextureComponent(this);
        particles_c = new ParticlesComponent(this, "explosion.pfx");

        motion_c.setStatic();
        particles_c.setInfinite();
        particles_c.start();

        setShapeRectangle(1.2f, 1.7f);
        initializePhysics();
    }
}
