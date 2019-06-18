module.exports = (sequelize, type) => {
    return sequelize.define('invitation', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        businessID: {

            type: type.INTEGER,
            field: 'businessID',
            foreignKey: true
        },
        userID: {

            type: type.INTEGER,
            field: 'userID',
            foreignKey: true
        },

    }, {
        freezeTableName: true // Model tableName will be the same as the model name
    });


};
