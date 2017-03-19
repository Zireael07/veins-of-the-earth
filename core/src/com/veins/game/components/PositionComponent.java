/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;

/**
 *
 * @author AdmKasia
 */
public class PositionComponent implements Component {
    public int x;
    public int y;
    
    public PositionComponent(){
        x = 1;
        y = 1;
    }
    
    public PositionComponent(int fx, int fy){
        x = fx;
        y = fy;
    }
}
