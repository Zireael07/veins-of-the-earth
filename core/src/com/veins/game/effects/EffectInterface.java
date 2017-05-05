/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.effects;

import com.badlogic.ashley.core.Entity;

/**
 *
 * @author AdmKasia
 */
public interface EffectInterface {
    public void apply(Entity entity);
    public void remove(Entity entity);
}
