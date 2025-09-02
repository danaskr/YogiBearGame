package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import static game.Level.H;
import static game.Level.W;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author saker
 */
public class GameEngine extends JPanel {
    
    private final int FPS = 240;
    
    private boolean paused = false;
    private Image background;
    private Level level;
    private Bear bear;
    private Timer timer;
    
    private final HashMap<String, String> Keys = new HashMap<>();
    private  Point StartingPoint = new Point(0,0);

    public JLabel timerLabel;
    public JLabel lifeLabel = new JLabel();
    public JLabel basketsLabel = new JLabel();
    private int elapsedTime = 0; 
    
    
    private int currentLevel = 1; // Tracks the current level (1-5)
// Tracks the current level (1-5)
    private boolean gameFinished = false;    

    private String PLAYER;
    private String TIME = "";
    
    public static YogiDB DB;
   

    public GameEngine(String PLAYER) throws IOException, SQLException {
        super();
        this.DB = new YogiDB(3);
        this.bear = new Bear(0,0,W,H);
        background = new ImageIcon("src/images/background.png").getImage();
        this.PLAYER = PLAYER;
        
        
       
        // Initialize the timer label
        timerLabel = new JLabel("Time:0");
        timerLabel.setBounds(10, 10, 30, 30);  // Set the position and size of the label
        this.add(timerLabel);  // Add it to the panel
        
        // Initialize the keys
        Keys.put("UP", "move up"); Keys.put("W", "move up");
        Keys.put("DOWN", "move down"); Keys.put("S", "move down");
        Keys.put("LEFT", "move left"); Keys.put("A", "move left");
        Keys.put("RIGHT", "move right"); Keys.put("D", "move right");
        Keys.put("ESCAPE", "pause");
        
        Keys.forEach((key, value) -> {
            this.getInputMap().put(KeyStroke.getKeyStroke(key), value);
            this.getActionMap().put(value, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (paused && key != "ESCAPE") return; // Ignore input if paused
                    switch (key) {
                        case "UP":
                        case "W":
                            if(checkY(-1)){
                            bear.moveY(-1);}
                            else System.out.println("moving blocked Y");
                            break;
                        case "DOWN":
                        case "S":
                            if(checkY(1)){
                            bear.moveY(1);}
                            else System.out.println("moving blocked Y");
                            break;
                        case "D":
                        case "RIGHT":
                            if(checkX(1)){
                            bear.moveX(1);
                            }else System.out.println("moving blocked X");
                            break;
                        case "A":
                        case "LEFT":
                            if(checkX(-1)){
                            bear.moveX(-1);
                            }else System.out.println("moving blocked X");
                            break;
                        case "ESCAPE":
                            togglePause();
                            break;
                        default:
                            break;
                    }
                }
            });
        });

        start();
        
        timer = new Timer(1000 / FPS, new NewFrameListener());
        timer.start();
    } 
    
    public void start() throws IOException {
        
        try {
            
            level = new Level(currentLevel);
            StartingPoint = level.startingPoint;
            elapsedTime = 0; // Reset timer for the new level
            bear.reposition(StartingPoint);
            bear.revive();
            basketsLabel.setText("Collected Baskets: " + level.collectedBaskets);
            lifeLabel.setText("Remaining tries: " + bear.getLives());
            
        } catch (IOException e) {
            System.out.printf("Level file %d is not found", currentLevel);
        }
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            timer.stop();
        } else {
            timer.start();
        }
    }
    
    private boolean checkX(int dx){
        int nextX = bear.getX() + (dx) * (W); // left -1, right +1
        int currentY = bear.getY();      
        //check if the tile at (nextX, currentY) is walkable
        for(Drawable d : level.map) {
        if (d.getX() == nextX && d.getY() == currentY) {
            return (d instanceof Walkable) || (d instanceof Basket); // The tile is walkable = true
            }
        }
        return false;
    }
    
    private boolean checkY(int dy) {
        int nextY = bear.getY() + dy * H; 
        int currentX = bear.getX();
        //check if the tile at (currentX, nextY) is walkable
        for (Drawable d : level.map) {
            if (d.getX() == currentX && d.getY() == nextY) {
                return (d instanceof Walkable) ||  (d instanceof Basket);
                
            }
        }
        return false; 
    }
    

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 350, 350, null);
        level.draw(grphcs);
        bear.draw(grphcs);
    }
    
    private void endGame() throws SQLException {
        paused = true;
        timer.stop();

        String message = (bear.getLives() <= 0 && currentLevel <= 4)
            ? "Game Over! You lost all your lives."
            : "Congratulations!";

        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
       
        DB.putScore(PLAYER,TIME);
        
        this.getTopLevelAncestor().setVisible(false);
        
        Leaderboard leaderboard = new Leaderboard(DB);
        leaderboard.setVisible(true);
       
    }

    private void proceedToNextLevel() throws SQLException {
        if (currentLevel < 5) {
            int response = JOptionPane.showConfirmDialog(
                this,
                "Level " + currentLevel + " complete! Do you want to proceed to the next level?",
                "Next Level?",
                JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION) {
   
                try {
                    currentLevel++;
                    start();
                } catch (IOException e) {
                    System.out.println("src.GameEngine.proceedToNextLevel()");
                }
            } else {
                endGame();
            }
        } else {
            endGame();
        }
    }
    
    private void killTheGame() throws SQLException {
        
        int response = JOptionPane.showConfirmDialog(
                this,
                "Level " + currentLevel + " you have failed this level, would you like to retry (yes)?",
                "sad",
                JOptionPane.YES_NO_OPTION
        );
        if(response == JOptionPane.YES_OPTION){
            try {start();}
            catch(IOException e){System.out.println("src.GameEngine.killTheGame()");}
        }else{
            
            //DB.putScore(PLAYER,TIME); shouldnt store when losing
        }
    }


    private class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
                if(!paused && !bear.isDead()) { // stop when he's dead
                    repaint();
                    updateTimer();
                    checkCollisions();
                    for (Drawable d : level.map) {
                        if (d instanceof Ranger) {
                            ((Ranger) d).moving(bear,StartingPoint);
                            lifeLabel.setText("Remaining tries: " + bear.getLives());
                        }
                    }
                }
        // Check if the game is over
            if(level.isOver() && !bear.isDead()) {
                    try {
                        proceedToNextLevel();
                    } catch (SQLException ex) {
                        //Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
            }
            if(bear.isDead()){
                    try {
                        killTheGame();
                    } catch (SQLException ex) {
                        //Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
            }
        }

        public void updateTimer() {
            elapsedTime++; 
            int mm = elapsedTime / 60;  
            int ss = elapsedTime % 60;  
    
            // Update the timer label
            timerLabel.setText(String.format("Time: %02d:%02d", mm, ss));
            TIME = String.format("%02d:%02d", mm,ss);
        }
        
        private void checkCollisions() {
            if (level.collides(bear)) {
            basketsLabel.setText("Collected Baskets : "+level.collectedBaskets);
            
                System.out.println("Collision detected!");
            }
        }
    }
}
