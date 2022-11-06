package dungeonmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Boss extends Space {

    public static BufferedImage boss, bossRight;
    private boolean left;

    public Boss(int r, int c) {
        super(r, c);
        blocked = true;
        left = true;
    }
    
    public void setLeft(boolean left) {
        this.left = left;
    }

    @Override
    public void paint(Graphics g, int x, int y, int w, int h) {
        super.paint(g, x, y, w, h);
        if (left) {
            g.drawImage(boss, x, y, x + w, y + h, 0, 0, boss.getWidth(), boss.getHeight(), null);
        } else {
            g.drawImage(bossRight, x, y, x + w, y + h, 0, 0, boss.getWidth(), boss.getHeight(), null);
        }
    }
}
