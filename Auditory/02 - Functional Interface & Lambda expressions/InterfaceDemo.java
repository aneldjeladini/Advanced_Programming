import java.util.*;
import java.util.function.*;


interface Operation{
    int apply(int op1, int op2);
}


interface MessageProvider{
    String getMessage();
}


class Addition implements Operation{

    @Override
    public int apply(int op1, int op2) {
        return op1 + op2;
    }
}

class StaticMessage implements MessageProvider{

    @Override
    public String getMessage() {
        return "Hello from regular class";
    }
}




public class InterfaceDemo {
    public static void main(String[] args) {

        Operation op1 = new Addition();
        System.out.println("Addition: " + op1.apply(3,2));

        Operation op2 = new Operation() {
            @Override
            public int apply(int a, int b) {
                return a * b;
            }
        };

        System.out.println("Multiplication: " + op2.apply(3,2));

        Operation op3 = (a,b) -> a - b;
        System.out.println("Subtraction: " + op3.apply(3,2));

        MessageProvider m1 = new StaticMessage();
        System.out.println(m1.getMessage());

        MessageProvider m2 = new MessageProvider() {
            @Override
            public String getMessage() {
                return "Hello from anonymous class";
            }
        };

        System.out.println(m2.getMessage());

        MessageProvider m3 = () -> "Hello from lambda";
        System.out.println(m3.getMessage());

    }
}
