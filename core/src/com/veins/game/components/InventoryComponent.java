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
        for (int i = 0; i < 20; i++) {
            slots.add("inven_"+(i+1));
        }
        
        slots.add("body");
        items_map = new HashMap<String, Entity>();
    }
}
