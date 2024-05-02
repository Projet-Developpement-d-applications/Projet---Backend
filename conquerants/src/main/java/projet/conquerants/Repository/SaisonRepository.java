package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Saison;

public interface SaisonRepository extends CrudRepository<Saison, Integer> {

    Saison findByDebut(String debut);
}
