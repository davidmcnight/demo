module.exports = function(app, models, admin) {

    //get invites of business
    app.get('/api/v1/business/:businessID/invitation', async function (req, res) {

        var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
        if(ensureAuth) {
            var invitesSent = await Invitation.findAll({
                include: [
                    {model: Business, as: "invitationBusiness"},
                    {model: User, as: "invitee"}
                ],
                where: {businessID: req.params.businessID},
            });
            res.json({"success":true, "data": invitesSent, "code": 200});
        }else {
            res.json({"success": false, "data": {}, "code":400});
        }
        res.json({"success": false, "data": {}, "code":401});
    });

    //get invites of by invitee
    app.get('/api/v1/user/:userID/invitation', async function (req, res) {

        var ensureAuth = await admin.auth().verifyIdToken(req.headers.token);
        if(ensureAuth) {
            var invitesSent = await Invitation.findAll({
                include: [
                    {model: Business, as: "invitationBusiness"},
                    {model: User, as: "invitationUser"}
                ],
                where: {userID: req.params.userID},
            });
            res.json({"success":true, "data": invitesSent, "code": 200});
        }else {
            res.json({"success": false, "data": {}, "code":400});
        }
        res.json({"success": false, "data": {}, "code":401});
    });





};