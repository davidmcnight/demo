package executables.priceupdates;

import config.Config;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class KichlerPricing {


    public static void main(String[] args){

        try {
            System.out.println("Kichker Pricing");
            FileInputStream file = new FileInputStream(new File("price-sheets/kichler-pricing.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);


            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();

            int counter = 1;
            for (Row row : sheet) {
                if(counter != 1){
                    DataFormatter formatter = new DataFormatter();
                    String val = formatter.formatCellValue(row.getCell(0));
                    String sku = val;
                    String upc  = String.valueOf(row.getCell(1).getStringCellValue()).trim();
                    String price  = String.valueOf(row.getCell(10).getNumericCellValue()).trim();
                    String query = "UPDATE product set listPrice = '" + price + "', upc = '" +  upc+ "' WHERE modelNumber = '"  + sku + "' AND manufactuter_id = 8" ;
                    System.out.println(counter + ") " + query);
                    statement.execute(query);
                }
                counter++;
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
