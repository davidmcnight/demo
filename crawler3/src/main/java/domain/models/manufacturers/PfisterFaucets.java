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

public class PfisterFaucets extends Manufacturer implements ICrawler {

    public int manufacturer_id = 11;





    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        return curURL;
    }

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.pfisterfaucets.com/kitchen/product/prive-f-534-7pv-1-handle-pull-out-kitchen-faucet?modelNumber=F-534-7PVS");
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
                || href.contains("ca.pfisterfaucets.com")
               // || href.contains("/parts-support/")

        ){
            return false;
        }else {
            if(href.contains("www.pfisterfaucets.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        Document document =  new Document(page.getWebURL().getURL());
        try {


             document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();

            if(!document.select(".v-product-detail__hero-content-col").isEmpty()){

                //product
                Product product = new Product();

                Elements name = document.select(".v-product-detail__hero-title .h2");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                Elements collection = document.select("h1");
                if(!collection.isEmpty()){
                    product.setCollection(collection.first().ownText().trim());
                }



                //categories
                Elements categories = document.select(".breadcrumbs__item a");
                if(!categories.isEmpty()){
                    int categoryCounter = 1;
                    for (Element category: categories){
                        if(categoryCounter == 2){
                            product.setCategory1(category.text());
                        }
                        if(categoryCounter == 3){
                            product.setCategory2(category.text());
                        }
                        if(categoryCounter == 4){
                            product.setCategory3(category.text());
                        }
                        categoryCounter++;
                    }

                }

                Elements details = document.select(".v-product-detail__info-bar li");
                if(!details.isEmpty()){
                    product.setFinishes(details.get(0).text().trim());
                    product.setModelNumber(details.get(1).text().trim().replace("Model:", "").trim());
                }
                //



                Elements images = document.select("meta[name=og:image]");
                if(!images.isEmpty()){
                    product.setImageLink(document.select("meta[name=og:image]")
                            .first().attr("abs:content"));
                }else {
                    System.out.println(document.location());
                    System.exit(0);
                }

                Elements pricing = document.select("meta[name=product:price:amount]");
                if(!pricing.isEmpty()){
                    product.setListPrice(document.select("meta[name=product:price:amount]")
                            .first().attr("content"));
                }


                product.setName(product.getCollection() + " " + product.getName() + " " + product.getFinishes());
                product.setName(product.getName().replace("null", "").trim());
                product.setUrl(document.location());

                Elements docs = document.select(".icons-block__link");
                if(!docs.isEmpty()){
                    for (Element doc: docs){
                        if(doc.text().toLowerCase().contains("instruction")){
                            product.setInstallationDocument(doc.attr("abs:href"));
                        }
                        if(doc.text().toLowerCase().contains("specification")){
                            product.setSpecificationDocument(doc.attr("abs:href"));
                        }
                        if(doc.text().toLowerCase().contains("maintenance")){
                            product.setCareCleaningDocument(doc.attr("abs:href"));
                        }
                        if(doc.text().toLowerCase().contains("parts")){
                            product.setPartsBreakdownDocument(doc.attr("abs:href"));
                        }
                    }
                }

                Elements desc = document.select("#features .lead");
                if(!desc.isEmpty()){
                    product.setDescription(desc.first().text());
                }

                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 1000000000){
                 System.exit(0);
            }

        } catch (Exception e) {
            System.out.println("THIS IS THE FALILED URL" + document.location());
            e.printStackTrace();
        }

    }
}
