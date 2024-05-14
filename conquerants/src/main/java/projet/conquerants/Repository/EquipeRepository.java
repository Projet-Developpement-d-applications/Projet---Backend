package projet.conquerants.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;

import java.util.List;

public interface EquipeRepository extends CrudRepository<Equipe, Integer> {
    List<Equipe> findByJeuAndSaison(Jeu jeu, Saison saison);
    Equipe findByNomAndJeuAndSaison(String nom, Jeu jeu, Saison saison);
    Equipe findById(int id);
}
