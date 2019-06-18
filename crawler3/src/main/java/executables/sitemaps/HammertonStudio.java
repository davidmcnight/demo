package executables.sitemaps;

import config.Config;
import controllers.SpecBooksController;
import domain.models.Product;
import domain.services.ProductService;
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.ArrayList;

public class HammertonStudio {


    public static void main(String[] args) {
        try {

            ArrayList<Product> products = new ArrayList<Product>();
            Connection.Response response = null;
            response = Jsoup.connect("http://hammertonstudio.com/sitemap.xml")
                    .userAgent(Config.USER_AGENT)
                    .timeout(0).followRedirects(true)
                    .execute();
            if (response.statusCode() != 200) {
                System.out.println("Error");
            } else {
                Document sitemapDoc = response.parse();
                Elements urls = sitemapDoc.select("loc");
                int counter = 0;

                for (Element url : urls) {

//                    if(counter < 3266){
//                        counter++;
//                        continue;
//                    }

                    products.clear();

                    if (url.text().contains("-products/")) {
                        System.out.println(counter + ") " + url.text());
                        counter++;

                        Connection.Response response2 = null;
                        response2 = Jsoup.connect(url.text().trim())
                                .userAgent(Config.USER_AGENT)
                                .timeout(0).followRedirects(true)
                                .execute();
                        if (response.statusCode() != 200) {
                            System.out.println("Error");
                        } else {


                            Document document = response2.parse();
                            Product product = new Product();

//                            FileInputStream file = new FileInputStream(new File("price-sheets/maxim-pricing.xlsx"));
//                            Workbook workbook = new XSSFWorkbook(file);
//                            Sheet sheet = workbook.getSheetAt(0);
//
//                            int innerCounter =1;
//                            for (Row row2 : sheet) {
//                                System.out.println(product.getModelNumber());
//                                System.out.println(product.getUrl());
//                                System.out.println(innerCounter);
//                                Cell sku2 = row2.getCell(0);
//                                if(product.getModelNumber().toUpperCase().trim().contains(sku2.toString().toUpperCase().trim())){
//                                    product.setListPrice(row2.getCell(1).toString());
//                                    product.setUpc(row2.getCell(3).toString());
//
//                                }
//                                innerCounter++;
//                            }

                            System.out.println(product);
//                            ProductService.createProduct(product,41);
                            System.out.println("________________________________________________________________________________________");

//                            if(counter >1){
//                                System.exit(0);
//                            }

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
