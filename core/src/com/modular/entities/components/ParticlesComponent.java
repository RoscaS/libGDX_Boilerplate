package com.modular.entities.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.modular.entities.base.CoreEntity;

public class ParticlesComponent {

    protected CoreEntity entity;

    protected ParticleEffect effect;


    private class ParticleRenderer extends Actor {
        private ParticleEffect effect;

        ParticleRenderer(ParticleEffect e) {
            effect = e;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            effect.draw(batch);
        }
    }

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public ParticlesComponent(CoreEntity entity) {
        this.entity = entity;
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

	public void loadParticles(String pfxFile) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(pfxFile), Gdx.files.internal(""));

        ParticleRenderer renderingActor = new ParticleRenderer(effect);

        entity.addActor(renderingActor);
    }

    public void start() {
        effect.start();
    }

    /**
     * pauses continous emitters
     */
    public void stop() {
        effect.allowCompletion();
    }

    public boolean isRunning() {
        return !effect.isComplete();
    }



    public void setInfinite() {
        effect.getEmitters().forEach(i -> i.setContinuous(true));
    }

    public void act(float dt) {
        effect.update(dt);

        if (effect.isComplete() && !effect.getEmitters().first().isContinuous()) {
            effect.dispose();
            entity.remove();
        }
    }
}
