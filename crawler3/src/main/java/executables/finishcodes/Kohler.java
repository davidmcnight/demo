package executables.finishcodes;


import config.Config;
import domain.services.FinishCodeService;
import domain.services.ProductService;
import org.json.JSONObject;

import java.sql.*;

public class Kohler {


    public static void main(String[] args){
        try {

            Connection connection = DriverManager.getConnection(Config.connString);
            System.out.println("Running script to update finish codes with Kohler...");
            ResultSet products = ProductService.getAllProductsByManufacturer(1);
            JSONObject finishCodes = FinishCodeService.getKohlerCodes();
            finishCodes.put("NA", "");
            finishCodes.put("SH", "");
            finishCodes.put("WB", "");
            finishCodes.put("WC", "");
            finishCodes.put("WA", "");
            finishCodes.put("WD", "");
            finishCodes.put("F2", "");

            while (products.next()) {
                int id = products.getInt("id");
                String finishes = products.getString("finishes");
                if(finishes.trim().length() < 3){
                    String sku = products.getString("modelNumber");
                    String[] splits = sku.split("-");
                    if(!splits.equals("NA")){
                        String name = products.getString("name");
                        String finishCode = splits[splits.length-1];
                        String finishName = finishCodes.getString(finishCode);
                        System.out.println(name);
                        System.out.println(sku);
                        System.out.println(name);
                        System.out.println(finishCode);
                        System.out.println(finishName);
                        Statement stmt = connection.prepareStatement("UPDATE product SET name = ?, finishes = ? WHERE id = ?");
                        ((PreparedStatement) stmt).setString(1, name + " " + finishName);
                        ((PreparedStatement) stmt).setString(2, finishName);
                        ((PreparedStatement) stmt).setInt(3, id);
                        ((PreparedStatement) stmt).execute();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
