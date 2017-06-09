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
import com.veins.game.components.ActorStatsComponent;
import com.veins.game.components.ChatComponent;
import com.veins.game.components.CoinsComponent;
import com.veins.game.components.CombatComponent;
import com.veins.game.components.EffectsComponent;
import com.veins.game.components.FactionComponent;
import com.veins.game.components.InventoryComponent;
import com.veins.game.components.LifeComponent;
import com.veins.game.components.MoneyComponent;
import com.veins.game.components.NameComponent;
import com.veins.game.components.PlayerComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.components.TurnsComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author AdmKasia
 */
public class EntityFactory {
    public Engine _engine;
    MyVeinsGame _game;
    public Entity _player;
    int[] normalArray = { 13, 12, 11, 10, 9, 8 };
    
    HashMap<String, Entity> loaded_entities;
    
    public EntityFactory(MyVeinsGame game)
    {
        //engine = engine;
        _game = game;
        _engine = new Engine();
        
        loaded_entities = new HashMap<String, Entity>();
    }

    //ECS
    //no loading data, used for e.g. player
    public Entity CreateActor(String name, TextureRegion tile, String faction){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent(20)); //give the player more hp
        actor.add(new FactionComponent(faction));
        actor.add(new ActorStatsComponent(normalArray));
        actor.add(new CombatComponent());
        actor.add(new EffectsComponent());
        actor.add(new MoneyComponent(100));
        
        _engine.addEntity(actor);
        return actor;
    }
    
    //spawn an actor using loaded data
    public Entity CreateActor(String name, int fx, int fy){
        Entity proto_actor = loaded_entities.get(name);
        
        Entity actor = new Entity();
        actor.add(new PositionComponent(fx, fy));
        actor.add(new SpriteComponent(proto_actor.getComponent(SpriteComponent.class).sprites.get(0)));
        actor.add(new NameComponent(proto_actor.getComponent(NameComponent.class).string));
        actor.add(new TurnsComponent());
        if (proto_actor.getComponent(LifeComponent.class) != null){
            actor.add(new LifeComponent(proto_actor.getComponent(LifeComponent.class).hp));
        }
        else{
            actor.add(new LifeComponent());
        }
        actor.add(new FactionComponent(proto_actor.getComponent(FactionComponent.class).string));
        actor.add(new ActorStatsComponent(normalArray));
        actor.add(new CombatComponent(proto_actor.getComponent(CombatComponent.class).damage_num, proto_actor.getComponent(CombatComponent.class).damage_dice));
        actor.add(new EffectsComponent());
        
        //conditional
        if (proto_actor.getComponent(ChatComponent.class) != null){
            actor.add(new ChatComponent(proto_actor.getComponent(ChatComponent.class).text));
        }
        
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
    
    public Entity CreateItem(String name, int fx, int fy){
        Entity proto_item = loaded_entities.get(name);
        
        Entity item = new Entity();
        item.add(new PositionComponent(fx, fy));
        item.add(new SpriteComponent(proto_item.getComponent(SpriteComponent.class).sprites.get(0)));
        item.add(new NameComponent(proto_item.getComponent(NameComponent.class).string));
        item.add(new SlotComponent(proto_item.getComponent(SlotComponent.class).string));
        
        //conditional
        if (proto_item.getComponent(CombatComponent.class) != null){
            item.add(new CombatComponent(proto_item.getComponent(CombatComponent.class).damage_num, proto_item.getComponent(CombatComponent.class).damage_dice));
        }
        
        
        Gdx.app.log("Spawn", "Spawned item at" + fx + ", " + fy);
        _engine.addEntity(item);
        return item;
    }
    
    public Entity CreateMoney(String name, int fx, int fy){
        Entity proto_item = loaded_entities.get(name);
        
        Entity money = new Entity();
        money.add(new PositionComponent(fx, fy));
        money.add(new SpriteComponent(proto_item.getComponent(SpriteComponent.class).sprites.get(0)));
        money.add(new NameComponent(proto_item.getComponent(NameComponent.class).string));
        money.add(new CoinsComponent());
        
        Gdx.app.log("Spawn", "Spawned money at" + fx + ", " + fy);
        _engine.addEntity(money);
        return money;
    }
    
    
    //Loading entities
    public Entity LoadActor(String name, TextureRegion tile, String faction){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent());
        actor.add(new FactionComponent(faction));
        actor.add(new CombatComponent());
        Gdx.app.log("Loading", "Loaded actor " + name);
        return actor;
    }
    
    public Entity LoadActor(String name, TextureRegion tile, String faction, int hp){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent(hp));
        actor.add(new FactionComponent(faction));
        actor.add(new CombatComponent());
        Gdx.app.log("Loading", "Loaded actor with hp " + name);
        return actor;
    }
    
    public Entity LoadActor(String name, TextureRegion tile, String faction, int hp, int dam_die, int num_die){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent(hp));
        actor.add(new FactionComponent(faction));
        actor.add(new CombatComponent(num_die, dam_die));
        
        Gdx.app.log("Loading", "Loaded actor with defined damage " + name);
        return actor;
    }
    
     public Entity LoadActor(String name, TextureRegion tile, String faction, int hp, int dam_die, int num_die, String text){
        Entity actor = new Entity();
        actor.add(new PositionComponent());
        actor.add(new SpriteComponent(tile));
        actor.add(new NameComponent(name));
        actor.add(new TurnsComponent());
        actor.add(new LifeComponent(hp));
        actor.add(new FactionComponent(faction));
        actor.add(new CombatComponent(num_die, dam_die));
        
        //conditional component - text
        if (faction.equals("neutral")){
            actor.add(new ChatComponent(text));
        }
        
        Gdx.app.log("Loading", "Loaded actor with defined text " + text);
        return actor;
    }
    
    public Entity LoadMoney(String name, TextureRegion tile){
        Entity money = new Entity();
        money.add(new PositionComponent());
        money.add(new SpriteComponent(tile));
        money.add(new NameComponent(name));
        Gdx.app.log("Loading", "Loaded money " + name);
        return money;
    }
     
     
    public Entity LoadItem(String name, TextureRegion tile, String slot){
        Entity item = new Entity();
        item.add(new PositionComponent());
        item.add(new SpriteComponent(tile));
        item.add(new NameComponent(name));
        item.add(new SlotComponent(slot));
        Gdx.app.log("Loading", "Loaded item " + name);
        return item;
    }
    
    public Entity LoadItem(String name, TextureRegion tile, String slot, int dam_die, int num_die){
        Entity item = new Entity();
        item.add(new PositionComponent());
        item.add(new SpriteComponent(tile));
        item.add(new NameComponent(name));
        item.add(new SlotComponent(slot));
        item.add(new CombatComponent(num_die, dam_die));
        Gdx.app.log("Loading", "Loaded item with defined damage " + name);
        return item;
    }
    
    public void loadStoreActor(String name, TextureRegion tile, String faction){
        Entity actor = LoadActor(name, tile, faction);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, actor);
        }
    }
    
    public void loadStoreActor(String name, TextureRegion tile, String faction, int hp){
        Entity actor = LoadActor(name, tile, faction, hp);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, actor);
        }
    }
    
    public void loadStoreActor(String name, TextureRegion tile, String faction, int hp, int dam_die, int num_die){
        Entity actor = LoadActor(name, tile, faction, hp, dam_die, num_die);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, actor);
        }
    }
    
    public void loadStoreActor(String name, TextureRegion tile, String faction, int hp, int dam_die, int num_die, String text){
        Entity actor = LoadActor(name, tile, faction, hp, dam_die, num_die, text);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, actor);
        }
    }
    
    //item
    public void loadStoreItem(String name, TextureRegion tile, String slot){
        Entity item = LoadItem(name, tile, slot);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, item);
        }
    }
    
    public void loadStoreItem(String name, TextureRegion tile, String slot, int dam_die, int num_die){
        Entity item = LoadItem(name, tile, slot, dam_die, num_die);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, item);
        }
    }
    
    public void loadStoreMoney(String name, TextureRegion tile){
        Entity item = LoadMoney(name, tile);
        if (loaded_entities.get(name) == null){
            loaded_entities.put(name, item);
        }
    }
    
    
    public void testLoading(String file){
    //test loading
    JsonReader reader = new JsonReader();
    JsonValue root = reader.parse(Gdx.files.internal(file));
    
    parseActorJSON(root);
    }
    
    public void parseActorJSON(JsonValue root){
        //print json to console
    //System.out.println(root);
        
        for (JsonValue table : root.iterator()) //returns a list of children
        { 
            JsonValue item = table.child;
            Gdx.app.log("Loading JSON", "Reading " + item.asString());

            //parse the JSON
            String tilename = new String();
            String name = new String();
            String factionname = new String();
            int hp = 0;
            int dam_die = 0;
            int num_die = 0;
            String text = new String();
        
            for (JsonValue child : table.iterator()) //returns a list of children
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
                if ("hit_points".equals(child.name)){
                    hp = child.asInt();
                    Gdx.app.log("Loading JSON", "We have hit points " + hp);
                }
                if ("damage_dice".equals(child.name)){
                    dam_die = child.asInt();
                    Gdx.app.log("Loading JSON", "We have damage die " + dam_die);
                }
                if ("damage_number".equals(child.name)){
                    num_die = child.asInt();
                    Gdx.app.log("Loading JSON", "We have damage die number " + num_die);
                }
                if ("text".equals(child.name)){
                    text = child.asString();
                    Gdx.app.log("Loading JSON", "We have text");
                }
            } //end for
            
        //paranoia
        if (!name.isEmpty() && !factionname.isEmpty())
        {
            if (hp > 0){
                if (dam_die > 0 && num_die > 0){
                    if (!text.isEmpty()){
                        if ("human".equals(tilename)){
                            loadStoreActor(name, _game.res.human_tex, factionname, hp, dam_die, num_die, text);
                        }
                    }
                    else{
                        
                        //pick sprite
                        if ("kobold".equals(tilename)){
                            loadStoreActor(name, _game.res.kobold_tex, factionname, hp, dam_die, num_die);
                        }
                        if ("drow".equals(tilename)){
                            loadStoreActor(name, _game.res.drow_tex, factionname, hp, dam_die, num_die);
                        }
                        if ("human".equals(tilename)){
                            loadStoreActor(name, _game.res.human_tex, factionname, hp, dam_die, num_die);
                        }
                        if ("goblin".equals(tilename)){
                            loadStoreActor(name, _game.res.goblin_tex, factionname, hp, dam_die, num_die);
                        }
                    }
                }
                else{
                    //pick sprite
                    if ("kobold".equals(tilename)){
                        loadStoreActor(name, _game.res.kobold_tex, factionname, hp);
                    }
                    if ("drow".equals(tilename)){
                        loadStoreActor(name, _game.res.drow_tex, factionname, hp);
                    }
                    if ("human".equals(tilename)){
                        loadStoreActor(name, _game.res.human_tex, factionname, hp);
                    }
                }
            }
        }
        }//end outer for
    }
    
    
    public void itemtestLoading(){
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal("data/items_test.json"));
    
        //print json to console
        //System.out.println(root);
        
        //Gdx.app.log("Dummy", "dummy");
        for (JsonValue table : root.iterator()) //returns a list of children
        {   
            JsonValue item = table.child;
            Gdx.app.log("Loading JSON", "Reading " + item.asString());
            
            //parse the JSON
            String name = new String();
            String slotname = new String();
            String spritename = new String();
            int dam_die = 0;
            int num_die = 0;
            //money only
            int min_coins = 0;
            int max_coins = 0;
            
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
                if ("damage_dice".equals(child.name)){
                    dam_die = child.asInt();
                    Gdx.app.log("Loading JSON", "We have damage die " + dam_die);
                }
                if ("damage_number".equals(child.name)){
                    num_die = child.asInt();
                    Gdx.app.log("Loading JSON", "We have damage die number " + num_die);
                }
                if ("num_coins_min".equals(child.name)){
                    min_coins = child.asInt();
                    Gdx.app.log("Loading JSON", "We have minimum coins " + min_coins);
                }
                if ("num_coins_max".equals(child.name)){
                    max_coins = child.asInt();
                    Gdx.app.log("Loading JSON", "We have maximum coins " + max_coins);
                }
            }
            
            //paranoia
            if (!name.isEmpty() && !spritename.isEmpty() && !slotname.isEmpty()){
                if (dam_die > 0 && num_die > 0){
                    if ("longsword".equals(spritename)){
                        loadStoreItem(name, _game.res.sword_tex, slotname, dam_die, num_die);
                    }
                }
                else{
                    //pick sprite
                    if ("longsword".equals(spritename)){
                        loadStoreItem(name, _game.res.sword_tex, slotname);
                    }
                    if ("leather".equals(spritename)){
                        loadStoreItem(name, _game.res.armor_tex, slotname);
                    }
                }
            }
            
            //money
            if (!name.isEmpty() && !spritename.isEmpty()){
                if (min_coins > 0 && max_coins > 0){
                    //pick sprite
                    if ("silver_coins".equals(spritename)){
                        loadStoreMoney(name, _game.res.silver_tex);
                    }
                    if ("gold_coins".equals(spritename)){
                        loadStoreMoney(name, _game.res.gold_tex);
                    }
                }
            }
            
        }//end for
        
        Gdx.app.log("Loading JSON", "Finished loading");
    }
    
    public class RaceData{
        private String name;
        private int dex_bonus;
        private int con_bonus;
        private int int_bonus;
        
        public void setName(String str){
            name = str;
        }
        
        public void setDexBonus(int val){
            dex_bonus = val;
        }
        
        public void setConBonus(int val){
            con_bonus = val;
        }
        
        public void setIntBonus(int val){
            int_bonus = val;
        }
        
        public int getDexBonus(){
            return dex_bonus;
        }
        
        public int getConBonus(){
            return con_bonus;
        }
        
        public int getIntBonus(){
            return int_bonus;
        }
        
        public String getName(){
            return name;
        }
    }
    
    
    public ArrayList<RaceData> loadRaces(){
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal("data/races.json"));
    
        //print json to console
        //System.out.println(root);
        
        ArrayList<String> racenames = new ArrayList<String>();
        
        ArrayList<RaceData> races = new ArrayList<RaceData>();
        //HashMap<String, Integer> racebonus_dex = new HashMap<String, Integer>();
        
        
        for (JsonValue table : root.iterator()) //returns a list of children
        {   
            JsonValue item = table.child;
            Gdx.app.log("Loading JSON", "Reading " + item.asString());
            
            //master data
            RaceData race = new RaceData();
            
            //parse the JSON
            String name = new String();
            int dex_plus = 0;
            int con_plus = 0;
            int int_plus = 0;
            
            
            for (JsonValue child : table.iterator())
            {
            
                Gdx.app.log("Loading JSON", "Reading " + child.name);
                if ("name".equals(child.name)){
                    name = child.asString();
                }
                
                if ("dex".equals(child.name)){
                    dex_plus = child.asInt();
                }
                if ("con".equals(child.name)){
                    con_plus = child.asInt();
                }
                if ("int".equals(child.name)){
                    int_plus = child.asInt();
                }
                
                //load
                if (!name.isEmpty()){ //&& !racenames.contains(name)){
                    //RaceData race = new RaceData();
                    race.setName(name);
                    
                    if (dex_plus != 0){
                        race.setDexBonus(dex_plus); 
                    }
                    
                    if (con_plus != 0){
                        race.setConBonus(con_plus); 
                    }
                    
                    if (int_plus != 0){
                        race.setIntBonus(int_plus);
                    }
                    
                    if (!races.contains(race)){
                        races.add(race);
                    }
                }
                
                
            }
            
        } //end outer for
        
        return races;
        
    }
}
