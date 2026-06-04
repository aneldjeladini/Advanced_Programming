import java.io.*;
import java.util.*;

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

    public MovieTheater(){
        this.movies = new ArrayList<>();
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