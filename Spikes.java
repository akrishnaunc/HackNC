package dungeonmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Spikes extends Space {

    public static BufferedImage spikes;

    public Spikes() {
        blocked = true;
    }

    @Override
    public void paint(Graphics g, int x, int y, int w, int h) {
        super.paint(g, x, y, w, h);
        g.drawImage(spikes, x, y, x + w, y + h, 0, 0, spikes.getWidth(), spikes.getHeight(), null);
    }
}
