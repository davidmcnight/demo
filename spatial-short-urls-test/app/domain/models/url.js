module.exports = (sequelize, type) => {
    return sequelize.define('url', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        short_url: {

            type: type.STRING,
            field: 'short_url',
        },
        original_url: {
            type: type.STRING,
            field: 'original_url',
        },

    }, {
        tableName: "url",
        sequelize
    });


};
