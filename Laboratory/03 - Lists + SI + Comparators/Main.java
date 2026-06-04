import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class Ad implements Comparable<Ad>{
    private String id;
    private String category;
    private double bidValue;
    private double ctr;
    private String content;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public double getBidValue() {
        return bidValue;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getCtr() {
        return ctr;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(Ad other) {
         int result = Double.compare(other.bidValue,this.bidValue);

         if (result == 0){
             result = this.id.compareTo(other.id);
         }

         return result;
    }

    @Override
    public String toString(){
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s", id,category,bidValue,ctr*100,content);
    }

}

class AdRequest{
    private String id;
    private String category;
    private double floorBid;
    private String keywords;

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString(){
        return String.format("%s %s (floor=%.2f): %s", id,category,floorBid,keywords);
    }
}


class AdNetwork {
    private List<Ad> ads;

    public AdNetwork(){
        this.ads = new ArrayList<>();
    }

    public void readAds(BufferedReader in) throws IOException {
        BufferedReader br = in;

        while (true){
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;

            String [] parts = line.trim().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (int i = 4; i < parts.length; i++){
                sb.append(parts[i]).append(" ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

            ads.add(new Ad(parts[0],parts[1],Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),sb.toString()));
        }
    }


    public List<Ad> placeAds(BufferedReader in, int k, PrintWriter outputStream) throws IOException {
        BufferedReader br = new BufferedReader(in);
        PrintWriter pw = outputStream;

        String [] parts = br.readLine().trim().split("\\s+");
        StringBuilder keywords = new StringBuilder();
        for (int i = 3; i < parts.length; i++){
            keywords.append(parts[i]).append(" ");
        }
        if (keywords.length() > 0) {
            keywords.setLength(keywords.length() - 1);
        }

        AdRequest request = new AdRequest(parts[0],parts[1],Double.parseDouble(parts[2]),keywords.toString());

        Comparator<Ad> scoreComparator = Comparator.comparingDouble(a -> relevanceScore(a,request) + 5.0 * a.getBidValue() + 100.0 * a.getCtr());

        List<Ad> topKAds = ads.stream()
                .filter(a -> a.getBidValue() >= request.getFloorBid())
                .sorted(scoreComparator.reversed())
                .limit(k)
                .sorted()
                .collect(Collectors.toList());

        pw.println("Top ads for request " + request.getId() + ":");

        topKAds.forEach(pw::println);

        pw.flush();


        return topKAds;

    }




    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}