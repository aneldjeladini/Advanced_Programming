import java.util.function.*;

public class FunctionalInterfacesDemo {
    public static void main(String[] args) {

        Function<String,Integer> stringLength = s -> s.length();
        System.out.println("Length of \"Hello\": " + stringLength.apply("Hello"));

        BiFunction<Integer,Integer,Integer> addition = (a,b) -> a+b;
        System.out.println("Sum of 5 and 3 is: " + addition.apply(5,3));

        Predicate<Integer> isEven = a -> a%2 == 0;
        System.out.println("Is the number 7 an even number? " + isEven.test(7));

        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("This is printed by a consumer's accept method");

        Supplier<Long> currentTimeMilis = () -> System.currentTimeMillis();
        System.out.println("Current time in milliseconds: " + currentTimeMilis.get());
    }
}
