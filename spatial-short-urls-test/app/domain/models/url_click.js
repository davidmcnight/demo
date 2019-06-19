module.exports = (sequelize, type) => {
    return sequelize.define('url_click', {
        id: {

            type: type.INTEGER,
            field: 'id',
            primaryKey: true,
            autoIncrement: true
        },
        url_id: {
            type: type.INTEGER,
            field: 'url_id',
        },
        user_agent: {
            type: type.STRING,
            field: 'user_agent',
        },
        host: {
            type: type.STRING,
            field: 'host',
        },
        ip: {
            type: type.STRING,
            field: 'ip',
        },

    }, {
        tableName: "url_click",
        sequelize
    });


};
