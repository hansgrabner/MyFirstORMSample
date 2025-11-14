package at.campus02.dbp.model;

import jakarta.persistence.*;


@Entity
@Table(name = "kleidungsstueck")
public class Kleidungsstueck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private String farbe;

    public Kleidungsstueck() {
    }

    public Kleidungsstueck(String bezeichnung, String farbe) {
        this.bezeichnung = bezeichnung;
        this.farbe = farbe;
    }

    public Long getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    @Override
    public String toString() {
        return "Kleidungsstueck{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", farbe='" + farbe + '\'' +
                '}';
    }
}