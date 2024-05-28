package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByPseudo(String pseudo);
    boolean existsByPseudo(String pseudo);

    Utilisateur findByPseudoAndMotPasse(String pseudo, String mot_passe);
}
