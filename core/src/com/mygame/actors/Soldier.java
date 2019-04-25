package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.templates.MobileEntity;
import com.modular.player.Player;

public class Soldier extends MobileEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public Soldier(float x, float y, Stage stage, Player owner) {
        super(x, y, stage, owner);

        textureComponent().loadAnimationsFromSheet("soldier.png", 4, 4, .2f);
        setShapeRectangle(1.2f, 1.7f);
        setFixedRotation();
        initializePhysics();

    }
}
