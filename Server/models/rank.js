var mongoose = require('mongoose');
var Schema = mongoose.Schema;
 
var rankSchema = new Schema({

    name: String,
    score: String,

});
 
module.exports = mongoose.model('rank', rankSchema);
