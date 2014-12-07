import java.util.ArrayList;

public class Schedule {

    private ArrayList<Game> games;

    public Schedule() {
        games = new ArrayList<Game>();
    }

    public void add(Game g) {
         games.add(g);
    }

    public void print() {
        for(Game g : games) {
            g.print();
        }
    }

}
