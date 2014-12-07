public class NBAOracle {

    private League nba;

    public NBAOracle() {
        nba = new League();
        nba.init(); // reads in stats from file
    }

    public static void main(String[] args) {
        Scraper s = new Scraper();
        s.getStats();
        s.getScheds();
        s.writeStats();
        s.writeScheds();
        NBAOracle oracle = new NBAOracle();
        oracle.nba.get("Houston").getSched().print();
        s.dump(); 
    }

}
