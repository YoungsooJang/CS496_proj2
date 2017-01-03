var fs = require('fs');

module.exports = function(app, Contact, Image, Rank)
{
    // GET ALL CONTACTS
    app.get('/api/contacts', function(req,res){
        Contact.find(function(err, contacts){
            if(err) return res.status(500).send({error: 'database failure'});
            
            console.log("Get all contacts! " + contacts.toString());
            res.json(contacts);
        })
    });
 
    // CREATE CONTACTS
    app.post('/api/contacts', function(req, res){
        var contact = new Contact();
        contact.name = req.body.name;
        contact.phoneNumber = req.body.phoneNumber;
 
        contact.save(function(err){
            if(err){
                console.error(err);
                res.json({result: 0});
                return;
            }
 
            console.log("Create new contact! name : " + contact.name + ", number : " + contact.phoneNumber);
            res.json({result: 1});
        });
    });
 
    // DELETE ALL CONTACTS
    app.delete('/api/contacts', function(req, res){
        Contact.remove(function(err, output){
            if(err) return res.status(500).json({ error: "database failure" });

            res.status(204).end();
        })
    });

    // UPLOAD IMAGE
    app.post('/api/images', function(req, res) {
        console.log("originalFilename : " + req.files.image.originalFilename + ", path : " + req.files.image.path);
        
        var image = new Image();
        image.path = req.files.image.originalFilename;

        image.save(function(err){
            if(err){
                console.error(err);
                return;
            }

            console.log("Create new image path! path : " + image.path);
        });

        fs.readFile(req.files.image.path, function (err, data){
            var dirname = "/home/ubuntu/Youngsoo/proj2";
            var newPath = dirname + "/images/" + req.files.image.originalFilename;
            fs.writeFile(newPath, data, function (err) {
                if(err){
                    res.json({'response':"Error"});
                }else {
                    res.json({'response':"Saved"});
                }
            });
        });
    });

    // GET SINGLE IMAGE
    app.get('/api/images/:file', function (req, res){
        file = req.params.file;
        var dirname = "/home/ubuntu/Youngsoo/proj2";
        var img = fs.readFileSync(dirname + "/images/" + file);
        res.writeHead(200, {'Content-Type': 'image/jpg' });
        res.end(img, 'binary');
    });

    // GET ALL IMAGES' PATH
    app.get('/api/images', function(req,res){
        Image.find(function(err, images){
            if(err) return res.status(500).send({error: 'database failure'});
            
            console.log("Get all images! " + images.toString());
            res.json(images);
        })
    });

    // GET ALL RANKS
    app.get('/api/ranks', function(req,res){
        Rank.find(function(err, ranks){
            if(err) return res.status(500).send({error: 'database failure'});
            
            console.log("Get all ranks! " + ranks.toString());
            res.json(ranks);
        })
    });

    // CREATE A RANK
    app.post('/api/ranks', function(req, res){
        var rank = new Rank();
        rank.name = req.body.name;
        rank.score = req.body.score;
 
        rank.save(function(err){
            if(err){
                console.error(err);
                res.json({result: 0});
                return;
            }
 
            console.log("Create new rank! name : " + rank.name + ", score : " + rank.score);
            res.json({result: 1});
        });
    });

}
