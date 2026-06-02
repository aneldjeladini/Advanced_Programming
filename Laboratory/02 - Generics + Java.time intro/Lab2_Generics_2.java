//import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Timestamp<T> implements Comparable<Timestamp<?>>{
    final T element;
    final LocalDateTime time;

    public Timestamp(LocalDateTime time, T element){
        this.time = time;
        this.element = element;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public T getElement(){
        return element;
    }

    public int compareTo(Timestamp<?> t){
        if (getTime().equals(t.getTime())) return 0;
        else if (getTime().isAfter(t.getTime())) return 1;
        else return -1;
    }

    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (!(o instanceof Timestamp<?>)) {
            return false;
        }
        Timestamp<?> other = (Timestamp<?>) o;
        return time.equals(other.time);
    }

    @Override
    public String toString(){
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) + " " + element;
    }

}

class Scheduler<T>{
    ArrayList<Timestamp<T>> stamps;

    public Scheduler(){
        stamps = new ArrayList<>();
    }

    public void add(Timestamp<T> t){
        stamps.add(t);
    }

    public void remove(Timestamp<T> t){
        stamps.remove(t);
    }

    public Timestamp<T> last(){
        Timestamp<T> min = null;
        for (int i = 0; i < stamps.size(); i++){
            if (stamps.get(i).getTime().isBefore(LocalDateTime.now())){
                min = stamps.get(i);
                break;
            }
        }
        for (int i = 0; i < stamps.size(); i++){
            if (stamps.get(i).getTime().isBefore(LocalDateTime.now()) && stamps.get(i).getTime().isAfter(min.getTime())){
                min = stamps.get(i);
            }
        }
        return min;
    }

    public Timestamp<T> next(){
        Timestamp<T> min = null;
        for (int i = 0; i < stamps.size(); i++){
            if (stamps.get(i).getTime().isAfter(LocalDateTime.now())){
                min = stamps.get(i);
                break;
            }
        }
        for (int i = 0; i < stamps.size(); i++){
            if (stamps.get(i).getTime().isAfter(LocalDateTime.now()) && stamps.get(i).getTime().isBefore(min.getTime())){
                min = stamps.get(i);
            }
        }
        return min;
    }

    public List<Timestamp<T>> getAll(LocalDateTime begin, LocalDateTime end){
        List<Timestamp<T>> list = new ArrayList<>();
        for (int i = 0; i < stamps.size(); i++){
            if (stamps.get(i).getTime().isAfter(begin) && stamps.get(i).getTime().isBefore(end)){
                list.add(stamps.get(i));
            }
        }
        return list;
    }

    // ========================== ADDITIONAL METHODS =============================== //

    public <R> Scheduler<R> map(Scheduler<? extends T> source, Function<? super T,? extends R> mapper, Predicate<? super T> filter){
        Scheduler<R> result = new Scheduler<>();
        for (Timestamp<? extends T> t : source.stamps) {
            if (filter.test(t.getElement())) {
                result.add(new Timestamp<>(t.getTime(), mapper.apply(t.getElement())));
            }
        }
        return result;
    }

    public long countIf(Scheduler<? extends T> source, Predicate<? super T> predicate){
        long count = 0;
        for (Timestamp<? extends T> t : source.stamps) {
            if (predicate.test(t.getElement())) {
                count++;
            }
        }
        return count;
    }

    public static <T> Scheduler<T> merge(Scheduler<? extends T> first, Scheduler<? extends T> second){
        Scheduler<T> result = new Scheduler<>();
        for (Timestamp<? extends T> t : first.stamps) {
            result.add(new Timestamp<>(t.getTime(), t.getElement()));
        }
        for (Timestamp<? extends T> t : second.stamps) {
            result.add(new Timestamp<>(t.getTime(), t.getElement()));
        }
        return result;
    }

    // ============================================================================= //


}

public class Lab2_Generics_2 {

    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }

}


