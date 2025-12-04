package at.campus02.dbp;

import at.campus02.dbp.model.*;
import at.campus02.dbp.sampleRelationship.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {

        BeispielDepartmentEmployees();
        //BeispielKleidngsstuecke();
        //BeispielSchrank();
       // BeispielHQL();
        //BeispielUserBooks();
    }

    public static void BeispielSchrank(){
        Schrank s1 =new  Schrank();
        s1.setStandort(Standort.WOHNZIMMER);
        s1.setMaxAnzahlAnKleidungsstuecken(50);
        SchrankDAOImpl schrankDAOImpl = new SchrankDAOImpl();
        schrankDAOImpl.save(s1);
        Schrank s2 = schrankDAOImpl.findById(s1.getId());
        s2.setStandort(Standort.KUECHE);
        // schrankDAOImpl.save(s2); //Fehler weil detacted
        schrankDAOImpl.update(s2); //OK
    }
    public static void BeispielKleidngsstuecke(){
        Kleidungsstueck ks = new Kleidungsstueck();
        ks.setBezeichnung("Handschuhe");
        ks.setFarbe("Rot");



        KleidungsstueckDAO dao=new KleidungsstueckDAO();
        //transiend - ruft session.persist auf
        dao.save(ks);
        ks.setFarbe("Orange");
        dao.update(ks); //merge von Hibernate

        Kleidungsstueck orangeHandschuhe =  dao.findById(9L);
        orangeHandschuhe.setFarbe("Grün");
        dao.update(orangeHandschuhe);

        Kleidungsstueck ksx =dao.findById(5L);
        dao.delete(ksx);

        Kleidungsstueck ksCopy =new Kleidungsstueck(); //transient
        ksCopy.setFarbe(ksx.getFarbe());
        ksCopy.setBezeichnung(ksx.getBezeichnung());
        ksCopy.setFarbe("GElb");
        dao.save(ksCopy);
    }
    public static void KleidungsstueckDAO(){
        KleidungsstueckDAO k = new KleidungsstueckDAO();
        Kleidungsstueck k1 = new Kleidungsstueck();
        k1.setBezeichnung("Hose");
        k1.setFarbe("Blau");
        k.update(k1);
    }
    public static void BeispielUserBooks()
    {
        insertUser("Max", "maxi123");
        insertUser("Anna", "annie");
        listAllUsers();
        // Books einfügen
        insertBook("978-3-16-148410-0", "Hibernate Basics");
        insertBook("978-1-23-456789-7", "Advanced Hibernate");


        listAllBooks();
        listAllUserAliases();


        User user1 = getUserWithId_session_get(1);
        Book book1 = getBookWithId_session_get(2);

        addFavoriteBook(user1,book1,9);
        listAllFavoriteBooks();
    }
    public static void BeispielHQL(){

        /*HQL verwenden 27.11.2025
        https://www.tutorialspoint.com/hibernate/hibernate_query_language.htm
        WHERE, GROUP BY, Aggregate, Pagination , ORDER BY, Alias,*/

        SchrankQuery schrankQuery =new SchrankQuery();

        List<Object[]> aggErgebnis= schrankQuery.groupSchrankByOrt(Standort.WOHNZIMMER);


        List<Schrank> pagList = schrankQuery.showSchrankPagination();
        System.out.println("Alle Pag");
        for (Schrank schrank : pagList) {
            System.out.println(schrank);
        };

        List<Schrank> schraenkeSortiert = schrankQuery.findAllOrderedByMaxAmount(false);
        System.out.println("Alle Schränke sortiert:");
        for (Schrank schrank : schraenkeSortiert) {
            System.out.println(schrank);
        };


        List<Schrank> schraenkeSortiertImWohnzimmer = schrankQuery.getAlleSchraenkeWhere(Standort.WOHNZIMMER);

        List<Schrank> ergebnisAlle = schrankQuery.getAlleSchraenke();
        System.out.println("Alle Schränke:");
        for (Schrank schrank : ergebnisAlle) {
            System.out.println(schrank);
        };

        List<Schrank> ergebnisWohnzimmer = schrankQuery.findSchraenkeByStandort(Standort.WOHNZIMMER);
        System.out.println("Schränke im Wohnzimmer:");
        for (Schrank schrank : ergebnisWohnzimmer) {
            System.out.println(schrank);
        };

        List<Schrank> ergebnisCriteria = schrankQuery.findSchraenkeByStandortCriteria(Standort.WOHNZIMMER);
        System.out.println("Schränke im ergebnisCriteria:");
        for (Schrank schrank : ergebnisCriteria) {
            System.out.println(schrank);
        };

        List<Schrank> ergebnisWhereV2 = schrankQuery.findSchraenkeByStandortUndMaxAnzahl(Standort.WOHNZIMMER,60);
        System.out.println("Schränke im ergebnisWhereV2:");
        for (Schrank schrank : ergebnisWhereV2) {
            System.out.println(schrank);
        };

        List<Schrank> ergebnisCriterieV2 = schrankQuery.findSchraenkeByStandortUndMaxAnzahlCriteria(Standort.WOHNZIMMER,60);
        System.out.println("Schränke im ergebnisCriterieV2:");
        for (Schrank schrank : ergebnisCriterieV2) {
            System.out.println(schrank);
        };
    }

    public static void BeispielDepartmentEmployees(){
        Department d1 =new Department();
        d1.setDepartmentName("Softwareentwicklung");
        Employee e1 =new Employee();
        e1.setFirstName("Anna");

        Employee e2 = new Employee();
        e2.setFirstName("Inna");

        // 3) Employees dem Department zuordnen
        List<Employee> mitarbeiter = new ArrayList<>();
        mitarbeiter.add(e1);
        mitarbeiter.add(e2);

        d1.setEmployees(mitarbeiter);

        DepartmentDAO deptarmentDAO =new DepartmentDAO();
         deptarmentDAO.save(d1);

        Department ausDb = deptarmentDAO.findById(1L);

        System.out.println("Department: " + ausDb.getDepartmentName());
        System.out.println("Mitarbeiter:");

        //Bei Eager
        /*
        if (ausDb.getEmployees() != null) {
            for (Employee e : ausDb.getEmployees()) {
                System.out.println(" - " + e.getFirstName() + " (ID: " + e.getId() + ")");
            }
        } else {
            System.out.println("Keine Mitarbeiter gefunden.");
        }
*/
        /*
        System.out.println("Mitarbeiter:");
        for (Employee e : ausDb.getEmployees()) {
            System.out.println(" - " + e.getFirstName());
        }
*/
        //ohne Eager
        ausDb = deptarmentDAO.findByIdWithEmployees(1L);

        System.out.println("Department: " + ausDb.getDepartmentName());
        System.out.println("Mitarbeiter:");
        for (Employee e : ausDb.getEmployees()) {
            System.out.println(" - " + e.getFirstName());
            System.out.println(" - " + e.getDepartment().getDepartmentName());


        }
    }

        public static void beispielCoursesStudents() {

            // 1) Kurs anlegen
            ManyCourse kurs1 = new ManyCourse();
            kurs1.setCourseName("Softwareentwicklung");
            kurs1.setEcts(6);

            // 2) Students anlegen
            ManyStudent s1 = new ManyStudent();
            s1.setFirstName("Anna");
            s1.setLastName("Muster");
            s1.setMatriculationNumber("s12345");

            ManyStudent s2 = new ManyStudent();
            s2.setFirstName("Inna");
            s2.setLastName("Beispiel");
            s2.setMatriculationNumber("s67890");

            // 3) Students dem Kurs zuordnen (Owning Side = ManyCourse.students)
            List<ManyStudent> teilnehmer = new ArrayList<>();
            teilnehmer.add(s1);
            teilnehmer.add(s2);
            kurs1.setStudents(teilnehmer);

            // inverse Seite (optional, aber sauber)
            List<ManyCourse> kurseS1 = new ArrayList<>();
            kurseS1.add(kurs1);
            s1.setCourses(kurseS1);

            List<ManyCourse> kurseS2 = new ArrayList<>();
            kurseS2.add(kurs1);
            s2.setCourses(kurseS2);

            // 4) DAO verwenden, um zu speichern
            ManyCourseStudentDAO dao = new ManyCourseStudentDAO();

            // Wichtig: Owning Side (kurs1) speichern, damit Join-Tabelle befüllt wird
            dao.saveCourse(kurs1);
            // (Optional: Students extra speichern – je nach Cascade-Settings)
            // dao.saveStudent(s1);
            // dao.saveStudent(s2);

            // 5) Kurs ohne JOIN FETCH laden (nur zur Demo – wie bei deinem Beispiel)
            ManyCourse kursAusDb = dao.findCourseById(kurs1.getId());
            System.out.println("Kurs (ohne JOIN FETCH): " + kursAusDb.getCourseName());

            // 6) Kurs MIT Students laden (analog zu findByIdWithEmployees)
            kursAusDb = dao.findCourseByIdWithStudents(kurs1.getId());

            System.out.println("Kurs: " + kursAusDb.getCourseName());
            System.out.println("Ects: " + kursAusDb.getEcts());
            System.out.println("Teilnehmende Studierende:");

            for (ManyStudent s : kursAusDb.getStudents()) {
                System.out.println(" - " + s.getFirstName() + " " + s.getLastName()
                        + " (ID: " + s.getId() + ", Matrikelnummer: " + s.getMatriculationNumber() + ")");
            }

            // 7) Optional: Einen Student inkl. seiner Kurse laden
            ManyStudent studentAusDb = dao.findStudentByIdWithCourses(s1.getId());

            System.out.println("\nStudent: " + studentAusDb.getFirstName() + " " + studentAusDb.getLastName());
            System.out.println("Belegte Kurse:");
            for (ManyCourse c : studentAusDb.getCourses()) {
                System.out.println(" - " + c.getCourseName() + " (" + c.getEcts() + " ECTS)");
            }
        }

    public static void BeispielCoursesStudents() {

        // 1) Kurs anlegen
        ManyCourse kurs1 = new ManyCourse();
        kurs1.setCourseName("Softwareentwicklung");
        kurs1.setEcts(6);

        // 2) Students anlegen
        ManyStudent s1 = new ManyStudent();
        s1.setFirstName("Anna");
        s1.setLastName("Muster");
        s1.setMatriculationNumber("s12345");

        ManyStudent s2 = new ManyStudent();
        s2.setFirstName("Inna");
        s2.setLastName("Beispiel");
        s2.setMatriculationNumber("s67890");

        // 3) Students dem Kurs zuordnen (Owning Side = ManyCourse.students)
        List<ManyStudent> teilnehmer = new ArrayList<>();
        teilnehmer.add(s1);
        teilnehmer.add(s2);
        kurs1.setStudents(teilnehmer);

        // inverse Seite (optional, aber sauber)
        List<ManyCourse> kurseS1 = new ArrayList<>();
        kurseS1.add(kurs1);
        s1.setCourses(kurseS1);

        List<ManyCourse> kurseS2 = new ArrayList<>();
        kurseS2.add(kurs1);
        s2.setCourses(kurseS2);

        // 4) DAO verwenden, um zu speichern
        ManyCourseStudentDAO dao = new ManyCourseStudentDAO();

        // Wichtig: Owning Side (kurs1) speichern, damit Join-Tabelle befüllt wird
        dao.saveCourse(kurs1);
        // (Optional: Students extra speichern – je nach Cascade-Settings)
        // dao.saveStudent(s1);
        // dao.saveStudent(s2);

        // 5) Kurs ohne JOIN FETCH laden (nur zur Demo – wie bei deinem Beispiel)
        ManyCourse kursAusDb = dao.findCourseById(kurs1.getId());
        System.out.println("Kurs (ohne JOIN FETCH): " + kursAusDb.getCourseName());

        // 6) Kurs MIT Students laden (analog zu findByIdWithEmployees)
        kursAusDb = dao.findCourseByIdWithStudents(kurs1.getId());

        System.out.println("Kurs: " + kursAusDb.getCourseName());
        System.out.println("Ects: " + kursAusDb.getEcts());
        System.out.println("Teilnehmende Studierende:");

        for (ManyStudent s : kursAusDb.getStudents()) {
            System.out.println(" - " + s.getFirstName() + " " + s.getLastName()
                    + " (ID: " + s.getId() + ", Matrikelnummer: " + s.getMatriculationNumber() + ")");
        }

        // 7) Optional: Einen Student inkl. seiner Kurse laden
        ManyStudent studentAusDb = dao.findStudentByIdWithCourses(s1.getId());

        System.out.println("\nStudent: " + studentAusDb.getFirstName() + " " + studentAusDb.getLastName());
        System.out.println("Belegte Kurse:");
        for (ManyCourse c : studentAusDb.getCourses()) {
            System.out.println(" - " + c.getCourseName() + " (" + c.getEcts() + " ECTS)");
        }
    }
    public static void BeispielVorlesungStudent(){

        VorlesungDAO vorlesungDAO = new VorlesungDAO();

        // 1) Vorlesung anlegen
        Vorlesung v1 = new Vorlesung();
        v1.setTitel("Einführung in Hibernate");

        // 2) Studierende anlegen
        Student s1 = new Student();
        s1.setVorname("Anna");

        Student s2 = new Student();
        s2.setVorname("Lukas");

        // 3) Beziehung setzen (BEIDSEITIG über Helper-Methode)
        v1.addStudent(s1);
        v1.addStudent(s2);

        // 4) speichern -> cascade = ALL speichert auch die Studenten
        vorlesungDAO.save(v1);

        // ========================
        // Navigation 1 -> n Seite
        // ========================
        Vorlesung vorlesungAusDb = vorlesungDAO.findById(1L);
        System.out.println("Vorlesung: " + vorlesungAusDb.getTitel());
        System.out.println("Studierende der Vorlesung:");
        vorlesungAusDb.getStudierende().forEach(st ->
                System.out.println(" - " + st.getVorname() + " (ID: " + st.getId() + ")")
        );


        Student studentAusDB = vorlesungAusDb.getStudierende().get(0);
        System.out.println("Student: " + studentAusDB.getVorname());
        System.out.println("gehört zu Vorlesung: " + studentAusDB.getVorlesung().getTitel());


    }
    // Methode zum Einfügen eines Users
    public static void insertUser(String firstname, String alias) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = new User();
            user.setFirstname(firstname);
            user.setAlias(alias);

            session.persist(user);

            session.getTransaction().commit();
            System.out.println("User gespeichert: " + user);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // Methode zum Auslesen aller User
    public static void listAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users;
            users = session.createQuery("SELECT u.firstname from User u", User.class)
                    .list();
            System.out.println("Alle User aus der DB:");
            for (User u : users) {
                System.out.println(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertBook(String isbn, String title) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(title);

            session.persist(book);
            session.getTransaction().commit();

            System.out.println("Book gespeichert: " + book);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listAllBooks() {
        try (Session session = sessionFactory.openSession()) {
            List<Book> books = session.createQuery("from Book", Book.class).list();
            System.out.println("Alle Books in der DB:");
            for (Book b : books) {
                System.out.println(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listAllUserAliases() {
        try (Session session = sessionFactory.openSession()) {
            // HQL: nur die alias-Spalte abfragen
            List<String> aliases = session.createQuery("SELECT u.alias FROM User u", String.class)
                    .list();

            System.out.println("Alle User-Aliases:");
            for (String alias : aliases) {
                System.out.println(alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUserWithId_createQuery(int id) {
        try (Session session = sessionFactory.openSession()) {
            // HQL-Abfrage: User mit bestimmter ID
            User user = session.createQuery("FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .uniqueResult();  // gibt genau einen User oder null zurück

            if (user != null) {
                System.out.println("User mit ID " + id + ": " + user);
            } else {
                System.out.println("Kein User mit ID " + id + " gefunden.");
            }
            return  user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static User getUserWithId_session_get(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, (long) id);  // ID muss Long sein, falls dein User id ein Long ist

            if (user != null) {
                System.out.println("User mit ID " + id + ": " + user);
            } else {
                System.out.println("Kein User mit ID " + id + " gefunden.");
            }
            return  user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static Book getBookWithId_session_get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.get(Book.class, (long) id);
            return  book;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void addFavoriteBook(User user, Book book, int stars) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            FavoriteBook favorite = new FavoriteBook();
            favorite.setUser(user);
            favorite.setBook(book);
            favorite.setStars(stars);

            session.persist(favorite);
            session.getTransaction().commit();

            System.out.println("FavoriteBook gespeichert: " + favorite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listAllFavoriteBooks() {
        try (Session session = sessionFactory.openSession()) {
            List<FavoriteBook> favorites = session.createQuery("FROM FavoriteBook", FavoriteBook.class).list();
            System.out.println("Alle Lieblingsbücher:");
            for (FavoriteBook f : favorites) {
                System.out.println(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TestKleidungsstuecke(){

        KleidungsstueckDAO dao = new KleidungsstueckDAO();

        // CREATE
        Kleidungsstueck ks = new Kleidungsstueck("Jacke", "Blau");
        dao.save(ks);
        System.out.println("Gespeichert: " + ks);

        // READ
        Kleidungsstueck gefunden = dao.findById(ks.getId());
        System.out.println("Gefunden: " + gefunden);

        // UPDATE
        gefunden.setFarbe("Rot");
        dao.update(gefunden);
        System.out.println("Aktualisiert: " + dao.findById(gefunden.getId()));

        // DELETE
        dao.delete(gefunden);
        System.out.println("Nach dem Löschen: " + dao.findById(gefunden.getId()));
    }



}