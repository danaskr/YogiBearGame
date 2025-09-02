package game;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MainMenuGUI extends JFrame {
    public static String PLAYER;
    public static Font font = new Font("Georgia", Font.PLAIN, 15);
    
    // Background Panel class
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            backgroundImage = new ImageIcon("src/images/mmbg.png").getImage();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    public MainMenuGUI() {
        this.setSize(350, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        // Create background panel
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        // Title Label
        JLabel titleLabel = new JLabel("Yogi Bear Game");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(0, 0, 0, 128));
        titleLabel.setOpaque(true);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        mainPanel.add(titleLabel, gbc);
        
        // Name Label
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(font);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBackground(new Color(0, 0, 0, 128));
        nameLabel.setOpaque(true);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(nameLabel, gbc);
        
        // Name Field
        JTextField nameField = new JTextField();
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(nameField.getText().length() >= 15) e.consume();
            }
        });
        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setFont(font);
        nameField.setOpaque(true);
        nameField.setBackground(new Color(255, 255, 255, 220));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        mainPanel.add(nameField, gbc);
        
        // Start Button
        JButton startBtn = new JButton("Start Game");
        startBtn.setPreferredSize(new Dimension(150, 50));
        startBtn.setMaximumSize(new Dimension(150, 50));
        startBtn.setFont(new Font("Georgia", Font.BOLD, 16));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBackground(new Color(44, 62, 80));
        startBtn.setFocusPainted(false);
        startBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startBtn.setBackground(new Color(64, 82, 100));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                startBtn.setBackground(new Color(44, 62, 80));
            }
        });
        
        startBtn.addActionListener((ActionEvent e) -> {
            if(nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "You need to enter your name first!", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                PLAYER = nameField.getText();
                try {
                    GameGUI G = new GameGUI(PLAYER);
                    this.setVisible(false);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(MainMenuGUI.class.getName())
                          .log(Level.SEVERE, "Error starting game", ex);
                }
            }
        });
        mainPanel.add(startBtn, gbc);
        
        this.add(mainPanel);
        this.setVisible(true);
    }
}