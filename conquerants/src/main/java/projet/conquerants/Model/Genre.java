package projet.conquerants.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @OneToMany(mappedBy = "genre")
    private List<Jeu> jeus = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
}
