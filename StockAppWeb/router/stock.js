var express = require('express');
var router = express.Router();

var jwt = require('jsonwebtoken');
var config = require('../config');

var mongoose = require('mongoose');
var db_url = 'mongodb://localhost:27017/StockApp';
mongoose.connect(db_url);

var Stock = require('../schemas/stockSchema');
var User = require('../schemas/userSchema');

var db = mongoose.connection;

db.on('error', console.error.bind(console, 'connection error: '));

db.once('open', function() {
  console.log('Connected to database!');
});

router.use(function(req, res, next) {

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
});

/**
Get all ticket symbols known on the server
*/
router.get('/tickerSymbols', function(req, res) {
  res.setHeader('Content-Type', 'application/json');

  Stock.find().distinct('code', function(err, codes) {
    if(err) {
      res.send(err);
    }

    res.json(codes);
  });
});

/**
Get one or more stock object based on the provided tickerSymbol
*/
router.get('/:code', function(req, res) {
  var symbol = req.params.code;

  Stock.find({code: symbol}, function(err, stocks) {
    if(err) {
      res.send(err);
    }

    res.json(stocks);
  });
});

module.exports = router;
