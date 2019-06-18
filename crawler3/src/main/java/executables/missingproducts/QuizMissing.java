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

public class QuizMissing {


    public static void main(String[] args){

        try {

            System.out.println("Running Quiozel Missing Products");

            JSONObject jsonObject = new JSONObject();
            ResultSet products = ProductService.getAllProductsByManufacturer(2);
            while (products.next()){
               jsonObject.put(products.getString("modelNumber").trim(), 1);
            }


            FileInputStream file = new FileInputStream(new File("price-sheets/quoizel-ps.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int counter = 1;
            int missing_counter =1;


            Workbook workbook2 = new XSSFWorkbook();
            Sheet sheet2 = workbook.createSheet("missing_products");


            for (Row row : sheet) {
                Cell sku = row.getCell(0);;
                if(counter > 1){
                   if(!jsonObject.has(sku.getStringCellValue().trim())){
                       //missing-products-sheets
                       missing_counter++;
                       System.out.println(sku);
                   }
                }
                counter++;
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
