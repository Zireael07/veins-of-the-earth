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
         public TextureRegion drow_tex;
         public TextureRegion human_tex;
         public TextureRegion goblin_tex;
         //items
         public TextureRegion sword_tex;
         public TextureRegion armor_tex;
         //misc
         public TextureRegion unit_marker_tex;
         public Sprite unit_marker;
         public TextureRegion shield_splash_tex;
         public TextureRegion damage_splash_tex;
         public Sprite shield_splash;
         public Sprite damage_splash;
         
         public TextureRegion slot_tex;
         
         TextureAtlas inventorySprites;
         
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
         drow_tex = new TextureRegion(gameSprites.findRegion("drow_fighter"));
         human_tex = new TextureRegion(gameSprites.findRegion("human"));
         goblin_tex = new TextureRegion(gameSprites.findRegion("goblin"));
         sword_tex = new TextureRegion(gameSprites.findRegion("longsword"));
         armor_tex = new TextureRegion(gameSprites.findRegion("armor_studded"));
         //markers
         unit_marker_tex = new TextureRegion(gameSprites.findRegion("unit_marker"));
         shield_splash_tex = new TextureRegion(gameSprites.findRegion("splash_shield"));
         damage_splash_tex = new TextureRegion(gameSprites.findRegion("splash_gray"));
         
         unit_marker = new Sprite(unit_marker_tex);
         shield_splash = new Sprite(shield_splash_tex);
         damage_splash = new Sprite(damage_splash_tex);
         
         player = new Sprite(player_tex);
         kobold = new Sprite(kobold_tex);
         
         inventorySprites = new TextureAtlas(Gdx.files.internal("packed/inventory.atlas"));
         slot_tex = new TextureRegion(inventorySprites.findRegion("background"));
         
         
         UIStone_tex = new Texture(Gdx.files.internal("stone_background.png"));
         UIStone = new Image(UIStone_tex);
         
         menu_bg_tex = new Texture(Gdx.files.internal("Veins.png"));
         menu_background = new Image(menu_bg_tex);
         
     }
 
     public void dispose()
     {
         gameSprites.dispose();
         inventorySprites.dispose();
     }
}
