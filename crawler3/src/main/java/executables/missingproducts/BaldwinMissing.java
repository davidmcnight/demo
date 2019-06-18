package executables.missingproducts;

import domain.services.ProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;

public class BaldwinMissing {


    public static void main(String[] args){

        try {

            System.out.println("Alno Missing Products");


            ResultSet products = ProductService.getAllProductsByManufacturer(15);


            FileInputStream file = new FileInputStream(new File("price-sheets/del-3.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int counter = 1;
            int missing_counter =1;


            Workbook workbook2 = new XSSFWorkbook();
            Sheet sheet2 = workbook.createSheet("missing_products_baldwin");

            while (products.next()){

                for (Row row : sheet) {
                    Cell sku = row.getCell(0);
                    System.out.println(products.getString("modelNumber"));
                }
            }





        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
