package dungeonmaze;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Arrow {

    public static BufferedImage arrow, arrow_right;
    private int x, y;
    private boolean left;
    private final int speed;

    public Arrow(int x, int y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
        speed = 8;
    }

    public Space update(Space[][] board) {

        int w = Game.width / board[0].length;
        int h = Game.height / (board.length + 2);
        
        int row = y / h - 2;
        if (row >= board.length) {
            row = board.length - 1;
        }
        int col = x / w;
        if (col >= board[0].length) {
            col = board[0].length - 1;
        }

        if (x < 0 || x > Game.width) {
            return new Wall();
        }

        if (board[row][col] instanceof Player|| board[row][col] instanceof Spikes || board[row][col] instanceof Boss || board[row][col] instanceof Tools || board[row][col] instanceof Wall || board[row][col] instanceof Enemy) {
            return board[row][col];
        }

        if (left) {
            x -= speed;
        } else {
            x += speed;
        }

        return null;
    }

    public void paint(Graphics g) {
        BufferedImage i;
        int sx, ex;
        if (left) {
            i = arrow;
            sx = x;
            ex = x + 32;
        } else {
            i = arrow_right;
            sx = x - 32;
            ex = x;
        }
        g.drawImage(i, sx, y, ex, y + 32, 0, 0, i.getWidth(), i.getHeight(), null);
    }

}
