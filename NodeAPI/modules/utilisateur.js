const bcrypt = require("bcrypt");
const saltRounds = 10;

module.exports = function (app, db) {
  app.post("/inscription", (req, res) => {
    const prenom = req.body.prenom;
    const nom = req.body.nom;
    const pseudo = req.body.pseudo;
    const mot_passe = req.body.mot_passe;
    const role = 2;

    // Regarde si le pseudo est deja existant
    db.query(
      "SELECT * FROM utilisateur WHERE pseudo = ?",
      [pseudo],
      (err, results) => {
        if (err) {
          res.send({ err: err });
          return;
        }

        if (results.length > 0) {
          // S'il existe deja renvoie un message d'erreur
          res.send({message:"Le pseudo existe déjà."});
          return;
        }

        // S'il n'existe pas créé le compte
        bcrypt.hash(mot_passe, saltRounds, (err, hash) => {
          if (err) {
            console.log(err);
            res.send(err);
            return;
          }

          db.query(
            "INSERT INTO utilisateur (prenom, nom, pseudo, mot_passe, id_role) VALUES (?,?,?,?,?)",
            [prenom, nom, pseudo, hash, role],
            (err, result) => {
              if (err) {
                res.send({ err: err });
                return;
              }

              if (result.affectedRows > 0) {
                res.send("Utilisateur créé avec succès!");
              }
            }
          );
        });
      }
    );
  });

  app.get("/connexion", (req, res) => {
    if (req.session.user) {
      res.send({ loggedIn: true, user: req.session.user });
    } else {
      res.send({ loggedIn: false });
    }
  });

  app.post("/connexion", (req, res) => {
    const pseudo = req.body.pseudo;
    const mot_passe = req.body.mot_passe;

    db.query(
      "SELECT * FROM utilisateur WHERE pseudo = ?;",
      [pseudo],
      (err, result) => {
        if (err) {
          res.send({ err: err });
          return;
        }

        if (result.length > 0) {
          bcrypt.compare(mot_passe, result[0].mot_passe, (error, response) => {
            if (response) {
              req.session.user = result;
              console.log(req.session.user[0].pseudo + " connecté!");
              res.status(200).send("Connecté avec succès!");
            } else {
              res.send({ message: "Mauvais mot de passe!" });
            }
          });
        } else {
          res.send({ message: "Cet utilisateur n'existe pas." });
        }
      }
    );
  });

  app.post("/disconnect", (req, res) => {
    req.session.destroy();

    if (!req.session) {
      res.clearCookie("sessionID");
      res.status(200).send("Déconnecté avec succès!");
    } else {
      res.status(500).send("Une erreur est survenue durant la requête.");
    }
  });
}