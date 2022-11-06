package dungeonmaze;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends Space {

    public static BufferedImage wall;

    public Wall() {
        blocked = true;
    }

    @Override
    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(wall, x, y, x + w, y + h, 0, 0, wall.getWidth(), wall.getHeight(), null);
    }
}
