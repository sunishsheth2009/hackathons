import java.util.HashMap;
import java.util.ArrayList;

public class Team {

    private String name;
    private String abbr;
    private HashMap<String, String> stats;
    private Schedule sched;
    private int score;

    public Team(String s) {
        name = s;
        abbr = "default"; // fix this
        stats = new HashMap<String, String>();
        sched = new Schedule();
        score = 0;
    }

    public int score() {
        return score;
    }

    public void add(String stat, String value) {
        stats.put(stat, value);
    }

    public String get(String stat) {
        return stats.get(stat);
    }

}
