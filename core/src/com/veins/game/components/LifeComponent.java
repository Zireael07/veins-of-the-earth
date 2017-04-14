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
public class LifeComponent implements Component {
    //0 for nil, -1 for miss, 1 for hit 
    public int hit;
    public int hp;
    public int max_hp;
    
    public LifeComponent(){
        hit = 0;
        hp = 10;
        max_hp = 10;
    }
    
    public LifeComponent(int life){
        hit = 0;
        hp = life;
        max_hp = hp;
    }
}
