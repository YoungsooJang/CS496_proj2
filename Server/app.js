var mongoose    = require('mongoose');

var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost:27017/proj2');

var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var Contact     = require('./models/contact');
var Image       = require('./models/image');
var Rank        = require('./models/rank');
var connect     = require('connect');

app.use(express.static(__dirname + '/public'));
app.use(connect.cookieParser());
app.use(connect.logger('dev'));
 
app.use(connect.json());
app.use(connect.urlencoded());
app.use(connect.bodyParser());

// [CONFIGURE APP TO USE bodyParser]
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
 
// [CONFIGURE SERVER PORT]
var port = process.env.PORT || 3000;
 
// [CONFIGURE ROUTER]
var router = require('./routes')(app, Contact, Image, Rank);
 
// [RUN SERVER]
var server = app.listen(port, function(){
     console.log("Express server has started on port " + port)
});
