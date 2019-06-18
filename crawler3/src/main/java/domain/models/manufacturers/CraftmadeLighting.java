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

public class CraftmadeLighting extends Manufacturer implements ICrawler {


    public int manufacturer_id = 9;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.craftmade.com/interior-lighting");
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
            if(href.contains("www.craftmadelightinglights.com")){
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

            if(!document.select("#pr_top").isEmpty()) {

                //product
                Product product = new Product();

                product.setCategory1("Lighting");
                product.setUrl(document.location());

                Elements name = document.select(".pr_name span");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }


                product.setModelNumber(document.select("meta[itemprop=sku]")
                        .first().attr("content"));



                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));
                product.setListPrice(document.select("meta[property=og:price:amount]")
                        .first().attr("content"));
                product.setUpc(document.select("meta[property=og:upc]")
                        .first().attr("content"));
                product.setDescription(document.select("meta[name=description]")
                        .first().attr("content"));

                Elements specs = document.select("#general_info li");
                if(!specs.isEmpty()){
                    for (Element spec : specs ){
                        if(spec.text().contains("Collection")){
                            product.setCollection(spec.text().replace("Collection:", "").trim());
                        }
                        if(spec.text().contains("Width")){
                            product.setWidth(spec.text().replace("Width:", "").trim());
                        }
                        if(spec.text().contains("Height")){
                            product.setHeight(spec.text().replace("Height:", "").trim());
                        }
                        if(spec.text().contains("Finish")){
                            product.setFinishes(spec.text().replace("Finish:", "").trim());
                        }
                        if(spec.text().contains("Category")){
                            product.setCategory2(spec.text().replace("Category:", "").trim());
                        }
                        if(spec.text().contains("Install")){
                            product.setInstallationDocument(spec.select("a").attr("abs:href"));
                        }
                        if(spec.text().contains("Spec")){
                            product.setSpecificationDocument(spec.select("a").attr("abs:href"));
                        }
                    }
                }

                //og:price:amount
                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 10) {
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
