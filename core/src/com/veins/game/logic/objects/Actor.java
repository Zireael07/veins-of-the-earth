/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class Actor extends Sprite {
    private int x;
    private int y;
    private GameLogic g_logic;
    
    public Actor(int fx, int fy, GameLogic logic)
    {
        x = fx;
        y = fy;
        g_logic = logic;
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
                fx <= g_logic.MAP_WIDTH-1 && fy >= 0 &&
                fy <= g_logic.MAP_HEIGHT-1);
    }
    
    public void AssignGridPosition(int fx, int fy)
    {
        setSelfX(fx);
        setSelfY(fy);
    }
    
     public void AttemptMove(int dx, int dy){
         if (CheckMove(getSelfX() + dx, getSelfY() + dy))
        {
            AssignGridPosition(getSelfX() + dx, getSelfY() + dy);
            Gdx.app.log("Movement", "Player coords" + getSelfX() + ","+ getSelfY()); 
            
            Place();
         }
         else
         {
             System.out.println("Can't move to" + (getSelfX()+dx) + " " + (getSelfY() + dy));
         }
     }
     
     //handles both on-screen display and grid coords
     public void Place(){
        Vector3 gridactorPos = new Vector3(getSelfX(), getSelfY(), 0);
        Vector3 isoTile = g_logic.IsotoWorld(gridactorPos);
        int actor_x = (int) isoTile.x+g_logic.ISO_WIDTH/4;
        int actor_y = (int) isoTile.y-g_logic.ISO_HEIGHT/4;
        setPosition(actor_x, actor_y);
     }
}
