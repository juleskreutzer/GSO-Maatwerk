var http = require("http");
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var mongoose = require('mongoose');

var stockRoute = require('./router/stock');
var userRoute = require('./router/user');
var infoRoute = require('./router/info');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.use('/stock', stockRoute);
app.use('/user', userRoute);
app.use('/info', infoRoute);


var server = app.listen(8080, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log("App listening at http://%s:%s", host, port);
});
