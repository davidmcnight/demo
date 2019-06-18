module.exports = function(app, models, admin, helpers){

    //TODO: USE STATUS CODES



    //get user after auth
    app.get('/api/v1/user/auth/:uid', async function (req, res) {
        try {

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);

            if(ensureAuth){

                //get user from parameter
                user = await models.User.findOne({
                    where: {uid: req.params.uid},
                    include: [{model: models.Business, as: "businesses",
                        include:  {model: models.Card, as: "cards"}
                    }]
                });

                //last check of auth
                if(ensureAuth.user_id === user.uid){
                    res.json({"success":true, "data": user, "code": 200});
                }else {
                    res.json({"success": false, "data": {}, "code": 403})
                }

            }else {
                res.json({"success": false, "data": {}, "code": 401});
            }
        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401})
        }

    });



    //get user
    app.get('/api/v1/user/:id', async function (req, res) {
        try {

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);

            if(ensureAuth){
                //get user by param
                user = await models.User.findOne({
                    where: {id: req.params.id},
                    include: [{model: models.Business, as: "businesses",
                        include:  {model: models.Card, as: "cards"}
                    }]
                });
                res.json({"success":true, "data": user, "code": 200});
            }else {
                res.json({"success": false, "data": {}, "code": 401})
            }
        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401})
        }

    });




    //register and create user
    app.post('/api/v1/user/register', async function (req, res) {

        try {

            //grab data
            data = req.body;


            //TODO: refactor in a helper class
            if(!data.hasOwnProperty("uid")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Uid required."});
                return;
            }

            if(!data.hasOwnProperty("firstName")){
                res.json({"success": false, "data": {}, "code": 400, "message": "First name required."});
                return;
            }

            if(!data.hasOwnProperty("lastName")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Last name required."});
                return;
            }

            //check required fields
            if(!data.hasOwnProperty("email")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Email required."});
                return;
            }

            if(!data.hasOwnProperty("phone")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Phone required."});
                return;
            }

            if(!data.hasOwnProperty("username")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Username required."});
                return;
            }


            if(!data.hasOwnProperty("username")){
                res.json({"success": false, "data": {}, "code": 400, "message": "Username required."});
                return;
            }

            //validate email
            if(helpers.emailValidator.validate(data.email) === false){
                res.json({"success": false, "data": {}, "code": 400, "message": "Invalid email."});
                return;
            }

            //validate phone
            if(!helpers.phoneValidator.validate(data.phone)){
                res.json({"success": false, "data": {}, "code": 400, "message": "Invalid phone."});
                return;
            }

            //make sure there are no other fields
            users = await models.User.findAndCountAll({
                where: {
                    $or: [
                        {email: data.email},
                        {phone: data.phone},
                        {uid: data.uid},
                        {username: data.username},
                    ]
                }
            });

            if(users.count === 0){
                user = await models.User.create(data);
                res.json({"success":true, "data": user, "code": 200});
            }else {
                res.json({"success":false, "data": {}, "code": 400, "message": "There is a user with this information already."})
            }
        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 400, "message": "Bad request."});
        }

    });


    //update user
    //register and create user
    app.put('/api/v1/user', async function (req, res) {

        try {

            //ensure auth
            var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
            console.log(ensureAuth);
            if(ensureAuth){

                //get user by uid
                user = await models.User.findOne({
                    where: {uid: ensureAuth.user_id},
                });

                //get request body
                data = req.body;


                //TODO: Refactor into a helper class.

                //validate email
                if(helpers.emailValidator.validate(data.email) === false){
                    res.json({"success": false, "data": {}, "code": 400, "message": "Invalid email."});
                    return;
                }

                //validate phone
                if(!helpers.phoneValidator.validate(data.phone)){
                    res.json({"success": false, "data": {}, "code": 400, "message": "Invalid phone."});
                    return;
                }


                //check email if it exists and it is different
                var checkEmail = await models.User.findAndCountAll({
                    where:
                        {"uid": {$not: user.uid}, "email": data.email},
                });

                if(checkEmail.count > 0){
                    res.json({"success": false,  "code": 400, "message": "This email is already being used.", "data": {}});
                    return;
                }

                //check phone if it exists and it is different
                var checkPhone = await models.User.findAndCountAll({
                    where:
                        {"uid": {$not: user.uid}, "phone": data.phone},
                });

                if(checkPhone.count > 0){
                    res.json({"success": false,  "code": 400, "message": "The phone is already being used", "data": {}});
                    return;
                }


                //check username if it exists and it is different
                var checkUsername = await models.User.findAndCountAll({
                    where:
                        {"uid": {$not: user.uid}, "username": data.username},
                });

                if(checkUsername.count > 0){
                    res.json({"success": false, "code": 400, "message":  "The phone is already being used.", "data": {}});
                    return;
                }

                //update user
                user = await user.update(data);
                res.json({"success": true, "code": 200, "data": user});

            }else {
                res.json({"success": false, "code": 401, "data": {}});
            }

        }catch (e) {
            console.log(e);
            res.json({"success": false, "code": 401, "data": {}});
        }

    });

    //get user network




};