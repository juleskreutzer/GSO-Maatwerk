var mongoose = require('mongoose');
Schema = mongoose.Schema;

var UserSchema = new Schema({
  username: String,
  password: String,
  email: String,
  preferredStock: []
});

var User = mongoose.model('User', UserSchema, 'user');

module.exports = User;
