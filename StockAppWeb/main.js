var http = require("http");
var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var morgan = require('morgan');
var config = require('./config');

var stockRoute = require('./router/stock');
var userRoute = require('./router/user');
var infoRoute = require('./router/info');
var authRoute = require('./router/authenticate');

app.set('superSecret', config.secret);
app.use(morgan('dev'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.use('/auth', authRoute);
app.use('/stock', stockRoute);
app.use('/user', userRoute);
app.use('/info', infoRoute);



var server = app.listen(8080, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log("App listening at http://%s:%s", host, port);
});
