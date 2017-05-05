/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.veins.game.components.EffectsComponent;
import com.veins.game.components.NameComponent;
import java.util.HashMap;

/**
 *
 * @author AdmKasia
 */
public class NumericEffect implements EffectInterface {
    
    String id;
    String variable;
    int val;
    
    public NumericEffect(String name, String what, int value){
        id = name;
        variable = what;
        val = value;
    }
    
    public String getComponentFromID(){
        String[] parts = id.split("-");
        return parts[0];
    }
    
    public String getVariableFromID(){
        String[] parts = id.split("-");
        return parts[1];
    }
    
    public int getValue(){
        return val;
    }
    
    
    @Override
    public void apply(Entity entity) {
        String name = entity.getComponent(NameComponent.class).string;
        if (entity.getComponent(EffectsComponent.class) !=null){
        
            Gdx.app.log("Effect", "Applying effect " + id + " value " + val + " to entity " + name);
            HashMap<String, EffectInterface> effects = entity.getComponent(EffectsComponent.class).getEffects();

            if (effects.containsKey(id)){
                Gdx.app.log("Effect", "We already contain an effect with id " + id);
            }
            else 
            {
                effects.put(id, this);
            }
        }
    }

    @Override
    public void remove(Entity entity) {
        String name = entity.getComponent(NameComponent.class).string;
        if (entity.getComponent(EffectsComponent.class) !=null){
            Gdx.app.log("Effect", "Removing effect " + id + "value " + val + "from entity " + name);
            HashMap<String, EffectInterface> effects = entity.getComponent(EffectsComponent.class).getEffects();
            if (effects.containsKey(id)){
                effects.remove(id);
                Gdx.app.log("Effect", "Removed effect with id " + id);
            }
        }
       
    }
    
}
