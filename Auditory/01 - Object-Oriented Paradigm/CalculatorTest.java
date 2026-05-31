import java.util.*;

class UnknownOperatorException extends Exception{
    public UnknownOperatorException(String message){
        super(message);
    }

    public String message(){
        return getMessage();
    }
}

class Calculator{
    private double result;
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char MULTIPLY = '*';
    private static final char DIVIDE = '/';

    public Calculator(){
        this.result = 0.0;
    }

    public String calculate(String expr) throws UnknownOperatorException {
        char operator;
        double value = 0.0;
        double oldResult = result;
        String [] parts = expr.trim().split("\\s+");
        if (parts.length == 2){
            operator = parts[0].charAt(0);
            value = Double.parseDouble(parts[1]);
        }
        else if (parts.length == 1){
            operator = expr.charAt(0);
            value = Double.parseDouble(expr.substring(1));
        }
        else{
            throw new UnknownOperatorException("Invalid Operation");
        }

        if (operator == PLUS){
             result += value;
        }
        else if (operator == MINUS){
             result -= value;
        }
        else if (operator == MULTIPLY){
            result *= value;
        }
        else if (operator == DIVIDE){
            result /= value;
        }
        else{
            throw new UnknownOperatorException(String.format("%s is an unknown operation.",operator));
        }
        return String.format("%.1f %s %.1f = %.1f",oldResult,operator,value,result);
    }

    public void resetResult(){
        this.result = 0.0;
    }

    public double getResult(){
        return result;
    }

}


public class CalculatorTest {
    public static void main(String[] args) {

         Calculator calculator = new Calculator();
         Scanner scanner = new Scanner(System.in);

        System.out.println("Calculator is on.");
        boolean off = false;


         while (!off){
             System.out.println("result = 0.0");
             while (true){
                 String expr = scanner.nextLine();
                 if (expr.equalsIgnoreCase("r")){
                     System.out.printf("Final result = %.1f\n",calculator.getResult());
                     System.out.println("Again? (y/n)");
                     String answer = scanner.nextLine();
                     if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")){
                         calculator.resetResult();
                         break;
                     }
                     else{
                         off = true;
                         break;
                     }
                 }
                 try{
                     System.out.println(calculator.calculate(expr));
                 }catch (UnknownOperatorException e){
                     System.out.println(e.message());
                     System.out.println("Reenter your last line:");
                 }
             }
             if (!off){
                 continue;
             }
             System.out.println("End of Program");
         }



    }
}
