package executables.sitemaps;

import config.Config;
import domain.models.Product;
import domain.services.ProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Schonbek {


    public static void main(String[] args) {
        try {

            ArrayList<Product> products = new ArrayList<Product>();
            Connection.Response response = null;
            response = Jsoup.connect("https://www.swarovski-lighting.com/sitemap.xml")
                    .userAgent(Config.USER_AGENT)
                    .timeout(0).followRedirects(true)
                    .execute();
            if (response.statusCode() != 200) {
                System.out.println("Error");
            } else {
                Document sitemapDoc = response.parse();
                Elements urls = sitemapDoc.select("loc");
                int counter = 1;

                for (Element url : urls) {

//                    if(counter < 6739){
//                        counter++;
//                        continue;
//                    }

                    if(url.text().contains("cache")){
                        continue;
                    }

                    if (url.text().contains("/product/")) {
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

                            System.out.println(document.text());

                            Elements specs = document.select(".links a");
                            if(!specs.isEmpty()){
                                for (Element spec: specs){
                                    if(spec.text().toLowerCase().contains("download specs")){
                                        System.out.println(spec);
                                    }
                                }
                            }

                            System.out.println("HERE");
                            Elements scripts = document.getElementsByTag("script");
                            System.out.println(scripts.size());
                            if(!scripts.isEmpty()) {
                                for (Element script: scripts) {
                                    for (DataNode node : script.dataNodes()) {
                                        System.out.println(node);
                                    }
                                    if(script.attr("type").contains("text/x-magento-init")){
                                        System.out.println(script.text());
                                    }
                                }
                            }
                            FileInputStream file = new FileInputStream(new File("price-sheets/schonbek-pricing.xlsx"));
                            Workbook workbook = new XSSFWorkbook(file);
                            Sheet sheet = workbook.getSheetAt(0);
                            for (Row row2 : sheet) {
                                Cell sku2 = row2.getCell(0);
                                if(product.getModelNumber().contains(sku2.toString().toUpperCase())){
                                    Product finishProduct = new Product(product);
                                    finishProduct.setName(row2.getCell(1).toString());
                                    finishProduct.setCategory2(row2.getCell(2).toString().replace("null", "").trim());
                                    finishProduct.setUpc(row2.getCell(3).toString());
                                    finishProduct.setListPrice(row2.getCell(4).toString());
                                }
                            }

//                            System.out.println(product);
                            System.out.println("________________________________________________________________________________________");

//                            if(counter >50){
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
