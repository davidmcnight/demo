package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class RalphLauren extends Manufacturer implements ICrawler {
    public int manufacturer_id = 51;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.ralphlaurenhome.com/products/lighting/item.aspx?haid=16&collId=&shaid=&sort=&itemId=36726&phaid=");

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

        ){
            return false;
        }else {
            if(href.contains("www.ralphlaurenhome.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {

            System.out.println(page.getWebURL().getURL());

            if(!page.getWebURL().getURL().toLowerCase().contains("/products/lighting")){
                return;
            }

            Document document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();



            if(!document.select("#ctl00_cphContentFull_cphContentRight_ctl00_lblSKU").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");


                //name
                Elements name = document.select("#ctl00_cphContentFull_cphContentRight_ctl00_lblItemName");
                if(!name.isEmpty()){
                    product.setName(name.first().text());
                }

                //sku
                Elements sku = document.select("#ctl00_cphContentFull_cphContentRight_ctl00_lblSKU");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text());
                }

                //image
                Elements image = document.select("#ctl00_cphContentFull_cphContentRight_ctl00_imgItem");
                if(!image.isEmpty()){
                    product.setImageLink(image.attr("abs:src"));
                }

                FileInputStream file = new FileInputStream(new File("price-sheets/ralph-lauren.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);



                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(1);

                    if(sku2.toString().toUpperCase().contains(product.getModelNumber().toUpperCase())){
                        System.out.println(sku2.toString());
                        product.setListPrice(row2.getCell(5).toString());
                        product.setCollection(row2.getCell(3).toString());
                    }
                }


                System.out.println(product);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

//                for(Product p : products){

//                    ProductService.createProduct(p, this.manufacturer_id);
//
//                }

                ProductService.createProduct(product, this.manufacturer_id);


            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

//

            if(SpecBooksController.PRODUCT_COUNTER > 100000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
