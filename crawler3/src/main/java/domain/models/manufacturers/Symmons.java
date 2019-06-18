package domain.models.manufacturers;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Symmons extends Manufacturer implements ICrawler {

    private final Logger logger = LoggerFactory.getLogger(Symmons.class);
    public final int manufacturer_id = 74;
    private static final Pattern NON_ASCII = Pattern.compile("[^\\x00-\\x7f]");

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds = new ArrayList<String>();
        // seeds.add("https://www.symmons.com");
        seeds.add("https://www.symmons.com/products/");
        /* seeds.add("https://www.symmons.com/families/kitchen/");
        seeds.add("https://www.symmons.com/segments/commercial/");
        seeds.add("https://www.symmons.com/showcase-design-studio/");
        seeds.add("https://www.symmons.com/temptrol-valve/");
        seeds.add("https://www.symmons.com/new-products/");*/
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")
                || href.contains("?product_style=")
                || href.contains("?product_finish")
                || href.contains("?product_room")) {
            return false;
        } else {
            if (href.contains("https://www.symmons.com")) {
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        try {
            List<Product> products = new ArrayList<Product>();
            Document document = Jsoup.connect(page.getWebURL().getURL()).get();

            SpecBooksController.PAGE_COUNTER++;

            if (!document.select(".product_info").isEmpty()) {
                SpecBooksController.PRODUCT_COUNTER++;
                //System.err.println("Product " + SpecBooksController.PRODUCT_COUNTER);
                Elements elements1 = document.select(".product_info");
                Elements elements2 = elements1.select(".variant_option");
                for (Element e2 : elements2) {
                    Product product = new Product();
                    product.setUrl(document.baseUri());

                    //Name
                    String strName = NON_ASCII.matcher(elements1.select("h1").text()).replaceAll("");
                    product.setName(strName);

                    Elements e = e2.select("input");
                    //Model Number
                    product.setModelNumber(e.attr("id"));
                    //Image

                    //product.setImageLink(e.attr("data-permalink"));
                    Elements imgElement = document.select(".product_image_container");
                    Elements imgsub = imgElement.select("img");
                    String strImg = imgsub.attr("abs:src");
                    product.setImageLink(strImg);

                    String label = e2.select("label").text().toLowerCase();
                    String variant[] = label.split("(?=\\d)", 2);
                    //Finishing
                    product.setFinishes(variant[0].replace("$", ""));
                    //Price
                    product.setListPrice(variant[1].replace("usd", "").trim());
                    //Collection
                    String collection = document.select(".product_collections h3").text();
                    collection = NON_ASCII.matcher(collection).replaceAll("");
                    product.setCollection(collection);

                    //Description
                    String desc = document.select(".product_collections p").text();
                    product.setDescription(desc);
                    //Category
                    Elements categoryElements = elements1.select(".product_categories a");

                    int size = categoryElements.size();
                    if (size >= 1 && categoryElements.get(0) != null) {
                        String category1 = categoryElements.get(0).text();
                        product.setCategory1(category1);
                    }
                    if (size >= 2 && categoryElements.get(1) != null) {
                        String category2 = categoryElements.get(1).text();
                        product.setCategory2(category2);
                    }
                    if (size >= 3 && categoryElements.get(2) != null) {
                        String category3 = categoryElements.get(2).text();
                        product.setCategory3(category3);
                    }

                    //Features
                    if (!document.select(".product_features .features_content ul").isEmpty()) {
                        // product.setFeatures(document.select(".product_features .features_content ul li").text());
                    }
                    //docs
                    Elements documents = document.select(".product_documents ul li");
                    for (Element doc : documents) {
                        Elements element = doc.select("li");
                        if (element.text().equals("Product Sheet")) {
                            product.setAdditionalDocuments(element.select("a").attr("href"));
                        }
                        if (element.text().equals("Installation Instructions")) {
                            product.setInstallationDocument(element.select("a").attr("href"));
                        }
                        if (element.text().equals("Specification Submittal")) {
                            product.setSpecificationDocument(element.select("a").attr("href"));
                        }
                    }
                    products.add(product);
                    break;
                }
            }

            for (Product p : products) {
                ProductService.createProduct(p, this.manufacturer_id);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
