package com.modular.entities.components;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.modular.Static.World;
import com.modular.entities.base.CoreEntity;

public class LightComponent {

    private CoreEntity entity;
    private PointLight light;
    private Handler handler;

    private float size;


    // Animation
    private float oscillation;
    private boolean dansing;
    private float bodyX;
    private float bodyY;
    private int cursor;
    private int direction;

	/*------------------------------------------------------------------*\
	|*							Constructors						    *|
	\*------------------------------------------------------------------*/

    public LightComponent(CoreEntity entity, float size) {
        this.oscillation = .9f;
        this.cursor = 0;
        this.direction = 1;

        this.entity = entity;
        this.size = size;

        handler = new Handler();
        light = new PointLight(World.lights, 1000, Color.ORANGE, size, 0, 0);
        light.attachToBody(handler.getBody());
        light.setSoftnessLength(4.75f);

    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 						    *|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Getters		    *|
	\*------------------------------*/

    public PointLight getLight() {
        return light;
    }

	/*------------------------------*\
	|*				Setters		    *|
	\*------------------------------*/

    public void setDansing() {
        dansing = true;
    }

    /*------------------------------------------------------------------*\
   	|*							    Parts  						        *|
   	\*------------------------------------------------------------------*/

    private class Handler extends CoreEntity {

        public Handler() {
            super(entity.getStage());

            getFixtureDef().isSensor = true;
            CircleShape circleShape = new CircleShape();
            // circleShape.setRadius(1);
            getFixtureDef().shape = circleShape;
            initializePhysics();
        }

        @Override
        public void act(float dt) {
            super.act(dt);

            bodyX = entity.getX() / World.SCALE;
            bodyY = entity.getY() / World.SCALE;

            cursor += direction;
            if (MathUtils.random(1) == 0) {
                bodyX += (cursor * 0.2) / World.SCALE;
            } else {
                bodyY += (cursor * 0.2) / World.SCALE;
            }
            if (cursor == 6) direction = -1;
            if (cursor == -6) direction = 1;

            getBody().setTransform(bodyX, bodyY - 0.5f, 0);

            if (dansing && MathUtils.random(3) == 0) {
                light.setDistance(MathUtils.random(size, size + oscillation));
            }
        }
    }

}
