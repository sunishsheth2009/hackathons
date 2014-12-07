import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.ArrayList;

public class Scraper {

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

    public void getHollingerStats() {
        String hollingerStatsUrl = "http://espn.go.com/nba/hollinger/teamstats";
        /*try {
            doc = Jsoup.connect(hollingerStatsUrl).get();
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
        }*/
        readTable(hollingerStatsUrl);
    }

    public void getTeamStandings() {
        String teamStandingsUrl = "http://espn.go.com/nba/standings/_/group/1";
        /*try {
            doc = Jsoup.connect(teamStandingsUrl).get();
            Element table = doc.select("table").first();
            Element header = table.select("tr.colhead").first();
            //String[] cols = splitStr(header.text()); // column headers (ignore cols[0])
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
            for(Element row: rows) {
                String team = row.select("td[align=left]").text();
                Elements stats = row.select("td:not([align=left])");
                int i = 0;
                for(Element stat : stats) {
                    tab.put(team, cols.get(i++), stat.text());
                }
            }
        } catch(IOException e) {
            jsoupPanic(e);
        }*/
        readTable(teamStandingsUrl);
    }

    public void getTeamStats() {
        String teamStatsUrl = BASE_URL + "/team/_/stat/team-comparison-per-game";
        /*try {
            doc = Jsoup.connect(teamStatsUrl).get();
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
            Elements rows = table.select("tr:not(.colhead)");
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
        }*/
        readTable(teamStatsUrl);
    }

    private void readTable(String url) {
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

    public void print() {
        for (Table.Cell<String, String, String> cell: tab.cellSet()){
            System.out.println(cell.getRowKey() + " " + cell.getColumnKey() + " " + cell.getValue());
        }
    }

}
