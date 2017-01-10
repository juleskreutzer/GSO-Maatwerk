var express = require('express');
var router = express.Router();

var jwt = require('jsonwebtoken');
var config = require('../config');

var mongoose = require('mongoose');
var db_url = 'mongodb://localhost:27017/StockApp';

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
  res.setHeader('Access-Control-Allow-Origin', '*');

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

/**
Get all stock objects stored in the database for the provided :code param.
Days represent the number of days back from the current date you want to start searching for the stock object

This endpoint will return a JSON object with the key 'success' defining if the request is successful.
If success is true, an array of stock objects will be sent to the client, if success is false, an error message will be returned
*/
router.get('/:code/:days', function(req, res) {
  var code = req.params.code;
  var days = req.params.days;

  var endDate = new Date();
  var startDate = endDate.getDay() - days;

  Stock.find({code: code, date: { '$gte': startDate, '$lt': endDate}}, function(err, result) {
    if(err) {
      res.json({ success: false, message: 'Couldn\'t find any stocks.\n' + err});
    }

    res.json({ success: true, result });
  });
});

/**
Get all stock object stored in the database for the provided :code param
With this endpoint you can search history in the database.

:code will contain the ticker symbol for the stock object
:endDate will contain a JS date object in JSON format (now.toJSON)
:days will be used to calculate the start date

THis endpoint will return a JSON object with the key 'success' defining if the request is successful.
If success is true, an array of stock objects will be sent to the client, if success is false, an error message will be returned
*/
router.get('/history/:code/:endDate/:days', function(req, res) {
  var code = req.params.code;
  var endDateJsonString = req.params.endDate; // Add endDate params by calling now.toJSON() on client
  var days = req.params.days;

  var endDate = new Date(endDateJsonString);
  var startDate = endDate.getDay() - days;

  Stock.find({code: code, date: { '$gte': startDate, '$lt': endDate}}, function(err, result) {
    if(err) {
      res.json({ success: false, message: 'Couldn\'t find any stocks.\n' + err});
    }

    res.json({ success: true, result });
  });
});

module.exports = router;
