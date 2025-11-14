package at.campus02.dbp;

import at.campus02.dbp.model.Kleidungsstueck;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KleidungsstueckDAO {

    public void save(Kleidungsstueck ks) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(ks);
            tx.commit();
        }
    }

    public Kleidungsstueck findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Kleidungsstueck.class, id);
        }
    }

    public void update(Kleidungsstueck ks) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(ks);
            tx.commit();
        }
    }

    public void delete(Kleidungsstueck ks) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(ks);
            tx.commit();
        }
    }
}
