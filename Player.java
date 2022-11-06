package dungeonmaze;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Space {

    public static BufferedImage player, player2, playerLeft, player2Left, playerWifTools, player2WifTools, heart, ghostLeft, ghostRight;
    private static String name;

    private static int lives, score, timeScore;
    private static boolean usingCloak;
    private boolean left, hasTools;
    private int pnum = 1;
    private boolean display = true;
    private Space preplace = new Space();

    public Player(int r, int c) {
        super(r, c);
        lives = 3;
        score = 300;
        hasTools = false;
        blocked = false;
        usingCloak = false;
    }

    public Player(int r, int c, int a) {
        super(r, c);
        hasTools = false;
        blocked = false;
        pnum = a;
    }

    public static void setName(String s) {
        name = s;
    }

    public void giveTools() {
        hasTools = true;
        SoundManager.playSound("TOOL");
    }

    public void useTools() {
        hasTools = false;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean checkTools() {
        return hasTools;
    }

    public void loseLife() {
        lives -= 1;
        if (score >= 100) {
            score -= 100;
        }
        SoundManager.playSound("HURT");
    }

    public int getLives() {
        return lives;
    }

    public void changeScore(int n) {
        score += n;
    }

    public void setTimeScore(int timeScore) {
        this.timeScore = timeScore;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public boolean isLeft() {
        return left;
    }

    public int getTotalScore() {
        return score + timeScore;
    }

    public boolean isUsingCloak() {
        return usingCloak;
    }

    public void changeUsingCloak() {
        usingCloak = !usingCloak;
    }

    public void setDisplay(boolean d) {
        display = d;
    }

    public Space getPreplace() {
        System.out.println("got preplace as " + preplace);
        return preplace;
    }

    public void setPreplace(Space preplace) {
        this.preplace = preplace;
        System.out.println("set preplace to " + preplace);
    }
    
    

    @Override
    public void paint(Graphics g, int x, int y, int w, int h) {
        super.paint(g, x, y, w, h);
        BufferedImage p;
        if (usingCloak) {
            if (left) {
                p = ghostLeft;
            } else {
                p = ghostRight;
            }
        } else {
            if (pnum == 2) {
                if (hasTools) {
                    p = player2WifTools;
                } else if (left) {
                    p = player2Left;
                } else {
                    p = player2;
                }
            } else {
                if (hasTools) {
                    p = playerWifTools;
                } else if (left) {
                    p = playerLeft;
                } else {
                    p = player;
                }
            }
        }
        if (display) {
            g.drawImage(p, x, y, x + w, y + h, 0, 0, p.getWidth(), p.getHeight(), null);
        }

        for (int i = 0; i < Game.width / w; i++) {
            for (int j = 0; j < 2; j++) {
                g.drawImage(StartScreen.background, i * w, j * h, i * w + w, j * h + h, 0, 0, StartScreen.background.getWidth(), StartScreen.background.getHeight(), null);
            }
        }

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, (w * 5) + i * (w * 3), (h / 4), (w * 5) + i * (w * 3) + (w * 3 / 2), (h * 7 / 4), 0, 0, heart.getWidth(), heart.getHeight(), null);
        }

        g.setColor(Color.white);
        g.setFont(StartScreen.font);
        g.drawString("Score " + score, w * 20, h * 4 / 3);

    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void newLevel() {
        hasTools = false;
        left = false;
        usingCloak = false;
    }

    public boolean getDisplay() {
        return display;
    }

}
