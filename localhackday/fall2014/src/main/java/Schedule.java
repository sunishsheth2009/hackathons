import java.util.ArrayList;

public class Schedule {

    private ArrayList<Team> opponents;
    private int strength;

    public Schedule() {
        opponents = new ArrayList<Team>();
        strength = 0;
    }

    public void add(Team team) {
        opponents.add(team);
    }

    public void sos() {
        for(Team team : opponents) {
            strength += team.score();
        }
        strength /= opponents.size();
    }

}
