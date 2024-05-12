package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;

import java.util.List;

public interface PartieRepository extends CrudRepository<Partie, Integer> {

    Partie findById(int id_partie);
    List<Partie> findAllByMatch(Match match);
}
