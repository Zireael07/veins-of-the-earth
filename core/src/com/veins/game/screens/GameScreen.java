/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.veins.game.MyVeinsGame;
import com.veins.game.logic.GameLogic;
import com.veins.game.logic.MapGenerator;
import com.veins.game.logic.objects.Player;

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
    
    //map
    MapGenerator mapgen;
    IsometricTiledMapRenderer renderer;
    TiledMapTileLayer layer;
    
    public GameScreen(MyVeinsGame _game) {
        super(_game);
        logic = new GameLogic();
        
        //map gen
        mapgen = new MapGenerator(logic);
        TiledMap map = mapgen.createMap();
        renderer = new IsometricTiledMapRenderer(map);
        layer = (TiledMapTileLayer)map.getLayers().get(0);
        
        //camera stuffs
        camera = new OrthographicCamera(logic.MAP_WIDTH*logic.TILE_WIDTH, logic.MAP_HEIGHT*logic.TILE_HEIGHT);
        camera.translate(camera.viewportWidth/2,camera.viewportHeight/2);
        //let's have some padding
        viewport = new ScalingViewport(Scaling.none, logic.MAP_WIDTH*logic.TILE_WIDTH+5, logic.MAP_HEIGHT*logic.TILE_HEIGHT+5, camera);
        
        batch = new SpriteBatch();
        //add player
        player = logic.getPlayer();
        player.set(game.res.player);
        int player_x = (int) (player.getSelfX()*logic.TILE_WIDTH+0.25*logic.TILE_WIDTH);
        int player_y = player.getSelfY()*logic.TILE_HEIGHT;
        player.setPosition(player_x, player_y);
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
     public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //camera
        camera.update();
        
        // set the TiledMapRenderer view based on what the
	// camera sees, and render the map
        renderer.setView(camera);
        renderer.render();
        
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        //know our fps
        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());

        //draw player
        player.draw(batch);
        batch.end();
     }
    @Override
     public void dispose(){
         batch.dispose();
         Gdx.input.setInputProcessor(null);
     }
    
    
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
    }
}
