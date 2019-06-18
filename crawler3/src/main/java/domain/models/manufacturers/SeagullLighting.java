package domain.models.manufacturers;

import com.sleepycat.utilint.StringUtils;
import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.apache.poi.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SeagullLighting extends Manufacturer implements ICrawler {


    public int manufacturer_id = 7;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.seagulllighting.com/57766/One-Light-Wall-/-Bath-Sconce-4134501EN-848.html");
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
            if(href.contains("seagulllighting.com")){
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

            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(!document.select(".maincontentProduct").isEmpty()) {

                //product
                Product product = new Product();

                product.setCategory1("Lighting");
                product.setUrl(document.location());

                //sku
                product.setModelNumber(document.select("meta[name=pNumber]")
                        .first().attr("content"));

                //name
                product.setName(document.select("meta[name=pName]")
                        .first().attr("content"));

                //price
                product.setListPrice(document.select("meta[name=pPrice]")
                        .first().attr("content"));

                //image
                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                Elements finish = document.select(".productDescText a");
                if(!finish.isEmpty()){
                    for (Element f : finish){
                        if(f.text().toLowerCase().trim().endsWith("finish")){
                            product.setFinishes(StringManipulation.capitalCase(
                                    f.text().toLowerCase().trim()).replace("Finish", "").trim());
                        }
                    }
                }

                Elements collection = document.select(".productTextHeading");
                if(!collection.isEmpty()){
                    for (Element c : collection){
                        if(c.text().toLowerCase().trim().endsWith("collection")){
                            product.setCollection(StringManipulation.capitalCase(
                                    c.text().toLowerCase().trim()).replace("Collection", "").trim());
                        }
                    }
                }

                product.setName(product.getCollection() + " " + product.getName() + " " +
                        product.getFinishes().replace("null",""));

                Elements docs = document.select("a");
                for(Element d: docs){
                    if (d.text().trim().toLowerCase().contains("install")){
                        product.setInstallationDocument(d.attr("abs:href"));
                    }
                    if (d.text().trim().toLowerCase().contains("part")){
                        product.setPartsBreakdownDocument(d.attr("abs:href"));
                    }
                }

                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 10000000) {
                     System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
