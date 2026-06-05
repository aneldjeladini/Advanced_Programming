import javax.swing.tree.TreeCellRenderer;
import java.io.*;
import java.util.*;
import java.util.function.Function;

class Movie{
    private String title;
    private String genre;
    private int year;
    private double avgRating;

    public Movie(String title,String genre,int year,double avgRating){
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.avgRating = avgRating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getAvgRating() {
        return avgRating;
    }

    @Override
    public String toString(){
        return String.format("%s, %s, %d, %.2f",title,genre,year,avgRating);
    }
}


class MovieTheater{
    private List<Movie> movies;
    private Map<String,Movie> bestMoviesByGenre; // for additional task
    Map<String, Set<String>> actorsByMovie; // for additional task

    public MovieTheater(){
        this.movies = new ArrayList<>();
        this.bestMoviesByGenre = new TreeMap<>();
        this.actorsByMovie = new HashMap<>();
    }

    public void readMovies(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int numMovies = Integer.parseInt(br.readLine());

        for (int i = 0; i < numMovies; i++){
            String name = br.readLine();
            String genre = br.readLine();
            int year = Integer.parseInt(br.readLine());
            String [] ratings = br.readLine().trim().split("\\s+");
            double avgRating = Arrays.stream(ratings)
                    .mapToInt(Integer::parseInt)
                    .average()
                    .getAsDouble();

            movies.add(new Movie(name,genre,year,avgRating));
        }
    }

    public void printByGenreAndTitle(){
        movies.stream()
                .sorted(Comparator.comparing(Movie::getGenre).thenComparing(Movie::getTitle))
                .forEach(System.out::println);
    }

    public void printByYearAndTitle(){
        movies.stream()
                .sorted(Comparator.comparingInt(Movie::getYear).thenComparing(Movie::getTitle))
                .forEach(System.out::println);
    }

    public void printByRatingAndTitle(){
        movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getAvgRating).reversed().thenComparing(Movie::getTitle))
                .forEach(System.out::println);
    }

    // =============================== ADDITIONAL METHODS ================================= //

    public void printBestMovieByGenre(){
        movies.forEach(m -> {
            bestMoviesByGenre.putIfAbsent(m.getGenre(),null);
        });

        bestMoviesByGenre.keySet()
                .forEach(g -> {
                   Movie best = movies.stream()
                           .filter(m -> m.getGenre().equals(g))
                           .max(Comparator.comparingDouble(Movie::getAvgRating))
                           .orElse(null);

                   if (best != null){
                       bestMoviesByGenre.put(g,best);
                       System.out.println("Best " + g + " movie:");
                       System.out.println(best);
                   }
                });
    }

    public void addActors(String movieTitle, List<String> actors){
        Set<String> actorSet = new HashSet<>(actors);
        actorsByMovie.putIfAbsent(movieTitle, actorSet);
    }

    
    // ==================================================================================== //

}


public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}