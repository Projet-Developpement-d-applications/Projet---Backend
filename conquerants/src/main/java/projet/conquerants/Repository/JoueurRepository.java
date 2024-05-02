package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Joueur;
import projet.conquerants.Model.Saison;

import java.util.List;

public interface JoueurRepository extends CrudRepository<Joueur, Integer> {
    Joueur findByPseudoAndJeuAndSaison(String pseudo, Jeu jeu, Saison saison);
    List<Joueur> findAllByEquipe(Equipe equipe);
}
