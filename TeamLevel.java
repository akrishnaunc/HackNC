package dungeonmaze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TeamLevel {

    private Space[][] board;
    private static Player player;
    private static Player player2;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Arrow> arrows;
    private Boss boss;
    private final int level;
    private final Space space = new Space();
    private final Wall wall = new Wall();
    private final Spikes spike = new Spikes();
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private Space preplace = space;
    private Space breplace = space;
    private boolean pSpace = false;
    public static BufferedImage arrow;
    private static int frames;
    private boolean firstMove = true;

    public TeamLevel(int level) {
        this.level = level;
        enemies = new ArrayList();
        arrows = new ArrayList();
        generateBoard(level);
    }

    private void generateBoard(int level) {
        board = new Space[16][30];
        setWalls();

        player = new Player(startRow, startCol);
        player2 = new Player(startRow, startCol, 2);
        board[startRow][startCol] = player;

        switch (level) {
            case 1:
                addSpikes();
                break;
            case 2:
                addToolsAndEnemies();
                break;
            case 3:
                addSpikes();
                addToolsAndEnemies();
                break;
            case 4:
                addBoss();
                break;
            case 5:
                addSpikes();
                addToolsAndEnemies();
                addBoss();
                break;
            default:
                break;
        }

        frames = 10 * 60 * 5;
    }

    public void init() {
        player.changeLoc(startRow, startCol);
        player.newLevel();
        player2.changeLoc(startRow, startCol);
        player2.newLevel();
        board[startRow][startCol] = player;
    }

    private void setWalls() {

        ArrayList<Space> cells = new ArrayList();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Space s = new Space(i, j);
                board[i][j] = s;
                board[i][j].setBlocked(true);
                cells.add(s);
            }
        }
        startRow = (board.length - 1) / 2;
        startCol = 0;
        Space start = board[startRow][startCol];
        start.setBlocked(false);
        cells.remove(start);

        endRow = startRow;
        endCol = board[0].length - 1;
        Space end = board[endRow][endCol];
        end.setBlocked(false);
        cells.remove(end);

        while (!pathExists(start, end)) {
            Space s = cells.get((int) (Math.random() * cells.size()));
            s.setBlocked(false);
            cells.remove(s);
        }

        fillGaps();
        ensureWalls();
        fillGaps();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].blocked()) {
                    board[i][j] = wall;
                } else {
                    board[i][j] = new Space(i, j);
                }
            }
        }
    }

    private void ensureWalls() {
        ArrayList<Space> spots = new ArrayList();

        double numFilled = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Space s = board[row][col];
                if (!s.blocked() && !sameLocation(startRow, startCol, row, col) && !sameLocation(endRow, endCol, row, col)) {
                    spots.add(s);
                } else {
                    numFilled++;
                }
            }
        }

        while (numFilled < (board.length * board[0].length / 2)) {

            Space s = spots.get((int) (Math.random() * spots.size()));
            board[s.getRow()][s.getCol()].setBlocked(true);
            if (!pathExists(board[startRow][startCol], board[endRow][endCol])) {
                board[s.getRow()][s.getCol()].setBlocked(false);
            }

            spots.remove(s);
            numFilled++;
        }
    }

    private void fillGaps() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Space s = board[row][col];
                Space start = board[startRow][startCol];
                if (!s.blocked() && !pathExists(start, s)) {
                    s.setBlocked(true);
                }
            }
        }
    }

    private boolean pathExists(Space a, Space b) {
        ArrayList<Space> stack = new ArrayList();
        stack.add(a);

        boolean[][] visited = new boolean[board.length][board[0].length];

        while (!stack.isEmpty()) {

            int row = stack.get(stack.size() - 1).getRow();
            int col = stack.get(stack.size() - 1).getCol();

            visited[row][col] = true;

            if (row == b.getRow() && col == b.getCol()) {
                return true;
            }

            ArrayList<Space> neighbors = new ArrayList();
            if (row + 1 < board.length && !board[row + 1][col].blocked() && !visited[row + 1][col]) {
                neighbors.add(board[row + 1][col]);
            }
            if (row - 1 >= 0 && !board[row - 1][col].blocked() && !visited[row - 1][col]) {
                neighbors.add(board[row - 1][col]);
            }
            if (col + 1 < board[0].length && !board[row][col + 1].blocked() && !visited[row][col + 1]) {
                neighbors.add(board[row][col + 1]);
            }
            if (col - 1 >= 0 && !board[row][col - 1].blocked() && !visited[row][col - 1]) {
                neighbors.add(board[row][col - 1]);
            }

            if (neighbors.isEmpty()) {
                stack.remove(stack.size() - 1);
            } else {
                stack.add(neighbors.get((int) (Math.random() * neighbors.size())));
            }
        }

        return false;
    }

    public void update(int frameCount) {
        updateEnemies(frameCount);
        updateArrows();
        if (player.getLives() == 0) {
            EndScreen.setMessage("You Died!");
            endScreen();
        }

        if (!player.getDisplay() && !player2.getDisplay()) {
            if (level == 5) {
                player.setTimeScore(frames / 10);

                EndScreen.setMessage("You Escaped!");
                endScreen();
            } else {
                player.changeScore(500);
                player.setDisplay(true);
                player2.setDisplay(true);
                TeamGame.nextLevel();
                if (level == 3) {
                    SoundManager.playBackgroundMusic(true);
                }
            }
        }
    }

    private void updateEnemies(int frameCount) {
        for (Enemy e : enemies) {

            if (e.useBow(frameCount)) {
                int w = Game.width / board[0].length;
                int h = Game.height / (board.length + 2);
                int x;
                if (e.isLeft()) {
                    x = e.getCol() * w - 16;
                } else {
                    x = e.getCol() * w + 80;
                }
                arrows.add(new Arrow(x, (e.getRow() + 2) * h + 16, e.isLeft()));
            }

            if (e.update(frameCount)) {
                int r = e.getRow();
                int c = e.getCol();

                ArrayList<Space> neighbors = new ArrayList();
                if (!unableMoveEnemy(r + 1, c)) {
                    neighbors.add(new Space(r + 1, c));
                }
                if (!unableMoveEnemy(r - 1, c)) {
                    neighbors.add(new Space(r - 1, c));
                }
                if (!unableMoveEnemy(r, c + 1)) {
                    neighbors.add(new Space(r, c + 1));
                }
                if (!unableMoveEnemy(r, c - 1)) {
                    neighbors.add(new Space(r, c - 1));
                }

                if (!neighbors.isEmpty()) {
                    Space s = neighbors.get((int) (Math.random() * neighbors.size()));
                    board[e.getRow()][e.getCol()] = space;

                    if (s.getCol() == e.getCol() + 1) {
                        e.setLeft(false);
                    } else if (s.getCol() == e.getCol() - 1) {
                        e.setLeft(true);
                    }
                    e.changeLoc(s.getRow(), s.getCol());
                    board[s.getRow()][s.getCol()] = e;
                }
            }
        }
    }

    public void move(int code) {
        Player p = null;
        Player op = null;
        switch (code) {
            case 37:
            case 38:
            case 39:
            case 40:
                if (!player2.getDisplay()) {
                    return;
                }

                if (firstMove) {
                    preplace = player;
                }
                p = player2;
                op = player;

                break;
            case 65:
            case 87:
            case 68:
            case 83:
                if (!player.getDisplay()) {
                    return;
                }
                if (firstMove) {
                    preplace = player2;
                }
                p = player;
                op = player2;
                break;

        }

        int newpRow = p.getRow(), newpCol = p.getCol();
        int newbRow = 0, newbCol = 0;
        if (boss != null) {
            newbRow = boss.getRow();
            newbCol = boss.getCol();
        }
        char pdirection, bdirection = ' ';
        switch (code) {
            case 37:
            case 65:
                newpCol--;
                pdirection = 'l';
                p.setLeft(true);
                break;
            case 38:
            case 87:
                newpRow--;
                pdirection = 'u';
                break;
            case 39:
            case 68:
                if (p.getRow() == endRow && p.getCol() == endCol) {
                    p.setDisplay(false);
                    board[p.getRow()][p.getCol()] = space;
                    p.changeLoc(1000, 1000);
                }
                newpCol++;
                pdirection = 'r';
                p.setLeft(false);
                break;
            case 40:
            case 83:
                newpRow++;
                pdirection = 'd';
                break;
            default:
                return;
        }

        if (boss != null && !p.isUsingCloak()) {
            if (newpRow > boss.getRow() && (pdirection == 'u' || pdirection == 'd')) {
                newbRow++;
                bdirection = 'd';
            } else if (newpCol > boss.getCol() && (pdirection == 'l' || pdirection == 'r')) {
                newbCol++;
                bdirection = 'r';
                boss.setLeft(false);
            } else if (newpRow < boss.getRow() && (pdirection == 'u' || pdirection == 'd')) {
                newbRow--;
                bdirection = 'u';
            } else if (newpCol < boss.getCol() && (pdirection == 'l' || pdirection == 'r')) {
                newbCol--;
                bdirection = 'l';
                boss.setLeft(true);
            }
        }
        if (unableMove(newpRow, newpCol) || board[newpRow][newpCol] instanceof Player) {
            return;
        }

        if (firstMove) {
            firstMove = false;
        }

        if (board[newpRow][newpCol] instanceof Spikes) {
            p.loseLife();
        } else if (board[newpRow][newpCol] instanceof Enemy) {
            if (p.checkTools()) {
                p.useTools();
                p.changeScore(100);
                SoundManager.playSound("KILL");
                enemies.remove((Enemy) board[newpRow][newpCol]);
                pSpace = true;
            } else {
                p.loseLife();
                newpRow = p.getRow();
                newpCol = p.getCol();
            }
        } else if (board[newpRow][newpCol] instanceof Tools) {
            if (!p.checkTools()) {
                p.giveTools();
                pSpace = true;
            }
        }
        if (level > 3) {
            if (((op.getRow() == newbRow && op.getCol() == newbCol) || pdirection != bdirection) && (board[newpRow][newpCol] instanceof Boss || board[newbRow][newbCol] instanceof Player || (newpRow == newbRow && newpCol == newbCol))) {
                while (p.getLives() > 0) {
                    p.loseLife();
                }
            }
        }

        if (p.isUsingCloak() && !sameLocation(p.getRow(), p.getCol(), newpRow, newpCol)) {
            p.changeScore(-100);
        }

        if (p.getScore() <= 0 && p.isUsingCloak()) {
            p.changeUsingCloak();
        }
        
        if (preplace instanceof Player) {
        board[p.getRow()][p.getCol()] = preplace;
        preplace = null;
        }
        else
            board[p.getRow()][p.getCol()] = p.getPreplace();

        if (pSpace) {
            p.setPreplace(space);
            pSpace = false;

        } else {
            p.setPreplace(board[newpRow][newpCol]);
        }
        board[newpRow][newpCol] = p;
        p.changeLoc(newpRow, newpCol);
        
                System.out.println("end move");

        
        if (unableMove(newbRow, newbCol) || board[newbRow][newbCol] instanceof Enemy) {
            return;
        }
        if (boss != null) {
            board[boss.getRow()][boss.getCol()] = breplace;
            breplace = board[newbRow][newbCol];
            board[newbRow][newbCol] = boss;
            boss.changeLoc(newbRow, newbCol);
        }
    }

    public void updateArrows() {
        for (int i = arrows.size() - 1; i >= 0; i--) {
            Space s = arrows.get(i).update(board);
            if (s instanceof Player) {
                Player p = (Player) s;
                if (p.checkTools()) {
                    p.useTools();
                    SoundManager.playSound("TOOL");
                } else {
                    p.loseLife();
                }
            }
            if (s != null) {
                arrows.remove(i);
            }
        }
    }

    private int getNeighborCount(int row, int col) {
        int neighborCount = 0;
        if (row + 1 == board.length || board[row + 1][col].blocked()) {
            neighborCount++;
        }
        if (row - 1 == -1 || board[row - 1][col].blocked()) {
            neighborCount++;
        }
        if (col + 1 == board[0].length || board[row][col + 1].blocked()) {
            neighborCount++;
        }
        if (col - 1 == -1 || board[row][col - 1].blocked()) {
            neighborCount++;
        }

        return neighborCount;
    }

    public boolean sameLocation(int r1, int c1, int r2, int c2) {
        return r1 == r2 && c1 == c2;
    }

    private void addSpikes() {
        // place in any space where there are at least two blocked neighbors, but a path must exist between start and end

        ArrayList<Space> spots = new ArrayList();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Space s = board[row][col];
                if (!s.blocked() && !sameLocation(startRow, startCol, row, col) && !sameLocation(endRow, endCol, row, col)) {
                    if (getNeighborCount(row, col) == 2) {
                        spots.add(s);
                    }
                }
            }
        }

        int numAvailable = 35;
        if (spots.size() < numAvailable) {
            numAvailable = spots.size();
        }

        for (int i = 0; i < numAvailable; i++) {

            Space s = spots.get((int) (Math.random() * spots.size()));
            board[s.getRow()][s.getCol()] = spike;
            if (!pathExists(board[startRow][startCol], board[endRow][endCol])) {
                i--;
                numAvailable--;
                board[s.getRow()][s.getCol()] = new Space(s.getRow(), s.getCol());
            }

            spots.remove(s);
        }

    }

    private void addToolsAndEnemies() {
        // place in any unblocked space, but a path must exist between start and tools

        ArrayList<Space> spots = new ArrayList();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Space s = board[row][col];
                if (!s.blocked() && !sameLocation(startRow, startCol, row, col)
                        && !sameLocation(endRow, endCol, row, col) && pathExists(board[startRow][startCol], board[row][col])) {
                    spots.add(s);
                }
            }
        }

        int numPlacements = 5;
        if (spots.size() < numPlacements) {
            numPlacements = spots.size();
        }

        while (numPlacements > 0 && spots.size() > 1) {

            Space e = spots.get((int) (Math.random() * spots.size()));
            board[e.getRow()][e.getCol()] = new Enemy(e.getRow(), e.getCol(), (int) (Math.random() * 2) == 1);
            spots.remove(e);

            Space t = spots.get((int) (Math.random() * spots.size()));
            board[t.getRow()][t.getCol()] = new Tools(t.getRow(), t.getCol());

            if (!pathExists(board[startRow][startCol], board[t.getRow()][t.getCol()])) {
                board[e.getRow()][e.getCol()] = new Space(e.getRow(), e.getCol());
                board[t.getRow()][t.getCol()] = new Space(t.getRow(), t.getCol());

            } else {
                enemies.add((Enemy) board[e.getRow()][e.getCol()]);
                spots.remove(t);
                numPlacements--;
            }

        }

    }

    private void addBoss() {
        // place at end
        boss = new Boss(endRow, endCol);
        board[endRow][endCol] = boss;
    }

    private boolean unableMove(int row, int col) {
        return row < 0 || col < 0 || row > board.length - 1 || col > board[0].length - 1 || board[row][col] instanceof Wall;
    }

    private boolean unableMoveEnemy(int row, int col) {
        return unableMove(row, col) || board[row][col] instanceof Spikes || board[row][col] instanceof Boss || board[row][col] instanceof Player || board[row][col] instanceof Tools || board[row][col] instanceof Enemy;
    }

    public void paint(Graphics g) {

        int rows = board.length;
        int cols = board[0].length;
        int w = Game.width / cols;
        int h = Game.height / (rows + 2);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * w;
                int y = (h * 2) + (i * h);
                board[i][j].paint(g, x, y, w, h);
            }
        }
        int x = endCol * w;
        int y = (h * 2) + (endRow * h);
        if (!(board[endRow][endCol] instanceof Player) && !(board[endRow][endCol] instanceof Boss)) {
            g.drawImage(arrow, x, y, x + w, y + h, 0, 0, arrow.getWidth(), arrow.getHeight(), null);
        }
        if (frames > 0) {
            frames--;
        }

        for (Arrow a : arrows) {
            a.paint(g);
        }
    }

    private void endScreen() {
        EndScreen.setScores(player.getScore(), player.getTimeScore());
        EndScreen.addToFile(player.getName(), player.getTotalScore());
        EndScreen.readFile();
        DungeonMaze.setPanel("ENDSCREEN");
    }

    public void hidePlayer() {
        if (player.getScore() > 0) {
            player.changeUsingCloak();
            if (player.getDisplay()) {
                board[player.getRow()][player.getCol()] = player;
            }
            if (player2.getDisplay()) {
                board[player2.getRow()][player2.getCol()] = player2;
            }
        }
    }
}
