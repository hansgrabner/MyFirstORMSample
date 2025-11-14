package at.campus02.dbp;
import at.campus02.dbp.model.Kleidungsstueck;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg = new Configuration().configure();
            cfg.addAnnotatedClass(Kleidungsstueck.class);

            sessionFactory = cfg.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(cfg.getProperties())
                            .build());
        }
        return sessionFactory;
    }
}
