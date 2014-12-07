import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Scraper {

    public static final String STATS_FILE = ".stats.nba";
    public static final String SCHEDS_FILE = ".scheds.nba";
    private static final String BASE_URL = "http://espn.go.com/nba/statistics";
    private static final int TIMEOUT = 10;
    private Document doc;
    private Table<String, String, String> tab;
    private HashMap<String, String> abbrs;
    private HashMap<String, ArrayList<String>> scheds;

    public Scraper() {
        try {
            doc = Jsoup.connect(BASE_URL).get();
            System.out.println("jsoup connected! base source: " + doc.title());
        } catch(IOException e) {
            jsoupPanic(e);
        }
        tab = HashBasedTable.create();
        abbrs = new HashMap<String, String>();
        scheds = new HashMap<String, ArrayList<String>>();
    }

    public void writeScheds() {
        System.out.println("writing schedules to " + SCHEDS_FILE);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SCHEDS_FILE), "utf-8"));
            String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Calendar.getInstance().getTime());
            writer.write("retrieved: " + timeStamp);
            writer.newLine();
            for(String team : scheds.keySet()) {
                for(String opponent : scheds.get(team)) {
                    writer.write(team + "~" + opponent);
                    writer.newLine();
                }
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

    public void writeStats() {
        System.out.println("writing stats to " + STATS_FILE);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(STATS_FILE), "utf-8"));
            String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Calendar.getInstance().getTime());
            writer.write("retrieved: " + timeStamp);
            writer.newLine();
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
        System.out.println("dumping scraped data:");
        System.out.println("=======================================");
        for(Table.Cell<String, String, String> cell : tab.cellSet()) {
            System.out.println("[" + cell.getRowKey() + "] " + cell.getColumnKey() + ": " + cell.getValue());
            System.out.println("=======================================");
        }
        tab.clear();
        abbrs.clear();
        scheds.clear();
        tab = null;
        abbrs = null;
        scheds = null;
        doc = null;
    }

    public void getStats() {
        getRpiStats();
        getHollingerStats();
        getTeamStandings();
        getTeamStats();
    }

    public void getScheds() {
        if(abbrs.keySet().size() == 0) {
            System.out.println("must scrape stats first");
        } else {
            for(String team : abbrs.keySet()) {
                readSchedTable(team, abbrs.get(team)); 
            }
        }
    }

    public void clear() {
        tab.clear();
    }

    public void getRpiStats() {
        String rpiStatsUrl = "http://espn.go.com/nba/stats/rpi";
        readStatTable(rpiStatsUrl, "rpi stats");
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

    private void readSchedTable(String team, String abbr) {
            System.out.println("getting schedule for " + team);
            try {
                String teamSchedUrl = "http://espn.go.com/nba/team/schedule/_/name/" + abbrs.get(team) + "/" + team;
                doc = Jsoup.connect(teamSchedUrl).timeout(TIMEOUT*1000).get();
                Element table = doc.select("table").first();
                ArrayList<String> opponents = new ArrayList<String>();
                Elements rows = table.select("tr:not(.stathead, .colhead)");
                for(Element row : rows) {
                    String status = row.select("li.game-status").first().text();
                    String opponentName = row.select("li.team-name").text();
                    if(opponentName.equals("Los Angeles")) {
                        opponentName = "LA Lakers";
                    } else if(opponentName.equals("NY Knicks")) {
                        opponentName = "New York";
                    }
                    String opponent = status + opponentName;
                    opponents.add(opponent);
                }
                scheds.put(team, opponents);
            } catch(IOException e) {
                jsoupPanic(e);
            }
            System.out.println("finished getting schedule for " + team);
    }

    private void readStatTable(String url, String type) {
        System.out.println("getting " + type + "...");
        try {
            doc = Jsoup.connect(url).timeout(TIMEOUT*1000).get();
            Element table = doc.select("table").first();
            Element header = table.select("tr.colhead").last();
            ArrayList<String> cols = new ArrayList<String>();
            Elements titles = header.select("td:not([align=left])");
            for(Element title : titles) {
                if(title.hasAttr("title")) { // title in td element
                    cols.add(title.attr("title"));
                }
                else if(title.child(0).tagName().equals("a")) { // title in link
                    cols.add(title.select("a").attr("title"));
                } else { // title in span
                    cols.add(title.select("span").attr("title"));
                }
            }
            Elements rows = table.select("tr:not(.stathead, .colhead)");
            for(Element row : rows) {
                Element team = row.select("td[align=left]").last();
                String link = team.select("a").attr("href");
                String abbr = link.substring(secondLastIndexOf(link, '/') + 1, link.lastIndexOf('/'));
                abbrs.put(team.text(), abbr);
                Elements stats = row.select("td:not([align=left])");
                int i = 0;
                for(Element stat : stats) {
                    //if(!tab.contains(team.text(), cols.get(i))) {
                        tab.put(team.text(), cols.get(i++), stat.text());
                    //}
                }
            }
        } catch(IOException e) {
            jsoupPanic(e);
        }
        System.out.println("finished getting " + type);
    } 

    private void jsoupPanic(IOException e) {
        System.out.println("jsoup failed to make a connection!"); 
        e.printStackTrace();
    }

    private static int secondLastIndexOf(String s, char c) {
        int last = s.lastIndexOf(c);
        if(last == -1) {
            return -1;
        }
        return s.substring(0, last).lastIndexOf(c);
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
