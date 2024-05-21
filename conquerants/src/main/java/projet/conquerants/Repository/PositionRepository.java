package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Position;
import projet.conquerants.Model.Saison;

import java.util.List;

public interface PositionRepository extends CrudRepository<Position, Integer> {
    Position findByNom(String position);

    List<Position> findAll();

    List<Position> findByJeu(Jeu jeu);
}
