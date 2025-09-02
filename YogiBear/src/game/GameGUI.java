package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static game.MainMenuGUI.font;
import java.sql.SQLException;

/**
 *
 * @author saker
 */
public class GameGUI {

    private GameEngine game;
    private JFrame frame;

    public GameGUI(String PLAYER) throws IOException, SQLException {
        
        frame = new JFrame("Yogi Bear");
        game = new GameEngine(PLAYER);
        game.setPreferredSize(new Dimension(357, 350));
        //frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Create a panel for the labels
        JPanel labelPanel = new JPanel(new GridLayout(1, 3)); // Single row, 3 columns
        
        
        labelPanel.add(game.timerLabel);
        labelPanel.add(game.lifeLabel);
        labelPanel.add(game.basketsLabel);

        // Add components to the frame
        frame.setLayout(new BorderLayout());
        frame.add(labelPanel, BorderLayout.NORTH); // Labels at the top
        frame.add(game, BorderLayout.CENTER);      // Game in the center

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
