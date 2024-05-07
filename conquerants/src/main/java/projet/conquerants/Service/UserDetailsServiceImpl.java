package projet.conquerants.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projet.conquerants.Repository.UtilisateurRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByPseudo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }
}
