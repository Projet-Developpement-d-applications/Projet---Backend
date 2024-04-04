module.exports = function (app, db) {
  app.post("/ajouterJoueur", (req, res) => {
    const prenom = req.body.prenom;
    const nom = req.body.nom;
    const pseudo = req.body.pseudo;
    const date_naissance = req.body.date_naissance;
    const position = req.body.id_position;
    const equipe = req.body.id_equipe;
    const jeu = req.body.id_jeu;
    const saison = req.body.id_saison;

    db.query(
      "INSERT INTO joueur (prenom, nom, pseudo, date_naissance, id_position, id_equipe, id_jeu, id_saison) VALUES (?,?,?,?,?,?,?,?)",
      [prenom, nom, pseudo, date_naissance, position, equipe, jeu, saison],
      (err, result) => {
        if (err) {
          res.send({ err: err });
          return;
        }

        if (result.affectedRows > 0) {
            res.send("Joueur ajouté avec succès!")
        }
      }
    );  
  });

  app.post("/modifierJoueur", (req, res) => {
    const id = req.body.id;
    const nouveauPrenom = req.body.prenom;
    const nouveauNom = req.body.nom;
    const nouveauPseudo = req.body.pseudo;
    const nouvelleDateNaissance = req.body.date_naissance;
    const nouvellePosition = req.body.id_position;
    const nouvelleEquipe = req.body.id_equipe;
    const nouveauJeu = req.body.id_jeu;
    const nouvelleSaison = req.body.id_saison;

    db.query(
        "UPDATE joueur SET prenom = ?, nom = ?, pseudo = ?, date_naissance = ?, id_position = ?, id_equipe = ?, id_jeu = ?, id_saison = ? WHERE id = ?",
        [nouveauPrenom, nouveauNom, nouveauPseudo, nouvelleDateNaissance, nouvellePosition, nouvelleEquipe, nouveauJeu, nouvelleSaison, id],
        (err, result) => {
            if (err) {
                res.send({ err: err });
                return;
            }

            if (result.affectedRows > 0) {
                res.send("Joueur modifié avec succès!");
            } else {
                res.send("Aucun joueur trouvé avec cet identifiant.");
            }
        }
    );
});

  app.get("/obtenirToutJoueurs", (req, res) => {
    db.query(
      "SELECT * FROM joueur;",
      (err, result) => {
          if (err) {
              res.send({err: err });
              return;
          }

          if (result.length > 0) {
              res.send(result);
          } else {
              res.send({ message: "Il n'y a aucun joueur." })
          }
      }
    );
  });
}