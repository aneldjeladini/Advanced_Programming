import java.awt.*;
import java.util.*;
import java.io.*;

class Window{
    String ID;
    ArrayList<Integer> sizes;

    public Window(String ID, ArrayList<Integer> sizes) {
        this.ID = ID;
        this.sizes = sizes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Integer> sizes) {
        this.sizes = sizes;
    }

    public int perimetar(){
        int sum = 0;
        for(int i = 0; i < sizes.size(); i++){
            sum += sizes.get(i)*4;
        }
        return sum;
    }
}

class ShapesApplication{
    ArrayList<Window> windows;

    public ShapesApplication() {
        this.windows = new ArrayList<>();
    }

    int sizeCount = 0;

    public int readCanvases (InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.isEmpty()) break;
            line = line.trim();
            String[] parts = line.split("\\s+");
            if(line.isEmpty()){
                continue;
            }
            String ID = parts[0];
            ArrayList<Integer> sizes = new ArrayList<>();
            for(int i = 1; i < parts.length; i++){
                sizes.add(Integer.parseInt(parts[i]));
                sizeCount++;
            }
            Window w = new Window(ID, sizes);
            windows.add(w);
        }


        return sizeCount;
    }

    void printLargestCanvasTo (OutputStream outputStream){
        Window max = windows.get(0);
        for(int i = 1; i < windows.size(); i++){
            if(windows.get(i).perimetar() > max.perimetar()){
                max = windows.get(i);
            }
        }
        System.out.println(max.getID() + " " + max.getSizes().size() + " " + max.perimetar());

    }

}


public class ShapesTest {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}