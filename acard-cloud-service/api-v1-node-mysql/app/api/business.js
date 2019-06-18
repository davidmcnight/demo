module.exports = function(app, models, admin) {

    //get business
    app.get('/api/v1/business/:id', async function (req, res) {
        try {
            //get user by id
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){
                business = await models.Business.findOne({
                    where: {id: req.params.id},
                    include: [{model: models.User, as: "businessUser"}]
                });
                res.json({"success":true, "data": business, "code": 200});
            }else {
                res.json({"success": false, "data": {}, "code": 401})
            }
        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });

    //get user businesses
    app.get('/api/v1/user/:userID/business', async function (req, res) {
        try {
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){

                businesses = await models.Business.findAll({
                    where: {userID: req.params.userID},
                    include: [{model: models.User, as: "businessUser"}]
                });
                res.json({"success":true, "data": businesses, "code": 200});
            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }

        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });


    //create business
    app.post('/api/v1/business', async function (req, res) {

        try {

            //data
            data = req.body;

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){

                //set user ID
                data["userID"] = user.rows[0].id;
                //create business
                business = await models.Business.create(data);
                res.json({"success":true, "data":business, "code": 200});

            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }

        }catch (e) {
            res.json({"success": false, "data": {}, "code": 401});
        }

    });

    //update business
    app.put('/api/v1/business', async function (req, res) {

        try {


            //data
            data = req.body;

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth){

                //get business by id given
                if(!data.hasOwnProperty("id")){
                    res.json({"success": false, "data": {}, "code": 400, "message": "No id given."});
                    return;
                }

                business = await models.Business.findOne({
                    where: {id: data.id},
                    include: [{model: models.User, as: "businessUser"}]
                });

                if (ensureAuth.user_id === business.businessUser.uid){
                    business = await business.update(data);
                    res.json({"success":true, "data":business, "code": 200});
                }else {
                    res.json({"success":false, "data": user,"code": 403});
                }

            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }

        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });


    //delete card
    app.delete('/api/v1/business/:id', async function (req, res) {

        try {

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            if(ensureAuth) {
                //get user business to verify it is the right user
                var business = await models.Business.findOne({
                    where: {id: req.params.id},
                    include: [{
                        model: models.User, as: "businessUser"
                    }]
                });

                if (ensureAuth.user_id === business.businessUser.uid) {
                    business = business.destroy();
                    res.json({"success":true, "data":business, "code": 200});
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