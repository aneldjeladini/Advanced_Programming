import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;


interface DoctorEvaluator{
    boolean evaluate(Doctor doctor);
}

class HighExpertieseEvaluator implements DoctorEvaluator{

    @Override
    public boolean evaluate(Doctor doctor) {
        return doctor.getLevel() >= 7;
    }
}

class Doctor {
    private final int licenseNumber;
    private String name;
    private int level;
    private int patients;

    public Doctor(int licenseNumber, String name, int level, int patients) {
        this.licenseNumber = licenseNumber;
        this.name = name;
        this.level = level;
        this.patients = patients;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level > 10) {
            this.level = 10;
            return;
        }
        if (level < 1) {
            this.level = 1;
            return;
        }
        this.level = level;
    }

    public int getPatients() {
        return patients;
    }

    public void setPatients(int patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d %d %s", name, licenseNumber, level, patients, level == 10 ? "[Chief]" : "");
    }
}

class EmergencyRoom {
    private final String hospitalName;
    private final Doctor[] doctors;
    private int size = 0;

    public EmergencyRoom(String title, int doctorCapacity) {
        this.hospitalName = title;
        this.doctors = new Doctor[doctorCapacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return doctors.length;
    }

    public boolean treat(Supplier<Doctor> supplier) {
        if (size >= doctors.length) {
            return false;
        }
        doctors[size++] = supplier.get();
        return true;
    }

    public void forEach(Consumer<Doctor> action) {
        for (int i = 0; i < size; i++) {
            action.accept(doctors[i]);
        }
    }

    public int count(Predicate<Doctor> predicate) {
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                c++;
            }
        }
        return c;
    }

    public Doctor findFirst(Predicate<Doctor> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                return doctors[i];
            }
        }
        return null;
    }


    public Doctor[] filter(Predicate<Doctor> predicate) {
        int matches = count(predicate);
        Doctor[] out = new Doctor[matches];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                out[j++] = doctors[i];
            }
        }
        return out;
    }

    public String[] mapToLabels(Function<Doctor, String> mapper) {
        String[] out = new String[size];
        for (int i = 0; i < size; i++) {
            out[i] = mapper.apply(doctors[i]);
        }
        return out;
    }

    public void mutate(Consumer<Doctor> mutator) {
        for (int i = 0; i < size; i++) {
            mutator.accept(doctors[i]);
        }
    }

    public void conditionalMutate(Predicate<Doctor> condition, Consumer<Doctor> mutator) {
        for (int i = 0; i < size; i++) {
            if (condition.test(doctors[i])) {
                mutator.accept(doctors[i]);
            }
        }

    }

    public int countForEvaluation(DoctorEvaluator evaluator) {
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (evaluator.evaluate(doctors[i])) {
                c++;
            }
        }
        return c;
    }

    public Doctor[] evaluate(DoctorEvaluator evaluator) {
        int outSize = countForEvaluation(evaluator);
        Doctor[] out = new Doctor[outSize];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (evaluator.evaluate(doctors[i])) {
                out[j++] = doctors[i];
            }
        }
        return out;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hospital: " + hospitalName + " (" + size + "/" + doctors.length + " doctors)");
        for (Doctor doctor : doctors) {
            sb.append(doctor.toString()).append("\n");
        }
        return sb.toString();

    }
}

public class HospitalDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmergencyRoom er = new EmergencyRoom("University Clinic", 10);

        int n = sc.nextInt();
        sc.nextLine();

        //TODO: Supplier that reads line by line
        Supplier<Doctor> doctorBuilder = () -> {
            String [] parts = sc.nextLine().trim().split("\\s+");
            return new Doctor(Integer.parseInt(parts[0]),parts[1],Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
        };
        //TODO: Add n doctors to treat using the supplier
        for (int i = 0; i < n; i++){
            er.treat(doctorBuilder);
        }
        sc.close(); // close scanner after done

        //TODO: Print all of the doctors with forEach
        System.out.println("Doctors that are treating:");
        er.forEach(System.out::println);

        //TODO: Print all doctors that treat using Consumer + forEach ---
        System.out.println("\n=== All Doctors ===");
        Consumer<EmergencyRoom> doctorPrinter = r -> r.forEach(System.out::println);
        doctorPrinter.accept(er);

        //TODO: Use Functional Interface to filter the good doctors ---

        Doctor [] passing = new Doctor[n];
        passing = er.filter(d -> d.getPatients()>20 && d.getLevel() >= 7);


        System.out.println("\n=== Doctors with higher number of patients and a higher level of expertise ===");
        for (Doctor d : passing){
            System.out.println(d);
        }

        //TODO: Print the chief of the department (level = 10)
        System.out.println("\n=== Chief doctor (level = 10) ===");
        Doctor chief = er.findFirst(d -> d.getLevel() == 10);

        System.out.println(chief != null ? chief : "No chief found");

        //TODO: Increase the level of expertise for every doctor by 1
        System.out.println("\n=== Increase all expertise levels by 1 (max 10) ===");
        er.mutate(d -> {
            if (d.getLevel() < 10){
                d.setLevel(d.getLevel()+1);
            }
        });
        doctorPrinter.accept(er);


        //TODO: Conditional mutation: increase the level of expertise of every doctor that has more than 30 patients
        System.out.println("\n=== Increase the level of expertise of every doctor by 1 ===");
        er.conditionalMutate(d -> d.getPatients()>=30, d -> {
            if (d.getLevel() < 10){
                d.setLevel(d.getLevel()+1);
            }
        });

        //TODO: Map doctors to labels in the format: Name: name, Level: level
        System.out.println("\n=== Map doctors to labels ===");
        String [] mapped = er.mapToLabels(d -> String.format("Name: %s, Level: %d" , d.getName(),d.getLevel()));
        for (String str : mapped){
            System.out.println(str);
        }
    }
}