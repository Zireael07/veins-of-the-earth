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
public class MoneyComponent implements Component {
    int silver;
    int gold;
    int platinum;
    
    
    public MoneyComponent(int start_purse){
        silver = start_purse;
        gold = 0;
        platinum = 0;
    }
    
    public int getSilver(){
        return silver;
    }
    
    public int getGold(){
        return gold;
    }
    
    public int getPlatinum(){
        return platinum;
    }
    
    public void addSilver(int val){
        silver = silver + val;
    }
    
    public void addGold(int val){
        gold = gold + val;
    }
    
    public void addPlatinum(int val){
        platinum = platinum + val;
    }
}
