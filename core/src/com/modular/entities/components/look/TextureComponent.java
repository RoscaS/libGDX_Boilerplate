package com.modular.entities.components.look;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.modular.entities.base.CoreEntity;

public class TextureComponent {

    protected CoreEntity entity;

    protected boolean animationPaused;
    protected Animation<TextureRegion> animation;

    // Animation
    private Animation north;
    private Animation south;
    private Animation east;
    private Animation west;

    // Controle
    private boolean simple;
    private String direction;

    /*------------------------------------------------------------------*\
   	|*							Constructors						  *|
   	\*------------------------------------------------------------------*/

    public TextureComponent(CoreEntity entity) {
        this.entity = entity;
        animation = null;
        simple = false;
        direction = "South";
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

    public void act(float dt) {
        if (animation != null && !simple) animationUpdate(dt);
    }

    /*------------------------------------------------------------------*\
   	|*							Update                                  *|
   	\*------------------------------------------------------------------*/

    private void animationUpdate(float dt) {
        // if (entity.mouseComponent().destination == null) {
        if (entity.motionComponent().getVelocity().len() == 0) {
            animationPaused = true;
        } else {
            animationPaused = false;
            float angle = entity.motionComponent().getVelocity().angle();

            if (angle >= 45 && angle <= 135) {
                direction = "North";
                setAnimation(north);

            } else if (angle > 135 && angle < 225) {
                setAnimation(west);
                direction = "West";
            } else if (angle >= 225 && angle <= 315) {
                setAnimation(south);
                direction = "South";
            } else {
                setAnimation(east);
                direction = "East";
            }
        }
    }

    /*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

    public void loadAnimationsFromSheet(String fileName, int rows, int cols, float frameDuration) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();
        Array<Animation<TextureRegion>> animations = new Array<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                textureArray.add(temp[row][col]);
            }
            animations.add(new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG));
            textureArray.clear();
        }

        south = animations.get(0);
        west = animations.get(1);
        east = animations.get(2);
        north = animations.get(3);
        setAnimation(south);
    }

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
     * Convenience method for creating a 1-frame animation from a single texture_c.
     * @param fileName names of image file
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        simple = true;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    /*------------------------------*\
   	|*				Getters		   *|
   	\*------------------------------*/

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public boolean isAnimationPaused() {
        return animationPaused;
    }

    /**
     * Checks if animation is complete: if play mode is normal (not looping)
     * and elapsed time is greater than time corresponding to last frame.
     */
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(entity.getElapsedTime());
    }
    /*------------------------------*\
   	|*				Setters		   *|
   	\*------------------------------*/

    public void setAnimationPaused(boolean paused) {
        this.animationPaused = paused;
    }


}
