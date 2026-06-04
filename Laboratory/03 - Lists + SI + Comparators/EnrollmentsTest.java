import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Applicant {
    private int id;
    private String name;
    private double gpa;
    private List<SubjectWithGrade> subjectsWithGrade;
    private StudyProgramme studyProgramme;
    private Faculty faculty;

    public Applicant(int id, String name, double gpa, StudyProgramme studyProgramme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.studyProgramme = studyProgramme;
        this.subjectsWithGrade = new ArrayList<>();
        this.faculty = this.studyProgramme.getFaculty();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }
    public List<SubjectWithGrade> getSubjectsWithGrade() { return subjectsWithGrade; }
    public StudyProgramme getStudyProgramme() { return studyProgramme; }
    public Faculty getFaculty() { return faculty; }

    public void addSubjectAndGrade(String subject, int grade) {
        subjectsWithGrade.add(new SubjectWithGrade(subject, grade));
    }

    public void setSubjectsWithGrade(List<SubjectWithGrade> subjectsWithGrade) {
        this.subjectsWithGrade = subjectsWithGrade;
    }

    public double calculatePoints() {
        double result = gpa * 12;
        for (SubjectWithGrade subject : subjectsWithGrade) {
            if (faculty.getAppropriateSubjects().contains(subject.getSubject())) {
                result += subject.getGrade() * 2;
            } else {
                result += subject.getGrade() * 1.2;
            }
        }
        return result;
    }


    @Override
    public String toString() {
        return String.format("Id: %d, Name: %s, GPA: %.1f - %s",
                id, name, gpa, calculatePoints());
    }
}


class StudyProgramme {
    private String code;
    private String name;
    private int numPublicQuota;
    private int numPrivateQuota;
    private int enrolledInPublicQuota = 0;
    private int enrolledInPrivateQuota = 0;
    private Faculty faculty;
    private List<Applicant> applicants;
    private List<Applicant> enrolledPublic;
    private List<Applicant> enrolledPrivate;
    private List<Applicant> rejected;

    public StudyProgramme(String code, String name, Faculty faculty,
                          int numPublicQuota, int numPrivateQuota) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.numPublicQuota = numPublicQuota;
        this.numPrivateQuota = numPrivateQuota;
        this.applicants = new ArrayList<>();
        this.enrolledPublic = new ArrayList<>();
        this.enrolledPrivate = new ArrayList<>();
        this.rejected = new ArrayList<>();
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getNumPublicQuota() { return numPublicQuota; }
    public int getNumPrivateQuota() { return numPrivateQuota; }
    public int getEnrolledInPublicQuota() { return enrolledInPublicQuota; }
    public int getEnrolledInPrivateQuota() { return enrolledInPrivateQuota; }
    public Faculty getFaculty() { return faculty; }
    public List<Applicant> getApplicants() { return applicants; }
    public List<Applicant> getEnrolledPublic() { return enrolledPublic; }
    public List<Applicant> getEnrolledPrivate() { return enrolledPrivate; }
    public List<Applicant> getRejected() { return rejected; }


    public int getTotalAppropriateSubjects() {
        return faculty.getAppropriateSubjects().size();
    }

    public double acceptancePercentage() {
        return (1.0 * (enrolledInPublicQuota + enrolledInPrivateQuota)
                / (1.0 * (numPublicQuota + numPrivateQuota))) * 100;
    }

    public void addApplicant(Applicant applicant) {
        applicants.add(applicant);
    }

    public void calculateEnrollmentNumbers() {
        List<Applicant> sorted = applicants.stream()
                .sorted(Comparator.comparingDouble(Applicant::calculatePoints).reversed())
                .collect(Collectors.toList());

        enrolledPublic = sorted.stream()
                .limit(numPublicQuota)
                .collect(Collectors.toList());
        enrolledInPublicQuota = enrolledPublic.size();


        enrolledPrivate = sorted.stream()
                .skip(enrolledInPublicQuota)
                .limit(numPrivateQuota)
                .collect(Collectors.toList());
        enrolledInPrivateQuota = enrolledPrivate.size();

        rejected = sorted.stream()
                .skip(enrolledInPublicQuota + enrolledInPrivateQuota)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append('\n');
        sb.append("Public Quota:").append('\n');
        for (Applicant a : enrolledPublic) sb.append(a.toString()).append('\n');
        sb.append("Private Quota:").append('\n');
        for (Applicant a : enrolledPrivate) sb.append(a.toString()).append('\n');
        sb.append("Rejected:").append('\n');
        for (Applicant a : rejected) sb.append(a.toString()).append('\n');
        return sb.toString();
    }
}


class Faculty {
    private String shortName;
    List<String> appropriateSubjects;
    List<StudyProgramme> studyProgrammes;

    public Faculty(String shortName) {
        this.shortName = shortName;
        this.appropriateSubjects = new ArrayList<>();
        this.studyProgrammes = new ArrayList<>();
    }

    public String getShortName() { return shortName; }
    public List<String> getAppropriateSubjects() { return appropriateSubjects; }
    public List<StudyProgramme> getStudyProgrammes() { return studyProgrammes; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Faculty: ").append(shortName).append('\n');
        sb.append("Subjects: [");
        appropriateSubjects.forEach(s -> sb.append(s).append(", "));
        sb.setLength(sb.length() - 2);
        sb.append("]").append('\n');

        sb.append("Study Programmes:").append('\n');


        studyProgrammes.stream()
                .sorted(Comparator
                        .comparingInt((StudyProgramme sp) -> appropriateSubjects.size())
                        .thenComparingDouble((StudyProgramme sp) -> -sp.acceptancePercentage()))
                .forEach(s -> sb.append(s.toString()).append('\n'));

        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public void addStudyProgramme(StudyProgramme sp) { studyProgrammes.add(sp); }
    public void addSubject(String subject) { appropriateSubjects.add(subject); }
}


class SubjectWithGrade {
    private String subject;
    private int grade;

    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() { return subject; }
    public int getGrade() { return grade; }
}


class EnrollmentsIO {

    public static void printRanked(List<Faculty> faculties) {
        PrintWriter pw = new PrintWriter(System.out);
        for (Faculty faculty : faculties) pw.println(faculty);
        pw.flush();
    }

    public static void readEnrollments(List<StudyProgramme> studyProgrammes,
                                       InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        while (true) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) break;

            String[] parts = line.trim().split(";");

            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double gpa = Double.parseDouble(parts[2]);
            List<SubjectWithGrade> subjects = new ArrayList<>();
            for (int i = 3; i <= 9; i += 2) {
                subjects.add(new SubjectWithGrade(parts[i], Integer.parseInt(parts[i + 1])));
            }
            String code = parts[11];

            studyProgrammes.stream()
                    .filter(sp -> sp.getCode().equals(code))
                    .forEach(sp -> {
                        Applicant app = new Applicant(id, name, gpa, sp);
                        app.setSubjectsWithGrade(subjects);
                        sp.addApplicant(app);
                    });
        }
    }
}


public class EnrollmentsTest {

    public static void main(String[] args) throws IOException {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);
    }
}