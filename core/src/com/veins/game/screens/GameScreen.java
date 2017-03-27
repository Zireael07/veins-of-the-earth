/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.veins.game.MyVeinsGame;
import com.veins.game.components.NameComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.components.TurnsComponent;
import com.veins.game.logic.GameLogic;
import com.veins.game.logic.MapGenerator;
import com.veins.game.systems.MovementSystem;
import com.veins.game.systems.PositionSystem;
import com.veins.game.systems.RemovalSystem;
import com.veins.game.systems.RenderingSystem;
import com.veins.game.systems.TurnTimeSystem;
import java.util.ArrayList;
import java.util.List;
import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;

/**
 *
 * @author AdmKasia
 */
public class GameScreen extends DefaultScreen implements InputProcessor {
    SpriteBatch batch;
    GameLogic logic;
    Entity player;
    OrthographicCamera camera;
    ScalingViewport viewport;
    
    //gui
    Stage stage;
    ScreenViewport hud_viewport;
    OrthographicCamera hud_camera;
    final VisLabel coords_label;
    VisTable message_table;
    
    //map
    MapGenerator mapgen;
    IsometricTiledMapRenderer renderer;
    TiledMapTileLayer layer;
    
    DijkstraMap AIMap;
    
    //show border around tile
    ShapeRenderer shape_renderer;
    Polygon tile_border;
    
    //ecs
    Engine engine;
    
    public GameScreen(MyVeinsGame _game) {
        super(_game);
        logic = new GameLogic();
        
        //ecs
        engine = logic.engine;
        
        //gui
        hud_camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());    
        //set viewport
        hud_viewport = new ScreenViewport(hud_camera);
        stage = new Stage();
        stage.setViewport(hud_viewport);
        
        //map gen
        mapgen = new MapGenerator(logic);
        TiledMap map = mapgen.createMap();
        renderer = new IsometricTiledMapRenderer(map);
        layer = (TiledMapTileLayer)map.getLayers().get(0);
        
        //char dungeon
        logic.setDungeon(mapgen.createCharMap());
        Gdx.app.log("Char dungeon", '\n' + mapgen.toString());
        AIMap = new DijkstraMap(logic.getDungeon(), DijkstraMap.Measurement.CHEBYSHEV);
        logic.setAIMap(AIMap);
        
        shape_renderer = new ShapeRenderer();
        
        //camera stuffs
        camera = new OrthographicCamera(logic.MAP_WIDTH*logic.ISO_WIDTH, logic.MAP_HEIGHT*logic.TILE_HEIGHT);
        camera.translate(camera.viewportWidth/2,camera.viewportHeight/2);
        //let's have some padding
        viewport = new ScalingViewport(Scaling.none, logic.MAP_WIDTH*logic.ISO_WIDTH+5, logic.MAP_HEIGHT*logic.TILE_HEIGHT+5, camera);
        
        batch = new SpriteBatch();
        //add player

        player = logic.CreatePlayer("Player", game.res.player_tex);

        //spawn some monsters
        for (int x = 0; x < logic.NUM_NPC; x++)
        {
            int act_x = logic.rng.between(0, logic.MAP_WIDTH-1);
            int act_y = logic.rng.between(0, logic.MAP_HEIGHT-1);

            logic.CreateActor("kobold" + "#" + x, game.res.kobold_tex, "enemy", act_x, act_y);
        }
        
        //spawn an item
        int item_x = logic.rng.between(1, logic.MAP_WIDTH-1);
        int item_y = logic.rng.between(1, logic.MAP_HEIGHT-1);
        logic.CreateItem("longsword", game.res.sword_tex, "main_hand", item_x, item_y);
        
        item_x = logic.rng.between(1, logic.MAP_WIDTH-1);
        item_y = logic.rng.between(1, logic.MAP_HEIGHT-1);
        logic.CreateItem("leather armor", game.res.armor_tex, "body", item_x, item_y);
        
        
        Gdx.input.setInputProcessor(this);
        
        //polygon
        tile_border = new Polygon();
        
        tile_border.setVertices(new float[]{0, 0, logic.ISO_WIDTH/2, -logic.ISO_HEIGHT/2, logic.ISO_WIDTH, 0f, logic.ISO_WIDTH/2, logic.ISO_HEIGHT/2});
    
        
        //ecs
        engine.addSystem(new TurnTimeSystem(0));
        engine.addSystem(new MovementSystem(1, logic));
        engine.addSystem(new PositionSystem(2, logic));
        engine.addSystem(new RenderingSystem(3, batch));
        //needs to be last in the list
        engine.addSystem(new RemovalSystem(4));
        
        
        //ui elements
        coords_label = new VisLabel("Hello");
        stage.addActor(coords_label);
        
        message_table = new VisTable();
        stage.addActor(message_table);
        message_table.setPosition(70, 70);
        message_table.setSize(40, 100);
        message_table.columnDefaults(0).left();
    }
    
    
    @Override
     public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //camera
        camera.update();
        viewport.apply();
        // set the TiledMapRenderer view based on what the
	// camera sees, and render the map
        renderer.setView(camera);
        renderer.render();
        
        //know our fps
        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        
        //draw tile border
        shape_renderer.setProjectionMatrix(camera.combined);
	shape_renderer.begin(ShapeRenderer.ShapeType.Line);
        
        TiledMapTileLayer.Cell cell = layer.getCell((int)isoPos.x, (int)isoPos.y);
	if (cell != null) {
            
            worldPos = logic.IsotoWorld(isoPos);
            
            float x1 = worldPos.x;
            float y2 = worldPos.y;
            
            tile_border.setPosition(x1, y2);
            shape_renderer.setColor(Color.YELLOW);
            shape_renderer.polygon(tile_border.getTransformedVertices());
        }
        
        //debug goes here
        
        shape_renderer.end();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        engine.update(delta);
        
        batch.end();
        
        //update the log
        message_table.clear();
        if (logic.messages.size() < 5) {
            for (String log : logic.messages){
                message_table.add(new VisLabel(log)).expand().fill().row();
            }    
        }else{
             List<String> recent_messages = logic.messages;
             recent_messages = recent_messages.subList(logic.messages.size()-5, logic.messages.size());
             
             for (String log : recent_messages){
                 message_table.add(new VisLabel(log)).expand().fill().row();
             }
            }
        
        stage.getViewport().apply();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	stage.draw();
     }
    @Override
     public void dispose(){
         batch.dispose();
         stage.dispose();
         Gdx.input.setInputProcessor(null);
     }
    
     //initing some stuff we absolutely need to function
     private Vector3 isoPos = new Vector3();
     private Vector3 worldPos = new Vector3();
     
    @Override
    public boolean keyDown(int i) {
        switch (i)
        {
            case Input.Keys.RIGHT:
                if (player.getComponent(TurnsComponent.class).blocking){
                    engine.getSystem(MovementSystem.class).attemptMove(player, 1, 0);
                    engine.getSystem(TurnTimeSystem.class).UnblockTurns(player);
                }
                //player.AttemptMove(1,0);
                break;
            case Input.Keys.LEFT:
                if (player.getComponent(TurnsComponent.class).blocking){
                    engine.getSystem(MovementSystem.class).attemptMove(player, -1, 0);
                    engine.getSystem(TurnTimeSystem.class).UnblockTurns(player);
                }
                //player.AttemptMove(-1,0);
                break;
            case Input.Keys.UP:
                if (player.getComponent(TurnsComponent.class).blocking){
                    engine.getSystem(MovementSystem.class).attemptMove(player, 0, 1);
                    engine.getSystem(TurnTimeSystem.class).UnblockTurns(player);
                }
                //player.AttemptMove(0,1);
                break;
            case Input.Keys.DOWN:
                if (player.getComponent(TurnsComponent.class).blocking){
                    engine.getSystem(MovementSystem.class).attemptMove(player, 0, -1);
                    engine.getSystem(TurnTimeSystem.class).UnblockTurns(player);
                }
                //player.AttemptMove(0, -1);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Mouse Event","Click at " + screenX + "," + screenY);
      
      Vector3 screenCoordinates = new Vector3(screenX,screenY,0);
      //remember to take viewport into account
      Vector3 worldCoordinates = camera.unproject(screenCoordinates, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
      Gdx.app.log("Mouse Event","Projected at " + worldCoordinates.x + "," + worldCoordinates.y);
   
      Vector3 outputPos = logic.worldToIso(worldCoordinates, true);
     
      Gdx.app.log("Mouse Event", "Tile coords " + outputPos);
      
      if (outputPos.x > 0 && outputPos.x <= logic.MAP_WIDTH
          && outputPos.y > 0 && outputPos.y <= logic.MAP_HEIGHT){
          
          Gdx.app.log("Player path", "Pathing to " + (int)outputPos.x + " ," + (int) outputPos.y);
          
          //path to touched tile
          int player_x = player.getComponent(PositionComponent.class).x;
          int player_y = player.getComponent(PositionComponent.class).y;
          Coord start = Coord.get(player_x, player_y);
          Coord target = Coord.get((int)outputPos.x, (int)outputPos.y);
          
          logic.getAIMap().findPath(20, null, null, start, target);
          for (Coord c: logic.getAIMap().path){
                Gdx.app.log("Player path", Integer.toString(c.x) + ", " + Integer.toString(c.y));
          }

          if (logic.getAIMap().path != null){
                ArrayList<Coord> path = logic.getAIMap().path;
                if (path.size() > 0){
                        engine.getSystem(MovementSystem.class).moveTo(player, path.get(0).x, path.get(0).y);
                        engine.getSystem(TurnTimeSystem.class).UnblockTurns(player);
                    }
          }
      }
      else
      {
          Gdx.app.log("Player path", "clicked position is out of bounds: " + (int)outputPos.x + ", " + (int)outputPos.y);
      }
      
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 cs = new Vector3();
        Vector3 temp = new Vector3();
        camera.unproject(temp.set(screenX, screenY, 0), viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        cs.set(temp.x, temp.y, 0);
        
	isoPos.set(logic.worldToIso(cs, true));
        //set the coords label
        coords_label.setX(temp.x+60);
        coords_label.setY(temp.y+60);
        coords_label.setText((int) isoPos.x + ", " + (int) isoPos.y);
        
        //if entity at x,y show name
        String name;
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(NameComponent.class, PositionComponent.class).get());
        for (int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            if (engine.getSystem(MovementSystem.class).getPositionX(entity) == (int)isoPos.x
                && engine.getSystem(MovementSystem.class).getPositionY(entity) == (int)isoPos.y)
            {
                name = entity.getComponent(NameComponent.class).string;
                coords_label.setText((int) isoPos.x + ", " + (int) isoPos.y + '\n' + name);
            }
            
        }
        
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        hud_viewport.update(width, height, true);
    }
}
