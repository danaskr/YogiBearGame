package game;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/**
 *
 * @author saker
 */
public class Level {
    
    public ArrayList<Drawable> map;
    
   /**
    * the map has a bear and that bear's starting point
    * should be the point where we find the the
    * entrance based on the maps generated.
    */
    
    private int x = 0;
    private int y = 0;
    public int collectedBaskets = 0;
    public int numOfBaskets = 0;
    private int levelNumber = 1;
    
    public Point startingPoint;
    
    public static final int W = 70;
    public static final int H = 70;
    
    public ArrayList<Point> rangerPositions = new ArrayList<>();
    
    private Point StartingPoint;
    
    public Level(int levelNumber) throws FileNotFoundException, IOException
    { 
        this.StartingPoint = new Point(0,0);
        this.levelNumber = levelNumber;
        loadLevel();
   
    }
    
    public void loadLevel() throws FileNotFoundException, IOException {
    
    String levelFile = String.format("src/levels/level%d.txt",levelNumber);
    
    BufferedReader br = new BufferedReader(new FileReader(new File(levelFile)));
    System.out.println(levelFile);
    map = new ArrayList<>();
        
    numOfBaskets = Integer.parseInt(br.readLine());
    
            
    String line;
    while ((line = br.readLine()) != null) {
        line = line.replace(" ", ""); 
                
        for (char obstacleType : line.toCharArray()) {
            System.out.println("Processing character: " + obstacleType);
            switch (obstacleType) {
                case 'T':
                    map.add(new Sprite(x, y, W, H,"src/images/tree.png"));
                    break;
                case 'M':
                    map.add(new Sprite(x, y, W, H,"src/images/mountain.png"));
                    break;
                case 'R': // R is a ranger moving in X
                    map.add(new Walkable(x, y, W, H));
                    map.add(new Ranger(x, y, W, H,1,0));
                    break;
                case 'V': // V is a ranger moving in Y
                    map.add(new Walkable(x, y, W, H));
                    map.add(new Ranger(x, y, W, H,0,1));
                    break;
                case 'E':
                    map.add(new Sprite(x, y, W, H,"src/images/entrance.png"));
                    startingPoint = new Point(x,y);
                    break;
                case 'P':
                    map.add(new Basket(x, y, W, H));
                    break;    
                case '.':
                    map.add(new Walkable(x, y, W, H));
                    break;
                default:
                    System.out.printf("Invalid file input '%c' at position (%d, %d)\n", obstacleType, x, y);
                    //for debugging, shall never be triggered :)
            }
            x += 70;
        }
        y += 70;
        x = 0;
       
    }
}

    
    public boolean isOver()
    { 
        
        return numOfBaskets == collectedBaskets;
    }
    
    public void draw(Graphics grphcs) {
    for (Drawable d : map) {
        d.draw(grphcs);
    }
}

    
    public Point getStartingPoint(){
        return startingPoint;
    }
   
    public boolean collides(Bear yogi) {
    for (int i = 0; i < map.size(); i++) {
        Drawable d = map.get(i);

        // Check only baskets for collisions
        if (d instanceof Basket && yogi.collides(d)) {
            // Replace basket with walkable tile
            map.set(i, new Walkable(d.getX(), d.getY(), W, H));
            collectedBaskets++; 
            
            return true; 
        }
    }
    return false;
}
    
    
}
