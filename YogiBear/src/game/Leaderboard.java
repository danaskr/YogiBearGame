package game;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;

public class Leaderboard extends JFrame {
    private JTable leaderboardTable;
    private YogiDB database;
    
    // Add a custom panel class for the background
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            backgroundImage = new ImageIcon("src/images/mmbg.png").getImage();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    public Leaderboard(YogiDB db) {
        setTitle("Yogi Bear Leaderboard");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setLocationRelativeTo(null); 
        setResizable(false);
        
        // Create the background panel
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);  
        
        // Title label with more visible styling
        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);  
        titleLabel.setBackground(new Color(0, 0, 0, 128));  
        titleLabel.setOpaque(true);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columns = {"Rank", "Player", "Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        leaderboardTable = new JTable(model);
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));
        leaderboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        leaderboardTable.setOpaque(false);
        leaderboardTable.setBackground(new Color(255, 255, 255, 200));  // Semi-transparent white
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Scroll pane with transparent background
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80, 200), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button with styled appearance
        JButton newGame = new JButton("new game");
        newGame.setFont(new Font("Arial", Font.BOLD, 14));
        newGame.setBackground(new Color(44, 62, 80));
        newGame.setForeground(Color.WHITE);
        newGame.setFocusPainted(false);
        newGame.addActionListener(e -> restart());
        mainPanel.add(newGame, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        database = db;
        updateLeaderboard();
    }
    
    private void restart(){
    new MainMenuGUI(); 
    this.setVisible(false);
    }
    
    private void updateLeaderboard() {
        try {
            ArrayList<Score> scores = database.getScores();
            DefaultTableModel model = (DefaultTableModel) leaderboardTable.getModel();
            model.setRowCount(0); 
            
            int rank = 1;
            for (Score score : scores) {
                model.addRow(new Object[]{
                    rank++,
                    score.getName(),
                    score.getTime()
                });
            }
            
            // If there are less than 10 scores, add empty rows
            while (model.getRowCount() < 10) {
                model.addRow(new Object[]{
                    rank++,
                    "-",
                    "-"
                });
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading scores: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
   
}