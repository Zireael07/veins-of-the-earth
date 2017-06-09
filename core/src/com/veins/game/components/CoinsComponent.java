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
public class CoinsComponent implements Component {
    int num;
    
    public CoinsComponent(){
        num = 1;
    }
    
    public CoinsComponent(int val){
        num = val;
    }
}
