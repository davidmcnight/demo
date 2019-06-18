package domain.services;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import config.Config;
import domain.Database;
import domain.models.Product;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductService {


    public static ResultSet getAllProductsByManufacturer(int id){
        try {
            String sql = "SELECT * FROM product WHERE manufactuter_id = " + id;
            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();
            ResultSet resultSet =  statement.executeQuery(sql);
            return  resultSet;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static void createProduct(Product product, int manu_id)throws Exception{

        try {
        Connection connection = Database.getInstance().getDsn();
        Statement statement =  connection.createStatement();
        String insertInto = "";
        String values = "";
        Field[] fields = Product.class.getDeclaredFields();
        for (Field field : fields) {
            insertInto += field.getName() + ",";

            if(product.getField(field.getName()) == null){
                values += "'',";
            }else {
                values += "'"+ product.getField(field.getName()).replace("'","") + "',";
            }

        }
        insertInto += "manufactuter_id";
        values += manu_id;

        String sql = "INSERT INTO product (" + insertInto + ") VALUES (" + values +")";
        System.out.println(sql);
        statement.executeUpdate(sql);

        }catch (MySQLIntegrityConstraintViolationException e){
          e.printStackTrace();
        }
    }


    public static boolean doesSkuExist(String sku, int manu_id) throws Exception{

        String query = "SELECT count(*) as count FROM product WHERE manufactuter_id= " + manu_id + " and modelNumber = '" + sku + "'";
        Connection connection = DriverManager.getConnection(Config.connString);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){

            if (resultSet.getInt("count") == 1){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

}
