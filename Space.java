package dungeonmaze;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Space {

    public static BufferedImage space;
    protected boolean blocked;
    protected int row;
    protected int col;

    public Space() {
        blocked = false;
    }

    public Space(int r, int c) {
        row = r;
        col = c;

        blocked = false;
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        g.drawImage(space, x, y, x + w, y + h, 0, 0, space.getWidth(), space.getHeight(), null);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void changeLoc(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean blocked() {
        return blocked;
    }
}
