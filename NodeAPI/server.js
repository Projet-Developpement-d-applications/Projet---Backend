const express = require("express");
const mysql = require("mysql");
const cors = require("cors");
const session = require("express-session");
const cookieParser = require('cookie-parser');

const app = express();
const port = 3001;

app.use(express.json());
app.use(cookieParser());


// to change
const db = mysql.createConnection({
  host: "formatif5-bd-appweb2-formatif5.aivencloud.com",
  database: "tp2db",
  user: "user-tp2",
  password: "AVNS_X4-y2mtSOgPvvph1Tz6",
  port: 18715,
});


// to change
app.use(
  cors({
    origin: ["http://localhost:3000", "https://app-web2-tp2.vercel.app", /\.nercolive\.site$/],
    methods: ["GET", "POST", "HEAD"],
    credentials: true,
    httpOnly: false
  })
);

app.use(
  session({                     // initialise la session
    key: "sessionID",              // nom du cookie
    secret: "wakawaka",         // utilisé pour hasché l'id de session
    resave: false,              // "L'option resave détermine si la session doit être enregistrée à nouveau à chaque fois que la page est chargée. Si resave est réglé sur false, cela signifie que la session ne sera pas sauvegardée à nouveau à moins qu'il y ait eu des modifications apportées à la session pendant la demande. Cela peut être utile pour améliorer les performances, car cela évite de sauvegarder la session à chaque requête, ce qui peut être coûteux en termes de ressources serveur." ChatGPT
    saveUninitialized: false,   // "L'option saveUninitialized détermine si une session non initialisée doit être enregistrée dans le store. Une session est considérée comme non initialisée si elle n'a pas été modifiée depuis sa création. Si saveUninitialized est réglé sur false, cela signifie que les sessions qui n'ont pas été modifiées (par exemple, en ajoutant des données à la session) ne seront pas enregistrées dans le store. Cela peut être utile pour économiser de l'espace de stockage et éviter de créer des sessions inutiles pour les utilisateurs qui visitent votre site sans effectuer d'action qui nécessite une session." ChatGPt
    cookie: {                   //
      sameSite: "lax",
      //secure: true,
      maxAge: 86400000,    // 24 heures
      httpOnly: false
    },
  })
);

app.listen(port, () => {
  console.log("running server");
});

//Modules
require('./modules/web_browser')(app);
//require('./modules/users')(app, db);
require('./modules/player')(app);