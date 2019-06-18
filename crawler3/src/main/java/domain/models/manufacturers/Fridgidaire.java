package domain.models.manufacturers;

import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Fridgidaire extends Manufacturer implements ICrawler {
    public int manufacturer_id = 200;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("https://www.frigidaire.com/Kitchen-Appliances/Refrigerators/French-Door-Refrigerator/FGHD2368TF/");
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")
                || href.contains("twitter")
                || href.contains("google")
                || href.contains("pintrest")
                || href.contains("showfeatures")
                || href.contains("?data=true")

        ){
            return false;
        }else {
            if(href.contains("www.frigidaire.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }

    @Override
    public void visit(Page page) {

        try {


            if(page.getParseData() instanceof HtmlParseData) {


                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();
                Document document = Jsoup.parseBodyFragment(html);
                System.out.println(page.getWebURL());


                if(document.select(".product-specs").isEmpty()){
                    return;
                }else {

                    ArrayList<Product> products = new ArrayList<Product>();
                    Product product  = new Product();

                    product.setName(document.select("h1").first().text().trim());

                    Elements colors = document.select(".colorChips li");
                    if(!colors.isEmpty()){
                        for (Element color:colors) {
                            if(color.select("a").hasClass("active")){
                                product.setName(product.getName() + " " + color.attr("data-tooltip").trim());
                                product.setFinishes(color.attr("data-tooltip").trim());
                            }
                        }
                    }

                    String url = document.location();
                    product.setUrl(url);
                    String[] strings = url.replace("https://www.frigidaire.com/","").split("/");
                    product.setCategory1(strings[0].replace("-", " "));
                    //product.setCategory2(strings[1].replace("-", " "));

                    product.setImageLink(document.select("#product_image").first().attr("abs:src"));

                    Elements docs = document.select(".documentItem");

                    for(Element doc: docs){
                        if(doc.select("p").first().text().toLowerCase().contains("owner")){
                            product.setHomeownersDocument(doc.select("a").first().attr("abs:href"));
                        }
                        if(doc.select("p").first().text().toLowerCase().contains("install")){
                            product.setInstallationDocument(doc.select("a").first().attr("abs:href"));
                        }
                        if(doc.select("p").first().text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(doc.select("a").first().attr("abs:href"));
                        }
                    }


                    product.setName(StringManipulation.stripSpecialCharacters(product.getName()));

                    String imgStr = "";
                    int counter = 1;
                    Elements images = document.select(".galleryThumbs li a img");
                    for (Element image: images){
                        if(counter ==1){
                            imgStr += image.attr("abs:src");
                        }else {
                            imgStr += "||" + image.attr("abs:src");
                        }
                        counter++;
                    }
                    product.setAdditionalImages(imgStr);
                    product.setModelNumber("'" + document.select("#pdp-sku").first().text());
                    product.setListPrice(document.select(".initPrice").first().text());
                    System.out.println(product);
                    products.add(product);

                    SpecBooksController.PRODUCT_COUNTER++;
                    for (Product p : products) {
                        System.out.println("PRODUCT...............");
                        System.out.println(p);
                        if (!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)) {
                            ProductService.createProduct(p, this.manufacturer_id);
                        } else {
                            System.out.println(p.getModelNumber() + ": exists for that manufacturer.");
                        }
                    }
                }
//                System.exit(4);






            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
