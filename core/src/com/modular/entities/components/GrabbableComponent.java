package com.modular.entities.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.*;
import com.modular.Static.World;
import com.modular.entities.base.CoreEntity;
import com.mygame.actors.Soldier;
import com.mygame.actors.Torch;


public class GrabbableComponent {

    protected CoreEntity entity;
    protected Soldier proximity;
    protected boolean isGrabbed;
    protected float radius;

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public GrabbableComponent(CoreEntity entity, float radius) {
        this.entity = entity;
        this.radius = radius;
        proximity = null;
        initFixture();


        World.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (CoreEntity.isContactBetween(contact, Soldier.class, Torch.Poste.class)) {
                    proximity = (Soldier) contact.getFixtureA().getBody().getUserData();
                }
            }

            @Override
            public void endContact(Contact contact) {
                proximity = null;
            }

            @Override public void preSolve(Contact contact, Manifold oldManifold) { }
            @Override public void postSolve(Contact contact, ContactImpulse impulse) { }

        });
    }

    public void act(float dt) {
        if (proximity != null && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            ((Torch.Poste)entity).grabedBy(proximity);
            isGrabbed = true;
        }

        if (isGrabbed && Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            ((Torch.Poste)entity).drop();
            isGrabbed = false;
        }
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

	public void initFixture() {
        FixtureDef sensor = new FixtureDef();
        sensor.isSensor = true;
        CircleShape circle = new CircleShape();
        circle.setRadius(radius / World.SCALE);
        sensor.shape = circle;
        Fixture sensorFixture = entity.getBody().createFixture(sensor);
        sensorFixture.setUserData("grab");
    }
}
