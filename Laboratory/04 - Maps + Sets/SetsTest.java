import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ExistingStudentException extends Exception{
    String id;
    public ExistingStudentException(String id){
        super("Student with ID " + id + " already exists");
    }

}

class Student implements Comparable<Student>{
   private String id;
   private List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void addGrade(int grade){
        grades.add(grade);
    }

    public double averageGrade(){
        return grades.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(-1.0);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student{id='%s', grades=[",id));
        for (int grade : grades){
            sb.append(grade).append(", ");
        }
        sb.setLength(sb.length()-2);
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public int compareTo(Student o) {
        return Comparator
                .comparingDouble(Student::averageGrade).reversed()
                .thenComparing(Comparator.comparingInt((Student s) -> s.getGrades().size()).reversed())
                .thenComparing(Student::getId)
                .compare(this,o);
    }
}

class Faculty{
    Map<String,Student> students;

    public Faculty(){
        this.students = new TreeMap<>();
    }

    public void addStudent(String id, List<Integer> grades) throws ExistingStudentException {
        if (students.containsKey(id)){
            throw new ExistingStudentException(id);
        }
        students.putIfAbsent(id,new Student(id,grades));
    }

    public void addGrade(String id, int grade){
        if (students.containsKey(id)){
            students.get(id).addGrade(grade);
        }
    }

    public Set<Student> getStudentsSortedByAverageGrade(){
         return new TreeSet<>(students.values());
    }

    public Set<Student> getStudentsSortedByCoursesPassed(){
        Comparator<Student> comp = Comparator
                .comparingInt((Student s) -> s.getGrades().size())
                .reversed()
                .thenComparing(Student::averageGrade,Comparator.reverseOrder())
                .thenComparing(Student::getId);

        return students.values()
                .stream()
                .sorted(Comparator.comparingInt((Student s) -> s.getGrades().size()).reversed().thenComparingDouble(Student::averageGrade).thenComparing(Student::getId))
                .collect(Collectors.toCollection(() -> new TreeSet<>(comp)));
    }

}




public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
