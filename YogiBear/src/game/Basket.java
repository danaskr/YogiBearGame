package game;

/**
 *
 * @author saker
 */
public class Basket extends Sprite {
    private static final String  IMAGE_PATH = "src/images/basket.png";
    
    public Basket(int x, int y, int width, int height) {
        super(x, y, width, height, IMAGE_PATH);
    }
   
    

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    
    
}
