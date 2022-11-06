package dungeonmaze;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
1st level - title
2nd level - text field
3rd level - play
4th level - inst
5th level - quit
 */
public class DungeonMaze {

    private static JFrame f;
    private static CardLayout cl;
    private static JPanel cards;
    private static String currentLayout;
    private static StartScreen ss;
    private static Game g;
    private static TeamGame team;
    private static EndScreen es;
    private static Timer t;

    public DungeonMaze() {
        loadContent();
        Toolkit tk = Toolkit.getDefaultToolkit();

        Game.width = tk.getScreenSize().width;
        Game.height = tk.getScreenSize().height;
        System.out.println(Game.width + " " + Game.height);
        f = new JFrame();
        f.setTitle("Mazescape");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setUndecorated(true);
        init();

        t = new Timer(1000 / 10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (currentLayout) {
                    case "START":
                        ss.repaint();
                        break;
                    case "GAME":
                        g.repaint();
                        g.update();
                        break;
                    case "TEAMGAME":
                        team.repaint();
                        team.update();
                        break;
                    case "ENDSCREEN":
                        es.repaint();
                        break;
                    default:
                        break;
                }
            }
        });

        t.start();

        f.setVisible(true);
    }

    public static void init() {
        if (cards != null) {
            f.remove(cards);
        }

        ss = new StartScreen();
        g = new Game();
        team = new TeamGame();
        es = new EndScreen();

        cl = new CardLayout();
        cards = new JPanel(cl);

        cards.setFocusable(true);
        cards.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                } else if (e.getKeyChar() == 'm') {
                    SoundManager.toggleMute();
                } else {
                    if (currentLayout.equals("GAME")) {
                        g.press(true, e.getKeyCode());
                    }
                    
                    else if (currentLayout.equals("TEAMGAME")) {
                        team.press(true, e.getKeyCode());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (currentLayout.equals("GAME")) {
                    g.press(false, e.getKeyCode());
                }
                else if (currentLayout.equals("TEAMGAME")) {
                    team.press(false, e.getKeyCode());
                }
            }
        });

        cards.add(ss, "START");
        cards.add(g, "GAME");
        cards.add(team, "TEAMGAME");
        cards.add(es, "ENDSCREEN");
        setPanel("START");
        f.add(cards);
        f.pack();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        
        SoundManager.playBackgroundMusic(false);
    }

    private void loadContent() {

        try {
            StartScreen.background = ImageIO.read(new File("dependencies/basalt_side.png"));
            Level.arrow = ImageIO.read(new File("dependencies/arrow.png"));
            TeamLevel.arrow = ImageIO.read(new File("dependencies/arrow.png"));
            Wall.wall = ImageIO.read(new File("dependencies/blackstone_top.png"));
            Space.space = ImageIO.read(new File("dependencies/spruce.png"));
            Player.player = ImageIO.read(new File("dependencies/player.png"));
            Player.playerLeft = ImageIO.read(new File("dependencies/player_left.png"));
            Player.playerWifTools = ImageIO.read(new File("dependencies/player_wif_tools.png"));
            Player.player2 = ImageIO.read(new File("dependencies/player2.png"));
            Player.player2Left = ImageIO.read(new File("dependencies/player2left.png"));
            Player.player2WifTools = ImageIO.read(new File("dependencies/player2wiftools.png"));
            Player.ghostLeft = ImageIO.read(new File("dependencies/ghost_left.png"));
            Player.ghostRight = ImageIO.read(new File("dependencies/ghost_right.png"));
            Player.heart = ImageIO.read(new File("dependencies/heart.png"));
            Tools.tools = ImageIO.read(new File("dependencies/tools.png"));
            Enemy.enemyLeft = ImageIO.read(new File("dependencies/skeleton.png"));
            Enemy.enemyRight = ImageIO.read(new File("dependencies/skeleton_right.png"));
            Enemy.bowEnemyLeft = ImageIO.read(new File("dependencies/bow.png"));
            Enemy.bowEnemyRight = ImageIO.read(new File("dependencies/bow_right.png"));
            Boss.boss = ImageIO.read(new File("dependencies/boss.png"));
            Boss.bossRight = ImageIO.read(new File("dependencies/boss_right.png"));
            Spikes.spikes = ImageIO.read(new File("dependencies/spikes.png"));
            Arrow.arrow = ImageIO.read(new File("dependencies/bow arrow.png"));
            Arrow.arrow_right = ImageIO.read(new File("dependencies/bow arrow_right.png"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("dependencies/joystix_monospace.ttf")));
            StartScreen.font = new Font("Joystix Monospace", Font.PLAIN, 40);
            StartScreen.titleFont = new Font("Joystix Monospace", Font.PLAIN, 72);
            EndScreen.lbFont = new Font("Joystix Monospace", Font.PLAIN, 20);
            StartScreen.smallFont = new Font("Joystix Monospace", Font.PLAIN, 16);
            EndScreen.font = new Font("Joystix Monospace", Font.PLAIN, 30);
            SoundManager.hurt = new File("dependencies/hurt.wav");
            SoundManager.killSound = new File("dependencies/killsound.wav");
            SoundManager.toolSound = new File("dependencies/toolsound.wav");
            SoundManager.background = new File("dependencies/background.wav");
            SoundManager.bossMusic = new File("dependencies/background2.wav");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void giveFocus() {
        cards.requestFocusInWindow();
    }

    public static void setPanel(String name) {
        currentLayout = name;
        if (name.equals("ENDSCREEN")) {
            es.updateComponent();
        }
        cl.show(cards, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DungeonMaze();
            }
        });
    }
}
