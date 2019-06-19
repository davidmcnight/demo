//libs
const express = require('express');
const Sequelize = require('sequelize');
const bodyParser = require('body-parser');
const shortHash = require('short-hash');


//app init

const app = express();
app.enable('trust proxy')


//middleware
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

/* INITS */

//db
const sequelize = new Sequelize('spatial', 'root', 'root', {
    host: '127.0.0.1',
    dialect: 'mysql',
    timezone: '+07:00',
    pool: {
        max: 5,
        min: 0,
        idle: 10000
    }
});



//models
const UrlModel = require("./app/domain/models/url");
const Url = UrlModel(sequelize, Sequelize);
const UrlClickModel = require("./app/domain/models/url_click");
const UrlClick = UrlClickModel(sequelize, Sequelize);






const models = {
    Url: Url,
    UrlClick: UrlClick
};

const helpers = {
    shortHash: shortHash
};


require('./app/api/url')(app, models, helpers);



app.get('/', async function (req, res) {
    var url = await models.Url.findOne({
        where: {id: 1}
    });
    res.json({"success": false, "message": "Not authorized.", "data": [url]});
});



const port = process.env.PORT || 5000;

app.listen(port, () =>{
    console.log(`Server started on port ${port}`)
});

module.exports = models;
