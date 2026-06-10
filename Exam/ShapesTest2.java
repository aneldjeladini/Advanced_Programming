

import java.util.*;
import java.io.*;
import java.lang.*;

class IrregularCanvasException extends Exception{
    String message;
    String id;
    double maxArea;

    IrregularCanvasException(String id,double maxArea){
        this.maxArea = maxArea;
        this.message = String.format("Canvas %s has a shape with area larger than %.2f",id,maxArea);
    }
    public String getMessage(){
        return message;
    }
}

class Shape{
    String type;
    int size;

    public Shape(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPlostina(){
        if(type.equals("C")){
            return Math.PI*size*size;
        }
        else{
            return size*size;
        }
    }

}



class Window{
    String ID;
    ArrayList<Shape> shapes;

    public Window(String ID, ArrayList<Shape> shapes) {
        this.ID = ID;
        this.shapes = shapes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public double sumaPlostini(){
        double sum = 0;
        for (Shape shape : shapes){
            sum += shape.getPlostina();
        }
        return sum;
    }

    double minArea(){
        double min = shapes.get(0).getPlostina();
        for (Shape shape : shapes){
            if (shape.getPlostina() < min){
                min = shape.getPlostina();
            }
        }
        return min;
    }

    double maxArea(){
        double max = shapes.get(0).getPlostina();
        for (Shape shape : shapes){
            if (shape.getPlostina() > max){
                max = shape.getPlostina();
            }
        }
        return max;
    }

    double averageArea(){
        return sumaPlostini() / shapes.size();
    }

}



class ShapesApplication{
    double maxArea;
    ArrayList<Window> windows;
    ShapesApplication(double maxArea){
        this.maxArea = maxArea;
        this.windows = new ArrayList<>();
    }

    void readCanvases(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.isEmpty()){
                break;
            }
            String [] parts = line.split("\\s+");
            ArrayList<Shape> shapes = new ArrayList<>();
            boolean invalid = false;

            for(int i = 1; i < parts.length; i+=2){
                Shape currentShape = new Shape(parts[i],Integer.parseInt(parts[i+1]));
                try {
                    if (currentShape.getPlostina() > maxArea){
                        throw new IrregularCanvasException(parts[0],maxArea);
                    }
                    shapes.add(currentShape);
                } catch (IrregularCanvasException e){
                    System.out.println(e.message);
                    invalid = true;
                    break;
                }

            }
            if (!invalid){
                windows.add(new Window(parts[0],shapes));
            }

        }
    }

    public void printCanvases(OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        windows.sort((w1,w2) -> Double.compare(w2.sumaPlostini(),w1.sumaPlostini()));
        for (Window window : windows){
            String IdCurrent = window.getID();
            int totalShapes = window.getShapes().size();
            int circleCount = 0;
            int squareCount = 0;
            for (Shape shape : window.getShapes()){
                if (shape.getType().equals("C")){
                    circleCount++;
                }
                else{
                    squareCount++;
                }
            }

            pw.printf("%s %d %d %d %.2f %.2f %.2f\n" ,
                    IdCurrent,totalShapes,circleCount,squareCount,window.minArea(),window.maxArea(),window.averageArea());
        }
        pw.flush();
    }


}



public class ShapesTest2 {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}






