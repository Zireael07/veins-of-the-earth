/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AdmKasia
 */
public class InventoryComponent implements Component {
    public ArrayList<String> slots;
    public HashMap<String, Entity> items_map;
    
    public InventoryComponent(){
        slots = new ArrayList<String>();
        //set up slots
        for (int i = 0; i < 20; i++) {
            slots.add("inven_"+(i+1));
        }
        
        //basics
        slots.add("body");
        slots.add("main_hand");
        slots.add("off_hand");
        slots.add("quiver");
        //for swapping weapons
        slots.add("shoulder");
        //knicknacks
        slots.add("ring_1");
        slots.add("ring_2");
        slots.add("cloak");
        slots.add("amulet");
        slots.add("belt");
        //armor
        slots.add("arms");
        slots.add("gloves");
        slots.add("legs");
        slots.add("boots");
        //tools
        slots.add("lite");
        slots.add("tool");
               
        
        
        //init items stuff
        items_map = new HashMap<String, Entity>();
    }
}
