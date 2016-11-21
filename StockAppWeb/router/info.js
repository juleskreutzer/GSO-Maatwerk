var express = require('express');
var router = express.Router();

var mongoose = require('mongoose');
var db_url = 'mongodb://localhost:27017/StockApp';
mongoose.connect(db_url);

var Stock = require('../schemas/stockSchema');
var User = require('../schemas/userSchema');

var db = mongoose.connection;

router.get('/', function(req, res) {
  res.end('Info endpoint called.');
});

router.get('/totalStocks', function(req, res) {
  Stock.count({}, function(err, count) {
    if(err) {
      res.send(err);
    }

    res.json(count);
  });
});

module.exports = router;
