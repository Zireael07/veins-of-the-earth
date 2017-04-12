/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.veins.game.MyVeinsGame;
import com.veins.game.factory.EntityFactory;
import java.util.ArrayList;
import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Dice;
import squidpony.squidmath.StatefulRNG;

/**
 *
 * @author AdmKasia
 */
public class GameLogic {
    public static final int MAP_WIDTH = 10;
    public static final int MAP_HEIGHT = 10;
    
    //iso
    public static final int ISO_WIDTH = 54;
    public static final int ISO_HEIGHT = 27;
    
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    
    MyVeinsGame game;
    
    
    public StatefulRNG rng;
    public Dice dice;
    
    public static int NUM_NPC = 4;
    
    //factory
    public EntityFactory factory;
    
    //ecs
    public Engine engine;
    Entity player;
    
    //internal map
    MapTile[][] inter_map;
    
    //Dijkstra
    char[][] dungeon;
    DijkstraMap g_AIMap;
    
    //message log
    public ArrayList<String> messages;
    //public List<String> recent_messages;
    
    public GameLogic(MyVeinsGame _game)
    {
        messages = new ArrayList<String>();
        game = _game;
        
        rng = new StatefulRNG();
        dice = new Dice();
        
        factory = new EntityFactory(game);
        engine = factory._engine; //new Engine();
    }
    
    //log messages
    public void addLog(String str){
        messages.add(str);
    }
    
    //internal map
    public void setInterMap(MapTile[][] int_map){
        inter_map = int_map;
    }
    
    public MapTile[][] getInterMap(){
        return inter_map;
    }
    
    
    //Dijkstra map
    public void setAIMap(DijkstraMap map)
    {
        g_AIMap = map;
    }
    
    public DijkstraMap getAIMap(){
        return g_AIMap;
    }
    
    public void setDungeon(char[][] chars){
        dungeon = chars;
    }
    
    public char[][] getDungeon(){
        return dungeon;
    }
    
    
    //used to convert from grid positions to iso
    public float getYOffset()
    {
        float calc = (float) (MAP_HEIGHT*ISO_HEIGHT)/2;
        //Gdx.app.log("Yoff", "Offset is " + calc);
        return -calc;
    }
    
    private Vector3 isoPos = new Vector3();
     private Vector3 worldPos = new Vector3();
     //convert world position to isometric position
    public Vector3 worldToIso(Vector3 point, boolean round) {
        point.x /= ISO_WIDTH;
        point.y = point.y + getYOffset();
        point.y = (point.y - ISO_HEIGHT / 2) / ISO_HEIGHT + point.x;
        point.x -= point.y - point.x;
        //round if asked to
        if (round){
            point.x = (int)point.x;
            point.y = (int)point.y;
        }
        
        isoPos.set(point);
    return point;
    }
    
    public Vector3 IsotoWorld(Vector3 iso){
         //Gdx.app.log("Iso", "iso is" + iso);
         Vector3 point = new Vector3();
         Vector3 origin = new Vector3 (0, -getYOffset()+ISO_HEIGHT/2, 0);
         //Gdx.app.log("Origin", "origin is" + origin);
         
         //vector from bottom to origin
         Vector3 bottom = new Vector3 (ISO_WIDTH/2, origin.y-ISO_HEIGHT/2, 0);
         Vector3 right = new Vector3(bottom.x-origin.x, bottom.y-origin.y, 0);
         //Gdx.app.log("IsotoWorld", "right vec is" + right);
         
         //vector from top to origin
         Vector3 top = new Vector3(ISO_WIDTH/2, origin.y+ISO_HEIGHT/2, 0);
        Vector3 up = new Vector3(top.x-origin.x, top.y-origin.y, 0); 
         //Gdx.app.log("IsotoWorld", "top vec is" + top);
         //Gdx.app.log("IsotoWorld", "up vec is" + up);
         
         //add
         Vector3 target_right = new Vector3(right.x*iso.x, right.y*iso.x, 0);
         Vector3 target_up = new Vector3(up.x*iso.y, up.y*iso.y, 0);
         //Gdx.app.log("IsotoWorld", "target right is" + target_right);
         //Gdx.app.log("IsotoWorld", "target up is" + target_up);
         
         Vector3 target_add = origin.add(target_right);
         //Gdx.app.log("IsotoWorld", "target add is" + target_add);
         Vector3 target = target_add.add(target_up);
         //Gdx.app.log("IsotoWorld", "target is" + target);
         
         point.set(target);
         
         return point;
     }
    
    public Entity getPlayer(){
        return player;
    }
    
    public Entity CreatePlayer(String name, TextureRegion tile){ 
        player = factory.CreatePlayer(name, tile);
        return player;
    }
}
