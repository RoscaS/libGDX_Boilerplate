package com.modular.Static;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.Box2DActor;


public abstract class Debug {

    // Debug renderer
    private static Box2DDebugRenderer b2dr;

    // Projection matrix
    public static Matrix4 matrix;

    /*------------------------------------------------------------------*\
	|*							Public Methods 					        *|
	\*------------------------------------------------------------------*/

	public static void initDebug(Stage stage) {
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawVelocities(true);
        b2dr.setDrawJoints(false);

        matrix = new Matrix4(stage.getCamera().combined);
        matrix.scl(Box2DActor.PPM);
    }

	/*------------------------------*\
	|*				Getters		    *|
	\*------------------------------*/

	public static void debugRenderUpdate(Stage stage) {
	    if (b2dr == null) initDebug(stage);
	    b2dr.render(World.world, matrix);
    }

	/*------------------------------*\
	|*				Setters		    *|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Private Methods 				        *|
	\*------------------------------------------------------------------*/
}
