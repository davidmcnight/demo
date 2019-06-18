var express = require('express');
var Sequelize = require('sequelize');
var bodyParser = require('body-parser');
const admin = require('firebase-admin');
const firebase = require("firebase");

//express init
var app = express();


//middleware
var serviceAccount = require('./acard-key');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),

});



app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());


firebase.initializeApp({
    apiKey : "AIzaSyDi2J_7nHc3OefQzRNUc98gt2uw0XBs1X0",
    databaseURL:"https://acardapp-io.firebaseio.com/"
});

var sequelize = new Sequelize('n64ohek8mpt5h9uw', 'jwwgmso14l8aecnr', 'l94rmi5mvbec5ly8', {
    host: 'tkck4yllxdrw0bhi.cbetxkdyhwsb.us-east-1.rds.amazonaws.com',
    dialect: 'mysql',
    timezone: '+07:00',

    pool: {
        max: 5,
        min: 0,
        idle: 10000
    }
});

//helpers
var emailValidator = require("email-validator");
const phoneValidator = require('validate-phone-number-node-js');

const helpers = {
    emailValidator: emailValidator,
    phoneValidator: phoneValidator
};



/* MODELS  */

//user
var UserModel = require("./app/models/user");
const User = UserModel(sequelize, Sequelize);

//business
var BusinessModel = require("./app/models/business");
const Business = BusinessModel(sequelize, Sequelize);

//card
var CardModel = require("./app/models/card");
const Card = CardModel(sequelize, Sequelize);

var InvitationModel = require("./app/models/invitation");
const Invitation = InvitationModel(sequelize, Sequelize);

const models = {
    User: User,
    Business: Business,
    Card: Card,
    Invitation: Invitation
};

// relationships (associations in sequelize)
User.hasMany(Business, {as: 'businesses', target_key: "userID"});
Business.belongsTo(User, {as: 'businessUser', foreignKey: "userID"});

Business.hasMany(Card, {as: 'cards', target_key: "businessID"});
Card.belongsTo(Business, {as: 'cardBusiness', foreignKey: "businessID"});




Business.hasMany(Invitation, {as: 'invitations', target_key: "businessID"});
Invitation.belongsTo(Business, {as: 'invitationBusiness', foreignKey: "businessID"});

Invitation.hasOne(User, {as: 'invitee', foreignKey: "id"});
User.belongsTo(Invitation, {as: 'invitationUser', foreignKey: "id"});



/* Services */
const UserService = require("./app/services/userService");


//define routes
require('./app/api/user')(app, models, admin, helpers);
require('./app/api/business')(app, models, admin);
require('./app/api/card')(app, models, admin);



// respond with "hello world" when a GET request is made to the homepage
app.get('/', async function (req, res) {
    res.json({"success": false, "message": "Not authorized.", "data": []});
});


app.get('/auth', async function (req, res) {

    try {
        var customToken =  await admin.auth().createCustomToken("GslEi3i8RfXnLlnjVp4FzSydggp2");
        var myResult = await  firebase.auth().signInWithCustomToken(customToken);
        console.log(myResult);
        res.json({"success": false, "message": "Not authorized.", "data": []});
    }catch (e) {
        console.log(e);
        res.json({"success": false, "message": "Not authorized.", "data": []});
    }

});


app.get('/test', async function (req, res) {
    try {


        var invitesSent = await Invitation.findAll({
            include: [
                {model: Business, as: "invitationBusiness"},
                {model: User, as: "invitee"},

            ],
            where: {userID: 1},
        });

        res.json(invitesSent);

    }catch (e) {
        console.log(e);
        res.json({"success": false, "message": "Not authorized.", "data": []});
    }

});




app.get('/test-res', async function (req, res) {
    try {

        res.json({"success": false, "message": "Not authorized.", "data": {}});
    }catch (e) {
        console.log(e);
        res.json({"success": false, "message": "Not authorized.", "data": []});
    }

});



const port = process.env.PORT || 5000;

app.listen(port, () =>{
    console.log(`Server started on port ${port}`)
});

module.exports = models;



