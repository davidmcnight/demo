package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import crawlers.SpecBooksCrawler;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class GE extends Manufacturer implements ICrawler {

    public int manufacturer_id = 26;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("https://www.us.kohler.com/us/iron-tones-33-x-18-3-4-x-9-5-8-top-under-mount-single-bowl-kitchen-sink/productDetail/kitchen-sinks/1124118.htm?skuId=1124103&brandId=432857");
//        seeds.add("https://www.us.kohler.com/us/adair-comfort-height-one-piece-elongated-1.28-gpf-toilet-w-aquapiston-flushing-technology-and-left-hand-trip-lever/productDetail/toilets/841539.htm?skuId=841482");
//        seeds.add("https://www.us.kohler.com/us/Bathroom/category/429204.htm");
//        seeds.add("https://www.us.kohler.com/us/Kitchen/category/432288.htm");
        seeds.add("https://products.geappliances.com/appliance/gea-specs/PDT855SSJSS");
        return seeds;
    }


    //debug

    //allowed domain

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
                || href.contains("?r")
                || href.contains("?content")
                || href.contains("sortBy")
                || href.contains("en_US/reviews/")


        ){
            return false;
        }else {
            if(href.contains("products.geappliances.com") || href.contains("www.geappliances.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {
            Document document = Jsoup.connect(page.getWebURL().getURL()).get();
            String url = document.location();
            System.out.println(SpecBooksController.PAGE_COUNTER + ") " + url);
            SpecBooksController.PAGE_COUNTER++;


            if(document.select(".collapse-for-small").text().contains("no longer being manufactured")){
                return;
            }

            //check to see if it is product page
            if(!document.select("#spec-top-container").isEmpty()){
                System.out.println("This is a product page.");
                System.out.println("Number of Products) " + SpecBooksController.PRODUCT_COUNTER);
                SpecBooksController.PRODUCT_COUNTER++;
                try {

                    ArrayList<Product> products = new ArrayList<Product>();
                    Product product = new Product();

                    String title[] = document.title().split("\\u007C");
                    product.setName(title[0].trim().replaceAll("[^\\x00-\\x7f]", ""));
                    product.setModelNumber(title[1].trim());

                    product.setCategory1(document.select(".breadcrumb .column").first().ownText());
                    Elements price = document.select(".bold-price");
                    if(!price.isEmpty()){
                        product.setListPrice(document.select(".bold-price")
                                .first().text().replace("$",""));
                    }



                    int imageCounter = 1;
                    String imageString = "";
                    Elements images = document.select("#spec-image-scroller img");
                    for (Element image : images){
                        if(imageCounter == 1){
                            product.setImageLink(image.attr("abs:src"));
                        }else if( imageCounter == 2){
                            imageString += image.attr("abs:src");
                        }else {
                            imageString += "||" + image.attr("abs:src");
                        }
                        imageCounter++;
                    }
                    product.setAdditionalImages(imageString);


                    product.setSpecificationDocument(document.select("thead a")
                            .first().attr("abs:href"));


                    Elements documents = document.select(".panel .downloads li a");
                    for (Element myDocument: documents ) {
                        if(myDocument.text().toLowerCase().trim().contains("owner")){
                            product.setHomeownersDocument(myDocument.attr("abs:href"));
                        }
                        if(myDocument.text().toLowerCase().trim().contains("install")){
                            product.setInstallationDocument(myDocument.attr("abs:href"));
                        }
                    }

                    //dimensions
                    Elements trs = document.select(".spec-table tr");
                    if(!trs.isEmpty()){
                        for (Element tr: trs){
                            Elements tds = tr.getElementsByTag("td");
                            if(!tds.isEmpty() & tds.size() > 1){
                                String td1 = tr.getElementsByTag("td").first().text();
                                String td2 = tr.getElementsByTag("td").get(1).text();
                                if(td1.toLowerCase().trim().contains("overall depth")){
                                    product.setDepth(td2);
                                }
                                if(td1.toLowerCase().trim().contains("overall height")){
                                    product.setHeight(td2);
                                }
                                if(td1.toLowerCase().trim().contains("overall width")){
                                    product.setWidth(td2);
                                }
                                if(td1.toLowerCase().trim().contains("overall length")){
                                    product.setLength(td2);
                                }
                                if(td1.toLowerCase().trim().contains("color appearance")){
                                    product.setFinishes(td2);
                                }

                            }
                        }
                    }

                    if(product.getFinishes() != null){
                        product.setName(product.getName() + " - " + product.getFinishes());
                    }

                    product.setUrl(document.location());



                    System.out.println(product);
                    products.add(product);

                    for (Product p: products){
                        ProductService.createProduct(p, this.manufacturer_id);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }



            }else {

                System.out.println("Skipping");
            }
            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


//  235981760
//  1565994328