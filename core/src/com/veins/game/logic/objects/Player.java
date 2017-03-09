/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class Player extends Sprite {
    private int x;
    private int y;
    private GameLogic logic;
    
    public Player(int fx, int fy, GameLogic logic)
    {
        x = fx;
        y = fy;
        logic = logic;
    }
    
    public int getSelfX() {
        return x;
    }
 
    public void setSelfX(int fx) {
        x = fx;
    }
 
    public int getSelfY() {
        return y;
    }
 
    public void setSelfY(int fy) {
        y = fy;
    }
    
    //move player-specific stuff here
     public boolean CheckMove(int fx, int fy)
    {
        return (fx >= 0 &&
                fx <= logic.MAP_WIDTH-1 && fy >= 0 &&
                fy <= logic.MAP_HEIGHT-1);
    }
    
    public void AssignPlayerPosition(int fx, int fy)
    {
        setSelfX(fx);
        setSelfY(fy);
    }
}
