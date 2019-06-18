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

public class Samsung extends Manufacturer implements ICrawler {

    public int manufacturer_id = 27;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://jennair.com/appliances/details/JF42NXFXDE");
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
                || href.contains("producthelp.jennair.com")



        ){
            return false;
        }else {
            if(href.contains("jennair.com")){
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
            if(!document.select("#prod-name").isEmpty()){
                System.out.println("This is a product page.");
                System.out.println("Number of Products) " + SpecBooksController.PRODUCT_COUNTER);
                SpecBooksController.PRODUCT_COUNTER++;
                try {

                    ArrayList<Product> products = new ArrayList<Product>();
                    Product product = new Product();

                    product.setName(StringManipulation.stripSpecialCharacters(document.select("#prod-name").first().ownText()));
                    product.setModelNumber(StringManipulation.stripSpecialCharacters(document.select("#prod-num").first().text()));
                    product.setImageLink(document.select("#product-detail-limbo img").attr("abs:src"));
                    if(!document.select(".prod-price").isEmpty()){
                        product.setListPrice(document.select(".prod-price").first().text()
                                .replace("MSRP","")
                                .replace("$","")
                                .replace(",", "")
                                .replace(" ", "")
                                .replace("*", "")
                                .trim()
                        );
                    }


                    product.setDescription(StringManipulation.stripSpecialCharacters(
                            document.select("#prod-desc").first().text().trim()
                    ));

                    Elements specsGroups = document.select(".specs-group");
                    for (Element element: specsGroups){
                        if(element.select(".heading").text().toLowerCase().contains("dimen")){
                            for (Element li :element.select("li")) {
                                if(li.text().trim().startsWith("Depth:")){
                                    product.setDepth(li.text().trim().replace("Depth:", ""));
                                }
                                if(li.text().trim().startsWith("Height:")){
                                    product.setHeight(li.text().trim().replace("Height:", ""));
                                }
                                if(li.text().trim().startsWith("Width:")){
                                    product.setWidth(li.text().trim().replace("Width:", ""));
                                }
                                if(li.text().trim().startsWith("Length:")){
                                    product.setLength(li.text().trim().replace("Length:", ""));
                                }
                            }
                        }
                    }

                    Elements images = document.select("#product-carousel-thumbs a");
                    int imageCounter =1;
                    String imgStr = "";
                    for (Element img: images){
                        if(imageCounter == 1){
                            imgStr += img.attr("abs:href");
                        }
                        imgStr += "||" + img.attr("abs:href");
                        imageCounter++;
                    }
                    product.setAdditionalImages(imgStr);

                    Elements docs = document.select("#manuals a");
                    for (Element myDoc: docs) {
                        if(myDoc.text().toLowerCase().trim().contains("dimen")){
                            product.setSpecificationDocument(myDoc.attr("abs:href"));
                        }
                        if(myDoc.text().toLowerCase().trim().contains("specs")){
                            product.setSpecificationDocument(myDoc.attr("abs:href"));
                        }
                        if(myDoc.text().toLowerCase().trim().contains("install")){
                            product.setInstallationDocument(myDoc.attr("abs:href"));
                        }
                        if(myDoc.text().toLowerCase().trim().contains("care")){
                            product.setCareCleaningDocument(myDoc.attr("abs:href"));
                        }
                    }

                    if(!document.select(".warranty-section a").isEmpty()){
                        product.setWarrantyDocument(document.select(".warranty-section a").first().attr("abs:href"));
                    }

                    product.setUrl(document.location());

                    Elements swatches = document.select("#swatch-list li a");

                    if(swatches.isEmpty()){
                        products.add(product);
                        System.out.println(product);
                    }else {
                        for(Element swatch: swatches){
                            Product childProduct = new Product(product);
                            childProduct.setModelNumber(swatch.attr("data-product"));
                            childProduct.setName(childProduct.getName() + " " + StringManipulation.stripSpecialCharacters(swatch.attr("alt")));
                            childProduct.setFinishes(StringManipulation.stripSpecialCharacters(swatch.attr("alt")));
                            childProduct.setImageLink(StringManipulation.stripSpecialCharacters(swatch.attr("abs:href")));
                            products.add(childProduct);
                            System.out.println(childProduct);
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