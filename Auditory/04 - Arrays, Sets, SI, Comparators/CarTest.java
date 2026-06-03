import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


class Car{
    private String manufacturer;
    private String model;
    private int price;
    float power;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }
}


class CarCollection{
    List<Car> cars;

    public CarCollection(){
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public void sortByPrice(boolean ascending){
        if (ascending){
            this.cars = cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice).thenComparingDouble(Car::getPower))
                    .toList();
        }
        else{
            this.cars = cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice,Comparator.reverseOrder()).thenComparingDouble(Car::getPower))
                    .toList();
        }
    }

    public List<Car> filterByManufacturer(String manufacturer){
        return cars.stream()
                .filter(car -> car.getManufacturer().equalsIgnoreCase(manufacturer))
                .sorted(Comparator.comparing(Car::getModel))
                .toList();
    }

    public List<Car> getList(){
        return cars;
    }

}



public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            if (parts.length == 1)
                return line;

            String manufacturer = parts[0];
            String model = parts[1];
            int price = Integer.parseInt(parts[2]);
            float power = Float.parseFloat(parts[3]);
            cc.addCar(new Car(manufacturer, model, price, power));
        }
        return "";
    }
}