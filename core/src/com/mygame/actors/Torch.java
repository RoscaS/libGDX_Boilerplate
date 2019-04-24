package com.mygame.actors;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.modular.entities.base.TopDownEntity;

public class Torch extends TopDownEntity {

    Poste poste;
    Fire fire;

    Soldier owner;

	/*------------------------------------------------------------------*\
	|*							Constructors						    *|
	\*------------------------------------------------------------------*/

    public Torch(float x, float y, Stage s) {
        super(x, y, s);
        owner = null;

        poste = new Poste(x, y, s);
        fire = new Fire(x, y, s);

        setSize(1,1);
        setShapeRectangle();
        initializePhysics();

        getBody().getFixtureList().forEach(f -> getBody().destroyFixture(f));


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
            // setDynamic();
            setStatic();
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
        }

        @Override
        public void act(float dt) {
            super.act(dt);
            centerAtActor(poste, 0, 35);
        }
    }
}
