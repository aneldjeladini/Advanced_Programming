import java.util.Scanner;

enum TYPE { POINT, CIRCLE }
enum DIRECTION { UP, DOWN, LEFT, RIGHT }

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp()    throws ObjectCanNotBeMovedException;
    void moveDown()  throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft()  throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
}

class MovablePoint implements Movable {
    private int x, y, xSpeed, ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x; this.y = y;
        this.xSpeed = xSpeed; this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int newY = y + ySpeed;
        if (!MovablesCollection.checkValidCoordinates(x, newY))
            throw new ObjectCanNotBeMovedException(x, newY);
        this.y = newY;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int newY = y - ySpeed;
        if (!MovablesCollection.checkValidCoordinates(x, newY))
            throw new ObjectCanNotBeMovedException(x, newY);
        this.y = newY;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int newX = x + xSpeed;
        if (!MovablesCollection.checkValidCoordinates(newX, y))
            throw new ObjectCanNotBeMovedException(newX, y);
        this.x = newX;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int newX = x - xSpeed;
        if (!MovablesCollection.checkValidCoordinates(newX, y))
            throw new ObjectCanNotBeMovedException(newX, y);
        this.x = newX;
    }

    @Override public int getCurrentXPosition() { return x; }
    @Override public int getCurrentYPosition() { return y; }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }
}

class MovableCircle implements Movable {
    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() { return radius; }
    public MovablePoint getCenter() { return center; }

    @Override public void moveUp()    throws ObjectCanNotBeMovedException { center.moveUp(); }
    @Override public void moveDown()  throws ObjectCanNotBeMovedException { center.moveDown(); }
    @Override public void moveRight() throws ObjectCanNotBeMovedException { center.moveRight(); }
    @Override public void moveLeft()  throws ObjectCanNotBeMovedException { center.moveLeft(); }

    @Override public int getCurrentXPosition() { return center.getCurrentXPosition(); }
    @Override public int getCurrentYPosition() { return center.getCurrentYPosition(); }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d",
                center.getCurrentXPosition(), center.getCurrentYPosition(), radius);
    }
}

class MovablesCollection {
    private Movable[] movables;
    private static int x_MAX;
    private static int y_MAX;
    private int objectCount = 0;

    public MovablesCollection(int x_MAX, int y_MAX) {
        MovablesCollection.x_MAX = x_MAX;
        MovablesCollection.y_MAX = y_MAX;
        this.movables = new Movable[100];
    }

    public static boolean checkValidCoordinates(int x, int y) {
        return x >= 0 && x <= x_MAX && y >= 0 && y <= y_MAX;
    }

    public static void setxMax(int x_MAX) { MovablesCollection.x_MAX = x_MAX; }
    public static void setyMax(int y_MAX) { MovablesCollection.y_MAX = y_MAX; }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m instanceof MovablePoint) {
            MovablePoint point = (MovablePoint) m;
            if (!checkValidCoordinates(point.getCurrentXPosition(), point.getCurrentYPosition()))
                throw new MovableObjectNotFittableException(point + " can not be fitted into the collection");
        } else {
            MovableCircle circle = (MovableCircle) m;
            int cx = circle.getCenter().getCurrentXPosition();
            int cy = circle.getCenter().getCurrentYPosition();
            int r  = circle.getRadius();
            if (!checkValidCoordinates(cx - r, cy - r) || !checkValidCoordinates(cx + r, cy + r))
                throw new MovableObjectNotFittableException(
                        String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",
                                cx, cy, r));
        }
        movables[objectCount++] = m;
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (int i = 0; i < objectCount; i++) {
            // Skip objects of the wrong type
            if (type == TYPE.CIRCLE && movables[i] instanceof MovablePoint) continue;
            if (type == TYPE.POINT  && movables[i] instanceof MovableCircle) continue;

            try {
                switch (direction) {
                    case UP:    movables[i].moveUp();    break;
                    case DOWN:  movables[i].moveDown();  break;
                    case LEFT:  movables[i].moveLeft();  break;
                    case RIGHT: movables[i].moveRight(); break;
                }
            } catch (ObjectCanNotBeMovedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size ").append(objectCount).append(": \n");
        for (int i = 0; i < objectCount; i++) {
            sb.append(movables[i].toString()).append('\n');
        }
        return sb.toString();
    }
}

public class CirclesTest {
    public static void main(String[] args) {
        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");
            int x      = Integer.parseInt(parts[1]);
            int y      = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) {
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());
    }
}