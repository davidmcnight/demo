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

import java.util.ArrayList;

public class CapitalLighting extends Manufacturer implements ICrawler {
    public int manufacturer_id = 31;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("http://www.capitallightingfixture.com/collection/capital-sconces/");
        seeds.add("http://www.capitallightingfixture.com/collection/capital-ceiling/");

        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")
                || href.contains("?product_style=")
                || href.contains("?product_finish")
                || href.contains("?product_room")

        ){
            return false;
        }else {
            if(href.contains("www.capitallightingfixture.com")){
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
            if (!document.select(".product-info").isEmpty()) {
                System.out.println("This is a product page.");
                System.out.println("Number of Products) " + SpecBooksController.PRODUCT_COUNTER);
                SpecBooksController.PRODUCT_COUNTER++;
                ArrayList<Product> products = new ArrayList<Product>();
                Product product = new Product();
                Elements sku = document.select(".item-number");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim());
                }


                Elements name = document.select(".info-basics h1");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                Elements category1 = document.select(".info-basics em");
                if(!category1.isEmpty()){
                    product.setCategory1(category1.first().text().trim());
                }

                Elements finish = document.select(".item-finish");
                if(!finish.isEmpty()){
                    String finishName = finish.first().text().trim();
                    product.setFinishes(finishName);
                    String name2 = product.getName() + " " + finishName;
                    product.setName(name2.trim());
                }

//                Elements specSheet = document.select(".wpptopdf");
//                if(!specSheet.isEmpty()){
//                    product.setSpecificationDocument(specSheet.attr("abs:href"));
//                }

                for (Element mySpec : document.select("a")){
                    if(mySpec.text().toLowerCase().contains("download specs")){
                        product.setSpecificationDocument(mySpec.attr("abs:href"));
                    }
                }

                Elements specs = document.select("dl dt,dd");
                if(!specs.isEmpty()){
                    int counter = 0;
                    for (Element spec: specs){
                        if(spec.text().toLowerCase().trim().equals("description")){
                            product.setDescription(specs.get(counter+1).text());
                        }
                        counter++;
                    }
                }




                Elements images = document.select(".product-gallery img");
                if(!images.isEmpty()){
                    int img_counter = 1;
                    String imgString = "";
                    for (Element img: images){
                        if(img_counter == 1){
                            product.setImageLink(img.attr("abs:src"));
                        }else {
                            if(img_counter == 2){
                                imgString = img.attr("abs:src");
                            }else {
                                imgString = imgString + "||" + img.attr("abs:src");
                            }
                        }
                        img_counter++;
                    }
                }



                Elements dimensions = document.select(".item-deminsions");
                if(!dimensions.isEmpty()){
                    String[] dims = dimensions.text().split("x");
                    for (String d: dims){
                        if(d.contains("W")){
                            product.setWidth(d.replace("W","").trim());
                        }
                        if(d.contains("L")){
                            product.setLength(d.replace("L","").trim());
                        }
                        if(d.contains("D")){
                            product.setDepth(d.replace("D","").trim());
                        }
                        if(d.contains("H")){
                            product.setHeight(d.replace("H","").trim());
                        }

                    }
                }
                product.setUrl(document.location());
                System.out.println(product);
                products.add(product);
                for (Product p: products){
                    ProductService.createProduct(p, this.manufacturer_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
