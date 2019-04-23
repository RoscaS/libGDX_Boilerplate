package com.modular.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.modular.base.CoreEntity;

public class TextureComponent {

    protected CoreEntity entity;

    protected boolean paused;
    protected Animation<TextureRegion> animation;


    /*------------------------------------------------------------------*\
   	|*							Constructors						  *|
   	\*------------------------------------------------------------------*/

    public TextureComponent(CoreEntity entity) {
        this.entity = entity;
        animation = null;
        paused = false;
    }

    /*------------------------------------------------------------------*\
   	|*							GDX Methods 						  *|
   	\*------------------------------------------------------------------*/

    public void draw(Batch batch, float parentAlpha) {
        Color c = entity.getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (animation != null && entity.isVisible()) {
            batch.draw(
                    animation.getKeyFrame(entity.getElapsedTime()),
                    entity.getX(), entity.getY(), entity.getOriginX(),
                    entity.getOriginY(), entity.getWidth(), entity.getHeight(),
                    entity.getScaleX(), entity.getScaleY(), entity.getRotation()
            );
        }
    }

    /*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     * Based on size of keyframes.
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        entity.setSize(w, h);
        entity.setOrigin(w / 2, h / 2);
    }

    /**
     * Creates an animation from images stored in separate files.
     * @param fileNames     array of names of files containing animation images
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {

        Array<TextureRegion> textureArray = new Array<>();

        for (String name : fileNames) {
            Texture texture = new Texture(Gdx.files.internal(name));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }

    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
     * @param fileName      name of file containing spritesheet
     * @param rows          number of rows of images in spritesheet
     * @param cols          number of columns of images in spritesheet
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,
                                                           float frameDuration, boolean loop) {

        Texture texture = new Texture(Gdx.files.internal(fileName), true);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
            }
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }

    /**
     * Convenience method for creating a 1-frame animation from a single texture.
     * @param fileName names of image file
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    /*------------------------------*\
   	|*				Getters		   *|
   	\*------------------------------*/

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    /*------------------------------*\
   	|*				Setters		   *|
   	\*------------------------------*/

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /*------------------------------*\
  	|*			  Query		       *|
  	\*------------------------------*/

    public boolean isPaused() {
        return paused;
    }

    /**
     * Checks if animation is complete: if play mode is normal (not looping)
     * and elapsed time is greater than time corresponding to last frame.
     */
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(entity.getElapsedTime());
    }

}