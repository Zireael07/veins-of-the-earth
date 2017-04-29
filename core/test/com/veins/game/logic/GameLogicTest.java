/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.veins.game.MyVeinsGame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AdmKasia
 */
public class GameLogicTest {
    
    public GameLogicTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of worldToIso method, of class GameLogic.
     */
    @Test
    public void testWorldToIso() {
        System.out.println("worldToIso");
        MyVeinsGame _game = new MyVeinsGame();
        GameLogic instance = new GameLogic(_game);
        Vector3 point = new Vector3(80, 280,0);
        boolean round = true;
        Vector3 expResult = new Vector3(1.0f, 1.0f, 0.0f);
        Vector3 result = instance.worldToIso(point, round);
        //check other case
        Vector3 point2 = new Vector3(620, 390, 0);
        Vector3 result2 = instance.worldToIso(point2, round);
        Vector3 expResult2 = new Vector3(7.0f, 15.0f, 0.0f);
        assertEquals(expResult, result);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of IsotoWorld method, of class GameLogic.
     */
    @Test
    public void testIsotoWorld() {
        System.out.println("IsotoWorld");
        MyVeinsGame _game = new MyVeinsGame();
        GameLogic instance = new GameLogic(_game);
        Vector3 input = new Vector3(100, 100, 0);
        Vector3 iso = instance.worldToIso(input, false);
        Vector3 expResult = new Vector3(100, 100, 0);
        Vector3 result = instance.IsotoWorld(iso);
        assertEquals(expResult, result);
    }
    
}
