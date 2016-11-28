var express = require('express');
var bodyParser = require('body-parser');
var sha1 = require('js-sha1');
var app = express();
var router = express.Router();
var mongoose = require('mongoose');

var jwt = require('jsonwebtoken');
var config = require('../config');

var db_url = 'mongodb://localhost:27017/StockApp';

var Stock = require('../schemas/stockSchema');
var User = require('../schemas/userSchema');

var db = mongoose.connection;

router.use(function(req, res, next) {

  var path = "/register";
  console.log("Path is: " + req.path);
  if(path != req.path) {

    console.log('Looking for token...');
    var token = req.body.token || req.query.token || req.header['x-access-token'];

    if(token) {
      console.log('token found: ' + token);

      jwt.verify(token, config.secret, function(err, decoded) {
        if(err) {
          return res.json({ success: false, message: 'You are not authorized to call this endpoint. (Failed to authenticate token)'});
        } else {
          req.decoded = decoded;
          next();
        }
      });
    } else {
        return res.status(403).send({
          success: false,
          message: 'No token provided to authorize this request.'
        });
    }
  } else {
    next();
  }
});

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

  var hashed = sha1(password);

  User.find({username: username}, function(err, result) {

    if(result.length > 0) { res.end("User with username \"" + username + "\" already exists."); }
  });

    // Store new user in database
    var newUser = new User({
      username: username,
      password: hashed,
      email: email,
      preferredStock: []
    });

    newUser.save(function(err) {
      if(err) { res.send(err); }
      else { res.json({ success: true }); }
    });

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
