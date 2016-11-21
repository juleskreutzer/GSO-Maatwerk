var express = require('express');
var router = express.Router();

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
