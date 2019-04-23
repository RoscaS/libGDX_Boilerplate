package com.modular.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.modular.Static.World;

import java.util.Iterator;

/**
 * Loads a Tiled map file (*.tmx), extends Actor to automatically render.
 */
// public class TilemapEntities extends Actor {
public class TilemapEntities extends Actor {

    // window dimensions
    public int windowWidth;
    public int windowHeight;


    // tiledmap utils
    private TiledMap tiledMap;
    private OrthographicCamera tiledCamera;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

	/*------------------------------------------------------------------*\
	|*							Initialization							*|
	\*------------------------------------------------------------------*/

    /**
     * Initialize Tilemap created with the Tiled CameraActor Editor.
     */
    public TilemapEntities(String filename, Stage mainStage) {

        // setup tile map, renderer and camera
        tiledMap = new TmxMapLoader().load(filename);

        int tileWidth = (int) tiledMap.getProperties().get("tilewidth");
        int tileHeight = (int) tiledMap.getProperties().get("tileheight");
        int numTilesHorizontal = (int) tiledMap.getProperties().get("width");
        int numTilesVertical = (int) tiledMap.getProperties().get("height");
        windowWidth = tileWidth * numTilesHorizontal;
        windowHeight = tileHeight * numTilesVertical;

        World.setWorldBounds(windowWidth, windowHeight);

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        tiledMapRenderer.setBlending(true);

        // tiledCamera = new OrthographicCamera();


        // by adding object to Stage, can be drawn automatically
        mainStage.addActor(this);
    }

    /**
     * Initialize Tilemap created with the Tiled CameraActor Editor.
     */
    public TilemapEntities(String filename, Stage mainStage, OrthographicCamera mainCamera) {

        this(filename, mainStage);
        tiledCamera = mainCamera;
        tiledCamera.setToOrtho(false, windowWidth, windowHeight);
        tiledCamera.update();
    }

	/*------------------------------*\
	|*				Getters			*|
	\*------------------------------*/

    public OrthographicCamera getTiledCamera() {
        return tiledCamera;
    }

    public OrthoCachedTiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

    /**
     * Search the map layers for Rectangle Objects that contain a property (key) called "name" with associated value propertyName.
     * Typically used to store non-actor information such as SpawnPoint locations or dimensions of SolidActor objects.
     * Retrieve data as object, then cast to desired type: for example, float w = (float)obj.getProperties().get("width").
     */
    public Array<MapObject> getRectangleList(String propertyName) {
        Array<MapObject> list = new Array<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof RectangleMapObject)) {
                    continue;
                }
                MapProperties props = obj.getProperties();

                if (props.containsKey("name") && props.get("name").equals(propertyName)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * Search the map layers for Tile Objects (tile-like elements of object layers)
     * that contain a property (key) called "name" with associated value propertyName.
     * Typically used to store actor information and will be used to create instances.
     */
    public Array<MapObject> getTileList(String propertyName) {
        Array<MapObject> list = new Array<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof TiledMapTileMapObject)) {
                    continue;
                }
                MapProperties props = obj.getProperties();

                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject

                TiledMapTileMapObject tmtmo = (TiledMapTileMapObject) obj;
                TiledMapTile t = tmtmo.getTile();
                MapProperties defaultProps = t.getProperties();

                if (defaultProps.containsKey("name") && defaultProps.get("name").equals(propertyName)) {
                    list.add(obj);
                }

                // get list of default property keys
                Iterator<String> propertyKeys = defaultProps.getKeys();

                // iterate over keys; copy default values into props if needed
                while (propertyKeys.hasNext()) {
                    String key = propertyKeys.next();

                    // check if value already exists; if not, create property with default value
                    if (props.containsKey(key)) {
                        continue;
                    } else {
                        Object value = defaultProps.get(key);
                        props.put(key, value);
                    }
                }
            }
        }
        return list;
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void alignCameraMouse() {
            OrthographicCamera cam = (OrthographicCamera) getStage().getCamera();
            // Viewport v = this.getStage().getViewport();

            //OPTIMISATION POSSIBLE
            Vector2 mouseInWorld2D = new Vector2();

            mouseInWorld2D.x = Gdx.input.getX();
            mouseInWorld2D.y = Gdx.input.getY();

            float velocityCamera = 0.1f;
            float mouseBounds = 80f;

            if (mouseInWorld2D.x < mouseBounds)
                cam.position.add(-velocityCamera * (mouseBounds - mouseInWorld2D.x), 0, 0);
            if (mouseInWorld2D.x > this.getStage().getWidth() - mouseBounds)
                cam.position.add(velocityCamera * (mouseBounds - (this.getStage().getWidth() - mouseInWorld2D.x)), 0, 0);
            if (mouseInWorld2D.y < mouseBounds)
                cam.position.add(0, velocityCamera * (mouseBounds - mouseInWorld2D.y), 0);
            if (mouseInWorld2D.y > this.getStage().getHeight() - 50)
                cam.position.add(0, -velocityCamera * (mouseBounds - (this.getStage().getHeight() - mouseInWorld2D.y)), 0);

            cam.update();
        }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // adjust tilemap camera to stay in sync with main camera
        OrthographicCamera mainCamera = (OrthographicCamera) getStage().getCamera();

        this.tiledCamera.position.x = mainCamera.position.x;
        this.tiledCamera.position.y = mainCamera.position.y;

        tiledMapRenderer.setView(mainCamera);
        mainCamera.update();



        // need the following code to force batch order,
        // otherwise it is batched and rendered last.
        batch.end();
        tiledMapRenderer.render();
        batch.begin();
    }
}
