package at.campus02.dbp.sampleRelationship;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ManyStudent {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String firstName;

        private String lastName;

        // z.B. Matrikelnummer / Student-ID
        @Column(unique = true)
        private String matriculationNumber;

        @ManyToMany(mappedBy = "students")
        private List<ManyCourse> courses = new ArrayList<>();

        // --- Konstruktoren ---

        public ManyStudent() {
        }

        public ManyStudent(String firstName, String lastName, String matriculationNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.matriculationNumber = matriculationNumber;
        }

        // --- Getter & Setter ---

        public Long getId() {
            return id;
        }

        // id bekommt JPA, Setter kannst du optional auch weglassen
        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMatriculationNumber() {
            return matriculationNumber;
        }

        public void setMatriculationNumber(String matriculationNumber) {
            this.matriculationNumber = matriculationNumber;
        }

        public List<ManyCourse> getCourses() {
            return courses;
        }

        public void setCourses(List<ManyCourse> courses) {
            this.courses = courses;
        }

        // optional: Helper-Methode zum Hinzufügen eines Kurses
        public void addCourse(ManyCourse course) {
            if (!this.courses.contains(course)) {
                this.courses.add(course);
            }
            if (!course.getStudents().contains(this)) {
                course.getStudents().add(this);
            }
        }

}