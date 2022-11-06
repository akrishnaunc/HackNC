package dungeonmaze;

import java.awt.*;
import javax.swing.*;

public class Game extends JPanel {

    private Level[] levels;
    private static boolean newLevel;
    private static int currentLevel;
    public static int width, height;
    private boolean pressed;
    private int code;
    private int frames;

    public Game() {

        levels = new Level[5];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new Level(i + 1);
        }

        pressed = false;
        newLevel = false;

        currentLevel = 1;
    }

    public void update() {

        levels[currentLevel - 1].update(frames);

        if (pressed) {
            levels[currentLevel - 1].move(code);
        }

        if (newLevel) {
            frames = 0;
            levels[currentLevel - 1].init();
            newLevel = false;
        }

        frames++;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        levels[currentLevel - 1].paint(g);
    }

    public void press(boolean pressed, int code) {
        if (code == 32 && pressed) {
            levels[currentLevel - 1].hidePlayer();
        } else {
            this.pressed = pressed;
            this.code = code;
        }
    }

    public static void nextLevel() {
        currentLevel++;
        newLevel = true;
    }
}
