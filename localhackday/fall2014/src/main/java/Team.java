import java.util.HashMap;

public class Team implements Comparable<Team>{

    private String name;
    private HashMap<String, String> stats;
    private Schedule sched;
    private double winningness;

    public Team(String s) {
        name = s;
        stats = new HashMap<String, String>();
        sched = new Schedule();
        winningness = 0;
    }

    public int compareTo(Team t) { // eventually incorporate all stats
        if(this.winningness - t.winningness == 0) {
            return 0;
        } else if(this.winningness < t.winningness) {
            return 1;
        }
        return -1;
    }

    public String name() {
        return name;
    }

    public Schedule getSched() {
        return sched;
    }

    public void add(String stat, String value) {
        stats.put(stat, value);
    }

    public String get(String stat) {
        return stats.get(stat);
    }

    public void print() {
        System.out.println("Team: " + name);
        System.out.println("Winningness: " + winningness);
    }

    public double getWinningness() {
        return winningness;
    }

    public void crunch() {
        winningness();
    }

    private void winningness() {
        double rpi = Double.parseDouble(stats.get("Relative Percent Index"));
        double sos = Double.parseDouble(stats.get("Strength of Schedule"));
        double ewp = Double.parseDouble(stats.get("Expected Winning Percentage"));
        double wp = Double.parseDouble(stats.get("Win Percentage"));
        double p = Double.parseDouble(stats.get("Points"));
        double pa = Double.parseDouble(stats.get("Points Against"));
        double pr = Double.parseDouble(stats.get("Power Rank"));
        String lastTen = stats.get("Last 10 Games");
        double won = Integer.parseInt(lastTen.split("-")[0]);
        double lost = Integer.parseInt(lastTen.split("-")[1]);
        winningness = ((rpi+sos)/2) + (Math.abs(ewp-wp) * (ewp+wp)) * (p/pa) + (1-(pr/30)) + (won-lost)/10;
    }

}
