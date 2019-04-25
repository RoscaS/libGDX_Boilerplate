package com.modular.entities.templates;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.CoreEntity;

public class DecorationEntity extends CoreEntity {

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public DecorationEntity(float x, float y, float width, float height, Stage s) {
        super(x, y, s);
        setSize(width, height);

        addTextureComponent();
        setShapeRectangle();
        setStatic();
        initializePhysics();
    }
}
