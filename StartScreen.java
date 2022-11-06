package dungeonmaze;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class StartScreen extends JPanel {

    public static BufferedImage background;
    public static Font font, titleFont, smallFont;
    private JButton[] buttons;
    private JLabel title;
    private JTextField name, name2;
    private JLabel header;
    private JTextArea instructions;
    private String single = " - Player: This is you. Use WASD or arrow keys to\n   move. You also have a cloak to hide from the boss\n - but it'll cost you some score to use.\n   Press Space to toggle.\n\n\n - Spikes: You can go through them, but you'll get\n   hurt and lose a life.\n\n\n - Enemy: Some shoot arrows, some have swords. \n   Can't kill 'em without"
            + " tools. If you try,\n - you'll lose a life. After the player loads in to \n   a level, they'll start moving after 3 seconds.\n\n\n - Tools: This is how you kill an enemy. You can\n   only have one at a time though.\n\n\n - Boss: He'll chase you around the map. If he\n   "
            + "touches you, you'll disintegrate.";
    private String multi = " - Player: These are you players. Use WASD to move\n   the player 1 (blue) and arrow keys to move\n - player 2 (pink) move. You also have a cloak to\n   hide from the boss but it'll cost you some score to use.\n - Press Space to toggle.\n\n\n - Spikes: You can go through them, but you'll get\n   hurt and lose a life.\n\n\n - Enemy: Some shoot arrows, some have swords. \n   Can't kill 'em without"
            + " tools. If you try,\n - you'll lose a life. After the player loads in to \n   a level, they'll start moving after 3 seconds.\n\n\n - Tools: This is how you kill an enemy. You can\n   only have one at a time though.\n\n\n - Boss: He'll chase you around the map. If he\n   "
            + "touches either player, you'll disintegrate.";

    private BufferedImage[] entities = new BufferedImage[5];

    public StartScreen() {
        setComponents();
        createIS();
        entities[0] = Player.player;
        entities[1] = Spikes.spikes;
        entities[2] = Enemy.enemyRight;
        entities[3] = Tools.tools;
        entities[4] = Boss.bossRight;
    }

    private void setComponents() {
        setLayout(null);

        MouseAdapter hover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton) e.getSource()).setForeground(Color.white);
            }
        };

        buttons = new JButton[3];
        buttons[0] = new JButton("Play");
        buttons[1] = new JButton("Quit");
        buttons[2] = new JButton("Singleplayer");
        for (JButton b : buttons) {
            add(b);
            b.setFont(font);
            b.setForeground(Color.white);
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.addMouseListener(hover);
        }

        buttons[0].setBounds(250, 600, 300, 100);
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttons[2].getText().equalsIgnoreCase("Singleplayer")) {
                    DungeonMaze.setPanel("GAME");
                    Player.setName(name.getText());
                } else {
                    DungeonMaze.setPanel("TEAMGAME");
                    Player.setName(name.getText() + " & " + name2.getText());
                }
            }
        });

        buttons[1].setBounds(250, 750, 300, 100);
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttons[2].setBounds(150, 450, 500, 100);
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttons[2].getText().equalsIgnoreCase("Singleplayer")) {
                    buttons[2].setText("Multiplayer");
                    instructions.setText(multi);
                    name2.setVisible(true);
                } else {
                    buttons[2].setText("Singleplayer");
                    instructions.setText(single);
                    name2.setVisible(false);
                }
            }
        });
        
        buttons[2].addKeyListener(new KeyAdapter() {
            

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
            }
        });

        title = new JLabel("Mazescape");
        title.setFont(titleFont);
        title.setForeground(Color.white);
        title.setBounds(0, 50, 800, 100);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title);

        name = new JTextField("PLAYER ONE");
        name.setFont(font);
        name.setForeground(Color.green);
        //name.setFocusable(true);
        name.setHorizontalAlignment(JTextField.CENTER);
        name.setBounds(200, 200, 400, 100);
        name.setOpaque(false);
        name.setBorder(null);
        name.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                name.setText("");
                name.setForeground(Color.cyan);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (name.getText().equals("PLAYER ONE") || name.getText().equals("")) {
                    name.setText("PLAYER ONE");
                    name.setForeground(Color.green);
                }
                DungeonMaze.giveFocus();
            }

        });

        name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (name.getText().length() >= 20 || (!(Character.isAlphabetic(e.getKeyChar())) && e.getKeyChar() != ' ')) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
            }
        });
        add(name);

        name2 = new JTextField("PLAYER TWO");
        name2.setFont(font);
        name2.setForeground(Color.green);
        //name2.setFocusable(true);
        name2.setHorizontalAlignment(JTextField.CENTER);
        name2.setBounds(200, 325, 400, 100);
        name2.setOpaque(false);
        name2.setBorder(null);
        name2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                name2.setText("");
                name2.setForeground(Color.cyan);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (name2.getText().equals("PLAYER TWO") || name2.getText().equals("")) {
                    name2.setText("PLAYER TWO");
                    name2.setForeground(Color.green);
                }
                DungeonMaze.giveFocus();
            }

        });

        name2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (name2.getText().length() >= 20 || (!(Character.isAlphabetic(e.getKeyChar())) && e.getKeyChar() != ' ')) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
            }
        });
        add(name2);

        name2.setVisible(false);

    }

    private void createIS() {
        header = new JLabel();

        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setBounds(Game.width / 2 + 200, 0, 400, 200);
        header.setText("Instructions");
        add(header);
        header.setFont(StartScreen.font);
        header.setForeground(Color.white);
        header.setOpaque(false);

        instructions = new JTextArea();

        instructions.setBounds(Game.width / 2 + 133, 155, 800, Game.height);
        add(instructions);
        instructions.setFont(smallFont);
        instructions.setEditable(false);
        instructions.setForeground(Color.white);
        instructions.setOpaque(false);
        instructions.setText(single);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = 32;
        int h = 32;
        for (int i = 0; i < Game.width / w; i++) {
            for (int j = 0; j < (Game.height + 64) / h; j++) {
                g.drawImage(StartScreen.background, i * w, j * h, i * w + w, j * h + h, 0, 0, StartScreen.background.getWidth(), StartScreen.background.getHeight(), null);
            }
        }

        int y = 150;
        for (BufferedImage i : entities) {
            g.drawImage(i, Game.width / 2 + 100, y, Game.width / 2 + 100 + w, y + h, 0, 0, i.getWidth(), i.getHeight(), null);
            if (i == Player.player) {
                y += 40;
                if (instructions.getText().equals(single)) {
                    g.drawImage(Player.ghostRight, Game.width / 2 + 100, y, Game.width / 2 + 100 + w, y + h, 0, 0, Player.ghostRight.getWidth(), Player.ghostRight.getHeight(), null);
                }

                if (instructions.getText().equals(multi)) {
                    g.drawImage(Player.player2, Game.width / 2 + 100, y, Game.width / 2 + 100 + w, y + h, 0, 0, Player.player2.getWidth(), Player.player2.getHeight(), null);
                    y += 40;
                    g.drawImage(Player.ghostRight, Game.width / 2 + 100, y, Game.width / 2 + 100 + w, y + h, 0, 0, Player.ghostRight.getWidth(), Player.ghostRight.getHeight(), null);
                    y -= 20;
                }
            }
            if (i == Enemy.enemyRight) {
                y += 40;
                g.drawImage(Enemy.bowEnemyRight, Game.width / 2 + 100, y, Game.width / 2 + 100 + w, y + h, 0, 0, Enemy.bowEnemyRight.getWidth(), Enemy.bowEnemyRight.getHeight(), null);
            }
            y += 80;

        }

    }

}
