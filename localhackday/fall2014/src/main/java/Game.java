public class Game {

    private Team thisTeam;
    private Team opponent;
    private boolean thisTeamHome;
    private int score;

    public Game(Team a, Team b, boolean home) {
        thisTeam = a;
        opponent = b;
        thisTeamHome = home;
        score = 0;
    }
    
    public Team opponent() {
        return opponent;
    }

    public void print() {
        String separator = thisTeamHome ? "@" : "vs";
        System.out.println(thisTeam.name() + " " + separator + " " + opponent.name());
    }

}
