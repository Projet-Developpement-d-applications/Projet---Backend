module.exports = function(app){
    app.get("/", (req, res) => {
        res.send("<h1>Hello, this is not a website but a web service!</h1>");
    });
}