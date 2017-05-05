/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;
import com.veins.game.effects.EffectInterface;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author AdmKasia
 */
public class EffectsComponent implements Component {
    
    HashMap<String, EffectInterface> effects;
    
    public EffectsComponent(){
        effects = new HashMap<String, EffectInterface>();
    }
    
    public HashMap<String, EffectInterface> getEffects(){
        return effects;
    }
    
}
