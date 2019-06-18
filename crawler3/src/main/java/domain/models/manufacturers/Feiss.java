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

public class Feiss extends Manufacturer implements ICrawler {

    public int manufacturer_id = 43;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.feiss.com/59373/5---Light-Lantern-F33255SMS.html");

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
            if(href.contains("/www.feiss.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {


            Document document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();

            if(!document.select(".productDetailLeftHolder").isEmpty()){

                //product
                Product product = new Product();

                product.setUrl(document.location());

                //sku
                product.setModelNumber(document.select("meta[name=pNumber]")
                        .first().attr("content"));

                //name
                product.setName(document.select("meta[name=pName]")
                        .first().attr("content"));

                //collection
                Elements collection = document.select(".page_titleDetail");
                if(!collection.isEmpty()){
                    String coll = StringManipulation.capitalCase(
                            collection.first().text().toLowerCase()
                                    .replace("the", "")
                                    .replace("collection", "")).trim();

                    product.setCollection(coll);
                    product.setName(product.getCollection() + " " + product.getName());
                }

                //finish
                Elements finish = document.select(".productDetailRightHolder a h1");
                if(!finish.isEmpty()){
                    String fin = finish.first().text().trim();
                    product.setFinishes(fin);
                    product.setName(product.getName() + " - " + product.getFinishes());
                }

                //final name
                product.setName(product.getName().replace("null", "").trim());

                //image
                product.setImageLink(document.select("meta[name=og:image]")
                        .first().attr("abs:content"));

                //docs
                Elements docs = document.select("td a");

                for (Element doc : docs){
                    if(doc.text().toLowerCase().trim().contains("pdf spec")){
                        product.setSpecificationDocument(document.attr("abs:href"));
                    }
                    if(doc.text().toLowerCase().trim().contains("instructions")){
                        product.setInstallationDocument(document.attr("abs:href"));
                    }
                }





                FileInputStream file = new FileInputStream(new File("price-sheets/feiss-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);


//                boolean hasSku = false;
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);
                    if(product.getModelNumber().contains(sku2.toString().toUpperCase())){
                        product.setListPrice(row2.getCell(3).toString());
                        product.setUpc(row2.getCell(1).toString());

                    }
                }




                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 10000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
