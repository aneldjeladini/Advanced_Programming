import java.util.*;

class OperationNotSupportedException extends Exception {
    public OperationNotSupportedException(String message) {
        super(message);
    }
}

interface Checkup{
    int price();
    String info();
}

class GeneralCheckup implements Checkup{


    @Override
    public int price() {
        return 1200;
    }

    @Override
    public String info() {
        return "General medical checkup";
    }

}


class SpecialistCheckup implements Checkup{

    @Override
    public int price() {
        return 2000;
    }

    @Override
    public String info() {
        return "Specialist medical examination";
    }

}

class CheckupDecorator implements Checkup{
    Checkup checkup;

    public CheckupDecorator(Checkup checkup){
        this.checkup = checkup;
    }

    @Override
    public int price() {
        return checkup.price();
    }

    @Override
    public String info() {
        return checkup.toString();
    }

}

class BloodWorkDecorator extends CheckupDecorator{

    public BloodWorkDecorator(Checkup checkup) {
        super(checkup);
    }

    @Override
    public int price(){
        return checkup.price() + 800;
    }

    @Override
    public String info(){
        return String.format("%s + blood test",checkup.info());
    }

}

class LaboratoryDecorator extends CheckupDecorator{

    public LaboratoryDecorator(Checkup checkup) {
        super(checkup);
    }

    @Override
    public int price(){
        return checkup.price() + 500;
    }

    @Override
    public String info(){
        return String.format("%s + laboratory",checkup.info());
    }

}

class UltrasoundDecorator extends CheckupDecorator{

    public UltrasoundDecorator(Checkup checkup) {
        super(checkup);
    }

    @Override
    public int price(){
        return checkup.price() + 1300;
    }

    @Override
    public String info(){
        return String.format("%s + ultrasound",checkup.info());
    }

}


class XrayDecorator extends CheckupDecorator{

    public XrayDecorator(Checkup checkup) {
        super(checkup);
    }

    @Override
    public int price(){
        return checkup.price() + 1500;
    }

    @Override
    public String info(){
        return String.format("%s + X-ray",checkup.info());
    }


}


class Visit{
    String visitId;
    Checkup checkup;
    List<String> types;

    public Visit(String visitId,Checkup checkup){
        this.visitId = visitId;
        this.checkup = checkup;
        this.types = new ArrayList<>();
    }

    public int visitCost(){
        return checkup.price();
    }

    public String getVisitId() {
        return visitId;
    }

    public Checkup getCheckup() {
        return checkup;
    }


    public void addType(String type){
        types.add(type);
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public String toString(){
        return String.format("%s | %s | %d",visitId,checkup.info(),visitCost());
    }
}


class Patient{
    String patientId;
    Map<String,Visit> visits;

    public Patient(String patientId){
        this.patientId = patientId;
        this.visits = new HashMap<>();
    }


    public Map<String,Visit> getVisits() {
        return visits;
    }

    public void addVisit(String id,Visit visit){
        visits.putIfAbsent(id,visit);
    }

    public double getTotalCost(){
        return visits.values()
                .stream()
                .mapToDouble(Visit::visitCost)
                .sum();
    }

    public void print(){
        visits.values()
                .stream()
                .sorted(Comparator.comparing(Visit::visitCost,Comparator.reverseOrder()))
                .forEach(System.out::println);
    }

    public int getNumVisits(){
        return visits.size();
    }

}



class Hospital{
    Map<String,Patient> patientMap = new HashMap<>();
    Map<String,Patient> visitMap = new HashMap<>();

    public void generalCheckup(String patientId, String visitId){
        patientMap.putIfAbsent(patientId,new Patient(patientId));
        patientMap.get(patientId).addVisit(visitId,new Visit(visitId,new GeneralCheckup()));
        visitMap.put(visitId,patientMap.get(patientId));
        patientMap.get(patientId).getVisits().get(visitId).addType("general");
    }

    public void specialist(String patientId, String visitId){
        patientMap.putIfAbsent(patientId,new Patient(patientId));
        patientMap.get(patientId).addVisit(visitId,new Visit(visitId,new SpecialistCheckup()));
        visitMap.put(visitId,patientMap.get(patientId));
        patientMap.get(patientId).getVisits().get(visitId).addType("special");
    }

    public void addBloodTest(String visitId) throws OperationNotSupportedException {
        if (visitMap.containsKey(visitId)){
            Patient current = visitMap.get(visitId);
            Visit old = current.getVisits().get(visitId);
            if (old.getTypes().contains("bloodwork")){
                throw new OperationNotSupportedException("Diagnostic already added: Blood");
            }
            Visit visit = new Visit(visitId, new BloodWorkDecorator(old.getCheckup()));
            visit.getTypes().addAll(old.getTypes());
            visit.getTypes().add("bloodwork");
            current.getVisits().put(visitId, visit);
        }
    }

    public void addUrineTest(String visitId) throws OperationNotSupportedException {
        if (visitMap.containsKey(visitId)){
            Patient current = visitMap.get(visitId);
            Visit old = current.getVisits().get(visitId);
            if (old.getTypes().contains("urine")){
                throw new OperationNotSupportedException("Diagnostic already added: Urine");
            }
            Visit visit = new Visit(visitId, new LaboratoryDecorator(old.getCheckup()));
            visit.getTypes().addAll(old.getTypes());
            visit.getTypes().add("urine");
            current.getVisits().put(visitId, visit);
        }
    }

    public void addUltrasound(String visitId) throws OperationNotSupportedException {
        if (visitMap.containsKey(visitId)){
            Patient current = visitMap.get(visitId);
            Visit old = current.getVisits().get(visitId);
            if (old.getTypes().contains("ultrasound")){
                throw new OperationNotSupportedException("Diagnostic already added: Ultrasound");
            }
            Visit visit = new Visit(visitId, new UltrasoundDecorator(old.getCheckup()));
            visit.getTypes().addAll(old.getTypes());
            visit.getTypes().add("ultrasound");
            current.getVisits().put(visitId, visit);
        }
    }

    public void addXRay(String visitId) throws OperationNotSupportedException {
        if (visitMap.containsKey(visitId)){
            Patient current = visitMap.get(visitId);
            Visit old = current.getVisits().get(visitId);
            if (old.getTypes().contains("x-ray")){
                throw new OperationNotSupportedException("Diagnostic already added: XRay");
            }
            if (old.getTypes().contains("special")){
                Visit visit = new Visit(visitId,new XrayDecorator(old.getCheckup()));
                visit.getTypes().addAll(old.getTypes());
                visit.getTypes().add("x-ray");
                current.getVisits().put(visitId,visit);
            }else{
                throw new OperationNotSupportedException("X-ray allowed only after specialist examination");
            }
        }
    }

    public double totalCostForPatient(String patientId){
        return patientMap.get(patientId).getTotalCost();
    }

    public int numberOfVisitsForPatient(String patientId) {
        return patientMap.get(patientId).getNumVisits();
    }

    public double averageVisitCost(){
        return  patientMap.values()
                .stream()
                .flatMap(patient -> patient.getVisits().values().stream())
                .mapToDouble(Visit::visitCost)
                .average()
                .orElse(0);

    }

    public void printVisits(String patientId){
        patientMap.get(patientId).print();
    }


}


public class HospitalTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Hospital hospital = new Hospital();

        while (sc.hasNext()) {
            String command = sc.next();

            try {
                switch (command) {

                    case "generalCheckup":
                        hospital.generalCheckup(sc.next(), sc.next());
                        break;

                    case "specialist":
                        hospital.specialist(sc.next(), sc.next());
                        break;

                    case "addBloodTest":
                        hospital.addBloodTest(sc.next());
                        break;

                    case "addUrineTest":
                        hospital.addUrineTest(sc.next());
                        break;

                    case "addUltrasound":
                        hospital.addUltrasound(sc.next());
                        break;

                    case "addXRay":
                        hospital.addXRay(sc.next());
                        break;

                    case "totalCostForPatient":
                        System.out.println(
                                hospital.totalCostForPatient(sc.next())
                        );
                        break;

                    case "numberOfVisitsForPatient":
                        System.out.println(
                                hospital.numberOfVisitsForPatient(sc.next())
                        );
                        break;

                    case "averageVisitCost":
                        System.out.println(
                                hospital.averageVisitCost()
                        );
                        break;

                    case "printVisits":
                        hospital.printVisits(sc.next());
                        break;

                    case "END":
                        return;
                }
            } catch (OperationNotSupportedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
