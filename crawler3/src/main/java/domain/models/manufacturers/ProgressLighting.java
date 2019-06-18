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

public class ProgressLighting extends Manufacturer implements ICrawler {

    public int manufacturer_id = 5;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://progresslighting.com/product/p400144-009/");
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
            if(href.contains("https://progresslighting.com")){
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

            if(!document.select(".product-container").isEmpty()){

                //product
                Product product = new Product();

                //model number
                Elements sku = document.select(".product_title");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim());
                }

                //name
                Elements name = document.select(".product-subtitle");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                //category
                product.setCategory1("Lighting");

                Elements breadCrumbs = document.select(".breadcrumbs a");

                if(!breadCrumbs.isEmpty()){
                    product.setCategory2(breadCrumbs.last().text().trim());
                }

                Elements image = document.select(".woocommerce-product-gallery__image img");
                if(!image.isEmpty()){
                    product.setImageLink(image.first().attr("abs:src"));
                }


                //price
                Elements price = document.select(".woocommerce-Price-amount");
                if(!price.isEmpty()){
                    product.setListPrice(price.first().ownText().trim());
                }

                //specs
                Elements specs = document.select(".shop_attributes tr");
                if(!specs.isEmpty()){
                    for (Element spec: specs){
                        if(spec.child(0).text().toLowerCase().trim().contains("length")){
                            product.setLength(spec.child(1).text());
                        }
                        if(spec.child(0).text().toLowerCase().trim().contains("width")){
                            product.setWidth(spec.child(1).text());
                        }
                        if(spec.child(0).text().toLowerCase().trim().contains("height")){
                            product.setHeight(spec.child(1).text());
                        }
                        if(spec.child(0).text().toLowerCase().trim().contains("depth")){
                            product.setDepth(spec.child(1).text());
                        }
                        if(spec.child(0).text().toLowerCase().trim().contains("finish")){
                            product.setFinishes(spec.child(1).text());
                        }
                        if(spec.child(0).text().toLowerCase().trim().contains("collection")){
                            product.setCollection(spec.child(1).text());
                        }
                    }
                }

                Elements files = document.select("a");
                if(!files.isEmpty()){
                    for (Element file : files){
                        if(file.text().toLowerCase().trim().contains("spec")){
                            product.setSpecificationDocument(file.attr("abs:href"));
                        }
                        if(file.text().toLowerCase().trim().contains("warranty")){
                            product.setWarrantyDocument(file.attr("abs:href"));
                        }
                        if(file.text().toLowerCase().trim().contains("install")){
                            product.setInstallationDocument(file.attr("abs:href"));
                        }
                    }
                }

                Elements desc = document.select(".product-short-description p");
                if(!desc.isEmpty()){
                    product.setDescription(desc.first().text());
                }


                product.setUrl(document.location());
                product.setName(product.getName() + " " + product.getFinishes());
                ProductService.createProduct(product, this.manufacturer_id);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
