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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.veins.game.MyVeinsGame;
import com.veins.game.logic.GameLogic;
import com.veins.game.logic.objects.Player;

/**
 *
 * @author AdmKasia
 */
public class GameScreen extends DefaultScreen implements InputProcessor {
    SpriteBatch batch;
    GameLogic logic;
    Player player;
    
    public GameScreen(MyVeinsGame _game) {
        super(_game);
        logic = new GameLogic();
        
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
    
}
