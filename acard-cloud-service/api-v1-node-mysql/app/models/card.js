module.exports = (sequelize, type) => {
    return sequelize.define('card', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        businessID: {

            type: type.INTEGER,
            field: 'businessID',
        },
        logoUrl: {

            type: type.STRING,
            field: 'logoUrl',
        },
        bgColor: {

            type: type.STRING,
            field: 'bgColor',
        },

        primaryColor: {

            type: type.STRING,
            field: 'primaryColor',
        },

        secondaryColor: {

            type: type.STRING,
            field: 'secondaryColor',
        },

    }, {
        tableName: "card",
        sequelize
    });


};
