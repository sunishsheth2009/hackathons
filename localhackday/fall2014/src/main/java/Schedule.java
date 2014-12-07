import java.util.ArrayList;

public class Schedule {

    private ArrayList<Game> games;
    private int strength;

    public Schedule() {
        games = new ArrayList<Game>();
        strength = 0;
    }

    public void add(Game g) {
         games.add(g);
    }

    public void sos() {
        for(Game g : games) {
            strength += g.opponent().score();
        }
        strength /= games.size();
    }

    public void print() {
        for(Game g : games) {
            g.print();
        }
    }

}
