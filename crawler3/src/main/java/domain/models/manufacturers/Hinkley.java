package domain.models.manufacturers;

import controllers.SpecBooksController;
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

public class Hinkley extends Manufacturer implements ICrawler {

    public int manufacturer_id = 34;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.hinkleylighting.com/67012en-67012en.html");
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
            if(href.contains("www.hinkleylighting.com")){
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


            Product product = new Product();

            product.setUrl(url);
            product.setCategory1("Lighting");

            if(!document.select(".product-top").isEmpty()){

                Elements sku = document.select(".page-title span");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim());
                }else {
                    System.out.println("SKU is different");
                    System.out.println(document.location());
                    System.exit(0);
                }

                Elements collection = document.select(".page-title");
                if(!collection.isEmpty()){
                    product.setCollection(collection.first().ownText().trim());
                }



//                Elements name = document.select(".product-name");
//                if(!name.isEmpty()){
//                    product.setName(name.first().text().trim());
//                }

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                product.setName(document.select("meta[property=og:title]")
                        .first().attr("content"));

                Elements attributes = document.select(".attributes-list li");
                if(!attributes.isEmpty()){
                    for (Element e: attributes){
                        if(e.child(0).text().toLowerCase().contains("finish")){
                            product.setFinishes(e.child(1).ownText().trim());
                        }
                        if(e.child(0).text().toLowerCase().contains("height")){
                            product.setHeight(e.child(1).ownText().trim());
                        }
                        if(e.child(0).text().toLowerCase().contains("width")){
                            product.setWidth(e.child(1).ownText().trim());
                        }
                        if(e.child(0).text().toLowerCase().contains("length")){
                            product.setLength(e.child(1).ownText().trim());
                        }
                        if(e.child(0).text().toLowerCase().contains("upc")){
                            product.setUpc(e.child(1).ownText().trim());
                        }

                    }
                }

                Elements docs = document.select(".link-list a");
                if(!docs.isEmpty()){
                    for (Element d: docs){
                        if(d.text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(d.attr("abs:href"));
                        }
                        if(d.text().toLowerCase().contains("install")){
                            product.setInstallationDocument(d.attr("abs:href"));
                        }
                        if(d.text().toLowerCase().contains("instruct")){
                            product.setInstallationDocument(d.attr("abs:href"));
                        }
                        if(d.text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(d.attr("abs:href"));
                        }
                    }
                }


                Elements price =  document.select("meta[property=product:price:amount]");
                if(!price.isEmpty()){
                    product.setListPrice(price.attr("content").trim());
                }


                product.setName(product.getCollection() + " " + product.getName() + " - " +  product.getFinishes());
                product.setName(product.getName().replace("null",""));
                product.setName(StringManipulation.stripSpecialCharacters(product.getName()));



                System.out.println(SpecBooksController.PRODUCT_COUNTER + ")" + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);
                if(SpecBooksController.PRODUCT_COUNTER > 10000000){
                    System.exit(0);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
