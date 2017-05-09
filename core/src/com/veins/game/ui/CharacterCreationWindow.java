/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class CharacterCreationWindow extends VisWindow {
    int[] stats;
    Entity player;
    GameLogic g_logic;
    
    public CharacterCreationWindow(Entity entity, GameLogic logic){
        
        super("Character Creation");
        player = entity;
        g_logic = logic;
                
        addWidgets();
    }
    
    public void addWidgets(){
        columnDefaults(0).left();
        
        VisLabel Str = new VisLabel("STR");
        VisLabel Dex = new VisLabel("DEX");
        VisLabel Con = new VisLabel("CON");
        VisLabel Int = new VisLabel("INT");
        VisLabel Wis = new VisLabel("WIS");
        VisLabel Cha = new VisLabel("CHA");
        
        VisTable labels = new VisTable();
        
        labels.add(Str).row();
        labels.add(Dex).row();
        labels.add(Con).row();
        labels.add(Int).row();
        labels.add(Wis).row();
        labels.add(Cha).row();
        
        stats = g_logic.generateStats();
        
        final VisTable source_vals = new VisTable();
        
        //for every stat, generate a label
        for (int i = 0; i < stats.length; i++) {
            VisLabel stat = new VisLabel(Integer.toString(stats[i]));
            source_vals.add(stat).row();
        }
        
        add(labels);
        add(source_vals).row();
        
        VisTextButton reroll_button = new VisTextButton("Reroll");
        reroll_button.addListener(new ChangeListener()
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                stats = g_logic.generateStats();
                source_vals.clearChildren();
                //for every stat, generate a label
                for (int i = 0; i < stats.length; i++) {
                    VisLabel stat = new VisLabel(Integer.toString(stats[i]));
                    source_vals.add(stat).row();
                }
            }
        });
        
        add(reroll_button);
        
        VisTextButton ready_button = new VisTextButton("Ready!");
        ready_button.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                g_logic.setStats(player, stats);
                fadeOut();
                
                Gdx.app.log("Created", "created character");
            }
        });
        
        add(ready_button);
        
        //creation_window.setCenterOnAdd(true);
        setHeight(400);
        setWidth(400);
        setModal(true);
        //creation_window.pack();
        
        //test purposes
        closeOnEscape();
}
}
