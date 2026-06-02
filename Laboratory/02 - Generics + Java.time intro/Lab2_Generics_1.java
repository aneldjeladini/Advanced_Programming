import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;

@SuppressWarnings("unchecked")
class ResizableArray<T>{
    private T[] array;
    int size;

    public ResizableArray(){
        array = (T[]) new Object[100];
        size = 0;
    }


    public void addElement(T element){
        if (array.length == size){
            array = Arrays.copyOf(array,(int)(size*1.5));
        }
        array[size++] = element;
    }

    public boolean removeElement(T element){
        int index = -1;
        for (int i = 0; i < size; i++){
            if (array[i].equals(element)){
                index = i;
                break;
            }
        }
        if (index == -1){
            return false;
        }
        for (int i = index; i < size-1; i++){
            array[i] = array[i+1];
        }
        array[size-1] = null;
        --size;

        if (array.length > 100 && size < array.length/2){
            array = Arrays.copyOf(array, Math.max(size,100));
        }

        return true;
    }


    public int count() {
        return size;
    }


    public boolean contains(T element) {
        for (int i = 0; i < size; i++){
            if (array[i].equals(element)){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public Object[] toArray(){
        return array;
    }

    public T elementAt(int index){
        if (index < 0 || index >= size) throw new ArrayIndexOutOfBoundsException();
        return array[index];
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){

        int count = src.count();
        Object[] snapshot = new Object[count]; // only real elements
        for (int i = 0; i < count; i++){
            snapshot[i] = src.elementAt(i);
        }

        for (int i = 0; i < count; i++){
            dest.addElement((T) snapshot[i]);
        }
    }

}

class IntegerArray extends ResizableArray<Integer>{
    public IntegerArray(){
        super();
    }

    public double sum(){
        double suma = 0;
        for (int i = 0; i < this.count(); i++){
            suma += this.elementAt(i);
        }
        return suma;
    }

    public double mean (){
        return sum()/ this.count();
    }

    public int countNonZero(){
        int count = 0;
        for (int i = 0; i < this.count(); i++){
            if (this.elementAt(i) != 0){
                count++;
            }
        }
        return count;
    }

    public IntegerArray distinct(){
        IntegerArray tmp = new IntegerArray();
        for (int i = 0; i < this.count(); i++){
            if (!tmp.contains((Integer) this.toArray()[i])){
                tmp.addElement((Integer) this.toArray()[i]);
            }
        }
        return tmp;
    }

    public IntegerArray increment(int offset){
        IntegerArray tmp = new IntegerArray();

        for (int i = 0; i < this.count(); i++){
            Integer value = this.elementAt(i);
            tmp.addElement(value+offset);
        }
        return tmp;
    }

}

public class Lab2_Generics_1 {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
