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

public class Acclaim extends Manufacturer implements ICrawler {

    public int manufacturer_id = 56;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.acclaim-lighting.com/products/indoor/in11240orb/");
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
            if(href.contains("acclaim-lighting.com")){
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




            if(!document.select(".product_title").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");


                Elements sku = document.select(".product_title");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text());
                }

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                //specs
                Elements height = document.select(".attribute-height");
                if(!height.isEmpty()){
                    product.setHeight(height.first().ownText());
                }
                Elements width = document.select(".attribute-width");
                if(!width.isEmpty()){
                    product.setWidth(width.first().ownText());
                }

                Elements parts = document.select(".prod-detail-part");
                if(!parts.isEmpty()){
                    for (Element part: parts){
                        if(part.text().toLowerCase().contains("finish")){
                            product.setFinishes(part.ownText().trim());
                        }
                    }
                }
//                Elements finish = document.select(".attribute-finish");
//                if(!finish.isEmpty()){
//                    product.setFinishes(finish.first().ownText());
//                }

                String material = "";
                Elements materialFinish = document.select(".attribute-material");
                if(!materialFinish.isEmpty()){
                    material = "/" + materialFinish.first().ownText();
                }


                ArrayList<Integer> indexesUsed = new ArrayList<Integer>();
                String name = "";
                Elements product_meta = document.select(".product_meta a");
                if(!product_meta.isEmpty()){
                    name += product_meta.first().ownText() + " ";

                    for (Element pm : product_meta){
                        if(pm.attr("href").toLowerCase().contains("collection")){
                            product.setCollection(pm.ownText());
                        }
                    }

                    String subCat = "";
                    int counter = 0;
                    for (Element pm : product_meta){
                        if(counter == 0){
                            counter++;
                            continue;
                        }
                        else if(pm.attr("href").toLowerCase().contains("collection")){
                           continue;
                        }
                        else if(pm.attr("href").toLowerCase().contains("finishes")){
                            continue;
                        }else {
                            subCat = pm.ownText().trim();
                            product.setCategory2(subCat.trim());
                            break;
                        }
                    }

                    name += product.getCollection() + " " +  subCat + " " + product.getFinishes() + material;
                    name = name.replace("null", "").replace("  ", " ").trim();
                    product.setName(name);
                }

                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);


            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
