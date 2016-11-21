var mongoose = require('mongoose');
Schema = mongoose.Schema;

var StockSchema = new Schema({
  code: String,
  name: String,
  currency: String,
  date: Date,
  maximum: Number,
  minimum: Number,
  values: [{
      String,
      Number
  }]
});

var Stock = mongoose.model('Stock', StockSchema, 'stock');

module.exports = Stock;
