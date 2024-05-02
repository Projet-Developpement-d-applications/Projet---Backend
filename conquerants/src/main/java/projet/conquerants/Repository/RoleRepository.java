package projet.conquerants.Repository;

import org.springframework.data.repository.CrudRepository;
import projet.conquerants.Model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByNom(String nom);
    Role findById(int id);
}
