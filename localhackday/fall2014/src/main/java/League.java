import java.util.HashMap;
import java.util.TreeSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class League {

    private HashMap<String, Team> teams;

    public League() {
        teams = new HashMap<String, Team>();
    }

    public TreeSet<Team> getRankedTeams() {
        TreeSet<Team> output = new TreeSet<Team>();
        for(String name : teams.keySet()) {
            output.add(teams.get(name));
        }
        return output;
    }
    
    public Set<String> getTeams() {
        return teams.keySet();
    }

    public Team get(String team) {
        return teams.get(team);
    }

    // TODO: clean this code up
    public void init() {
        BufferedReader reader = null;
        initStats(reader);
        initScheds(reader);        
    }

    public void crunch() {
        for(String t : teams.keySet()) {
            teams.get(t).crunch();
            teams.get(t).print();
        }
    }

    private void initScheds(BufferedReader reader) {
        try {
            reader = new BufferedReader(new FileReader(Scraper.SCHEDS_FILE));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] entries = line.split("~");
                Schedule s = teams.get(entries[0]).getSched();
                boolean home = entries[1].charAt(0) != '@';
                if(home) {
                    entries[1] = entries[1].substring(1);
                }
                Game g = new Game(teams.get(entries[0]), teams.get(entries[1].substring(1)), home);
                s.add(g);
            }
        } catch(IOException e) {
            System.out.println("error reading from file!");
        } finally {
            try {
                reader.close();
            } catch(Exception e) {}
        }  

    }

    private void initStats(BufferedReader reader) {
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
