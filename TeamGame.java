package dungeonmaze;

import java.awt.*;
import javax.swing.*;

public class TeamGame extends JPanel {

    private TeamLevel[] levels;
    private static boolean newLevel;
    private static int currentLevel;
    private boolean pressedWASD, pressedArrows;
    private int codeWASD, codeArrows;
    private int frames;

    public TeamGame() {

        levels = new TeamLevel[5];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new TeamLevel(i + 1);
        }

        pressedArrows = false;
        pressedWASD = false;
        newLevel = false;

        currentLevel = 1;
    }

    public void update() {

        levels[currentLevel - 1].update(frames);

        if (pressedWASD) {
            levels[currentLevel - 1].move(codeWASD);
        }
        if (pressedArrows) {
            levels[currentLevel - 1].move(codeArrows);
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

            switch (code) {
                case 37:
                case 38:
                case 39:
                case 40:
                    pressedArrows = pressed;
                    codeArrows = code;
                    break;
                case 65:
                case 87:
                case 68:
                case 83:
                    pressedWASD = pressed;
                    codeWASD = code;
                    break;
            }

        }
    }

    public static void nextLevel() {
        currentLevel++;
        newLevel = true;
    }
}
