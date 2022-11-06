package dungeonmaze;

public class LBPlayer implements Comparable {

    private final String name;
    private final Integer score;

    public LBPlayer(String n, int s) {
        name = n;
        score = s;
    }

    @Override
    public int compareTo(Object o) {
        return -score.compareTo(((LBPlayer) o).score);
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

}
