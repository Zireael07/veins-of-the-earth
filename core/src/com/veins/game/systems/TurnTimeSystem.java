/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.veins.game.components.NameComponent;
import com.veins.game.components.TurnsComponent;

/**
 *
 * @author AdmKasia
 */
public class TurnTimeSystem extends EntitySystem {
    private ComponentMapper<TurnsComponent>  turnsMap   = ComponentMapper.getFor(TurnsComponent.class);
    
    private ImmutableArray<Entity> entities;
    
    int blockingIndex;
    
    public TurnTimeSystem(int priority){
        super.priority = priority;
        blockingIndex = 0;
    }
    
        public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(TurnsComponent.class).get());
    }
    
    public void update(float deltaTime){
        
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            processEntity(i, entity, deltaTime);
        }
    }
    
    public void setBlocking(Entity entity, boolean block){
        TurnsComponent turnCom = turnsMap.get(entity);
        turnCom.blocking = block;
    }
    
    public boolean isBlocking(Entity entity){
        TurnsComponent turnCom = turnsMap.get(entity);
        return turnCom.blocking;
    }
    
    public void incrementIndex(){
        if (blockingIndex < entities.size() -1)
        {
            blockingIndex = blockingIndex + 1;
        }
        else
        {
            blockingIndex = 0;
        }
        Gdx.app.log("Turns test", "blocking Index set to " + blockingIndex);
    }
    
    public void UnblockTurns(Entity entity)
    {
        String str = entity.getComponent(NameComponent.class).string;
        Gdx.app.log("Turns test", "unblocking turns for entity " + str);
        setBlocking(entity, false);
        incrementIndex();
    }
    
    
    public void processEntity(int i, Entity entity, float deltaTime) {
        String str = entity.getComponent(NameComponent.class).string;
        //Gdx.app.log("Turns test", "processing #" + i + " blocking is " + blockingIndex);
        
        //if we equal the index, mark it as "blocking"
        if (i == blockingIndex){
            //Gdx.app.log("Turns test", "setting index " + blockingIndex + " as block");
            setBlocking(entity, true);
        }
        
        //need to know
        /*if (str == "Player")
        {
            Gdx.app.log("Turns test", "We're processing player");
        }
        
        if (!isBlocking(entity)){
            Gdx.app.log("Turns test", str + " does nothing");
        }*/
    }
}
