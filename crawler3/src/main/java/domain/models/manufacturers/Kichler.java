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

public class Kichler extends Manufacturer implements ICrawler {


    public int manufacturer_id = 8;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.kichler.com/products/product/outdoor-wall-1lt-oz-49227oz.aspx");
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
            if(href.contains("www.kichler.com")){
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

            if(!document.select("#product").isEmpty()) {

                //product
                Product product = new Product();


                product.setCategory1("Lighting");
                product.setUrl(document.location());
                product.setCollection("");


                product.setName(document.title());

                //sku
                Elements sku = document.select("#product h1 span");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim().split(" ")[0]);
                }

                Elements images = document.select("#Body_Body_ProductDetails_8_pnlImages img");
                if(!images.isEmpty()){
                    product.setImageLink(images.first().attr("abs:src"));
                }

                Elements specs = document.select(".technical tr");
                if(!specs.isEmpty()){
                    for (Element spec : specs){
                        if(spec.child(0).text().toLowerCase().contains("height")){
                            product.setHeight(spec.child(1).text().trim());
                        }
                        if(spec.child(0).text().toLowerCase().contains("width")){
                            product.setWidth(spec.child(1).text().trim());
                        }
                        if(spec.child(0).text().toLowerCase().contains("length")){
                            product.setLength(spec.child(1).text().trim());
                        }
                        if(spec.child(0).text().toLowerCase().contains("depth")){
                            product.setDepth(spec.child(1).text().trim());
                        }
                        if(spec.child(0).text().toLowerCase().contains("collection")){
                            product.setCollection(spec.child(1).text().trim().replace("Collection", "").trim());
                        }
                        if(spec.child(0).text().toLowerCase().contains("finish")){
                            product.setFinishes(spec.child(1).text().trim());
                            if(!product.getName().toLowerCase().contains(product.getFinishes().toLowerCase())){
                                product.setName(product.getName() + " " + product.getFinishes());
                            }
                        }
                    }
                }



                Elements docs = document.select(".document-pdf");
                if(!docs.isEmpty()){
                    for (Element doc : docs){
                        if(doc.child(0).text().toLowerCase().contains("is") && doc.child(0).text().toLowerCase().contains("us")){
                            product.setInstallationDocument(doc.child(1).select("a").attr("abs:href").trim());
                        }
                        if(doc.child(0).text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(doc.child(1).select("a").attr("abs:href").trim());
                        }
                        if(doc.child(0).text().toLowerCase().contains("parts")){
                            product.setPartsBreakdownDocument(doc.child(1).select("a").attr("abs:href").trim());
                        }
                        if(doc.child(0).text().toLowerCase().contains("warranty")){
                            product.setWarrantyDocument(doc.child(1).select("a").attr("abs:href").trim());
                        }
                    }
                }


                if(product.getName().toLowerCase().contains(product.getCollection().toLowerCase())){
                    product.setName(product.getName().replace(product.getCollection(), ""));
                    product.setName(product.getName().replace("Collection", ""));
                    product.setName(product.getName().replace("-", ""));
                    product.setName(product.getCollection() + " " + product.getName());
                }

                product.setName(product.getName().replace("  ", " "));


                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 100000000) {
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
