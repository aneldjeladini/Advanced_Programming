import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

class Driver{
    private String name;
    private String [] laps;

    public Driver(String name, String [] laps){
        this.name = name;
        this.laps = laps.clone();
    }

    public String getName() {
        return name;
    }

    public String[] getLaps() {
        return laps;
    }

    public String getBestLap(){
        return Arrays.stream(laps)
                .sorted()
                .findFirst()
                .orElse("Empty");
    }

    public int getMinutes(String lap){
        return Integer.parseInt(lap.split(":")[0]);
    }

    public int getSeconds(String lap){
        return Integer.parseInt(lap.split(":")[1]);
    }

    public int getMilliseconds(String lap){
        return Integer.parseInt(lap.split(":")[2]);
    }

    @Override
    public String toString(){
        return String.format("%-10s%10s",name,getBestLap());
    }
}


class F1Race{
    List<Driver> drivers;

    public F1Race(){
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while (true){
            String [] parts = sc.nextLine().trim().split("\\s+");
            if (parts.length == 1){
                break;
            }
            String [] laps = {parts[1],parts[2],parts[3]};
            drivers.add(new Driver(parts[0],laps));
        }

    }

    public void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        drivers.sort(Comparator.comparing(Driver::getBestLap));

        int i = 1;
        for (Driver driver : drivers){
            pw.println(i + ". " + driver);
            i++;
        }

        pw.flush();
        pw.close();

    }

}


public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}