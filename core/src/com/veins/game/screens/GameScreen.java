/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.veins.game.MyVeinsGame;
import com.veins.game.logic.GameLogic;
import com.veins.game.logic.MapGenerator;
import com.veins.game.logic.objects.Actor;
import com.veins.game.logic.objects.Player;
import java.util.ArrayList;

/**
 *
 * @author AdmKasia
 */
public class GameScreen extends DefaultScreen implements InputProcessor {
    SpriteBatch batch;
    GameLogic logic;
    Player player;
    OrthographicCamera camera;
    ScalingViewport viewport;
    
    //gui
    Stage stage;
    ScreenViewport hud_viewport;
    OrthographicCamera hud_camera;
    final VisLabel coords_label;
    
    //map
    MapGenerator mapgen;
    IsometricTiledMapRenderer renderer;
    TiledMapTileLayer layer;
    
    //show border around tile
    ShapeRenderer shape_renderer;
    Polygon tile_border;
    
    //NPCs
    ArrayList<Actor> actors = new ArrayList<Actor>();
    
    public GameScreen(MyVeinsGame _game) {
        super(_game);
        logic = new GameLogic();
        
        //gui
        hud_camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());    
        //set viewport
        hud_viewport = new ScreenViewport(hud_camera);
        stage = new Stage();
        stage.setViewport(hud_viewport);
        
        //map gen
        mapgen = new MapGenerator(logic);
        TiledMap map = mapgen.createMap();
        renderer = new IsometricTiledMapRenderer(map);
        layer = (TiledMapTileLayer)map.getLayers().get(0);
        
        shape_renderer = new ShapeRenderer();
        
        //camera stuffs
        camera = new OrthographicCamera(logic.MAP_WIDTH*logic.ISO_WIDTH, logic.MAP_HEIGHT*logic.TILE_HEIGHT);
        camera.translate(camera.viewportWidth/2,camera.viewportHeight/2);
        //let's have some padding
        viewport = new ScalingViewport(Scaling.none, logic.MAP_WIDTH*logic.ISO_WIDTH+5, logic.MAP_HEIGHT*logic.TILE_HEIGHT+5, camera);
        
        batch = new SpriteBatch();
        //add player
        player = logic.getPlayer();
        player.set(game.res.player);
        //place player
        player.Place();

        
        //spawn a monster
        Actor act = logic.spawnActor(game.res.kobold, 5,5);
        actors.add(act);
        //spawn some monsters
        for (int x = 0; x < logic.NUM_NPC; x++)
        {
            int act_x = logic.rng.between(0, logic.MAP_WIDTH-1);
            int act_y = logic.rng.between(0, logic.MAP_HEIGHT-1);
            act = logic.spawnActor(game.res.kobold, act_x, act_y);
            actors.add(act);
        }
        
        Gdx.input.setInputProcessor(this);
        
        //polygon
        tile_border = new Polygon();
        
        tile_border.setVertices(new float[]{0, 0, logic.ISO_WIDTH/2, -logic.ISO_HEIGHT/2, logic.ISO_WIDTH, 0f, logic.ISO_WIDTH/2, logic.ISO_HEIGHT/2});
    
        //ui elements
        coords_label = new VisLabel("Hello");
        stage.addActor(coords_label);
    }
    
    
    @Override
     public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //camera
        camera.update();
        viewport.apply();
        // set the TiledMapRenderer view based on what the
	// camera sees, and render the map
        renderer.setView(camera);
        renderer.render();
        
        //know our fps
        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        
        //draw tile border
        shape_renderer.setProjectionMatrix(camera.combined);
	shape_renderer.begin(ShapeRenderer.ShapeType.Line);
        
        TiledMapTileLayer.Cell cell = layer.getCell((int)isoPos.x, (int)isoPos.y);
	if (cell != null) {
            
            worldPos = logic.IsotoWorld(isoPos);
            
            float x1 = worldPos.x;
            float y2 = worldPos.y;
            
            tile_border.setPosition(x1, y2);
            shape_renderer.setColor(Color.YELLOW);
            shape_renderer.polygon(tile_border.getTransformedVertices());
        }
        
        //debug goes here
        
        shape_renderer.end();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //viewport.apply();
        //draw player
        player.draw(batch);
        
        //draw NPCs
        for (Actor act : actors)
        {
            act.draw(batch);
        }
        
        batch.end();
        
        stage.getViewport().apply();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	stage.draw();
     }
    @Override
     public void dispose(){
         batch.dispose();
         stage.dispose();
         Gdx.input.setInputProcessor(null);
     }
    
     //initing some stuff we absolutely need to function
     private Vector3 isoPos = new Vector3();
     private Vector3 worldPos = new Vector3();
     
    @Override
    public boolean keyDown(int i) {
        switch (i)
        {
            case Input.Keys.RIGHT:
                player.AttemptMove(1,0);
                break;
            case Input.Keys.LEFT:
                player.AttemptMove(-1,0);
                break;
            case Input.Keys.UP:
                player.AttemptMove(0,1);
                break;
            case Input.Keys.DOWN:
                player.AttemptMove(0, -1);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Mouse Event","Click at " + screenX + "," + screenY);
      
      Vector3 screenCoordinates = new Vector3(screenX,screenY,0);
      //remember to take viewport into account
      Vector3 worldCoordinates = camera.unproject(screenCoordinates, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
      Gdx.app.log("Mouse Event","Projected at " + worldCoordinates.x + "," + worldCoordinates.y);
   
      Vector3 outputPos = logic.worldToIso(worldCoordinates, true);
     
      Gdx.app.log("Mouse Event", "Tile coords " + outputPos);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 cs = new Vector3();
        Vector3 temp = new Vector3();
        camera.unproject(temp.set(screenX, screenY, 0), viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        cs.set(temp.x, temp.y, 0);
        
	isoPos.set(logic.worldToIso(cs, true));
        //set the coords label
        coords_label.setX(temp.x+60);
        coords_label.setY(temp.y+60);
        coords_label.setText((int) isoPos.x + ", " + (int) isoPos.y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        hud_viewport.update(width, height, true);
    }
}
