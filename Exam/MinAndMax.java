import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    T min;
    T max;
    int processed;
    int maxCount;
    int minCount;
    public MinMax(){
        max = null;
        min = null;
        processed = 0;
        maxCount = 0;
        minCount = 0;
    }

    public void update(T element){
        if (min == null && max == null){
            min = max = element;
        }
        if (element.compareTo(min) == 0){
            minCount++;
        }
        if (element.compareTo(max) == 0){
            maxCount++;
        }
        if (element.compareTo(max) > 0 ){
            max = element;
            maxCount = 1;
        }
        else if (element.compareTo(min) < 0){
            min = element;
            minCount = 1;
        }
        processed ++;
    }

    @Override
    public String toString(){
        return min + " " + max + " " + (processed - minCount - maxCount) + '\n';
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}