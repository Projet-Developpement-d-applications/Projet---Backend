package projet.conquerants.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projet.conquerants.Model.*;
import projet.conquerants.Repository.*;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public Optional<Utilisateur> getUtilisateur(String pseudo) {
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

    public List<Match> getMatchsParEquipe(Equipe equipe) {
        return matchRepository.findAllByEquipe1OrEquipe2(equipe, equipe);
    }

    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public Match getMatchParId(int idMatch) {
        return matchRepository.findMatchById(idMatch);
    }

    public Match modifierMatch(Match match) {
        return matchRepository.save(match);
    }

    public void deleteMatch(Match match) {
        matchRepository.delete(match);
    }

    public List<Match> getMatchDeLaSemaine(Date debut, Date fin) {
        return matchRepository.findAllByDateBetween(debut, fin);
    }

    public Partie createPartie(Partie partie) {
        return partieRepository.save(partie);
    }

    public Partie getPartieParId(int idPartie) {
        return partieRepository.findById(idPartie);
    }

    public Partie modifierPartie(Partie partie) {
        return partieRepository.save(partie);
    }

    public List<Partie> getPartiesParMatch(Match match) {
        return partieRepository.findAllByMatch(match);
    }

    public void deleteParties(List<Partie> parties) {
        partieRepository.deleteAll(parties);
    }

    public void deletePartie(Partie partie) {
        partieRepository.delete(partie);
    }

    public Prediction createPrediction(Prediction prediction) {
        return predictionRepository.save(prediction);
    }

    public List<Prediction> getPredictionParUtilisateur(Utilisateur utilisateur) {
        return predictionRepository.findAllByUtilisateur(utilisateur);
    }

    public List<Prediction> getPredictionParMatch(Match match) {
        return predictionRepository.findAllByMatch(match);
    }

    public void modifierPrediction(Prediction p) {
        predictionRepository.save(p);
    }

    public Optional<Joueur> getJoueurParId(int id) {
        return joueurRepository.findById(id);
    }

    public Prediction getPredictionParUtilisateurEtMatch(Utilisateur utilisateur, Match match) {
        return predictionRepository.findByMatchAndUtilisateur(match, utilisateur);
    }
}
