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

public class BaldwinPricing {


    public static void main(String[] args){

        try {
            System.out.println("Baldwin Pricing");
            FileInputStream file = new FileInputStream(new File("price-sheets/baldwin-estate-3.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);


            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();

            int counter = 1;
            for (Row row : sheet) {
                if(counter != 0){
                    String sku = row.getCell(0).getStringCellValue().trim();
                    System.out.println(sku);
                    String[] skusAr = sku.split("\\.");
                    String upc  = String.valueOf(row.getCell(5).getNumericCellValue()).trim();
                    String price  =  String.valueOf(row.getCell(4).getNumericCellValue()).trim();
                    String query = "";
                    if(skusAr.length > 2){

                         query = "UPDATE product set listPrice = '" + price + "', upc = '" +  upc+ "' WHERE modelNumber LIKE '"  + skusAr[0] + "." + skusAr[1] + "%' AND manufactuter_id = 13" ;
                         System.out.println("concat");
                         System.out.println(counter + ") " + query);
                    }else {

                         query = "UPDATE product set listPrice = '" + price + "', upc = '" +  upc+ "' WHERE modelNumber LIKE '"  + sku + "%' AND manufactuter_id = 13" ;
                        System.out.println(counter + ") " + query);
                    }


                    statement.execute(query);
                }
                counter++;
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
