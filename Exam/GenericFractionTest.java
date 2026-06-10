import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException(){
        super("Denominator cannot be zero");
    }
}

class GenericFraction<N extends Number, D extends Number>{
    private N nummerator;
    private D denominator;

    public GenericFraction(N nummerator, D denominator) throws ZeroDenominatorException {
        if (denominator.intValue() == 0){
            throw new ZeroDenominatorException();
        }
        this.nummerator = nummerator;
        this.denominator = denominator;
    }

    private double gcd(double a,double b){
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0){
            double tmp = b;
            b = a%b;
            a = tmp;
        }
        return a;
    }

    public GenericFraction<Double,Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double newNumerator = this.nummerator.doubleValue() * gf.denominator.doubleValue()
                + gf.nummerator.doubleValue() * this.denominator.doubleValue();
        double newDenominator = this.denominator.doubleValue() * gf.denominator.doubleValue();
        double common = gcd(newNumerator,newDenominator);
        return new GenericFraction<>(newNumerator / common,newDenominator/common);

    }

    public double toDouble(){
        return nummerator.doubleValue()/denominator.doubleValue();
    }

    @Override
    public String toString(){
        return String.format("%.2f / %.2f",nummerator.doubleValue(),denominator.doubleValue());
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

