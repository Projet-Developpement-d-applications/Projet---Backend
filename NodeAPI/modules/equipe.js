module.exports = function (app, db) {
    app.post("/ajouterEquipe", (req, res) => {
        const nom = req.body.nom;
        const division = req.body.division;
        const jeu = req.body.id_jeu;
        const saison = req.body.id_saison;
  
        db.query(
            "INSERT INTO equipe (nom, division, id_jeu, id_saison) VALUES (?,?,?,?)",
            [nom, division, jeu, saison],
            (err, result) => {
            if (err) {
                res.send({ err: err });
                return;
            }

            if (result.affectedRows > 0) {
                res.send("Équipe ajoutée avec succès!")
            }
            }
        );  
    });
  
    app.get("/obtenirToutesEquipes", (req, res) => {
      db.query(
        "SELECT * FROM equipe;",
        (err, result) => {
            if (err) {
                res.send({err: err });
                return;
            }

            if (result.length > 0) {
                res.send(result);
            } else {
                res.send({ message: "Il n'y a aucune équipe." })
            }
        }
      );
    });
}