/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.veins.game.MyVeinsGame;
import com.veins.game.components.FactionComponent;
import com.veins.game.components.InventoryComponent;
import com.veins.game.components.LifeComponent;
import com.veins.game.components.NameComponent;
import com.veins.game.components.PlayerComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.components.TurnsComponent;

/**
 *
 * @author AdmKasia
 */
public class EntityFactory {
    public Engine _engine;
    MyVeinsGame _game;
    public Entity _player;
    
    public EntityFactory(MyVeinsGame game)
    {
        //engine = engine;
        _game = game;
        _engine = new Engine();
    }

    //ECS
    public Entity CreateActor(String name, TextureRegion tile, String faction){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent());
        actor.add(new FactionComponent(faction));
        
        _engine.addEntity(actor);
        return actor;
    }
    
    public Entity CreateActor(String name, TextureRegion tile, String faction, int fx, int fy){
        Entity actor = new Entity();
        actor.add(new PositionComponent(fx, fy));
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent());
        actor.add(new FactionComponent(faction));
        
        Gdx.app.log("Spawn", "Spawned actor at" + fx + ", " + fy);
        
        _engine.addEntity(actor);
        return actor;
    }
    
    public Entity CreatePlayer(String name, TextureRegion tile){
        _player = CreateActor(name, tile, "player");
        _player.add(new PlayerComponent());
        _player.add(new InventoryComponent());
        return _player;
    }
    
    public Entity CreateItem(String name, TextureRegion tile, String slot, int fx, int fy){
        Entity item = new Entity();
        item.add(new PositionComponent(fx, fy));
        item.add(new SpriteComponent(tile));
        item.add(new NameComponent(name));
        item.add(new SlotComponent(slot));
        
        Gdx.app.log("Spawn", "Spawned item at" + fx + ", " + fy);
        _engine.addEntity(item);
        return item;
    }
    
    public void testLoading(){
    //test loading
    JsonReader reader = new JsonReader();
    JsonValue root = reader.parse(Gdx.files.internal("data/test.json"));
    
    //print json to console
    //System.out.println(root);
    
        //parse the JSON
        String tilename = new String();
        String name = new String();
        String factionname = new String();
        for (JsonValue child : root.iterator()) //returns a list of children
        {   
            Gdx.app.log("Loading JSON", "Reading " + child.name);
            if ("name".equals(child.name)){
                name = child.asString();
                Gdx.app.log("Loading JSON", "We have a name entry " + name);
                
            }
            if ("faction".equals(child.name)){
                factionname = child.asString();
                Gdx.app.log("Loading JSON", "We have a faction entry " + factionname);
                
            }
                
            if ("sprite".equals(child.name)){
                tilename = child.asString();
                Gdx.app.log("Loading JSON", "We have a sprite entry " + tilename);
            }
        } //end for
        
        //paranoia
        if (!name.isEmpty() && !factionname.isEmpty())
        {
            //pick sprite
            if ("kobold".equals(tilename)){
                CreateActor(name, _game.res.kobold_tex, factionname, 8, 8);
            }
        }
    }
    
    public void itemtestLoading(){
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal("data/items_test.json"));
    
        //print json to console
        System.out.println(root);
        
        //Gdx.app.log("Dummy", "dummy");
        for (JsonValue table : root.iterator()) //returns a list of children
        {   
            JsonValue item = table.child;
            Gdx.app.log("Loading JSON", "Reading " + item.asString());
            
            //parse the JSON
            String name = new String();
            String slotname = new String();
            String spritename = new String();
            for (JsonValue child : table.iterator())
            {
            
                Gdx.app.log("Loading JSON", "Reading " + child.name);
                if ("name".equals(child.name)){
                    name = child.asString();
                }
                
                if ("slot".equals(child.name)){
                    slotname = child.asString();
                }
                
                if ("sprite".equals(child.name)){
                    spritename = child.asString();
                }
            }
            
            //paranoia
            if (!name.isEmpty() && !spritename.isEmpty() && !slotname.isEmpty()){
                //pick sprite
                if ("longsword".equals(spritename)){
                    CreateItem(name, _game.res.sword_tex, slotname, 6,6);
                }
                if ("leather".equals(spritename)){
                    CreateItem(name, _game.res.armor_tex, slotname, 5,5);
                }
            }
        }
    }
}
