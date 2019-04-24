package com.mygame.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;

public class Torch extends TopDownEntity {

    private Soldier owner;
    private Poste poste;
    private Fire fire;

	/*------------------------------------------------------------------*\
	|*							Constructors						    *|
	\*------------------------------------------------------------------*/

    public Torch(float x, float y, Stage s) {
        super(x, y, s);
        owner = null;

        poste = new Poste(x, y, s);
        fire = new Fire(x, y, s);

        fire.particlesComponent().setInfinite();
        fire.particlesComponent().start();
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
            if (owner != null) {
                clearMotionComponent();
                centerAtActor(owner);
                clearFixtures();
                isGrabbed = false;
            } else if (!isGrabbed){
                isGrabbed = true;
                initBody();
            }
        }

        public void grabedBy(Soldier other) {
            owner = other;
        }

        public void drop() {
            owner = null;
        }
    }

    /*------------------------------*\
  	|*		        Fire            *|
  	\*------------------------------*/

    private class Fire extends TopDownEntity {

        public Fire(float x, float y, Stage s) {
            super(x, y, s);
            addParticlesComponent();
            particlesComponent().loadParticles("explosion.pfx");
            setScale(.4f);

            addLightComponent(6);
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
