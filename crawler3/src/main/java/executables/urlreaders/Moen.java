package executables.urlreaders;

import config.Config;
import domain.models.Product;
import domain.services.ProductService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;

public class Moen {


    public static void main(String[] args){

        try {

            System.out.println("Running Moen Script");
            FileInputStream file = new FileInputStream(new File("price-sheets/moen-2.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int counter = 0;
            for (Row row : sheet) {

                System.out.println(counter);

                if(counter < 2333){
                    counter++;
                    continue;
                }

                Product product = new Product();
                if (counter != 0) {
                    String sku = row.getCell(0).toString();

                    Connection.Response response = null;
                    response = Jsoup.connect("https://www.moen.com/products/" + sku.replace(".0", ""))
                            .userAgent(Config.USER_AGENT)
                            .timeout(0).followRedirects(true).ignoreHttpErrors(true)
                            .execute();
                    if (response.statusCode() != 200) {
                        System.out.println("https://www.moen.com/products/" + sku.replace(".0", ""));
                        System.out.println(response.statusCode());
                        System.out.println("Error");
                    } else {
                        Document document = response.parse();

                        product.setUrl("https://www.moen.com/products/" + sku.replace(".0", ""));

                        product.setModelNumber(sku.replace(".0", ""));
                        product.setUpc(row.getCell(1).toString());
                        product.setListPrice(row.getCell(2).toString());
                        product.setLength(row.getCell(3).toString());
                        product.setWidth(row.getCell(4).toString());
                        product.setHeight(row.getCell(5).toString());
                        product.setSpecificationDocument(row.getCell(6).toString());
                        product.setImageLink(row.getCell(7).toString());

                        if(row.getCell(8).toString().contains("Not Applicable")){
                            product.setCollection("");
                        }else {
                            product.setCollection(row.getCell(8).toString());
                        }

                        if(row.getCell(9).toString().contains("N/A")){
                            product.setFinishes("");
                        }else {
                            product.setFinishes(row.getCell(9).toString());
                        }

                        product.setPartsBreakdownDocument(row.getCell(10).toString());
                        product.setInstallationDocument(row.getCell(11).toString());

                        Elements name = document.select("h1");
                        if (!name.isEmpty()) {
                            product.setName(name.first().text());

                        }

                        ProductService.createProduct(product, 68);
                        System.out.println(product);
                    }
                    counter++;
                }else {
                    counter++;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
