import java.util.HashMap;

public class Team implements Comparable<Team>{

    private String name;
    private HashMap<String, String> stats;
    private Schedule sched;
    private double offense;
    private double defense;
    private double winningness;
    private double efficiency;
    private double secondary;
    private double overall;

    public Team(String s) {
        name = s;
        stats = new HashMap<String, String>();
        sched = new Schedule();
        winningness = 0;
        offense = 0;
        defense = 0;
        efficiency = 0;
        secondary = 0;
        overall = 0;
    }

    public int compareTo(Team t) { // eventually incorporate all stats
        if(this.overall - t.overall == 0) {
            return 0;
        } else if(this.overall < t.overall) {
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

    public Double getDoubStat(String stat) {
        return Double.parseDouble(stats.get(stat));
    }

    public void print() {
        System.out.println("Team: " + name);
        System.out.println("Overall: " + overall);
        System.out.println("Winningness: " + winningness);
        System.out.println("Offense: " + offense);
        System.out.println("Defense: " + defense);
        System.out.println("Secondary: " + secondary);
        System.out.println("Efficiency: " + efficiency);
    }

    public double getOffense() {
        return offense;
    }

    public double getDefense() {
        return defense;
    }

    public double getWinningness() {
        return winningness;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public double getSecondary() {
        return secondary;
    }
    
    public double getOverall() {
        return overall;
    }

    public void crunch() {
        winningness();
        offense();
        defense();
        efficiency();
        secondary();
        overall();
    }

    private void offense() {
        double ap = getDoubStat("Average Points For");
        double tss = getDoubStat("True Shooting Percentage");
        double fgp = getDoubStat("Field Goal Percentage"); 
        double ppgd = getDoubStat("Points Per Game Differential");
        double ftp = getDoubStat("Free Throw Percentage");
        double tpfg = getDoubStat("3-Point Field Goal Percentage");
        double efg = getDoubStat("Effective Field Goal Percentage");
        offense = (((fgp+tpfg)*100)+ap)/2 + (tss-efg) + ppgd + (ftp/100);
    }

    private void defense() {
        double ofg = getDoubStat("Opponent Field Goal Percentage");
        double oppg = getDoubStat("Opponent Points Per Game");
        double otfg = getDoubStat("Opponent 3-Point Field Goal Percentage");
        double spg = getDoubStat("Steals Per Game");
        double bpg = getDoubStat("Blocks Per Game");
        defense = ((1-ofg)*100) + ((1-otfg)*100) - oppg + spg + bpg;
    }

   private void secondary() {
        double rr = getDoubStat("Rebound Rate");
        double drr = getDoubStat("Defensive Rebound Rate");
        double orr = getDoubStat("Offensive Rebound Rate");
        double tr = getDoubStat("Turnover Ratio");
        double ot = getDoubStat("Opponent Turnovers Per Game");
        double t = getDoubStat("Turnovers Per Game");
        double ar = getDoubStat("Assist Ratio");
        secondary = (((rr+drr+orr)/3)-50) - (tr/100) * (t/ot) + (ar/100);
    } 

    private void efficiency() {
        double pf = getDoubStat("Pace Factor");
        double de = getDoubStat("Defensive Efficiency");
        double oe = getDoubStat("Offensive Efficiency");
        efficiency = (pf/100) * (oe-de);
    }

    private void overall() {
        overall = (offense/100*20) + (defense/100*20) + (efficiency/10) + (secondary) + (winningness * 10);
    }

    private void winningness() {
        double rpi = getDoubStat("Relative Percent Index");
        double sos = getDoubStat("Strength of Schedule");
        double ewp = getDoubStat("Expected Winning Percentage");
        double wp = getDoubStat("Win Percentage");
        double p = getDoubStat("Points");
        double pa = getDoubStat("Points Against");
        double pr = getDoubStat("Power Rank");
        String lastTen = stats.get("Last 10 Games");
        double w10 = Integer.parseInt(lastTen.split("-")[0]);
        double l10 = Integer.parseInt(lastTen.split("-")[1]);
        winningness = ((rpi+sos)/2) + (Math.abs(ewp-wp) * (ewp+wp)) * (p/pa) + (1-(pr/30)) + (w10-l10)/10;
    }

}
