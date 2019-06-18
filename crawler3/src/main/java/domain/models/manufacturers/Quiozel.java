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

public class Quiozel extends Manufacturer implements ICrawler {

    public int manufacturer_id = 2;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.quoizel.com/catalogsearch/result/index/?limit=all&q=track+lights");
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")

        ){
            return false;
        }else {
            if(href.contains("www.quoizel.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }

    @Override
    public void visit(Page page)  {
        try {

            Document document = Jsoup.connect(page.getWebURL().getURL()).get();
            String url = document.location();
            System.out.println(SpecBooksController.PAGE_COUNTER + ") " + url);
            SpecBooksController.PAGE_COUNTER++;

            //check to see if it is product page
            if(!document.select(".product-view").isEmpty()) {
                System.out.println("This is a product page.");
                System.out.println("Number of Products) " + SpecBooksController.PRODUCT_COUNTER);
                SpecBooksController.PRODUCT_COUNTER++;


                ArrayList<Product> products = new ArrayList<Product>();


                Product product = new Product();
                Elements base_name = document.select(".product-name h1");
                if (!base_name.isEmpty()) {
                    product.setName(StringManipulation.stripSpecialCharacters(base_name.first().text().trim()));
                }
                Elements finish = document.select(".product-finish span");
                if (!finish.isEmpty()) {
                    String finishName = StringManipulation.stripSpecialCharacters(finish.get(1).text().trim());
                    product.setName(product.getName() + " " + finishName);
                    product.setFinishes(finishName);
                }

                Elements sku = document.select(".product-sku span");
                if (!sku.isEmpty()) {
                    String model_number = StringManipulation.stripSpecialCharacters(sku.first().text());
                    product.setModelNumber(model_number);
                }


                product.setCategory1("Lighting");
                Elements category = document.select(".product-category");
                if (!category.isEmpty()) {
                    String category2 = StringManipulation.stripSpecialCharacters(category.first().text());
                    product.setCategory2(category2);
                }

                Elements docs = document.select("a");
                if (!docs.isEmpty()) {
                    for (Element doc : docs) {
                        if (doc.attr("attribute-code-value").equals("specification_sheet")) {
                            product.setSpecificationDocument(doc.attr("abs:href"));
                        }
                        if (doc.attr("attribute-code-value").equals("instruction_sheet")) {
                            product.setInstallationDocument(doc.attr("abs:href"));
                        }
                    }
                }

                Elements description = document.select("#product-description");
                if (!description.isEmpty()) {
                    product.setDescription(StringManipulation.stripSpecialCharacters(description.first().text().trim()));
                }

                Elements specs = document.select("li div");
                if (!specs.isEmpty()) {
                    for (Element spec : specs) {
                        if (spec.attr("attribute-code-value").equals("lighting_family")) {
                            product.setCollection(spec.text().trim());
                        }
                    }
                }
                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));
                product.setUrl(document.location());
                products.add(product);
                for (Product p: products){
                    ProductService.createProduct(p, this.manufacturer_id);
                }
            }
         }catch (Exception e){
            e.printStackTrace();
        }
    }
}
