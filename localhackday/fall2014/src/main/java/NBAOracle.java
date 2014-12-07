public class NBAOracle {

    public static void main(String[] args) {
        Scraper s = new Scraper();
        s.getTeamStandings();
        s.getTeamStats();
        s.getHollingerStats();
        s.print();
    }

}
