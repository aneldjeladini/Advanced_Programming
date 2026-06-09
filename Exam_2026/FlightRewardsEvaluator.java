import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


class Flight{
    String destination;
    int miles;

    public Flight(String destination, int miles){
        this.destination = destination;
        this.miles = miles;
    }

    public String getDestination() {
        return destination;
    }

    public int getMiles() {
        return miles;
    }
}

class Passenger{
    String id;
    Set<Flight> flights;
    Set<String> destinations;

    public Passenger(String id){
        this.id = id;
        flights = new HashSet<>();
        destinations = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void addFlight(Flight flight){
        flights.add(flight);
    }

    public int getDestinations(){
        return flights.size();
    }

    public int getTotalMiles(){
        return flights.stream()
                .mapToInt(Flight::getMiles)
                .sum();
    }

    public void addDestination(String destination){
        destinations.add(destination);
    }

    @Override
    public String toString(){
        return String.format("Passenger [%s] totalMiles [%d] destinations [%d]",id,getTotalMiles(),getDestinations());
    }
}



public class FlightRewardsEvaluator {

    Map<String,Passenger> passengerMap = new HashMap<>();
    Map<String,Integer> destinationMap = new TreeMap<>();

    public void loadFlights(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true){
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;

            String [] parts = line.trim().split("\\s+");
            String id = parts[0];
            Passenger passenger = new Passenger(id);

            for (int i = 1; i < parts.length; i++){
                String [] flightParts = parts[i].split(":");
                passenger.addFlight(new Flight(flightParts[0],Integer.parseInt(flightParts[1])));
                passenger.addDestination(flightParts[0]);

                if (!destinationMap.containsKey(flightParts[0])){
                    destinationMap.put(flightParts[0],0);
                }

                destinationMap.put(flightParts[0],destinationMap.get(flightParts[0])+1);
            }

            passengerMap.putIfAbsent(passenger.getId(),passenger);

        }
    }

    public void printPassengers(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        passengerMap.values()
                .stream()
                .sorted(Comparator.comparing(Passenger::getTotalMiles,Comparator.reverseOrder()).thenComparing(Passenger::getId))
                .forEach(pw::println);
        pw.flush();
    }

    public Map<String, Integer> groupByDestination() {
        Map<String,Integer> filteredMap = new TreeMap<>();
        destinationMap.keySet()
                .stream()
                .filter(d -> destinationMap.get(d) > 0)
                .sorted()
                .forEach(d -> filteredMap.put(d,destinationMap.get(d)));

        return destinationMap;
    }

    static void wtf(Scanner sc) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("data.txt"));
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            if (line.equals("---")){
                break;
            }
            pw.println(line);
        }
        pw.flush();
    }


    public static void main(String[] args) throws Exception {
        FlightRewardsEvaluator evaluator = new FlightRewardsEvaluator();

        Scanner sc = new Scanner(System.in);
        wtf(sc);

        evaluator.loadFlights(new FileInputStream("data.txt"));

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));

        String command = sc.nextLine();
        switch (command) {
            case "PRINT":
                evaluator.printPassengers(System.out);
                break;

            case "GROUP":
                evaluator.groupByDestination().forEach((dest, cnt) ->
                        pw.printf("Destination [%s] passengers [%d]%n", dest, cnt));
                pw.flush();
                break;

            default:
                pw.println("Invalid command");
                pw.flush();
                break;
        }
    }

}
