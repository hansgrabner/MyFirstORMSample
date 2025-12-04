package at.campus02.dbp.sampleRelationship;

import at.campus02.dbp.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManyCourseStudentDAO {

    // =========================
    //  Basis-Methoden: findById
    // =========================

    public ManyCourse findCourseById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ManyCourse.class, id);
        }
    }

    public ManyStudent findStudentById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ManyStudent.class, id);
        }
    }

    // =========================
    //  Basis-Methoden: save(...)
    // =========================

    public ManyCourse saveCourse(ManyCourse course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
            return course;
        }
    }

    public ManyStudent saveStudent(ManyStudent student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
            return student;
        }
    }

    // ============================================
    //  Laden inkl. Beziehung (JOIN FETCH / Lazy)
    // ============================================

    /**
     * Lädt einen Course inkl. seiner Students.
     * Entspricht vom Stil her: findByIdWithEmployees(...)
     */
    public ManyCourse findCourseByIdWithStudents(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select distinct c from ManyCourse c " +
                    "left join fetch c.students " +
                    "where c.id = :id";

            return session.createQuery(hql, ManyCourse.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    /**
     * Lädt einen Student inkl. seiner Courses.
     */
    public ManyStudent findStudentByIdWithCourses(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select distinct s from ManyStudent s " +
                    "left join fetch s.courses " +
                    "where s.id = :id";

            return session.createQuery(hql, ManyStudent.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    // ============================================
    //  Optionale Komfort-Methoden: Listen
    // ============================================

}