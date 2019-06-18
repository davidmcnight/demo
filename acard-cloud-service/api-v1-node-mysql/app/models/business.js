
module.exports = (sequelize, type) => {
    return sequelize.define('business', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        userID: {

            type: type.INTEGER,
            field: 'userID',
        },
        name: {

            type: type.STRING,
            field: 'name',
        },

        title: {

            type: type.STRING,
            field: 'title',
        },
        address1: {
            type: type.STRING,
            field: 'address1',
        },
        address2: {

            type: type.STRING,
            field: 'address2',
        },

        city: {

            type: type.STRING,
            field: 'city',
        },

        state: {

            type: type.STRING,
            field: 'state',
        },

        zip: {

            type: type.STRING,
            field: 'zip',
        },

        phone: {

            type: type.STRING,
            field: 'phone',
        },
        email: {

            type: type.STRING,
            field: 'email',
        },
        website: {

            type: type.STRING,
            field: 'website',
        }

    }, {
        tableName: "business",
        sequelize
    });


};
