/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author AdmKasia
 */
public class Resources {
         TextureAtlas gameSprites;
 
     public Sprite player;
 
     public Resources()
     {
         gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
         player = new Sprite(gameSprites.findRegion("human_m"));
     }
 
     public void dispose()
     {
         gameSprites.dispose();
     }
}
