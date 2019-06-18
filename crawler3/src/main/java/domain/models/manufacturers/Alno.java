package domain.models.manufacturers;
import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;

public class Alno extends Manufacturer implements ICrawler {


    public int manufacturer_id = 22;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://alnoinc.com/product-category/mirrors/");
//        seeds.add("http://alnoinc.com/product-category/mirror-cabinets/");
        seeds.add("http://alnoinc.com/product/acrylic-contemporary-pull-a860-8/");
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
                || href.contains("add-to-cart")

        ){
            return false;
        }else {
            if(href.contains("alnoinc.com")){
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

            ArrayList<Product> products = new ArrayList<Product>();

            if(!document.select(".single-product").isEmpty()) {

                //product
                Product product = new Product();
                product.setUrl(document.location());

                //name
                Elements name = document.select(".product_title");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                String finishString = "";
                Elements specs = document.select(".shop_attributes tr");
                if(!specs.isEmpty()){
                    for (Element spec: specs){
                        if(spec.child(0).text().toLowerCase().contains("item")){
                            product.setModelNumber(spec.child(1).text().trim());

                        }
                        if(spec.child(0).text().toLowerCase().contains("series")){
                            product.setCollection(spec.child(1).text().trim());

                        }
                        if(spec.child(0).text().toLowerCase().contains("finishes")){
                            finishString = spec.child(1).text().trim();

                        }
                    }
                }

                Elements docs = document.select("ul li a");
                if(!docs.isEmpty()){
                    for (Element doc : docs){

                        if(doc.text().toLowerCase().contains("spec") &&  !doc.text().toLowerCase().contains("bates")){
                            product.setSpecificationDocument(doc.attr("abs:href"));
                        }
                        if(doc.text().toLowerCase().contains("install")){
                            product.setInstallationDocument(document.attr("abs:href"));
                        }
                    }
                }

                product.setName(product.getName().replace(product.getCollection(), "").trim());
                product.setName(product.getCollection() + " " + product.getName().replace(product.getModelNumber(), "").trim());
                product.setName(product.getName().trim());
                JSONObject jsonObject = new JSONObject();

                String[] finishes = finishString.split(",");

                if(finishes.length > 1) {


                    for (String finish : finishes) {
                        String[] splitStrings = finish.split("–");

                        jsonObject.put(splitStrings[0].trim(), splitStrings[1].trim());
                    }

                    Elements finishSkus = document.select(".shop-inventory tr");
                    if (!finishSkus.isEmpty()) {
                        for (Element f : finishSkus) {
                            String[] splitFinish = f.child(0).text().split("-");
                            for (String sF : splitFinish) {
                                if (jsonObject.has(sF)) {
                                    Product finishProduct = new Product(product);
                                    finishProduct.setModelNumber(f.child(0).text().trim());
                                    finishProduct.setName(finishProduct.getName() + " " + jsonObject.get(sF));
                                    finishProduct.setFinishes(jsonObject.getString(sF));
                                    finishProduct.setListPrice(f.child(2).text().trim());

                                    Elements images = document.select(".thumbnails a");
                                    if (!images.isEmpty()) {
                                        for (Element image : images) {
                                            if (image.attr("abs:href").contains(finishProduct.getModelNumber().replace("/", ""))) {
                                                finishProduct.setImageLink(image.attr("abs:href"));
                                                break;
                                            }
                                        }
                                    }

                                    products.add(finishProduct);
                                    System.out.println(finishProduct);
                                }
                            }
                        }
                    } else {
                        //set image
                        Elements images = document.select(".thumbnails a");
                        if (!images.isEmpty()) {
                            for (Element image : images) {
                                if (image.attr("abs:href").contains(product.getModelNumber().replace("/", ""))) {
                                    product.setImageLink(image.attr("abs:href"));
                                }
                            }
                        }
                        products.add(product);
                    }
                }else {
                    Elements images = document.select(".thumbnails a");
                    if (!images.isEmpty()) {
                        for (Element image : images) {
                            if (image.attr("abs:href").contains(product.getModelNumber().replace("/", ""))) {
                                product.setImageLink(image.attr("abs:href"));

                            }
                        }
                    }

                    Elements price = document.select(".amount");
                    if(!price.isEmpty()){
                        product.setListPrice(price.first().text());
                    }

                    products.add(product);
                }

                for (Product p : products){
                    ProductService.createProduct(p, this.manufacturer_id);
                }


                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
//                if (SpecBooksController.PRODUCT_COUNTER > 1) {
//                    System.exit(0);
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
