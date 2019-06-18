package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
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

public class TechLighting extends Manufacturer implements ICrawler {
    public int manufacturer_id = 66;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.techlighting.com/Products/Fixtures/Linear-Suspension/Sweep-Linear-Suspension");
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
                || href.contains("twitter")
                || href.contains("pinterest")

        ){
            return false;
        }else {
            if(href.contains("www.techlighting.com")){
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



            if(!document.select(".prod-desc").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");



                String type = "";
                String mod = "";
                Elements baseSku = document.select("#buildDropDown .inb_SkuDropDown");
                if(!baseSku.isEmpty()){
                    for (Element e: baseSku){
                        if (e.text().toLowerCase().contains("type")){
                            type = e.ownText().trim();
                        }
                        if (e.text().toLowerCase().contains("model")){
                            mod = e.ownText().trim();
                        }
                    }
                    product.setModelNumber(type+mod);
                }

                Elements baseName = document.select(".header h1");
                if(!baseName.isEmpty()){
                    product.setName(baseName.first().text());
                }

                product.setImageLink(document.select("meta[name=og:image]")
                        .first().attr("abs:content"));



                FileInputStream file = new FileInputStream(new File("price-sheets/tech-lighting.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                ArrayList<Product> products = new ArrayList<Product>();

                Elements docs = document.select(".prod-sections a");
                if(!docs.isEmpty()){
                    for (Element d: docs){
                        if(!d.select("img").isEmpty()) {
                            if (d.select("img").first().attr("abs:src")
                                    .toLowerCase().contains("specifications")) {
                                product.setSpecificationDocument(d.attr("abs:href"));
                            }
                            if (d.select("img").first().attr("abs:src")
                                    .toLowerCase().contains("instructions")) {
                                product.setInstallationDocument(d.attr("abs:href"));
                            }
                        }
                    }
                }


                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);

                    if(sku2.toString().toUpperCase().trim().startsWith(type.trim().toUpperCase()) && sku2.toString().toUpperCase().contains(mod.trim().toUpperCase()) ){

                        System.out.println(sku2);

                        Product finishProduct = new Product(product);
                        finishProduct.setModelNumber(row2.getCell(0).toString());
                        finishProduct.setName(finishProduct.getName() + " " + row2.getCell(1).toString());

//                        finishProduct.setUpc(row2.getCell(3).toString());
                        finishProduct.setListPrice(row2.getCell(2).toString());
                        products.add(finishProduct);
                    }
                }


                System.out.println(product);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

                for(Product p : products){
                    ProductService.createProduct(p, this.manufacturer_id);
                }

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

//

            if(SpecBooksController.PRODUCT_COUNTER > 10000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
