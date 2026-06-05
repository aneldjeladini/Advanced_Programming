import java.io.*;
import java.util.*;

class Person{
    private String id;
    private List<String> meals;
    private int countHealthyMeals=0;



    public Person(String id){
        this.id = id;
        this.meals = new ArrayList<>();
    }

    public Person(String id, List<String> meals) {
        this.id = id;
        this.meals = meals;
    }

    public String getId() {
        return id;
    }

    public List<String> getMeals() {
        return meals;
    }

    public void calculateHealthyMeals(List<String> healthyMeals){
        for (String meal : meals){
            if (healthyMeals.contains(meal)){
                countHealthyMeals++;
            }
        }
    }

    public int getCountHealthyMeals() {
        return countHealthyMeals;
    }

    @Override
    public String toString(){
        return String.format("Person ID: %s (healthy meals: %d)",id,countHealthyMeals);
    }
}

public class HealthyMeals {
    private List<Person> people;
    private List<String> meals;

    public HealthyMeals(){
        this.people = new ArrayList<>();
        this.meals = new ArrayList<>();
    }

    public void evaluate(InputStream is, OutputStream os) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter pw = new PrintWriter(os);

        for (String meal : br.readLine().trim().split("\\s+")){
            this.meals.add(meal);
        }

        while (true){
            String personInfo = br.readLine();
            if (personInfo == null || personInfo.isEmpty())break;

            String [] parts = personInfo.trim().split("\\s+");

            String id = parts[0];
            List<String> personMeals = new ArrayList<>();
            for (int i = 1; i < parts.length; i++){
                personMeals.add(parts[i]);
            }

            Person current = new Person(id,personMeals);
            current.calculateHealthyMeals(meals);
            people.add(current);
        }

        people.stream()
                .sorted(Comparator.comparingInt(Person::getCountHealthyMeals).reversed().thenComparing(Person::getId))
                .forEach(pw::println);

        pw.flush();

    }

    public static void main(String[] args) throws IOException {
        HealthyMeals healthyMeals = new HealthyMeals();
        healthyMeals.evaluate(System.in,System.out);
    }

}
