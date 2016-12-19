var express = require('express');
var bodyParser = require('body-parser');
var sha1 = require('js-sha1');
var jwt = require('jsonwebtoken');
var config = require('../config');
var app = express();
var router = express.Router();
var mongoose = require('mongoose');
var db_url = 'mongodb://localhost:27017/StockApp';
mongoose.connect(db_url);

var User = require('../schemas/userSchema');

var db = mongoose.connection;


/**
Authenticate a user based on their username and password (provided in request body)
*/
router.post('/', function(req, res) {
  var username = req.body.username;
  var password = req.body.password;

  console.log(req.body);

  var hashed = sha1(password);

  User.findOne({
    username: username
  }, function(err, user) {
    if (err) { res.json({ success: false, message: err });}

    if(!user) {
      res.json({ success: false, message: 'Authentication failed. No user found with provided username.'});
    } else if (user) {
      if(user.password != hashed) {
        res.json({ success: false, message: 'Authentication failed. Incorrect username or password.'});
      } else {
        // if user is found and password is right
        // create a token
        var token = jwt.sign(user, config.secret, {
          expiresIn: 60*30 // expires in 30 minutes
        });

        // return the information including token as JSON
        res.json({
          success: true,
          message: '',
          token: token
        });
      }
    }
  });
});

module.exports = router;
