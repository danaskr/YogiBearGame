package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author saker
 */
public class Sprite implements Drawable{
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    protected boolean visible;


    public Sprite(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
    try {
        image= new ImageIcon(imagePath).getImage();
        System.out.println("src.Sprite.loadImage(), image has been loaded");
    } catch (Exception e) {
        System.err.println("Error loading image: " + imagePath);
        image = null;
    }
}

    public void draw(Graphics g) {
        if (visible && image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }
   
    /**
     * 
     * @param other
     * @return true if the object collides with something else
     */
    /*
    public boolean collides(Sprite other){
       Rectangle rect = new Rectangle(x,y,width,height);
       Rectangle otherR = new Rectangle(other.getX(),other.getY(),other.getWidth(),other.getHeight());
       return rect.intersects(otherR);
    }*/
    public boolean collides(Drawable other) {
    return this.getX() == other.getX() && this.getY() == other.getY();
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    
}
