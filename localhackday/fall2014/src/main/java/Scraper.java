import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class Scraper {

    public static final String STATS_FILE = ".stats.nba";
    private static final String BASE_URL = "http://espn.go.com/nba/statistics";
    private Document doc;
    private Table<String, String, String> tab;

    public Scraper() {
        try {
            doc = Jsoup.connect(BASE_URL).get();
            System.out.println("jsoup connected! base source: " + doc.title());
        } catch(IOException e) {
            jsoupPanic(e);
        }
        tab = HashBasedTable.create();
    }

    public void write() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(STATS_FILE), "utf-8"));
            for(Table.Cell<String, String, String> cell: tab.cellSet()) {
                writer.write(cell.getRowKey() + "~" + cell.getColumnKey() + "~" + cell.getValue());
                writer.newLine();
            }
        } catch(IOException e) {
            System.out.println("error writing to file!");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch(Exception e) {}
        }
    }

    public void dump() {
        tab.clear();
        tab = null;
        doc = null;
    }

    public Table<String, String, String> table() {
        return tab;
    }

    public void getAll() {
        getHollingerStats();
        getTeamStandings();
        getTeamStats();
    }

    public void clear() {
        tab.clear();
    }

    public void getHollingerStats() {
        String hollingerStatsUrl = "http://espn.go.com/nba/hollinger/teamstats";
        readStatTable(hollingerStatsUrl, "hollinger stats");
    }

    public void getTeamStandings() {
        String teamStandingsUrl = "http://espn.go.com/nba/standings/_/group/1";
        readStatTable(teamStandingsUrl, "team standings");
    }

    public void getTeamStats() {
        String teamStatsUrl = BASE_URL + "/team/_/stat/team-comparison-per-game";
        readStatTable(teamStatsUrl, "team stats");
    }

    private void readSchedTabl(String team, String abbr) {
        String teamSchedUrl = "http://espn.go.com/nba/team/schedule/_/name/" + abbr + "/" + team;
    }

    private void readStatTable(String url, String type) {
        System.out.println("getting " + type + "...");
        try {
            doc = Jsoup.connect(url).get();
            Element table = doc.select("table").first();
            Element header = table.select("tr.colhead").last();
            ArrayList<String> cols = new ArrayList<String>();
            Elements titles = header.select("td:not([align=left])");
            for(Element title : titles) {
                if(title.child(0).tagName().equals("a")) {
                    cols.add(title.select("a").attr("title"));
                } else {
                    cols.add(title.select("span").attr("title"));
                }
            }
            Elements rows = table.select("tr:not(.stathead, .colhead)");
            for(Element row : rows) {
                String team = row.select("td[align=left]").last().text();
                Elements stats = row.select("td:not([align=left])");
                int i = 0;
                for(Element stat : stats) {
                    tab.put(team, cols.get(i++), stat.text());
                }
            }
        } catch(IOException e) {
            jsoupPanic(e);
        }
        System.out.println("finished getting " + type + "!");
    } 

    private void jsoupPanic(IOException e) {
        System.out.println("jsoup failed to make a connection!"); 
        e.printStackTrace();
    }

    private static String[] splitStr(String s) {
        try {
            return s.split("\\s+");
        } catch(PatternSyntaxException e) {
            System.out.println("invalid string pattern");
            e.printStackTrace(); 
        }
        return null; // shouldn't get here
    }

}
