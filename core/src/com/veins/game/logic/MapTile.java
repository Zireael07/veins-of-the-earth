/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author AdmKasia
 */
public class MapTile {
    
    int self_x;
    int self_y;
    
    int tex_reg_x;
    int tex_reg_y;
    public TextureRegion texture;
    
    public char simple_char;
    
    public MapTile(int x, int y, int tx, int ty, char chara){
        tex_reg_x = tx;
        tex_reg_y = ty;
        simple_char = chara;
        self_x = x;
        self_y = y;
    }
    
    public int getTextureX(){
        return tex_reg_x;
    }
    
    public int getTextureY(){
        return tex_reg_y;
    }
    
    public char getCharacter(){
        return simple_char;
    }
}
