package executables.priceupdates;

import config.Config;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CapitalPricing {


    public static void main(String[] args){

        try {
            System.out.println("Capital Pricing");
            FileInputStream file = new FileInputStream(new File("price-sheets/capital-pricing.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);


            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();

            int counter = 1;
            for (Row row : sheet) {
                if(counter != 1){
                    String sku = row.getCell(0).getStringCellValue().trim();
                    String price  = String.valueOf(row.getCell(3).getNumericCellValue()).trim();
                    String query = "UPDATE product set listPrice = '" + price + "' WHERE modelNumber = '"  + sku + "' AND manufactuter_id = 3" ;
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
