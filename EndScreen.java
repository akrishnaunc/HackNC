package dungeonmaze;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;

public class EndScreen extends JPanel {

    private static JComponent[] components;
    public static Font font;
    static Font lbFont;
    private JLabel header;
    private JButton back;
    private JTextArea leaderboard;

    private static ArrayList<LBPlayer> lbplayers;

    public EndScreen() {
        setButtons();
        lbplayers = new ArrayList();
        setLeaderboard();
    }

    private void setButtons() {
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

        components = new JComponent[5];
        components[0] = new JButton("Quit");
        components[1] = new JLabel();
        components[2] = new JTextArea();
        components[3] = new JLabel();
        components[4] = new JButton("Replay");
        components[1].setBounds(200, 0, 400, 200);

        components[2].setBounds(150, 150, 500, 200);
        components[3].setBounds(150, 200, 450, 200);
        for (JComponent c : components) {
            add(c);
            c.setFont(StartScreen.font);
            c.setForeground(Color.white);
            c.setOpaque(false);
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setContentAreaFilled(false);
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.addMouseListener(hover);
            }
            if (c instanceof JTextArea)
                ((JTextArea) c).setEditable(false);
            if (c instanceof JLabel){
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setVerticalAlignment(SwingConstants.CENTER);
            }
        }
        components[2].setFont(lbFont);
        components[3].setFont(font);

        
        components[0].setBounds(200, 600, 400, 100);
        ((JButton) components[0]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        components[4].setBounds(200, 500, 400, 100);
        ((JButton) components[4]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DungeonMaze.init();
            }
        });

    }

    public static void setMessage(String msg) {
        ((JLabel) components[1]).setText(msg);

    }

    public static void setScores(int x, int y) {
        ((JTextArea) components[2]).setText("Maze Score:\t\t" + x + "\nBonus Timed Score: \t" + y);
        ((JLabel) components[3]).setText("Total Score: "+(x + y));
    }
    public void updateComponent() {
        String list = "";
        int max = 15;
        if (lbplayers.size() < max) {
            max = lbplayers.size();
        }
        for (int i = 0; i < max; i++) {
            Integer score = lbplayers.get(i).getScore();
            String line = lbplayers.get(i).getName();
            for (int j = 0; j < 8 - score.toString().length(); j++) {
                line += " ";
            }
            list += line + score + "\n";
        }
        
        leaderboard.setText(list);

    }

    public static void readFile() {
        try {
            File f = new File("leaderboard.txt");
            if (f.exists()) {
                Scanner s = new Scanner(f);
                String name;
                Integer score;
                while (s.hasNextLine()) {
                    name = s.nextLine();
                    score = Integer.parseInt(s.nextLine());
                    if (s.hasNextLine()) {
                        s.nextLine();
                    }
                    lbplayers.add(new LBPlayer(name, score));
                }
            } else {
                f.createNewFile();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collections.sort(lbplayers);
    }

    private void setLeaderboard() {
        setLayout(null);

        header = new JLabel();

        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setBounds(Game.width/2+200, 0, 400, 200);
        header.setText("Leaderboard");
        add(header);
        header.setFont(StartScreen.font);
        header.setForeground(Color.white);
        header.setOpaque(false);

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

        leaderboard = new JTextArea();

        leaderboard.setFont(lbFont);
        leaderboard.setEditable(false);
        leaderboard.setForeground(Color.white);
        leaderboard.setOpaque(false);

        leaderboard.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        leaderboard.setBounds(Game.width/2+50, 200, 550, 400);

        add(leaderboard);

    }

    public static void addToFile(String name, int score) {
        try {
            FileWriter fw = new FileWriter("leaderboard.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.append(name + "\n" + score + "\n\n");
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    }

}
