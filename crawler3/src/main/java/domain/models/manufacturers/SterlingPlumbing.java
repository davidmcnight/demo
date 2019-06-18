package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import crawlers.SpecBooksCrawler;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SterlingPlumbing extends Manufacturer implements ICrawler {

    public int manufacturer_id = 64;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.sterlingplumbing.com/product-detail/72180126?skuid=72180126-0");
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
                || href.contains("twitter")
                || href.contains("svg")


        ){
            return false;
        }else {
            if(href.contains("www.sterlingplumbing.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {

            System.out.println(SpecBooksController.PAGE_COUNTER + ") " + page.getWebURL());
            if(page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();
                Document document = Jsoup.parseBodyFragment(html);

                if(!document.select(".koh-product-details").isEmpty()) {

                    ArrayList<Product> products = new ArrayList<Product>();

                    Product product = new Product();

                    product.setUrl(page.getWebURL().toString().trim());

                    Elements collection = document.select(".koh-product-name");
                    if(!collection.isEmpty()){
                        product.setCollection(StringManipulation.stripSpecialCharacters(collection.first().text().trim()));
                    }

                    Elements name = document.select(".koh-product-short-description");
                    if(!name.isEmpty()){
                        product.setName(product.getCollection().trim().replace("null", "")
                                + " " + StringManipulation.stripSpecialCharacters(name.first().text().trim()));
                    }

                    for (Element spec : document.select(".koh-product-pdf")){
                        if(spec.text().toLowerCase().contains("dimen")){
                            product.setSpecificationDocument(spec.attr("abs:href"));
                        }
                    }



                    Elements finishes = document.select("li span.koh-product-variant");
                    if(!finishes.isEmpty()){

                        for (Element finish : finishes){
                            System.out.println(finish);
                            Product finishProduct = new Product(product);
                            finishProduct.setImageLink(finish.attr("data-koh-image"));
                            finishProduct.setModelNumber(finish.attr("data-koh-sku").trim());
                            finishProduct.setListPrice(finish.attr("data-koh-price").trim()
                                    .replace("{&quot;discounted&quot;:&quot;", "")
                                    .replace("&quot;}", "").trim()
                                    .replace("{\"discounted\":\"", "")
                                    .replace("\"}", "")
                                    .trim()
                            );
                            finishProduct.setFinishes(finish.attr("data-koh-color").trim());
                            finishProduct.setName(finishProduct.getName() + " - " + finishProduct.getFinishes());
                            products.add(finishProduct);
                        }

                    }

                    for (Product p : products) {
                        System.out.println(p);
                         ProductService.createProduct(p, this.manufacturer_id);
                    }

                    SpecBooksController.PRODUCT_COUNTER++;
                    if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                        System.exit(0);
                    }

                }else {
                    System.out.println("Not a product page");
                }

            }else {

                System.out.println("Skipping");
            }

            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");
            SpecBooksController.PAGE_COUNTER++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


//  235981760
//  1565994328