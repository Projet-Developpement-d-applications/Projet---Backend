package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {

    Utilisateur findByPseudo(String pseudo);
    boolean existsByPseudo(String pseudo);
}
