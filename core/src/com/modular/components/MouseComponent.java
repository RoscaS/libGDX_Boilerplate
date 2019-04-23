package com.modular.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.framework.BaseActor;
import com.modular.Static.Store;
import com.modular.base.CoreEntity;

public class MouseComponent {

    protected CoreEntity entity;

    // Displacement
    public Vector2 destination;

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public MouseComponent(CoreEntity entity) {
        this.entity = entity;
        inputHandler();
    }

	/*------------------------------------------------------------------*\
	|*							Update Methods 						    *|
	\*------------------------------------------------------------------*/

    public void act(float dt) {
        positionUpdate(dt);
    }

    private void positionUpdate(float dt) {
        if (destination != null) {
            goToDestination();
            float distance = distanceToDestination();

            if (distance <= 20) {
                clearDestination();
                entity.getMotionComponent().decelerate(dt);
            }
        }
        else if (entity.getMotionComponent().isMoving()) {
            entity.getMotionComponent().decelerate(dt);
        }
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 						    *|
   	\*------------------------------------------------------------------*/

    private void inputHandler() {
        entity.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (button == Input.Buttons.LEFT && !Store.selected.contains(entity, false)) {
                    Store.selected.add(entity);
                }
                System.out.println(Store.selected);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void goToDestination() {
        entity.getMotionComponent().moveTowards(getDestinationAngle());
    }

    public void clearDestination() {
        destination = null;
    }

	/*------------------------------*\
	|*				Getters		    *|
	\*------------------------------*/

    public Vector2 getDestination() {
        return destination != null ? destination : entity.getPosition();
    }


    /**
     * Calculates the angle between the facing angle of this Actor
     * and the destination point.
     *
     * @return angle (degrees)
     */
    public float getDestinationAngle() {
        if (destination == null) return entity.getMotionComponent().getAngle();
        float adj = (destination.x - entity.getX()) - (entity.getWidth() / 2);
        float opp = (destination.y - entity.getY()) - (entity.getHeight() / 2);
        return (float) Math.toDegrees(Math.atan2(opp, adj));
    }

    /**
     * Calculates the distance units between this actor and destination point.
     *
     * @return distance
     */
    public float distanceToDestination() {
        if (destination == null) return -1;
        float a = Math.abs(destination.y - entity.getY() - (entity.getHeight() / 2));
        float b = Math.abs(destination.x - entity.getX() - (entity.getWidth() / 2));
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

	/*------------------------------*\
	|*				Setters		   *|
	\*------------------------------*/

    public void setDestination(float x, float y) {
        destination = new Vector2(x, y);
        if (entity.getTextureComponent() != null)
            entity.getTextureComponent().animationPaused = false;

        System.out.println(destination);
    }

    public void setDestination(BaseActor other) {
        setDestination(other.getX(), other.getY());
    }
}
