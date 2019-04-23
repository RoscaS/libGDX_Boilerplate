package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.base.CoreEntity;
import com.modular.components.ParticlesComponent;

public class Torch extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Torch(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);

        particles = new ParticlesComponent(this, "explosion.pfx");

        setShapeRectangle();

        particles.setInfinite();
        particles.start();
    }
}
