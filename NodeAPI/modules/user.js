const bcrypt = require("bcrypt");
const saltRounds = 10;

module.exports = function (app, db) {
  app.post("/register", (req, res) => {
    // to change
    const username = req.body.username;
    const password = req.body.password;

    bcrypt.hash(password, saltRounds, (err, hash) => {
      if (err) {
        console.log(err);
        res.send(err);
        return;
      }

      db.query(
        "INSERT INTO user (username, password) VALUES (?,?)",
        [username, hash],
        (err, result) => {
          if (err) {
            res.send({ err: err });
            return;
          }

          if (result.affectedRows > 0) {
            res.send("User created successfully")
          }
        }
      );
    });
  });

  app.get("/login", (req, res) => {
    if (req.session.user) {
      res.send({ loggedIn: true, user: req.session.user });
    } else {
      res.send({ loggedIn: false });
    }
  });

  app.post("/login", (req, res) => {
    const username = req.body.username;
    const password = req.body.password;

    db.query(
      "SELECT * FROM user WHERE username = ?;",
      [username],
      (err, result) => {
        if (err) {
          res.send({ err: err });
          return;
        }

        if (result.length > 0) {
          bcrypt.compare(password, result[0].password, (error, response) => {
            if (response) {
              req.session.user = result;
              console.log(req.session.user[0].username + " logged in!");
              res.status(200).send("Successfully logged in");
            } else {
              res.send({ message: "Wrong username/password combination!" });
            }
          });
        } else {
          res.send({ message: "User doesn't exist" });
        }
      }
    );
  });

  app.post("/disconnect", (req, res) => {
    req.session.destroy();

    if (!req.session) {
      res.clearCookie("sessionID");
      res.status(200).send("Successfully disconnected");
    } else {
      res.status(500).send("An error occured while processing your request");
    }
  });
}