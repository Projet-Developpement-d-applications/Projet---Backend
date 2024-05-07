package projet.conquerants.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projet.conquerants.Model.*;
import projet.conquerants.Repository.*;

import java.util.List;

@Service
public class DatabaseService {

    private final int defaultRole = 2;
    private EquipeRepository equipeRepository;
    private JeuRepository jeuRepository;
    private JoueurRepository joueurRepository;
    private MatchRepository matchRepository;
    private PartieRepository partieRepository;
    private PositionRepository positionRepository;
    private PredictionRepository predictionRepository;
    private SaisonRepository saisonRepository;
    private StatistiqueRepository statistiqueRepository;
    private UtilisateurRepository utilisateurRepository;
    private RoleRepository roleRepository;

    @Autowired
    public DatabaseService(EquipeRepository equipeRepository, JeuRepository jeuRepository, JoueurRepository joueurRepository, MatchRepository matchRepository,
                           PartieRepository partieRepository, PositionRepository positionRepository, PredictionRepository predictionRepository, RoleRepository roleRepository,
                           SaisonRepository saisonRepository, StatistiqueRepository statistiqueRepository, UtilisateurRepository utilisateurRepository) {
        this.equipeRepository = equipeRepository;
        this.jeuRepository = jeuRepository;
        this.joueurRepository = joueurRepository;
        this.matchRepository = matchRepository;
        this.partieRepository = partieRepository;
        this.positionRepository = positionRepository;
        this.predictionRepository = predictionRepository;
        this.saisonRepository = saisonRepository;
        this.statistiqueRepository = statistiqueRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;

    }

    public Role getDefaultRole() {
        return roleRepository.findById(defaultRole);
    }
    public Jeu getJeuParNom(String nom) {
        return jeuRepository.findByNom(nom);
    }

    public Saison getSaisonParDebut(String debut) {
        return saisonRepository.findByDebut(debut);
    }

    public Utilisateur getUtilisateur(String pseudo) {
        return utilisateurRepository.findByPseudo(pseudo);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public List<Equipe> getEquipeParJeu(Jeu jeu, Saison saison) {
        return equipeRepository.findByJeuAndSaison(jeu, saison);
    }

    public Equipe getEquipeParNom(String nom, Jeu jeu, Saison saison) {
        return equipeRepository.findByNomAndJeuAndSaison(nom, jeu, saison);
    }

    public Equipe getEquipeParId(int id) {
        return equipeRepository.findById(id);
    }

    public Equipe createEquipe(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    public Equipe modifyEquipe(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    public Joueur getJoueurParNom(String pseudo, Jeu jeu, Saison saison) {
        return joueurRepository.findByPseudoAndJeuAndSaison(pseudo, jeu, saison);
    }

    public List<Joueur> getJoueursParEquipe(Equipe equipe) {
        return joueurRepository.findAllByEquipe(equipe);
    }

    public Joueur createJoueur(Joueur joueur) {
        return joueurRepository.save(joueur);
    }

    public List<Joueur> createJoueurs(List<Joueur> joueurs) {
        return (List<Joueur>) joueurRepository.saveAll(joueurs);
    }

    public Joueur modifyJoueur(Joueur joueur) {
        return joueurRepository.save(joueur);
    }

    public List<Joueur> modifyJoueurs(List<Joueur> joueurs) {
        return (List<Joueur>) joueurRepository.saveAll(joueurs);
    }

    public Position getPositionParNom(String position) {
        return positionRepository.findByNom(position);
    }
}
