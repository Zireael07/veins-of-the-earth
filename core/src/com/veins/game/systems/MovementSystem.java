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
import com.veins.game.components.ChatComponent;
import com.veins.game.components.CombatComponent;
import com.veins.game.components.FactionComponent;
import com.veins.game.components.InventoryComponent;
import com.veins.game.components.LifeComponent;
import com.veins.game.components.NameComponent;
import com.veins.game.components.PlayerComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.TurnsComponent;
import com.veins.game.logic.GameLogic;
import com.veins.game.logic.MapTile;
import java.util.ArrayList;
import java.util.HashMap;
import squidpony.squidmath.Coord;

/**
 *
 * @author AdmKasia
 */
public class MovementSystem extends EntitySystem {
    private ComponentMapper<PositionComponent>  positionMap   = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TurnsComponent> turnsMap = ComponentMapper.getFor(TurnsComponent.class);
    
    
    private ImmutableArray<Entity> entities;
    
    private GameLogic g_logic;
    private Engine g_engine;
    MapTile[][] inter_map;
    
    public MovementSystem(int priority, GameLogic logic){
        super.priority = priority;
        g_logic = logic;
        inter_map = g_logic.getInterMap();
    }
    
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TurnsComponent.class).get());
        g_engine = engine;
    }
    
    public void update(float deltaTime){
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            processEntity(entity, deltaTime);
        }
    }
    
    public int getPositionX(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.x;
    }
    
    public int getPositionY(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.y;
    }
    
    public void setPositionX(Entity entity, int x){
        PositionComponent positionCom  = positionMap.get(entity);
        positionCom.x = x;
    }
    
    public void setPositionY(Entity entity, int y){
        PositionComponent positionCom  = positionMap.get(entity);
        positionCom.y = y;
    }
    
    public void setPosition(Entity entity, int x, int y)
    {
        setPositionX(entity, x);
        setPositionY(entity, y);
    }
    
    //test combat
    public int[] getDamage(Entity entity){
        CombatComponent CombatComp = entity.getComponent(CombatComponent.class);
        
        //if we have an inventory
        if (entity.getComponent(InventoryComponent.class) != null){
            HashMap<String, Entity> items = entity.getComponent(InventoryComponent.class).items_map;
            Entity item = items.get("main_hand");
            //if we have a weapon
            if (item != null){
                if (item.getComponent(CombatComponent.class) !=null){
                    CombatComp = item.getComponent(CombatComponent.class);
                }
            }
        }
        
        int num_dice = CombatComp.damage_num;
        int die = CombatComp.damage_dice;
        int[] a = {num_dice, die};
        
        
        return a;
    }
    
    
    public void combatTest(Entity entity, Entity target){
        String str = entity.getComponent(NameComponent.class).string;
        String target_str = target.getComponent(NameComponent.class).string;
        int roll = g_logic.dice.rollDice(1, 100);
        String success = "";
        LifeComponent LifeComp = target.getComponent(LifeComponent.class);
        //roll UNDER!
        if (roll < 55) {
            success = "Success!";
            LifeComp.hit = 1;
            int[] dam = getDamage(entity);
            LifeComp.damage = g_logic.dice.rollDice(dam[0],dam[1]);
            LifeComp.hp = LifeComp.hp - LifeComp.damage;
        }else
        {
            success = "Miss!";
            LifeComp.hit = -1;
            
        }
        g_logic.addLog(str + " attacks " + target_str + ": 1d100 roll " + roll + " result: " + success);      
        if (roll < 55){
            int[] dam = getDamage(entity);
            g_logic.addLog(str + " deals " + LifeComp.damage + " ("+ dam[0] + "d"+ dam[1] + ") damage to " + target_str);
        }
        else{
            //test
            g_logic.addLog(target_str + "'s hit points are " + g_logic.getHP(target));
        }
    }
    
    public void talkTest(Entity entity, Entity target){
        if (target.getComponent(ChatComponent.class) != null){
            //if player
            if (entity.getComponent(PlayerComponent.class) != null){
                String target_str = target.getComponent(NameComponent.class).string;
                String text = target.getComponent(ChatComponent.class).text;
                //set flag to draw one-liner on screen
                target.getComponent(ChatComponent.class).chatted = true;

                g_logic.addLog(target_str + " says " + text);
            }
        }
        
        
    }
    
    
    public boolean checkMove(Entity entity, int fx, int fy){
        char[][] dunmap = g_logic.getDungeon();
        return (fx >= 0 &&
                fx <= g_logic.MAP_WIDTH-1 && fy >= 0 &&
                fy <= g_logic.MAP_HEIGHT-1 
                && dunmap[fx][fy] != '#'
                );
    }
    
    public boolean checkMoveActor(Entity entity, int fx, int fy){
        boolean res = true;
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity_check = entities.get(i);
            if (entity_check != entity && fx == getPositionX(entity_check) && fy == getPositionY(entity_check))
            {
                res = false;
                
                String str = entity.getComponent(NameComponent.class).string;
                if (str == "Player"){
                    Gdx.app.log("Player move", "There's an entity at " + fx + ", " + fy);
                }
                else
                {
                    Gdx.app.log("AI path", "There's an actor at " + fx + ", " + fy);
                }
                
                //basic faction recognition
                String self_faction = entity.getComponent(FactionComponent.class).string;
                String faction = entity_check.getComponent(FactionComponent.class).string;
                Gdx.app.log("Faction", "Self: " + self_faction + " target " + faction);
                if (str == "Player" && faction.equals("neutral")){
                    //Gdx.app.log("Movement", "Triggering talk");
                    talkTest(entity, entity_check);
                }
                else {
                    if (!faction.equals(self_faction) && !faction.equals("neutral") && !self_faction.equals("neutral")) {
                    combatTest(entity, entity_check);
                }
                }
                
                //stop the loop
                break;
            }
            else
            {
                res = true;
            }
        }
        //Gdx.app.log("AI path", "check move returns " + res);
        return res;
    }
    
    public boolean attemptMove(Entity entity, int dx, int dy){
        if (checkMove(entity, getPositionX(entity) + dx, getPositionY(entity) + dy) 
            && checkMoveActor(entity, getPositionX(entity) + dx, getPositionY(entity) + dy) 
            ){
            setMapTile(null, getPositionX(entity), getPositionY(entity));
            setPosition(entity, getPositionX(entity) + dx, getPositionY(entity) + dy);
            setMapTile(entity, getPositionX(entity), getPositionY(entity));
            return true;
        }
        return false;
    }
    
    public void moveTo(Entity entity, int tx, int ty){
        int self_x = getPositionX(entity);
        int self_y = getPositionY(entity);
        
        int dx = tx-self_x;
        int dy = ty-self_y;
        
        Gdx.app.log("AI path", "move to " + tx + ", " + ty + " result " + dx + ", " + dy);
        
        attemptMove(entity, dx, dy);
    }
    
    public void setMapTile(Entity entity, int tx, int ty){
        inter_map[tx][ty].setActor(entity);
        if (entity != null){
            Gdx.app.log("Map tile", "Setting actor in tile " + tx + " " + ty);
        }
        else{
            Gdx.app.log("Map tile", "Setting tile " + tx + " " + ty + " no actor");
        }
    }
    
    
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionCom  = positionMap.get(entity);
        TurnsComponent turnsCom = turnsMap.get(entity);
        
       // test NPC movement
       String str = entity.getComponent(NameComponent.class).string;
       
       
       if ("Player".equals(str)){       
       }
       else
       {
           if (turnsCom.blocking){
               Coord start = Coord.get(getPositionX(entity), getPositionY(entity));
               //Coord target = Coord.get(getPositionX(g_logic.getPlayer()), getPositionY(g_logic.getPlayer()));
               Coord target = getTarget(entity);
               
               if (getPositionX(entity) == getPositionX(g_logic.getPlayer()) && getPositionY(entity) == getPositionY(g_logic.getPlayer())) 
               {
                   Gdx.app.log("AI path", "Do nothing because same position as player");
               } 
               else
               {
                   /*char[][] dunmap = g_logic.getDungeon();
                   if (dunmap[start.x][start.y] == '#'){
                       Gdx.app.log("AI path", "Do nothing because we're on wall tile");
                   }
                   else*/
                   {
                   
                   g_logic.getAIMap().findPath(20, null, null, start, target);

                //debug
                for (Coord c: g_logic.getAIMap().path){
                    Gdx.app.log("AI path", Integer.toString(c.x) + ", " + Integer.toString(c.y));
                }

                if (g_logic.getAIMap().path != null){
                    ArrayList<Coord> path = g_logic.getAIMap().path;
                    if (path.size() > 0){
                        moveTo(entity, path.get(0).x, path.get(0).y);
                    }
                }
               }
               }
               
               //old stuff
               g_engine.getSystem(TurnTimeSystem.class).UnblockTurns(entity);
           }
           
       }     
    }
    
    public Coord getTarget(Entity entity){
        int target_x = 1;
        int target_y = 1;
        String faction = entity.getComponent(FactionComponent.class).string;
        if (faction.equals("neutral")){
            Gdx.app.log("Target", "Random target for neutral AI");
            target_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
            target_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        }
        else
        {
            target_x = getPositionX(g_logic.getPlayer());
            target_y = getPositionY(g_logic.getPlayer());
        }
        
        return Coord.get(target_x, target_y);
    }
}
