const fs = require("fs");

module.exports = function(app){
  app.get('/event', function (req, res) {
    res.end("Yolo");
  })
}