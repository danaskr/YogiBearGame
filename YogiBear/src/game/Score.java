package game;

/**
 *
 * @author saker
 */
public class Score {
    private String name;
    private String time; //time as "ss:ms" string
    
    public Score(String name, String time) {
        this.name = name;
        this.time = time;
    }
    
    public String getName() {
        return name;
    }
    
    public String getTime() {
        return time;
    }
}
