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
         public TextureRegion player_tex;
         public TextureRegion kobold_tex;
         public TextureRegion sword_tex;
         
    private Texture UIStone_tex;
    public Image UIStone;
    
    public Texture menu_bg_tex;
    public Image menu_background;
     
     public BitmapFont font;
 
     public Sprite player;
     public Sprite kobold;
     
//     public TextureAtlas UIatlas;
 
     public Resources()
     {
         font = new BitmapFont();
         gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
         player_tex = new TextureRegion(gameSprites.findRegion("human_m"));
         kobold_tex = new TextureRegion(gameSprites.findRegion("kobold"));
         sword_tex = new TextureRegion(gameSprites.findRegion("longsword"));
         player = new Sprite(player_tex);
         kobold = new Sprite(kobold_tex);

         UIStone_tex = new Texture(Gdx.files.internal("stone_background.png"));
         UIStone = new Image(UIStone_tex);
         
         menu_bg_tex = new Texture(Gdx.files.internal("Veins.png"));
         menu_background = new Image(menu_bg_tex);
         
     }
 
     public void dispose()
     {
         gameSprites.dispose();
     }
}
