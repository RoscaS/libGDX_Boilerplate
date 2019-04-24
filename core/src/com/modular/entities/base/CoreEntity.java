package com.modular.entities.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.modular.Static.World;
import com.modular.entities.components.*;

import java.util.ArrayList;

public class CoreEntity extends Group {

    // Core attributes
    protected Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;


    // Components
    private MotionComponent motion_c;
    private MouseComponent mouse_c;
    private TextureComponent texture_c;
    private ParticlesComponent particles_c;
    private GrabbableComponent grabbable_c;
    private LightComponent light_c;


    // State
    protected float elapsedTime;

    /*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public CoreEntity(float x, float y, Stage stage) {
        setPosition(x, y);
        stage.addActor(this);

        // Core attributes
        body = null;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        // Components
        motion_c = null;
        mouse_c = null;
        texture_c = null;
        particles_c = null;
        grabbable_c = null;

        // State
        elapsedTime = 0;
    }

    public CoreEntity(Stage stage) {
        this(100f, 100f, stage);
    }

    /**
     * Uses data to initialize physical object and add it to
     * the physical world. Must be called after initialization.
     */
    public void initializePhysics() {

        // Initialize the body and add it to the physical world (WORD)
        body = World.world.createBody(bodyDef);

        // Initialize a Fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("main");

        // Store reference to this, so can access from collision
        body.setUserData(this);
    }

    /*------------------------------------------------------------------*\
	|*						Initialization methods  				  *|
	\*------------------------------------------------------------------*/

    /**
     * Prevent body from rotating when colided by other bodies
     */
    public void setFixedRotation() {
        bodyDef.fixedRotation = true;
    }


    /*------------------------------*\
   	|*		      Behave            *|
   	\*------------------------------*/

    /**
     * The body type.
     * dynamic: positive mass, non-zero velocity determined by forces, moved by solve
     */
    public void setDynamic() {
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    /**
     * The body type.
     * kinematic: zero mass, non-zero velocity set by user, moved by solver.
     */
    public void setKinematic() {
        bodyDef.type = BodyDef.BodyType.KinematicBody;
    }

    /**
     * The body type.
     * static: zero mass, zero velocity, may be manually moved.
     */
    public void setStatic() {
        bodyDef.type = BodyDef.BodyType.StaticBody;
    }

    /*------------------------------*\
   	|*		  Shape of the body    *|
   	\*------------------------------*/

    public void setShapeRectangle(float coeffW, float coeffH) {
        // position must be centred
        setOriginCenter();
        PolygonShape rect = new PolygonShape();
        float w = getWidth() / (World.SCALE * 2 * coeffW);
        float h = getHeight() / (World.SCALE * 2 * coeffH);
        rect.setAsBox(w, h);
        fixtureDef.shape = rect;
    }

    public void setShapeCircle(float coeff) {
        setOriginCenter();
        // position must be centred
        CircleShape circ = new CircleShape();
        circ.setRadius(getWidth() / (World.SCALE * 2 * coeff));
        fixtureDef.shape = circ;
    }

    public void setShapeRectangle(float coeff) {
        setShapeRectangle(coeff, coeff);
    }

    public void setShapeRectangle() {
        setShapeRectangle(1, 1);
    }

    public void setShapeCircle() {
        setShapeCircle(1);
    }

    /**
     * Sets the origin position which is initially the actor's bottom
     * left corner to his center. Also Applyes the new center to the physical `body`.
     */
    private void setOriginCenter() {
        if (particles_c != null && texture_c == null) return;

        if (getWidth() == 0) {
            System.err.println("error: actor size not set");
        }
        setOrigin(getWidth() / 2, getHeight() / 2);

        float x = (getX() + getOriginX()) / World.SCALE;
        float y = (getY() + getOriginY()) / World.SCALE;
        bodyDef.position.set(x, y);
    }

    /*------------------------------*\
   	|*		 Physical values        *|
   	\*------------------------------*/

    /**
     * @param density     The density, usually in kg/m^2.
     * @param friction    The friction coefficient, usually in the range [0,1]
     * @param restitution The restitution (elasticity) usually in the range [0,1]
     */
    public void setPhysicsProperties(float density, float friction, float restitution) {
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
    }

    /*------------------------------------------------------------------*\
	|*						    GDX methods  				          *|
	\*------------------------------------------------------------------*/

    @Override
    public void act(float dt) {
        super.act(dt);

        if (grabbable_c != null) grabbable_c.act(dt);


        if (motion_c == null) {
            if (particles_c != null) particles_c.act(dt);
            // particles_c.act(dt);
            return;
        }

        if (texture_c.isAnimationPaused() && !motion_c.isMoving()) return;
        elapsedTime += dt;

        // if (texture_c != null && texture_c.isAnimationPaused()) return;


        if (motion_c != null) motion_c.act(dt);
        if (texture_c != null) texture_c.act(dt);
        if (particles_c != null) particles_c.act(dt);
        if (mouse_c != null) mouse_c.act(dt);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (texture_c != null) texture_c.draw(batch, parentAlpha);

        super.draw(batch, parentAlpha);
    }


	/*------------------------------------------------------------------*\
	|*							Public Methods   					  *|
	\*------------------------------------------------------------------*/

    public Body getBody() {
        return body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void clearFixtures() {
        body.getFixtureList().forEach(fixture -> body.destroyFixture(fixture));
    }

    /*------------------------------*\
  	|*		 Components Setters	   *|
  	\*------------------------------*/

    public void addMotionComponent() {
        motion_c = new MotionComponent(this);
    }

    public void addTextureComponent() {
        texture_c = new TextureComponent(this);
    }

    public void addParticlesComponent() {
        particles_c = new ParticlesComponent(this);
    }

    public void addMouseComponent() {
        mouse_c = new MouseComponent(this);
    }

    public void addLightComponent(float size) {
        light_c = new LightComponent(this, size);
    }

    public void addGrabableComponent(float radius) {
        grabbable_c = new GrabbableComponent(this, radius);
    }

    /*------------------------------*\
  	|*		 Components Getters	   *|
  	\*------------------------------*/

    public MotionComponent motionComponent() {
        return motion_c;
    }

    public TextureComponent textureComponent() {
        return texture_c;
    }

    public ParticlesComponent particlesComponent() {
        return particles_c;
    }

    public MouseComponent mouseComponent() {
        return mouse_c;
    }

    public LightComponent lightComponent() {
        return light_c;
    }

    public GrabbableComponent grabbableComponent() {
        return grabbable_c;
    }

    /*------------------------------*\
  	|*		 Components Clear	   *|
  	\*------------------------------*/

    public void clearMotionComponent() {
        motion_c = null;
    }

    public void clearTextureComponent() {
        texture_c = null;
    }

    public void clearParticlesComponent() {
        particles_c = null;
    }

    public void clearMouseComponent() {
        mouse_c = null;
    }

    public void clearLightComponent() {
        light_c = null;
    }

    public void clearGrabbableComponent() {
        grabbable_c = null;
    }

    /*------------------------------*\
  	|*			Getters		       *|
  	\*------------------------------*/

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    /*------------------------------*\
  	|*			  Tools  		   *|
  	\*------------------------------*/

    /**
     * Sets the opacity of this entity. (Actor)
     *
     * @param opacity value from 0 (transparent) to 1 (opaque)
     */
    public void setOpacity(float opacity) {
        getColor().a = opacity;
    }

    /**
     * Move given actor in front of this actor.
     * @param other    Box2DActor to set in front
     * @param distance how far ahead of this actor (metters)
     */
    public void putAhead(CoreEntity other, float distance) {
        float a = getAngle();
        float c = (float) Math.cos(Math.toRadians(a));
        float s = (float) Math.sin(Math.toRadians(a));
        float x = (getX() + getOriginX()) + c * distance;
        float y = (getY() + getOriginY()) + s * distance;
        other.centerAtPosition(x, y);
    }

    /*------------------------------------------------------------------*\
	|*							    Angle                               *|
	\*------------------------------------------------------------------*/

    /**
     * Instantly rotate in "angle" direction
     * If current speed is zero, this will have no effect.
     * @param angle (degrees)
     */
    public void setAngle(float angle) {
        Vector2 v = getBody().getPosition();
        float deg = (float) Math.toRadians(angle);
        getBody().setTransform(v.x, v.y, deg);
    }

    /**
     * Get facing angle in degree.
     * @return facing angle.
     */
    public float getAngle() {
        return (float) Math.toDegrees(getBody().getAngle());
    }

    /*------------------------------------------------------------------*\
	|*							    Translations                        *|
	\*------------------------------------------------------------------*/

    // /**
    //  * Align center of actor at given position coordinates.
    //  * @param x x-coordinate to center at (pixels)
    //  * @param y y-coordinate to center at (pixels)
    //  */
    // public void centerAtPosition(float x, float y) {
    //     float angle = getBody().getAngle();
    //     getBody().setTransform(x / World.SCALE, y / World.SCALE, angle);
    // }

    /**
     * Align center of actor at given position coordinates.
     * @param x x-coordinate to center at (pixels)
     * @param y y-coordinate to center at (pixels)
     */
    public void centerAtPosition(float x, float y) {
        float angle = getBody().getAngle();
        body.setTransform(x / World.SCALE, y / World.SCALE, angle);
    }

    public void centerAtActor(CoreEntity other, float adjustX, float adjustY) {
        float x = other.getX() + other.getWidth() / 2;
        float y = other.getY() + other.getHeight() / 2;
        setPosition(x + adjustX, y + adjustY);
    }

    public void centerAtActor(CoreEntity other) {
        centerAtActor(other, 0, 0);
    }

    /**
     * World coords
     * Instantly add "value" in meters to the x position of the Actor
     * @param value in metters
     */
    public void translateX(float value) {
        Vector2 v = getBody().getPosition();
        getBody().setTransform(v.x + value, v.y, 0);
    }

    /**
     * World coords
     * Instantly add "value" in meters to the y position of the Actor
     * @param value in metters
     */
    public void translateY(float value) {
        Vector2 v = getBody().getPosition();
        getBody().setTransform(v.x, v.y + value, 0);
    }

    /**
     * World coords
     * Instantly add `vector.x` and `vector.y` to the current
     * x and y components of this entity's position.
     * @param vector Vector2 containing values to add (metters)
     */
    public void translate(Vector2 vector) {
        translateX(vector.x);
        translateY(vector.y);
    }

    /*------------------------------------------------------------------*\
	|*							    Camera   				          *|
	\*------------------------------------------------------------------*/

    /**
     * Center camera on this object, while keeping camera's range of view
     * (determined by screen size) completely within WORLD bounds.
     */
    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set(getX() + getOriginX(), getY() + getOriginY(), 0);

        // bound camera to layout
        float maxX = World.worldBounds.width - cam.viewportWidth / 2;
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2, maxX);
        float maxY = World.worldBounds.height - cam.viewportHeight / 2;
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2, maxY);
    }

    /*------------------------------------------------------------------*\
	|*							 Collisions 				          *|
	\*------------------------------------------------------------------*/

    public static Object extractAfromContact(Contact contact) {
        return contact.getFixtureA().getBody().getUserData().getClass();
    }

    public static Object extractBfromContact(Contact contact) {
        return contact.getFixtureB().getBody().getUserData().getClass();
    }

    public static boolean isContactBetween(Contact contact, Class a, Class b) {
        Object oA = new Object();
        Object oB = new Object();
        try {
            oA = extractAfromContact(contact);
            oB = extractBfromContact(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (oA.equals(a) && oB.equals(b));
    }

    /**
     * Searches Contact for given type of object; returns null if not found
     * assumes Body user data stores reference to associated object
     * @param contact
     * @param theClass
     */
    public static Object getContactObject(Contact contact, Class theClass) {
        Object objA = contact.getFixtureA().getBody().getUserData();
        Object objB = contact.getFixtureB().getBody().getUserData();

        if (objA.getClass().equals(theClass))
            return objA;
        else if (objB.getClass().equals(theClass))
            return objB;
        else
            return null;
    }

    /**
     * Searches Contact for given type of object and given Fixture name;
     * returns null if not found
     * assumes Body user data stores reference to associated object
     * and Fixture user data stores String containing name
     * @param contact
     * @param theClass
     * @param fixtureName
     * @return contact object
     */
    public static Object getContactObject(Contact contact, Class theClass, String fixtureName) {
        Object objA = contact.getFixtureA().getBody().getUserData();
        String nameA = (String) contact.getFixtureA().getUserData();
        Object objB = contact.getFixtureB().getBody().getUserData();
        String nameB = (String) contact.getFixtureB().getUserData();

        if (objA.getClass().equals(theClass) && nameA.equals(fixtureName))
            return objA;
        else if (objB.getClass().equals(theClass) && nameB.equals(fixtureName))
            return objB;
        else
            return null;
    }

    /*------------------------------------------------------------------*\
	|*							    Behave   				          *|
	\*------------------------------------------------------------------*/

    /**
     * If this object moves completely past the WORLD bounds,
     * adjust its position to the opposite side of the WORLD.
     */
    public void wrapAroundWorld() {
        if (getX() + getWidth() < 0)
            centerAtPosition(World.worldBounds.width, getY());
        if (getX() > World.worldBounds.width)
            centerAtPosition(0, getY());
        if (getY() + getHeight() < 0)
            centerAtPosition(getX(), World.worldBounds.height);
        if (getY() > World.worldBounds.height)
            centerAtPosition(getX(), 0);
    }

    /**
     * If an edge of an object moves past the WORLD bounds,
     * adjust its position to keep it completely on screen.
     */
    public void boundToWorld() {
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > World.worldBounds.width)
            setX(World.worldBounds.width - getWidth());
        if (getY() < 0) setY(0);
        if (getY() + getHeight() > World.worldBounds.height)
            setY(World.worldBounds.height - getHeight());
    }

    /*------------------------------------------------------------------*\
	|*							    Entities   				          *|
	\*------------------------------------------------------------------*/

    /**
     * Retrieves a list of all instances of the object from the given stage
     * with the given class name or whose class extends the class with the given name.
     * If no instances exist, returns an empty list.
     * Useful when coding interactions between different types of game objects in update method.
     * @param stage     Stage containing BaseActor instances
     * @param className name of a class that extends the BaseActor class
     * @return list of instances of the object in stage which extend with the given class name
     */
    public static ArrayList<CoreEntity> getList(Stage stage, String className) {
        ArrayList<CoreEntity> actors = new ArrayList<>();
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Actor a : stage.getActors()) {
            if (theClass.isInstance(a)) {
                actors.add((CoreEntity) a);
            }
        }
        return actors;
    }

    /**
     * Returns number of instances of a given class (that extends BaseActor).
     * @param className name of a class that extends the BaseActor class
     * @return number of instances of the class
     */
    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }
}
