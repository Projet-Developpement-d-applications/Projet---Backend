package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Jeu;

import java.util.List;

public interface JeuRepository extends CrudRepository<Jeu, Integer> {

    Jeu findByNom(String nom);
    List<Jeu> findAll();
}
