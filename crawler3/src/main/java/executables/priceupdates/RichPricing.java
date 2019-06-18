package executables.priceupdates;

import config.Config;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RichPricing {


    public static void main(String[] args){

        try {

            FileInputStream file = new FileInputStream(new File("price-sheets/richleu.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();

            int counter = 1;
            for (Row row : sheet) {
                if(counter != 1){
                    String sku = row.getCell(0).toString().trim();
                    String price = row.getCell(7).toString().trim();


                    if(row.getCell(0).getCellType() == 0){
                        sku = new BigDecimal(sku.toString()).toPlainString().replace(".0", "");
                    }

                    String query = "UPDATE product set listPrice = '" + price + "'" + " WHERE modelNumber LIKE '"  + sku + "%' AND manufactuter_id = 2" ;
                    System.out.println(query);
                    statement.execute(query);
                }
                counter++;
            }




        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
