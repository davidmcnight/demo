package domain;

import config.Config;
import domain.factory.Factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static Database instance = null;
    private static Connection dsn;

    private Database() {
        // Exists only to defeat instantiation.
    }

    public static Database getInstance() {
        try {

        if(instance == null) {
            instance = new Database();
            return instance;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }


    public Connection getDsn() {
            if(dsn == null){
                try {
                    dsn = DriverManager.getConnection(Config.connString);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return dsn;
        }


}
