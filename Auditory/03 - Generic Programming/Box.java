import java.util.*;

public class Box<T> {
    private List<T> objects;

    public Box(){
        this.objects = new ArrayList<>();
    }

    public void add(T object){
        this.objects.add(object);
    }

    public boolean isEmpty(){
        return this.objects.size() == 0;
    }

    public T drawItem(){
        Random rand = new Random();
        int index = rand.nextInt(objects.size());
        if (this.isEmpty()) return null;
        return objects.get(index);
    }

    public static void main(String[] args) {

        Box<Integer> prizeBox = new Box<>();
        prizeBox.add(0);
        prizeBox.add(100);
        prizeBox.add(500);
        prizeBox.add(1000);
        prizeBox.add(100000);

        Box<String> candidates = new Box<>();
        candidates.add("John");
        candidates.add("Mark");
        candidates.add("Jane");
        candidates.add("Mary");
        candidates.add("Steve");

        String winner = candidates.drawItem();
        int prizeWon = prizeBox.drawItem();

        if (!candidates.isEmpty() && !prizeBox.isEmpty()){
            System.out.printf("%s WON %d$", winner,prizeWon);
        }
        
    }

}
