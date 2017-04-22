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
public class CombatComponent implements Component {
    public int damage_num;
    public int damage_dice;
    String type;
    public CombatComponent(){
        damage_num = 1;
        damage_dice = 2;   
    }
    
    public CombatComponent(int num, int dice){
        damage_num = num;
        damage_dice = dice;
    }
}
