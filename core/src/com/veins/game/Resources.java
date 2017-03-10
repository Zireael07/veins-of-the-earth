/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author AdmKasia
 */
public class Resources {
         TextureAtlas gameSprites;
    public Texture UIStone_tex;
    public Image UIStone;
     
     public BitmapFont font;
 
     public Sprite player;
     
//     public TextureAtlas UIatlas;
 
     public Resources()
     {
         font = new BitmapFont();
         gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
         player = new Sprite(gameSprites.findRegion("human_m"));

         UIStone_tex = new Texture(Gdx.files.internal("stone_background.png"));
         UIStone = new Image(UIStone_tex);
         //UIatlas = new TextureAtlas(Gdx.files.internal("skin/craftacular-ui.atlas"));
     }
 
     public void dispose()
     {
         gameSprites.dispose();
     }
}
