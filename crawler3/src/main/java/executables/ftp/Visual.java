package executables.ftp;

import config.Config;
import domain.models.Product;
import domain.services.ProductService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Visual {


    public static void main(String[] args){
        try {
            System.out.println("Visual Comfort pricing");
            FileInputStream file = new FileInputStream(new File("ftp-sheets/vc-ftp-sheet.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            Connection connection = DriverManager.getConnection(Config.connString);
            Statement statement = connection.createStatement();

            int counter = 0;
            for (Row row : sheet) {
                if (counter != 0) {
                    Product product = new Product();

                    String mn = row.getCell(1).toString().trim();

                    product.setModelNumber(row.getCell(0).toString().trim().replace(" ", ""));
                    product.setName(row.getCell(3).toString().trim());
                    product.setFinishes(row.getCell(4).toString().trim());

                    product.setCategory1("Lighting");
                    product.setCategory2(row.getCell(5).toString().trim());

                    if(!row.getCell(6).toString().trim().contains("N/A")){
                        product.setHeight(row.getCell(6).toString().trim());
                    }

                    if(!row.getCell(7).toString().trim().contains("N/A")){
                        product.setWidth(row.getCell(7).toString().trim());
                    }

                    product.setListPrice(row.getCell(8).toString().trim());


                    //docs and image (base from model normalized)
                    product.setImageLink("http://74.208.216.201/vc/images/" + mn.toUpperCase() + ".png");
                    product.setSpecificationDocument("http://74.208.216.201/vc/tearsheets/TS_" + mn.toUpperCase() + ".pdf");
                    product.setInstallationDocument("http://74.208.216.201/vc/installguides/IG_" + mn.toUpperCase() + ".pdf");

                    ProductService.createProduct(product, 62);
                    System.out.println(product);
                }
                counter++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
