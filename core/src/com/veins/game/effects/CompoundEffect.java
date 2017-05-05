/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.effects;

import com.badlogic.ashley.core.Entity;
import java.util.List;

/**
 *
 * @author AdmKasia
 */
public class CompoundEffect implements EffectInterface {
    private List<EffectInterface> effects;

    public CompoundEffect(List<EffectInterface> effects) {
        this.effects = effects;
    }

    @Override
    public void apply(Entity entity) {
        for (EffectInterface effect : effects){
            effect.apply(entity);
        }
    }

    @Override
    public void remove(Entity entity) {
        for (EffectInterface effect : effects){
            effect.remove(entity);
        }
    }
}