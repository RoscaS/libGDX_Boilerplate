package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;

public class Torch extends TopDownEntity {

    private Soldier grabber;
    private Poste poste;
    private Fire fire;

	/*------------------------------------------------------------------*\
	|*							Constructors						    *|
	\*------------------------------------------------------------------*/

    public Torch(float x, float y, float scale, Stage stage) {
        super(x, y, stage);
        grabber = null;

        poste = new Poste(x, y, stage);
        fire = new Fire(x, y, scale, stage);

        fire.particlesComponent().setInfinite();
        fire.particlesComponent().start();
    }

    /*------------------------------*\
   	|*				Getters		    *|
   	\*------------------------------*/

    public Fire getFire() {
        return fire;
    }

    /*------------------------------------------------------------------*\
   	|*							    Parts  						        *|
   	\*------------------------------------------------------------------*/

    /*------------------------------*\
  	|*		        Poste           *|
  	\*------------------------------*/

    public class Poste extends TopDownEntity {

        private boolean isGrabbed = false;

        public Poste(float x, float y, Stage s) {
            super(x, y, s);

            addTextureComponent();
            textureComponent().loadTexture("torch.png");
            setSize(32, 52);
        }

        private void initBody() {
            addMotionComponent();
            setDynamic();
            setPhysicsProperties(1, 1f, 0f);
            setShapeRectangle();
            setFixedRotation();
            initializePhysics();

            addGrabableComponent(64);
        }

        @Override
        public void act(float dt) {
            super.act(dt);
            if (grabber != null) {
                clearMotionComponent();
                centerAtActor(grabber);
                clearFixtures();
                isGrabbed = false;
            } else if (!isGrabbed){
                isGrabbed = true;
                initBody();
            }
        }

        public void grabedBy(Soldier other) {
            grabber = other;
        }

        public void drop() {
            grabber = null;
        }
    }

    /*------------------------------*\
  	|*		        Fire            *|
  	\*------------------------------*/

    private class Fire extends TopDownEntity {

        public Fire(float x, float y, float scale, Stage s) {
            super(x, y, s);
            addParticlesComponent();
            particlesComponent().loadParticles("explosion.pfx");
            setScale(scale);

            addLightComponent(10);
            lightComponent().setDansing();
            lightComponent().getLight().setIgnoreAttachedBody(true);
        }

        @Override
        public void act(float dt) {
            super.act(dt);
            centerAtActor(poste, 0, 35);
        }
    }



}
