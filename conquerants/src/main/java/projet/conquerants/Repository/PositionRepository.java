package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {
    Position findByNom(String position);
}
