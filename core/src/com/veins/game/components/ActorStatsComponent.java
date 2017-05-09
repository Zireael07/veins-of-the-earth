/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;

/**
 *
 * @author AdmKasia
 */
public class ActorStatsComponent implements Component {
    int Str;
    int Dex;
    int Con;
    int Int;
    int Wis;
    int Cha;
    //int[] normalArray = { 13, 12, 11, 10, 9, 8 };
    //int[] heroicArray = {15, 14, 13, 12, 10, 8 };
    
    public ActorStatsComponent(int[] array){
        Str = array[0];
        Dex = array[2];
        Con = array[1];
        Int = array[4];
        Wis = array[3];
        Cha = array[5];
    }
    
    public ActorStatsComponent(int[] array, String type){

        if ("standard".equals(type)){
            Str = array[0];
            Dex = array[2];
            Con = array[1];
            Int = array[4];
            Wis = array[3];
            Cha = array[5];
        }
    }
    
    public void setStr(int val){
        Str = val;
        Gdx.app.log("Stats", "setting Str to " + val);
    }
    
    public void setDex(int val){
        Dex = val;
        Gdx.app.log("Stats", "setting Dex to " + val);
    }
    
    public void setCon(int val){
        Con = val;
        Gdx.app.log("Stats", "setting Con to " + val);
    }
    
    public void setInt(int val){
        Int = val;
        Gdx.app.log("Stats", "setting Int to " + val);
    }
    
    public void setWis(int val){
        Wis = val;
        Gdx.app.log("Stats", "setting Wis to " + val);
    }
    
    public void setCha(int val){
        Cha = val;
        Gdx.app.log("Stats", "setting Cha to " + val);
    }
}
