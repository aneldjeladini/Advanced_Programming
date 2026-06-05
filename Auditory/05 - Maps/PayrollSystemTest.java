import java.io.*;
import java.util.*;


abstract class Employee implements Comparable<Employee>{
    protected String id;
    protected String level;
    protected double rate;

    public Employee(String id, String level,double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getRate() {
        return rate;
    }

    public abstract double calculateSalary();


    @Override
    public int compareTo(Employee o){
        return Comparator
                .comparing(Employee::calculateSalary,Comparator.reverseOrder())
                .thenComparing(Employee::getLevel)
                .compare(this,o);
    }
}

class HourlyEmployee extends Employee{
    private int hours;

    public HourlyEmployee(String id,String level,double rate,int hours){
        super(id,level,rate);
        this.hours = hours;
    }


    @Override
    public double calculateSalary() {
        if (hours <= 40){
            return hours * rate;
        }
        else{
            return (40 * rate) + (hours - 40) * 1.5;
        }
    }
}

class FreelanceEmployee extends Employee{
    List<Integer> ticketPoints;

    public FreelanceEmployee(String id,String level,double rate,List<Integer> ticketPoints){
        super(id,level,rate);
        this.ticketPoints = ticketPoints;
    }

    public List<Integer> getTicketPoints() {
        return ticketPoints;
    }

    @Override
    public double calculateSalary() {
        return ticketPoints.stream().
                mapToDouble(Integer::doubleValue)
                .sum() * rate;
    }
}


class PayrollSystem{
    private Map<String,Double> hourlyRateByLevel;
    private Map<String,Double> ticketRateByLevel;
    private List<Employee> employees;

    public PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }

    public void readEmployeesData(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        while (true){
            String line = br.readLine();
            if (line == null || line.isEmpty())break;

            String [] parts = line.trim().split(";");
            if (parts[0].equals("H")){
                if (hourlyRateByLevel.containsKey(parts[2])){
                    double rate = hourlyRateByLevel.get(parts[2]);
                    employees.add(new HourlyEmployee(parts[1],parts[2],rate,Integer.parseInt(parts[3])));
                }

            }
            else{
                String id = parts[1];
                String level = parts[2];
                List<Integer> points = new ArrayList<>();
                for (int i = 3; i < parts.length; i++){
                    points.add(Integer.parseInt(parts[i]));
                }
                if (ticketRateByLevel.containsKey(level)){
                    double rate = ticketRateByLevel.get(level);
                    employees.add(new FreelanceEmployee(id,level,rate,points));
                }
            }
        }
    }

    public Map<String,Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels){
        Map<String,Set<Employee>> employeesByLevel = new HashMap<>();

        for (Employee employee : employees){
            employeesByLevel.computeIfAbsent(employee.getLevel(), k -> new HashSet<>()).add(employee);
        }

        Map<String, Set<Employee>> result = new HashMap<>();
        for (String lvl : levels){
            Set<Employee> set = employeesByLevel.get(lvl);
            if (set != null){
                result.put(lvl,set);
            }
        }
        return result;
    }

    public Map<String,Double> totalPayPerEmployee(){
        Map<String, Double> totals = new HashMap<>();

        for (Employee e : employees){
            if (!totals.containsKey(e.getId())){
                totals.putIfAbsent(e.getId(),e.calculateSalary());
            }else{
                double currentValue = totals.get(e.getId());
                currentValue += e.calculateSalary();
                totals.put(e.getId(),currentValue);
            }
            totals.merge(e.getId(),e.calculateSalary(),Double::sum);
        }

        return totals;
    }

}




public class PayrollSystemTest {

    public static void main(String[] args) throws IOException {
        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployeesData(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, emps) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            emps.forEach(System.out::println);
            System.out.println("------------");
        });

        System.out.println("TOTAL PAY PER EMPLOYEE:");
        payrollSystem.totalPayPerEmployee().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> System.out.printf("%s -> %.2f%n", e.getKey(), e.getValue()));
    }
}