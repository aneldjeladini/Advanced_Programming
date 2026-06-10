import java.util.*;

class Triple<T extends Number & Comparable<T>> {
    private ArrayList<T> numbers;

    public Triple(T n1, T n2, T n3) {
        numbers = new ArrayList<>(Arrays.asList(n1, n2, n3));
    }


    public double max() {
        return numbers.stream().max(Comparator.naturalOrder()).get().doubleValue();
    }

    public double average() {
        return numbers.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }

    void sort() {
        numbers.sort(Comparator.naturalOrder());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", numbers.get(0).doubleValue(),  numbers.get(1).doubleValue(),  numbers.get(2).doubleValue());
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}



