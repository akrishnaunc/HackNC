package dungeonmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Enemy extends Space {

    public static BufferedImage enemyLeft, enemyRight, bowEnemyLeft, bowEnemyRight;
    private int movementTimer, bowTimer;
    private boolean left;
    private final boolean hasBow;

    public Enemy(int r, int c, boolean hasBow) {
        super(r, c);
        blocked = true;
        left = true;
        this.hasBow = hasBow;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isLeft() {
        return left;
    }
    
    public boolean update(int frameCount) {
        if (frameCount >= 30) {
            movementTimer--;
            if (movementTimer <= 0) {
                movementTimer = (int) (Math.random() * 11) + 10;
                return true;
            }
        }
        return false;
    }
    
    public boolean useBow(int frameCount) {
        if (!hasBow) {
            return false;
        }
        
        if (frameCount >= 35) {
            bowTimer--;
            if (bowTimer <= 0) {
                bowTimer = (int) (Math.random() * 11) + 10;
                return true;
            }
        }
        return false;
    }
   

    public void paint(Graphics g, int x, int y, int w, int h) {
        super.paint(g, x, y, w, h);
        BufferedImage i;
        if (hasBow && left)
            i = bowEnemyLeft;
        else if (hasBow && !left)
            i = bowEnemyRight;
        else if (!hasBow && left)
            i = enemyLeft;
        else 
            i = enemyRight;

        g.drawImage(i, x, y, x + w, y + h, 0, 0, i.getWidth(), i.getHeight(), null);
    }
}
