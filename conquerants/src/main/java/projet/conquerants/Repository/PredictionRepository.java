package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Prediction;

public interface PredictionRepository extends CrudRepository<Prediction, Integer> {
}
