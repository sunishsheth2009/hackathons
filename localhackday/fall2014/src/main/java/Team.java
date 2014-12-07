import java.util.HashMap;

public class Team {

    private String name;
    private HashMap<String, String> stats;
    private Schedule sched;
    private int score;

    public Team(String s) {
        name = s;
        stats = new HashMap<String, String>();
        sched = new Schedule();
        score = 0;
    }

    public String name() {
        return name;
    }

    public Schedule getSched() {
        return sched;
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
