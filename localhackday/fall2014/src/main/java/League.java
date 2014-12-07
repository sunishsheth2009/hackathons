import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class League {

    private HashMap<String, Team> teams;

    public League() {
        teams = new HashMap<String, Team>();
    }

    public Team get(String team) {
        return teams.get(team);
    }

    public void init() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Scraper.STATS_FILE));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] entries = line.split("~");
                if(!teams.containsKey(entries[0])) {
                    teams.put(entries[0], new Team(entries[0]));
                }
                teams.get(entries[0]).add(entries[1], entries[2]); 
            }
        } catch(IOException e) {
            System.out.println("error reading from file!");
        } finally {
            try {
                reader.close();
            } catch(Exception e) {}
        }  
    }

}
