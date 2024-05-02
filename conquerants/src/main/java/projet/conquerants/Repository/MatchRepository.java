package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Match;

public interface MatchRepository extends CrudRepository<Match, Integer> {
}
