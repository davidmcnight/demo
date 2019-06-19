module.exports = function(app, models, helpers) {


    function isURL(str) {
        var urlRegex = '^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&\'\\(\\)\\*\\+,;=.]+$';
        var url = new RegExp(urlRegex, 'i');
        return str.length < 2083 && url.test(str);
    }


    app.post('/', async function (req, res) {

        try {
            //data
            data = req.body;
            var signature = ":::dmcsl";

            //check proper payload
            if(data.hasOwnProperty("url")){

                //check alid url
                if(data.url !== "" && isURL(data.url) &&
                    (data.url.startsWith("http:") || data.url.startsWith("https:"))){

                    //check if it ends with this short link signature
                    if(!data.url.endsWith(signature)){

                        var shortUrl = helpers.shortHash(data.url) + signature;

                        myUrl = await models.Url.findAndCountAll({
                            where: {short_url: shortUrl}
                        });

                        if(myUrl.count === 0){
                            var myData = {
                                "original_url": data.url,
                                "short_url": shortUrl
                            };

                            result = await models.Url.create(myData);
                            res.json({"success": true, "data": result})
                        }else {
                            res.json({"success": true, "data": myUrl.rows[0]})
                        }



                    }else {
                        res.json({"success": false, "data": data, "message": "You can not shorten a shortened link."});

                    }
                }else {
                    res.json({"success": false, "data": data, "message": "Please supply a valid url."});
                }
            }else {
                res.json({"success": false, "data": data, "message": "Invalid Request"});
            }

        }catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "message": "Something happened."});
        }

    });



    //get url
    app.get('/:hash', async function (req, res) {
        try {
            //get user by id
            params = req.params;
            result = await models.Url.findAndCountAll({
                where: {short_url: params.hash}
            });

            if(result.count === 0){
                res.json({"success": false, "data": data, "message": "That url has not been shortened."})
            }else {

                url = result.rows[0];

                //create click
                var click = {};
                click["host"] = req.get('host');
                click["user_agent"] = req.get('User-Agent');
                click["ip"] = req.ip;
                click["url_id"] = url.id;
                result = await models.UrlClick.create(click);

                res.redirect(url.original_url+ "utm_campaign=spatial&utm_medium=social&utm_source=facebook")
            }




        } catch (e) {
            console.log(e);
            res.json({"success": false, "data": {}, "code": 401});
        }

    });


};