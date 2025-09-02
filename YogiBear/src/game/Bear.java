package game;

/**
 *
 * @author saker
 */
public class Bear extends Sprite{
    private double velx = 70;
    private double vely = 70;
    
    private int lives =3;
    
    public Bear(int x, int y, int width, int height){
        super(x, y, width, height, "src/images/bear.png");
        
    }
    public boolean isDead(){
        return lives == 0;
    }
    public void revive(){lives = 3;}
    public boolean getHit(){
        while(!isDead()){
            lives--;
            return true;
        }return false;
    }
    public int getLives(){return lives;}
    
    public boolean moveX(int direction){
        
        if(direction == 1 || direction == -1){
            x+= velx * direction;
            if(x < 0)
                x = 0;
            if(x + width > 350)
                x = 350 - width;
            System.out.println("moving in x " + x+","+y);
            return true;   
        }
        
            return false;       
    }
    public boolean moveY(int direction){
        if(direction == 1 || direction == -1){
            y += vely * direction;
                if(y < 0)
                    y = 0;
                if(y + height > 350)
                    y = 350 - height;
                System.out.println("moving in y " + x+","+y);
            return true;
        }
            return false;        
    }
    
    public void reposition(Point p){
        this.x = p.getX() ;
        this.y = p.getY();
        
        velx = 70;
        vely = 70;
    }
    
}
