
package domain.models.manufacturers;
import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Atlas extends Manufacturer implements ICrawler {


    private int manufacturer_id = 18;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.atlashomewares.com/appliance-hardware/collections/craftsman/ap03-o-craftsman-appliance-pull-15-inch-c-c.html");
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")
                || href.contains("add-to-cart")

        ){
            return false;
        }else {
            if(href.contains("www.atlashomewares.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        try {

            System.out.println(page.getWebURL());
            Document document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();

            if(!document.select(".product-view").isEmpty()){

                Product product = new Product();
                //final product name ===  Collection Name + Name + Finish

                //url
                product.setUrl(document.location());

                //name
                Elements name = document.select(".product-name h1");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                Elements price = document.select(".price");
                if (!price.isEmpty()) {
                    product.setListPrice(price.first().text().trim());
                }

                Elements specs = document.select(".attributes li");
                for(Element spec : specs){
                    if(spec.child(0).text().toLowerCase().contains("collection")){
                        product.setCollection(spec.child(1).text().trim());
                    }
                    if(spec.child(0).text().toLowerCase().contains("part")){
                        product.setModelNumber(spec.child(1).text().trim());
                    }
                    if(spec.child(0).text().toLowerCase().contains("finish")){
                        product.setFinishes(spec.child(1).text().trim());
                    }
                    if(spec.child(0).text().toLowerCase().contains("width")){
                        product.setWidth(spec.child(1).text().trim());
                    }
                    if(spec.child(0).text().toLowerCase().contains("length")){
                        product.setLength(spec.child(1).text().trim());
                    }
                }


                // Image is being used by background-image, concerned.
                Elements image = document.select("meta[property=og:image]");
                if(!image.isEmpty()){
                    product.setImageLink(image.first().attr("abs:content"));
                }

                String fullName = product.getName() + " " + product.getFinishes();
                fullName = fullName.replace("null", "").trim();
                product.setName(fullName);
                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");

                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 10) {
                    System.exit(0);
                }



            }





        }catch (Exception e){
            e.printStackTrace();
        }


    }
}




