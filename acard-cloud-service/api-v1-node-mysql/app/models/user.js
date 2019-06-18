module.exports = (sequelize, type) => {
    return sequelize.define('user', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        uid: {

            type: type.STRING,
            field: 'uid',
        },
        email: {

            type: type.STRING,
            field: 'email',
        },
        phone: {

            type: type.STRING,
            field: 'phone',
        },
        username: {

            type: type.STRING,
            field: 'username',
        },
        firstName: {

            type: type.STRING,
            field: 'firstName',
        },
        lastName: {

            type: type.STRING,
            field: 'lastName',
        },
        pictureUrl: {

            type: type.STRING,
            field: 'pictureUrl',
        },

    }, {
        freezeTableName: true // Model tableName will be the same as the model name
    });


};
