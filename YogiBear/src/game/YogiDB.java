package game;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

public class YogiDB {
    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection; 
    
    public YogiDB(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        
        Properties connectionProps = new Properties();
        connectionProps.put("user", "addurown");
        connectionProps.put("password", "addurown");
        connectionProps.put("serverTimezone", "UTC");
        
        String dbURL = "jdbc:mysql://localhost:3307/yogiscores";
        
        try {
            connection = DriverManager.getConnection(dbURL, connectionProps);
            if (connection == null) {
                throw new SQLException("Failed to make connection!");
            }
            if (!connection.isValid(5)) {  // 5 second timeout
                throw new SQLException("Database connection is not valid");
            }
            
            
            // Verify table exists
            String checkTable = "SELECT 1 FROM SCORES LIMIT 1";
            try {
                connection.createStatement().execute(checkTable);
            } catch (SQLException e) {
                throw new SQLException("Database table not properly configured");
            }
            
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
        
                String insertQuery = "INSERT INTO SCORES (NAME, TIME) VALUES (?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        
        String deleteQuery = "DELETE FROM SCORES WHERE TIME = ?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }
    
    private double timeToDouble(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            if (parts.length == 2) {
                int minutes = Integer.parseInt(parts[0].trim());
                int seconds = Integer.parseInt(parts[1].trim());
                return minutes + (seconds / 100.0);
            }
            System.err.println("Invalid time format: " + timeStr);
            return 0.0;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing time: " + timeStr + ", Error: " + e.getMessage());
            return 0.0;
        }
    }
    
    private String doubleToTimeStr(double time) {
        int minutes = (int)time;
        int seconds = (int)((time - minutes) * 100);
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public ArrayList<Score> getScores() throws SQLException {
        
        String query = "SELECT * FROM SCORES ORDER BY TIME ASC";
        ArrayList<Score> scores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            double time = results.getDouble("TIME");
            String timeStr = doubleToTimeStr(time);
            scores.add(new Score(name, timeStr));
            System.out.println("Retrieved score: " + name + " - " + timeStr);
        }
        sortScores(scores);
        return scores;
    }
    
    public void putScore(String name, String timeStr) throws SQLException {
        
        if (name == null || timeStr == null) {
            throw new SQLException("Name or time is null");
        }
        
        double time = timeToDouble(timeStr);
        if (time == 0.0) {
            throw new SQLException("Invalid time conversion");
        }
        
        ArrayList<Score> scores = getScores();
        if (scores.size() < maxScores) {
            System.out.println("Inserting new score (under max)");
            insertScore(name, time);
        } else {
            double worstTime = timeToDouble(scores.get(scores.size() - 1).getTime());
            System.out.println("Current worst time: " + worstTime + ", New time: " + time);
            if (time < worstTime) {
                System.out.println("Replacing worst score");
                deleteScores(worstTime);
                insertScore(name, time);
            } else {
                System.out.println("Score not good enough to be inserted");
            }
        }
    }
    
    private void sortScores(ArrayList<Score> scores) {
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score t, Score t1) {
                double time1 = timeToDouble(t.getTime());
                double time2 = timeToDouble(t1.getTime());
                return Double.compare(time1, time2);
            }
        });
    }
    
    private void insertScore(String name, double time) throws SQLException {
        
        insertStatement.setString(1, name);
        insertStatement.setDouble(2, time);
        int rowsAffected = insertStatement.executeUpdate();
        if (rowsAffected != 1) {
            throw new SQLException("Insert failed, " + rowsAffected + " rows affected");
        }
        
    }
    
    private void deleteScores(double time) throws SQLException {
       
        deleteStatement.setDouble(1, time);
        deleteStatement.executeUpdate();
    }
    
    public void closeResources() {
        try {
            if (insertStatement != null) insertStatement.close();
            if (deleteStatement != null) deleteStatement.close();
            if (connection != null) connection.close();
            
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}