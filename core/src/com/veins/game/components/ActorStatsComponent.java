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
public class ActorStatsComponent implements Component {
    int Str;
    int Dex;
    int Con;
    int Int;
    int Wis;
    int Cha;
    int[] normalArray = { 13, 12, 11, 10, 9, 8 };
    //int[] heroicArray = {15, 14, 13, 12, 10, 8 };
    
    public ActorStatsComponent(){
        Str = normalArray[0];
        Dex = normalArray[2];
        Con = normalArray[1];
        Int = normalArray[4];
        Wis = normalArray[3];
        Cha = normalArray[5];
    }
    
    public ActorStatsComponent(String type){

        if ("standard".equals(type)){
            Str = normalArray[0];
            Dex = normalArray[2];
            Con = normalArray[1];
            Int = normalArray[4];
            Wis = normalArray[3];
            Cha = normalArray[5];
        }
    }
}
