/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;

/**
 *
 * @author AdmKasia
 */
public class ChatComponent implements Component {
    public String text;
    public boolean chatted;
    
    public ChatComponent(String in){
        text = in;
        chatted = false;
    }
}
