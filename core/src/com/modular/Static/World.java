package com.modular.Static;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.modular.entities.base.CoreEntity;
import com.modular.map.TilemapEntities;

public abstract class World {

    public static final float SCALE = 100;
    public static final float GRAVITY_X = 0;
    public static final float GRAVITY_Y = -9.8f;


    public static com.badlogic.gdx.physics.box2d.World world;
    public static RayHandler lights;

    public static Body frictionerBox; // for topDown worlds;
    private static boolean topDown;

    // Stores size of game WORLD for all actors
    public static Rectangle worldBounds;

    // Debug renderer
    public static Box2DDebugRenderer b2r;

    // Projection matrix
    public static Matrix4 projectionMatrix;


	/*------------------------------------------------------------------*\
	|*							Initialization			                *|
	\*------------------------------------------------------------------*/

    public static void initialize() {
        initWorld(new Vector2(GRAVITY_X, GRAVITY_Y), true);
        initLights();
        topDown = false;
        frictionerBox = null;
    }

    public static void initWorld(Vector2 gravity, boolean doSleep) {
        world = new com.badlogic.gdx.physics.box2d.World(gravity, doSleep);
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 						    *|
   	\*------------------------------------------------------------------*/

	public static void initLights() {
        lights = new RayHandler(world);
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        lights.setBlurNum(3);
    }

    /**
     * Set the world physics in topdown mode
     */
    public static void setTopdownWorld(TilemapEntities tma, float density, float friction) {
        float ScaledMapCenterX = (tma.windowWidth / SCALE) / 2;
        float ScaledMapCenterY = (tma.windowHeight / SCALE) / 2;
        topDown = true;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(ScaledMapCenterX, ScaledMapCenterY);

        frictionerBox = world.createBody(bodyDef);
        PolygonShape worldShape = new PolygonShape();
        worldShape.setAsBox(ScaledMapCenterX, ScaledMapCenterY);

        FixtureDef fd = new FixtureDef();
        fd.shape = worldShape;
        fd.density = density;
        fd.friction = friction;
        frictionerBox.createFixture(fd);
    }

    /**
     * Set WORLD dimensions for use by methods boundToWorld() and scrollTo().
     * @param width  width of WORLD
     * @param height height of WORLD
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    /**
     * Set WORLD dimensions for use by methods boundToWorld() and scrollTo().
     * @param ba whose size determines the WORLD bounds (typically a background image)
     */
    public static void setWorldBounds(CoreEntity ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }

    /*------------------------------*\
   	|*				Query		   *|
   	\*------------------------------*/

    public boolean isIsTopdown() {
        return topDown;
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 				      *|
	\*------------------------------------------------------------------*/
}
