package projet.conquerants.Model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Developpeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @OneToMany(mappedBy = "dev")
    private List<Jeu> jeus = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Developpeur{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
