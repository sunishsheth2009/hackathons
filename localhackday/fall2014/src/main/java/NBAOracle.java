import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

public class NBAOracle {

    private Table<String, String, String> stats; 

    public NBAOracle(Table<String, String, String> tab) {
        stats = HashBasedTable.create(tab);
    }

    public void stats() {
        System.out.println("The oracle knows:");
        for(Table.Cell<String, String, String> cell: stats.cellSet()) {
            System.out.println("[" + cell.getRowKey() + "] " + cell.getColumnKey() + ": " + cell.getValue());
            System.out.println("=======================================");
        }
    }

    public static void main(String[] args) {
        Scraper s = new Scraper();
        s.getAll(); // get all stats
        s.write(); // write all stats
        League nba = new League();
        nba.init(); // reads in stats from file
        NBAOracle oracle = new NBAOracle(s.table());
        s.dump(); 
        oracle.stats();
    }

}
