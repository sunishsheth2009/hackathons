import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class Scraper {

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("http://nba.com/").get();
            String title = doc.title();
            System.out.println(title);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
