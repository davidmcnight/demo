package com.specbooks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainDriver {

    public static String connString = "jdbc:mysql://127.0.0.1/test-specbook-product?" +"user=root&password=root";

    public static void main(String[] args){

        try {

            String sql = "SELECT * FROM specbk_spec_prices_old limit 1000";

            Connection connection = DriverManager.getConnection(connString);
            Statement statement = connection.createStatement();
            ResultSet resultSet =  statement.executeQuery(sql);

            int counter = 1;
            while (resultSet.next()){
                System.out.println(counter);
                System.out.println(resultSet.getString("specID"));
                counter++;
            }

            System.out.println("Exit");
        }catch (Exception e){
            e.printStackTrace();
        }



    }


}
