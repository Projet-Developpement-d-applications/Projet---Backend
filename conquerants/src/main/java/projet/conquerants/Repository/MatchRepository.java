package projet.conquerants.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Match;

import java.util.Date;
import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Integer> {

    List<Match> findAllByEquipe1OrEquipe2(Equipe equipe1, Equipe equipe2);

    Match findMatchById(int id);

    List<Match> findAllByDateBetween(Date date1, Date date2);

    List<Match> findAllByDateAfterOrderByDate(Date date);

    List<Match> findAllByJouerIs(boolean jouer);
}
