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

public class Kohler extends Manufacturer implements ICrawler {

    public int manufacturer_id = 63;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("https://www.us.kohler.com/us/iron-tones-33-x-18-3-4-x-9-5-8-top-under-mount-single-bowl-kitchen-sink/productDetail/kitchen-sinks/1124118.htm?skuId=1124103&brandId=432857");
//        seeds.add("https://www.us.kohler.com/us/adair-comfort-height-one-piece-elongated-1.28-gpf-toilet-w-aquapiston-flushing-technology-and-left-hand-trip-lever/productDetail/toilets/841539.htm?skuId=841482");
//        seeds.add("https://www.us.kohler.com/us/Bathroom/category/429204.htm");
//        seeds.add("https://www.us.kohler.com/us/Kitchen/category/432288.htm");
        seeds.add("https://www.us.kohler.com/us/browse/_/N-1z126pu?Nr=AND%28product.language%3AEnglish%2CP_market%3AKPNASite%29");
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
            if(href.contains("www.us.kohler.com")){
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

            //check to see if it is product page
            if(!document.select("#product-detail").isEmpty()){
                System.out.println("This is a product page.");
                System.out.println("Number of Products) " + SpecBooksController.PRODUCT_COUNTER);
                SpecBooksController.PRODUCT_COUNTER++;
                try {

                    ArrayList<Product> products = new ArrayList<Product>();
                    Product baseProduct = new Product();

                    Elements collection = document.select(".product-detail__name");
                    String name = "";
                    if (!collection.isEmpty()) {
                        name = StringManipulation.stripSpecialCharacters(collection.first().text().trim());
                        baseProduct.setCollection(name);
                    }


                    Elements baseName = document.select(".product-detail__description");
                    if (!baseName.isEmpty()) {
                        if (StringManipulation.stripSpecialCharacters(baseName.first().text().trim()).startsWith(name.trim())) {
                            name = StringManipulation.stripSpecialCharacters(baseName.first().text().trim());
                        } else {
                            name = name + " " + StringManipulation.stripSpecialCharacters(baseName.first().text().trim());
                        }

                    }
                    baseProduct.setName(name);


                    Elements documents = document.select("a");
                    for (Element doc : documents) {
                        if (doc.text().contains("Care & Cleaning Tips")) {
//                baseProduct.setCareCleaningDocument(doc.attr("abs:href"));
                        }
                        if (doc.text().contains("Product Warranty")) {
//                baseProduct.setWarrantyDocument(doc.attr("abs:href"));
                        }
                        if (doc.text().contains("Home Guide")) {
                            baseProduct.setHomeownersDocument(doc.attr("abs:href"));
                        }

                        if (doc.text().contains("Parts")) {
                            baseProduct.setPartsBreakdownDocument(doc.attr("abs:href"));
                        }

                        if (doc.text().contains("Specification Sheet")) {
                            String spec = doc.attr("onclick").replace("window.open(", "").replace(")", "");
                            spec = "https://www.us.kohler.com/" + spec.replace("'", "");
                            baseProduct.setSpecificationDocument(spec);
                        }
                        if (doc.text().contains("Specification Sheet")) {
                            String spec = doc.attr("onclick").replace("window.open(", "")
                                    .replace(")", "").replace("'", "");
                            spec = "https://www.us.kohler.com" + spec;
                            baseProduct.setSpecificationDocument(spec);
                        }
                        Elements description = document.select(".product-detail__features-description");
                        if (!description.isEmpty()) {
                            baseProduct.setDescription(StringManipulation.stripSpecialCharacters(description.first().text().trim()));
                        }

                        if (doc.text().toLowerCase().contains("installation")) {
                            System.out.println("Has home owners");
                            String spec = doc.attr("onclick").replace("titlepath('", "")
                                    .replace(")", "").replace("'", "");
                            spec = "https://www.us.kohler.com" + spec;
                            System.out.println(spec);
                            baseProduct.setInstallationDocument(spec);
                        }

                        baseProduct.setUrl(document.location());
                    }
                    Elements finishes = document.select(".product-detail__colors div");
                    if (finishes.isEmpty()) {
                        System.out.println("Here at doc 1");
//            System.exit(14);
                    }
                    for (Element finish : finishes) {
                        String[] finishData = finish.attr("data-getdata").split(",");
                        String sku = finishData[0].replace("[", "").replace("'", "").trim();
                        String price = finishData[1].replace("[", "").replace("'", "").trim();
                        String finishName = StringManipulation.stripSpecialCharacters(finishData[5].replace("[", "").replace("'", "").trim());
                        Product finishProduct = new Product(baseProduct);
                        finishProduct.setModelNumber(sku);
                        finishProduct.setListPrice(price);
                        finishProduct.setFinishes(finishName);
                        finishProduct.setName(finishProduct.getName() + " " + finishName);
                        if(finishData[7].trim().replace("'", "").length() > 20){
                            finishProduct.setImageLink(finishData[7].trim().replace("'", ""));
                        }else if(finishData[8].trim().replace("'", "").length() > 20){
                            finishProduct.setImageLink(finishData[8].trim().replace("'", ""));
                        }
                        else if(finishData[6].trim().replace("'", "").length() > 20){
                            finishProduct.setImageLink(finishData[6].trim().replace("'", ""));
                        }

                        System.out.println(finishProduct);
                        products.add(finishProduct);
                    }
                    baseProduct = new Product();
                    Elements productOptions = document.select("#productDetailsOptions select option");
                    if (!productOptions.isEmpty()) {
                        for (Element po : productOptions) {
                            if (po.hasAttr("value")) {
                                if (po.attr("value").trim().length() > 10) {

                                    String url2 = "https://www.us.kohler.com/us/" + po.attr("value");
                                    Document document2 = Jsoup.connect(url2).
                                            timeout(0).maxBodySize(0).userAgent(Config.USER_AGENT).followRedirects(true).get();
                                    System.out.println(url2);

                                    Elements collection2 = document2.select(".product-detail__name");
                                    String name2 = "";
                                    if (!collection2.isEmpty()) {
                                        name2 = StringManipulation.stripSpecialCharacters(collection2.first().text().trim());
                                        baseProduct.setCollection(name2);
                                    }
                                    Elements baseName2 = document2.select(".product-detail__description");
                                    if (!baseName2.isEmpty()) {
                                        if (StringManipulation.stripSpecialCharacters(baseName2.first().text().trim()).startsWith(name2.trim())) {
                                            name2 = StringManipulation.stripSpecialCharacters(baseName2.first().text().trim());
                                        } else {
                                            name2 = name2 + " " + StringManipulation.stripSpecialCharacters(baseName2.first().text().trim());
                                        }
                                    }
                                    baseProduct.setName(name2);
                                    Elements documents2 = document2.select("a");
                                    for (Element doc : documents2) {
                                        if (doc.text().contains("Care & Cleaning Tips")) {
//                                baseProduct.setCareCleaningDocument(doc.attr("abs:href"));
                                        }
                                        if (doc.text().contains("Product Warranty")) {
//                                baseProduct.setWarrantyDocument(doc.attr("abs:href"));
                                        }
                                        if (doc.text().contains("Home Guide")) {
                                            baseProduct.setHomeownersDocument(doc.attr("abs:href"));
                                        }
                                        if (doc.text().contains("Parts")) {
                                            baseProduct.setPartsBreakdownDocument(doc.attr("abs:href"));
                                        }
                                        if (doc.text().contains("Specification Sheet")) {
                                            String spec = doc.attr("onclick").replace("window.open(", "").replace(")", "");
                                            spec = "https://www.us.kohler.com/" + spec.replace("'", "");
                                            baseProduct.setSpecificationDocument(spec.replace("'", ""));
                                        }
                                        if (doc.text().contains("Homeowners")) {
                                            String spec = doc.attr("onclick").replace("titlepath('", "")
                                                    .replace(")", "").replace("'", "");
                                            spec = "https://www.us.kohler.com" + spec;
                                            System.out.println(spec);
                                            baseProduct.setInstallationDocument(spec.replace("'", ""));
                                        }

                                        Elements description = document2.select(".product-detail__features-description");
                                        if (!description.isEmpty()) {
                                            baseProduct.setDescription(StringManipulation.stripSpecialCharacters(description.first().text().trim()));
                                        }
                                    }
                                    baseProduct.setUrl(document2.location());

                                    System.out.println(document2.location());

                                    Elements finishes2 = document2.select(".product-detail__colors div");
                                    if (finishes2.isEmpty()) {
                                        System.out.println("Here at doc 2");
//                            System.exit(14);
                                    }
                                    for (Element finish : finishes2) {
                                        String[] finishData = finish.attr("data-getdata").split(",");
                                        String sku = finishData[0].replace("[", "").replace("'", "").trim();
                                        String price = finishData[1].replace("[", "").replace("'", "").trim();
                                        String finishName = StringManipulation.stripSpecialCharacters(finishData[5].replace("[", "").replace("'", "").trim());
                                        Product finishProduct = new Product(baseProduct);
                                        finishProduct.setModelNumber(sku);
                                        finishProduct.setListPrice(price);
                                        finishProduct.setFinishes(finishName);
                                        finishProduct.setName(finishProduct.getName() + " " + finishName);
                                        if(finishData[7].trim().replace("'", "").length() > 20){
                                            finishProduct.setImageLink(finishData[7].trim().replace("'", ""));
                                        }else if(finishData[8].trim().replace("'", "").length() > 20){
                                            finishProduct.setImageLink(finishData[8].trim().replace("'", ""));
                                        }
                                        else if(finishData[6].trim().replace("'", "").length() > 20){
                                            finishProduct.setImageLink(finishData[6].trim().replace("'", ""));
                                        }
                                        System.out.println(finishProduct);
                                        products.add(finishProduct);
                                    }
                                }
                            }
                        }
                    }


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