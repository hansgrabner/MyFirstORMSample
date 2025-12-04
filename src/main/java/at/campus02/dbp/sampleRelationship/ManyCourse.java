package at.campus02.dbp.sampleRelationship;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ManyCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private int ects;

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<ManyStudent> students = new ArrayList<>();

    // --- Konstruktoren ---

    public ManyCourse() {
    }

    public ManyCourse(String courseName, int ects) {
        this.courseName = courseName;
        this.ects = ects;
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    // id wird von JPA generiert, Setter optional
    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public List<ManyStudent> getStudents() {
        return students;
    }

    public void setStudents(List<ManyStudent> students) {
        this.students = students;
    }

    // optional: Helper-Methode zum Hinzufügen eines Students
    public void addStudent(ManyStudent student) {
        if (!this.students.contains(student)) {
            this.students.add(student);
        }
        if (!student.getCourses().contains(this)) {
            student.getCourses().add(this);
        }
    }

}