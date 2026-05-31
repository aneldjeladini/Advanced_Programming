import java.util.*;
import java.util.function.*;

class Student{
    private final String index;
    private String name;
    private int grade;
    private int attendance;

    public Student(String index,String name,int grade,int attendance){
        this.index = index;
        this.name = name;
        this.attendance = attendance;
        if (grade >= 5 && grade <= 10){
            this.grade = grade;
        } else if (grade<5) {
            this.grade = 5;
        }
        else{
            this.grade = 10;
        }
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void incrementGrade(){
        if (grade < 10){
            grade++;
        }


    }

    @Override
    public String toString(){
        return String.format("Name and Surname: %s\nID: %s\nGrade: %d\nAttendance: %d\n",name,index,grade,attendance);
    }

}

class Course{
    private Student [] students;
    private String title;
    private int num_enrolled;

    public Course(String title,int capacity){
        this.title = title;
        this.num_enrolled = 0;
        this.students = new Student[capacity];
    }

    public void enroll(Supplier<Student> supplier){
        if (num_enrolled < students.length){
            students[num_enrolled] = supplier.get();
            num_enrolled++;
        }
    }

    public void forEach(Consumer<Student> action){
        for (int i = 0; i < num_enrolled; i++){
            action.accept(students[i]);
        }
    }

    public int count(Predicate<Student> condition){
        return (int) Arrays.stream(students)
                .filter(condition)
                .count();
    }

    public Student findFirst(Predicate<Student> condition){
        return Arrays.stream(students)
                .filter(condition)
                .findFirst()
                .orElse(new Student("n/a","n/a",5,0));
    }

    public Student [] filter(Predicate<Student> condition){
        return (Student[]) Arrays.stream(students,0,num_enrolled)
                .filter(condition)
                .toArray(Student[]::new);
    }

    public String [] mapToLabels(Function<Student,String> mapper){
        return (String[]) Arrays.stream(students,0,num_enrolled)
                .map(mapper)
                .toArray(String[]::new);
    }

    public void mutate(Consumer<Student> mutator){
        Arrays.stream(students)
                .forEach(mutator);
    }

    public void conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator){
        Arrays.stream(students)
                .filter(condition)
                .forEach(mutator);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ").append(title).append('\n');
        sb.append("Enrolled Students: ").append(num_enrolled).append('\n');
        for (Student s : students){
            sb.append(s.toString()).append('\n');
        }
        return sb.toString();
    }

}

public class CourseDemo {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int num_students = scanner.nextInt();

        Course course = new Course("Advanced Programming",num_students);

        Supplier<Student> studentBuilder = () -> {
            String name = scanner.next();
            String index = scanner.next();
            int grade = scanner.nextInt();
            int attendance = scanner.nextInt();

            return new Student(index,name,grade,attendance);
        };

        for (int i = 0; i < num_students; i++){
            course.enroll(studentBuilder);
        }

        System.out.println("All students: ");
        Consumer<Student> printStudnets = System.out::println;
        course.forEach(printStudnets);

        Predicate<Student> passingGrade = s -> s.getGrade() >= 6;
        Predicate<Student> hightAttendance = s -> s.getAttendance() >= 70;

        Predicate<Student> passedAndHighAttendance = passingGrade.and(hightAttendance);

        System.out.println("Passing grade and high attendance students:");
        for (Student s : course.filter(passedAndHighAttendance)){
            System.out.println(s);
        }

        System.out.println("First excellent student: ");
        System.out.println(course.findFirst(s -> s.getGrade() >= 9));

        System.out.println("Increment grades for every student");
        Consumer<Student> gradeIncrementer = Student::incrementGrade;
        course.forEach(gradeIncrementer);

        System.out.println("Increment grades for high attendance students");
        Predicate<Student> veryHighAttendance = s -> s.getAttendance() >= 90;
        course.conditionalMutate(veryHighAttendance,gradeIncrementer);

        System.out.println("Mapping students into String representation");
        Function<Student,String> studentStringMapper = Student::toString;
        for (String s : course.mapToLabels(studentStringMapper)){
            System.out.println(s);
        }

        System.out.println("Course Info: ");
        System.out.println(course);


    }
}
