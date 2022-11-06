package dungeonmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tools extends Space {

    public static BufferedImage tools;

    public Tools(int r, int c) {
        super(r, c);
    }

    @Override
    public void paint(Graphics g, int x, int y, int w, int h) {
        super.paint(g, x, y, w, h);
        g.drawImage(tools, x, y, x + w, y + h, 0, 0, tools.getWidth(), tools.getHeight(), null);
    }
}
