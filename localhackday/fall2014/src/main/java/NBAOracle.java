import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NBAOracle {

    private static final String RANKINGS_FILE = ".rank.nba";
    private League nba;
    private Scraper s;

    public NBAOracle() {
        s = new Scraper();
        nba = new League();
    }

    public void sow(boolean schedules) {
        s.getStats();
        s.writeStats();
        if(schedules) {
            s.getScheds();
            s.writeScheds();
        }
    }

    public void harvest() {
        nba.init(); // reads in stats from file
    }

    public void forget() {
        s.dump();
    }

    public void refresh() {
        forget();
        harvest();
    }

    public void predict() {
        nba.crunch();
    }

    public void write() {
        System.out.println("writing rankings to " + RANKINGS_FILE);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(RANKINGS_FILE), "utf-8"));
            String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Calendar.getInstance().getTime());
            writer.write("retrieved: " + timeStamp);
            writer.newLine();
            int rank = 1;
            for(Team t : nba.getRankedTeams()) {
                writer.write((rank++) + ". " + t.name());
                writer.newLine();
                writer.write("overall score: " + t.getOverall());
                writer.newLine();
                writer.write("winningness: " + t.getWinningness());
                writer.newLine();
                writer.write("offense: " + t.getOffense());
                writer.newLine();
                writer.write("defense: " + t.getDefense());
                writer.newLine();
                writer.write("secondary: " + t.getSecondary());
                writer.newLine();
                writer.write("efficiency: " + t.getEfficiency());
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

    public static void main(String[] args) {
        NBAOracle oracle = new NBAOracle();
        oracle.sow(false);
        oracle.harvest();
        oracle.forget();
        oracle.predict();
        oracle.write();
    }

}
