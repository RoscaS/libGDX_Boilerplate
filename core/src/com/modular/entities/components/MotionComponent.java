package com.modular.entities.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.modular.Static.World;
import com.modular.entities.base.CoreEntity;


public class MotionComponent {

    protected CoreEntity entity;

    protected Float maxSpeed;
    protected Float maxSpeedX;
    protected Float maxSpeedY;

    protected Float acceleration;
    protected Float deceleration;

    /*------------------------------------------------------------------*\
   	|*							Constructors						  *|
   	\*------------------------------------------------------------------*/

    public MotionComponent(CoreEntity entity) {
        this.entity = entity;

        maxSpeed = 1.5f;
        acceleration = 2f;
        deceleration = 2f;
    }

    /*------------------------------------------------------------------*\
	|*						Initialization methods  				  *|
	\*------------------------------------------------------------------*/

    /*------------------------------*\
   	|*		      Behave            *|
   	\*------------------------------*/

    /**
     * The body type.
     * dynamic: positive mass, non-zero velocity determined by forces, moved by solve
     */
    public void setDynamic() {
        entity.getBodyDef().type = BodyDef.BodyType.DynamicBody;
    }

    /**
     * The body type.
     * kinematic: zero mass, non-zero velocity set by user, moved by solver.
     */
    public void setKinematic() {
        entity.getBodyDef().type = BodyDef.BodyType.KinematicBody;
    }

    /**
     * The body type.
     * static: zero mass, zero velocity, may be manually moved.
     */
    public void setStatic() {
        entity.getBodyDef().type = BodyDef.BodyType.StaticBody;
    }

    /*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

    public boolean isMoving() {
        return getSpeed() > 0;
    }

    /*------------------------------*\
   	|*				Angles		   *|
   	\*------------------------------*/

    /**
     * Instantly rotate in "angle" direction
     * If current speed is zero, this will have no effect.
     * @param angle (degrees)
     */
    public void setAngle(float angle) {
        Vector2 v = entity.getBody().getPosition();
        float deg = (float) Math.toRadians(angle);
        entity.getBody().setTransform(v.x, v.y, deg);
    }

    /**
     * Get facing angle in degree.
     * @return facing angle.
     */
    public float getAngle() {
        return (float) Math.toDegrees(entity.getBody().getAngle());
    }

    /**
     * Get the normalized facing direction vector.
     * (cos(facingAngle), sin(facingAngle))
     * @return facing direction vector.
     */
    public Vector2 getRotationVector() {
        float angle = entity.getBody().getAngle();
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);
        return new Vector2(cos, sin);
    }

    /*------------------------------*\
   	|*				Speed		   *|
   	\*------------------------------*/

    /**
     * Set maximum speed of this object.
     * @param maxSpeed Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Set maximum speed in X direction of this object.
     * @param maxSpeedX Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeedX(Float maxSpeedX) {
        this.maxSpeedX = maxSpeedX;
    }

    /**
     * Set maximum speed in XY direction of this object.
     * @param maxSpeedY Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeedY(Float maxSpeedY) {
        this.maxSpeedY = maxSpeedY;
    }

    /**
     * Set the speed of movement (in pixels/second) in current direction.
     * If current speed is zero (direction is undefined),
     * direction will be set to angle of rotation.
     * @param speed of movement (pixels/second)
     */
    public void setSpeed(float speed) {
        if ((int) getSpeed() == 0) {
            setVelocity(getRotationVector().setLength(speed));
        } else {
            setVelocity(getVelocity().setLength(speed));
        }
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public Float getMaxSpeedX() {
        return maxSpeedX;
    }

    public Float getMaxSpeedY() {
        return maxSpeedY;
    }

    /**
     * Calculates the magnitude of current velocity.
     * @return speed of movement (pixels/second)
     */
    public float getSpeed() {
        return getVelocity().len();
    }

    /*------------------------------*\
   	|*			  Acceleration      *|
   	\*------------------------------*/

    public Float getAcceleration() {
        return acceleration;
    }

    public Float getDeceleration() {
        return deceleration;
    }

    public void setAcceleration(Float acceleration) {
        this.acceleration = acceleration;
    }

    public void setDeceleration(Float deceleration) {
        this.deceleration = deceleration;
    }

    /*------------------------------*\
   	|*			  Velocity         *|
   	\*------------------------------*/

    /**
     * Instantly set x and y components of velocity
     * @param x component
     * @param y component
     */
    public void setVelocity(float x, float y) {
        entity.getBody().setLinearVelocity(x, y);
    }

    /**
     * Instantly set velocity
     * @param vector with the new components
     */
    public void setVelocity(Vector2 vector) {
        entity.getBody().setLinearVelocity(vector);
    }


    public Vector2 getVelocity() {
        return entity.getBody().getLinearVelocity();
    }

    /*------------------------------------------------------------------*\
	|*				            Motion  Methods   		              *|
	\*------------------------------------------------------------------*/

    /*------------------------------*\
   	|*			   Natural         *|
   	\*------------------------------*/

    /**
     * Apply a force to the center of mass. This wakes up the body.
     * @param force
     */
    public void applyForce(Vector2 force) {
        entity.getBody().applyForceToCenter(force, true);
    }

    /**
     * Apply an impulse at a point. This immediately modifies the velocity.
     * It also modifies the angular velocity if the point of application
     * is not at the center of mass. This wakes up the body .
     * @param impulse
     */
    public void applyImpluse(Vector2 impulse) {
        Vector2 position = entity.getBody().getPosition();
        entity.getBody().applyLinearImpulse(impulse, position, true);
    }

    /**
     * @param strength strength of the impulse
     * @param angle    direction in degrees
     * @see #applyImpluse(Float, Float)
     */
    public void applyImpluse(Float strength, Float angle) {
        Vector2 v = new Vector2(1, 1);
        v.setAngle(getAngle() + angle);
        v.setLength(strength);
        entity.getBody().applyLinearImpulse(v, entity.getBody().getPosition(), true);
    }

    /**
     * Update velocity vector by current rotation angle and value stored in acceleration field.
     * @see #acceleration
     */
    public void moveForward() {
        applyForce(getRotationVector().setLength(acceleration));
    }

    /**
     * Apply impulse in angle direction.
     * @param angle angle between this and destination
     */
    public void moveTowards(float angle) {
        applyImpluse(acceleration, angle);

    }

    /**
     * Realistic topdown world deceleration, simulate gravity towards -z.
     * To have the correct behave, World gravity must be at a proper value (let's say -9.81),
     * a frictionerBox that covers the map must be set and this need to have a FrictionJoin defined.
     * @param dt
     */
    public void decelerate(float dt) {
        // setVelocity(getVelocity().setLength(getVelocity().len() - dt * getDeceleration()));
        setVelocity(getVelocity().setLength(getSpeed() - dt * deceleration));

        if (getSpeed() < .5f && entity.getTextureComponent().isAnimationFinished()) {
            setSpeed(0);
        }
    }

    /*------------------------------*\
   	|*		    Translations 	   *|
   	\*------------------------------*/

    /**
     * Align center of actor at given position coordinates.
     * @param x x-coordinate to center at (pixels)
     * @param y y-coordinate to center at (pixels)
     */
    public void centerAtPosition(float x, float y) {
        float angle = entity.getBody().getAngle();
        entity.getBody().setTransform(x / World.SCALE, y / World.SCALE, angle);
    }

    /**
     * World coords
     * Instantly add "value" in meters to the x position of the Actor
     * @param value in metters
     */
    public void translateX(float value) {
        Vector2 v = entity.getBody().getPosition();
        entity.getBody().setTransform(v.x + value, v.y, 0);
    }

    /**
     * World coords
     * Instantly add "value" in meters to the y position of the Actor
     * @param value in metters
     */
    public void translateY(float value) {
        Vector2 v = entity.getBody().getPosition();
        entity.getBody().setTransform(v.x, v.y + value, 0);
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
    |*							GDX Methods 						  *|
    \*------------------------------------------------------------------*/

    /**
     * Processes all Actions and related code for this object;
     * automatically called by act method in Stage class.
     * Here Act method serves two purposes. First, it will adjust the velocity of the
     * body if it exceeds any of the set maximum values. Second, it will update the actor properties (position and
     * angle) based on the properties of the body. In this process, physics units must be scaled back to pixel units,
     * and the angle of rotation must be converted from radians (used by the body) to degrees (used by the actor).
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt) {

        // Cap max speeds, if it's set.
        if (maxSpeedX != null) {
            Vector2 v = getVelocity();
            v.x = MathUtils.clamp(v.x, -maxSpeedX, maxSpeedX);
            setVelocity(v);
        }
        if (maxSpeedY != null) {
            Vector2 v = getVelocity();
            v.x = MathUtils.clamp(v.y, -maxSpeedY, maxSpeedY);
            setVelocity(v);
        }
        if (maxSpeed != null) {
            float s = getSpeed();
            if (s > maxSpeed)
                setSpeed(maxSpeed);
        }

        // update image data (position and rotation) based on physics data
        Vector2 center = entity.getBody().getWorldCenter();
        float x = World.SCALE * center.x - entity.getOriginX();
        float y = World.SCALE * center.y - entity.getOriginY();
        entity.setPosition(x, y);
    }
}
