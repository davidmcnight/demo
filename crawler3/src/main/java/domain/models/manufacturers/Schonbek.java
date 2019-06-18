package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Schonbek extends Manufacturer implements ICrawler {

    public int manufacturer_id = 49;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.swarovski-lighting.com/schonbek-brand/alea/al6501-core.html");
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("myFoldersNotLoggedIn")
                || href.contains("product.language")
                || href.contains("facebook")
                || href.contains("designservice")
                || href.contains("smartbim")
                || href.contains("youtube")
                || href.contains("pinterest")
                || href.contains("finish=")
                || href.contains("product_line=")
                || href.contains("light_source=")
                || href.contains("room=")
                || href.contains("brand=")
                || href.contains("cat=")
                || href.contains("id=")
                || href.contains("login=")
                || href.contains("/id/")

        ){
            return false;
        }else {
            if(href.contains("swarovski-lighting.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {


            Document document = Jsoup.connect(page.getWebURL().getURL()).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();



            if(!document.location().contains(".html")){

                if(document.select("meta[property=og:type]").isEmpty()){
                   System.out.println("NOT AVAILABLE");
                   System.out.println(document.html());
                   return;
                }else {
                    System.out.println("AWWWWWWWWWWW YYEEEEEEAAAAAHHHHH");
                }

                //product
                Product product = new Product();
                product.setUrl(document.location());



                Elements sku = document.select(".prod-name");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text());
                }

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                //specs
                for (Element a : document.select("a")){
                    if(a.text().toLowerCase().contains("download specs")){
                        product.setSpecificationDocument(a.attr("abs:href"));
                    }
                }



                FileInputStream file = new FileInputStream(new File("price-sheets/schonbek-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                ArrayList<Product> products = new ArrayList<Product>();

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

                for(Product p : products){

                    System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + p);
                    SpecBooksController.PRODUCT_COUNTER++;
//                    ProductService.createProduct(p, this.manufacturer_id);
                }

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 1){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
