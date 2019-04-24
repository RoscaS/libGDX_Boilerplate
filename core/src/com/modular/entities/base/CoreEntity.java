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
    protected MotionComponent motion_c;
    protected MouseComponent mouse_c;
    protected TextureComponent texture_c;
    protected ParticlesComponent particles_c;


    // State
    protected float elapsedTime;

    /*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public CoreEntity(float x, float y, Stage s) {
        setPosition(x, y);
        s.addActor(this);

        // Core attributes
        body = null;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        // Components
        motion_c = null;
        mouse_c = null;
        texture_c = null;
        particles_c = null;

        // State
        elapsedTime = 0;
    }

    /**
     * Uses data to initialize physical object and add it to
     * the physical world. Must be called after initialization.
     */
    public void initializePhysics() {

        // Initialize the body and add it to the physical world (WORD)
        body = com.modular.Static.World.world.createBody(bodyDef);

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
        circ.setRadius(getWidth() / (com.modular.Static.World.SCALE * 2 * coeff));
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
        if (getWidth() == 0) {
            System.err.println("error: actor size not set");
        }
        setOrigin(getWidth() / 2, getHeight() / 2);

        float x = (getX() + getOriginX()) / com.modular.Static.World.SCALE;
        float y = (getY() + getOriginY()) / com.modular.Static.World.SCALE;
        bodyDef.position.set(x, y);
    }

    /*------------------------------*\
   	|*		 Physical values       *|
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

    /*------------------------------*\
  	|*		 Components Getters	   *|
  	\*------------------------------*/

    public MotionComponent getMotionComponent() {
        return motion_c;
    }

    public TextureComponent getTextureComponent() {
        return texture_c;
    }

    public ParticlesComponent getParticlesComponent() {
        return particles_c;
    }

    public MouseComponent getMouseComponent() {
        return mouse_c;
    }

    /*------------------------------*\
  	|*		 Components Setters	   *|
  	\*------------------------------*/

    public void setMotionComponent(MotionComponent motion) {
        this.motion_c = motion;
    }

    public void setTextureComponent(TextureComponent texture) {
        this.texture_c = texture;
    }

    public void setParticlesComponent(ParticlesComponent particles) {
        this.particles_c = particles;
    }

    public void setMouseComponent(MouseComponent selection) {
        this.mouse_c = selection;
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
        float a = motion_c.getAngle();
        float c = (float) Math.cos(Math.toRadians(a));
        float s = (float) Math.sin(Math.toRadians(a));
        float x = (getX() + getOriginX()) + c * distance;
        float y = (getY() + getOriginY()) + s * distance;
        other.motion_c.centerAtPosition(x, y);
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
            motion_c.centerAtPosition(com.modular.Static.World.worldBounds.width, getY());
        if (getX() > com.modular.Static.World.worldBounds.width)
            motion_c.centerAtPosition(0, getY());
        if (getY() + getHeight() < 0)
            motion_c.centerAtPosition(getX(), com.modular.Static.World.worldBounds.height);
        if (getY() > com.modular.Static.World.worldBounds.height)
            motion_c.centerAtPosition(getX(), 0);
    }

    /**
     * If an edge of an object moves past the WORLD bounds,
     * adjust its position to keep it completely on screen.
     */
    public void boundToWorld() {
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > com.modular.Static.World.worldBounds.width)
            setX(com.modular.Static.World.worldBounds.width - getWidth());
        if (getY() < 0) setY(0);
        if (getY() + getHeight() > com.modular.Static.World.worldBounds.height)
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
