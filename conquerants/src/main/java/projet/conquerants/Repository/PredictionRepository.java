package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Utilisateur;

import java.util.List;

public interface PredictionRepository extends CrudRepository<Prediction, Integer> {
    Prediction findById(int id);
    List<Prediction> findAllByUtilisateur(Utilisateur utilisateur);
    List<Prediction> findAllByMatch(Match match);
}
