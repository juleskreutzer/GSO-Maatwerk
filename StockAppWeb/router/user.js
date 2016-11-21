var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var router = express.Router();
var mongoose = require('mongoose');
var db_url = 'mongodb://localhost:27017/StockApp';
mongoose.connect(db_url);

var Stock = require('../schemas/stockSchema');
var User = require('../schemas/userSchema');

var db = mongoose.connection;

router.get('/', function(req, res) {
  res.end('user endpoint');
});

/**
Register a new user with the database
Following values should be included in JSON format in the request's body:
<ul>
  <li>Useranme</li>
  <li>Password</li>
  <li>Email</li>
</ul>
*/
router.post('/register', function(req, res) {
  var username = req.body.username;
  var password = req.body.password;
  var email = req.body.email;

  var newUser = new User({
    username: username,
    password: password,
    email: email,
    preferredStock: []
  });

  newUser.save(function(err) {
    if(err) { res.send(err); }
    else { res.send("User created!"); }
  })
});

/**
Find usernames from users that match the provided :username params
This endpoint will return an json array containing all the usernames that meet the criteria
*/
router.get('/find/:username', function(req, res) {
  var username = req.params.username;

  User.find({username: new RegExp(username, 'i')}, function(err, result) {
    if(err)
    {
      res.send(err);
    }
    var usernames = [];

    for(var i = 0; i < result.length; i++) {
      usernames[i] = result[i].username;
    }

    res.json(usernames);
  });
});

/* User endpoints */

module.exports = router;
