package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;

import java.util.List;

public interface SaisonRepository extends CrudRepository<Saison, Integer> {

    Saison findByDebut(String debut);

    List<Saison> findAll();
}
