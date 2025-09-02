/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.Image;

/**
 *
 * @author saker
 */
public class Ranger extends Sprite {
     
    private double velx = 2;
    private double vely = 2;
    private int xo; // if 1 then it move horizontally else vertically
    private int yo;
    
    public Ranger(int x, int y, int width, int height, int xo, int yo) {
        super(x, y, width, height,"src/images/ranger.png");
        this.xo= xo;
        this.yo = yo;
    }
    /**
     * decides where to move based on the initialized, 
     * moves horizontally in case of 1 else vertically.
     * the function updates the values of x and y accordingly.
     * @param bear
     * @param st
     */
    
    public void moving(Bear bear, Point st) {
        
    if (xo == 1 && yo == 0) { // Horizontal movement
        x += velx; // Move in the x direction
        if (x <= 0 || x + width >= 350) { // Reverse direction at horizontal boundaries
            velx *= -1;
        }
    } else if (yo == 1 && xo == 0) { // Vertical movement
        y += vely; // Move in the y direction
        if (y <= 0 || y + height >= 350) { // Reverse direction at vertical boundaries
            vely *= -1;
        }
    }
    
    
    if (x == bear.getX() && y == bear.getY()) {
        if(bear.getHit()){
            bear.reposition(st);
        }
        System.out.println("Life status of bear: " + bear.isDead());
        System.out.println("Ranger has collided with Bear!");
       
    }
}


     
}
