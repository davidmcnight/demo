module.exports = function(app, models, admin) {




    //get card
    app.get('/api/v1/card/:id', async function (req, res) {
        try {
            //get user by id
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){
                var card = await models.Card.findOne({
                    where: {id: req.params.id},
                    include: [{
                        model: models.Business, as: "cardBusiness",
                        include: {model: models.User, as: "businessUser"}
                    }]
                });

                if(card != null){
                    res.json({"success":true, "data": card, "code": 200});
                }else {
                    res.json({"success": false, "data": {}, "code": 403});
                }


            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }
        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });

    //get business cards
    app.get('/api/v1/business/:businessID/card', async function (req, res) {
        try {
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){
                var business = await models.Business.findOne({
                    where: {id: req.params.businessID},
                    include: [{
                        model: models.Card, as: "cards",
                    }]
                });
                if(business != null){
                    res.json({"success":true, "data": business, "code": 200});
                }else {
                    res.json({"success": false, "data": {}, "code": 403});
                }


            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }
        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });




    //create card
    app.post('/api/v1/card', async function (req, res) {

        try {

            // ******** MUST HAVE BUSINESS ID *********** //
            //data
            data = req.body;

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){

                //check if businessID is there
                if(data.hasOwnProperty("businessID")){
                    //get user business to verify it is the right user
                    var business = await models.Business.findOne({
                        where: {id: data.businessID},
                        include: [{model: models.User, as: "businessUser"}]
                    });

                    //check if null
                    if(business != null){
                        //verify user->buisness->card
                        if (ensureAuth.user_id === business.businessUser.uid){

                            // create card
                            card = await models.Card.create(data);
                            res.json({"success":true, "data":card, "code": 200});

                        }else {
                            res.json({"success": false, "data": {}, "code": 403});
                        }
                    }else {
                        res.json({"success": false, "data": {}, "code": 400, "message": "No business."});
                    }
                }else {
                    res.json({"success": false, "data": {}, "code": 400, "message": "businessID is not prodided in body."});
                }
            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }

        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });


    //update card
    app.put('/api/v1/card', async function (req, res) {

        try {

            data = req.body;

            if(!data.hasOwnProperty("id")){
                res.json({"success": false, "message": "Please supply the card id."});
                return;
            }

            // ******** MUST HAVE BUSINESS ID *********** //
            //data


            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){

                //check if businessID is there
                if(data.hasOwnProperty("businessID")){
                    //get user business to verify it is the right user
                    var business = await models.Business.findOne({
                        where: {id: data.businessID},
                        include: [{model: models.User, as: "businessUser"}]
                    });

                    //check if null
                    if(business != null){
                        //verify user->buisness->card
                        if (ensureAuth.user_id === business.businessUser.uid){

                            // create card

                            card = await models.Card.findOne({
                                where: {id: data.id},
                            });

                            if(card != null){
                                card = await card.update(data);
                                res.json({"success":true, "data":card, "message": "Card updated."});
                            }
                            res.json({"success": false, "message": "No card associated with that id"});
                        }else {
                            res.json({"success": false, "message": "Not authorized."});
                        }
                    }else {
                        res.json({"success":false, "data":[], "message": "No business associated with id in request."});
                    }
                }else {
                    res.json({"success": false, "message": "No business id included with request."});
                }
            }else {
                res.json({"success": false, "message": "You are not authorized to use this."});
            }

        }catch (e) {
            console.log(e);
            res.json({"success":false, "message": "Something went wrong."})
        }

    });


    //delete card
    app.delete('/api/v1/card/:id', async function (req, res) {

        try {


            data = req.body;

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth) {
                //get user business to verify it is the right user
                var card = await models.Card.findOne({
                    where: {id: req.params.id},
                    include: [{
                        model: models.Business, as: "cardBusiness",
                        include: {model: models.User, as: "businessUser"}
                    }]
                });

                if (ensureAuth.user_id === card.cardBusiness.businessUser.uid) {
                    card = card.destroy();
                    res.json({"success":true, "data":card, "code": 200});
                }else {
                    res.json({"success": false, "data": {}, "code": 403});
                }
            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }


        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });




};