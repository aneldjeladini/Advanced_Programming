import java.util.Scanner;
import java.util.*;
import java.lang.*;
import java.math.*;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}

abstract class Shape implements Scalable,Stackable,Comparable<Shape>{
    String id;
    Color color;
    char type;

    public Shape(String id, Color color, char type) {
        this.id = id;
        this.color = color;
        this.type = type;
    }

    public String getId(){
        return id;
    }
    public Color getColor(){
        return color;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public char getType(){
        return type;
    }

    @Override
    public int compareTo(Shape other){
        return Float.compare(this.weight(),other.weight());
    }


}

class Circle extends Shape{
    float radius;
    public Circle(String id, Color color, float radius){
        super(id,color,'C');
        this.radius = radius;
    }


    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius *radius);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public String toString(){
        return String.format("%c: %-4s %-10s %9.2f\n",type,id,color,weight());
    }
}

class Rectangle extends Shape{
    float width;
    float height;

    public Rectangle(String id,Color color, float width, float height){
        super(id,color,'R');
        this.width = width;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public void scale(float scaleFactor) {
        width*=scaleFactor;
        height*=scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }


    @Override
    public String toString(){
        return String.format("%c: %-4s %-10s %9.2f\n",type,id,color,weight());
    }

}



class Canvas {
    ArrayList<Shape> shapes;

    public Canvas(){
        this.shapes = new ArrayList<>();
    }

    void add(String id,Color color, float radius){
        Shape ins = new Circle(id,color,radius);
        boolean added = false;

        if (shapes.isEmpty()){
            shapes.add(ins);
            return;
        }

        for (int i = 0; i < shapes.size(); i++){
            if (ins.compareTo(shapes.get(i)) > 0){
                shapes.add(i,ins);
                added = true;
                break;
            }
        }

        if (!added){
            shapes.add(ins);
        }

    }

    void add(String id,Color color, float width, float height){
        Shape ins = new Rectangle(id,color,width,height);
        boolean added = false;

        if (shapes.isEmpty()){
            shapes.add(ins);
            return;
        }

        for (int i = 0; i < shapes.size(); i++){
            if (ins.compareTo(shapes.get(i)) > 0){
                shapes.add(i,ins);
                added = true;
                break;
            }
        }

        if (!added){
            shapes.add(ins);
        }

    }

    void scale(String id, float scaleFactor){
        Shape target = null;
        for (int i = 0; i < shapes.size(); i++){
            if (id.equals(shapes.get(i).getId())){
                target = shapes.get(i);
                break;
            }
        }
        if (target != null){
            shapes.remove(target);
            target.scale(scaleFactor);

            if (target.getType() == 'C'){
                Circle circle = (Circle) target;
                add(circle.getId(),circle.getColor(),circle.getRadius());
            } else if (target.getType() == 'R'){
                Rectangle rectangle = (Rectangle) target;
                add(rectangle.getId(),rectangle.getColor(),rectangle.getWidth(),rectangle.getHeight());
            }

        }



    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Shape shape: shapes){
            sb.append(shape.toString());
        }
        return sb.toString();
    }

}

public class CanvasTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
