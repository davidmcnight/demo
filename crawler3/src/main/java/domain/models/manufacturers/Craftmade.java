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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Craftmade extends Manufacturer implements ICrawler {


    public int manufacturer_id = 10;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.craftmade.com/serene-49935");
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
            if(href.contains("www.craftmade.com")){
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

            if(!document.select(".product-info-main").isEmpty()) {

                ArrayList<Product> products = new ArrayList<Product>();


                //product
                Product product = new Product();

                product.setCategory1("Lighting");
                product.setUrl(document.location());

                Elements name = document.select(".product.attribute.overview");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                Elements dims = document.select("#tab-label-dimensions tr");
                if(!dims.isEmpty()){
                    for (Element d: dims){
                        if(d.child(0).text().toLowerCase().contains("height")){
                            product.setHeight(d.child(1).text().trim());
                        }
                        if(d.child(0).text().toLowerCase().contains("width")){
                            product.setWidth(d.child(1).text().trim());
                        }
                        if(d.child(0).text().toLowerCase().contains("length")){
                            product.setLength(d.child(1).text().trim());
                        }
                    }
                }


                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));



                Elements baseSku = document.select(".product-add-form form");
                if(!baseSku.isEmpty()){
                    product.setModelNumber(baseSku.attr("data-product-sku"));
                }else {
                    System.out.println(document.location());
                    System.out.println("failed sku");
                    System.exit(0);
                }


                System.out.println(product);


                FileInputStream file = new FileInputStream(new File("price-sheets/craftmade-2019.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                System.out.println(product.getModelNumber());
                for (Row row2 : sheet) {
                    Cell sku = row2.getCell(0);
                    if(sku.toString().toUpperCase().contains(product.getModelNumber())){
                        Product finishProduct = new Product(product);
                        finishProduct.setModelNumber(row2.getCell(0).toString());
                        finishProduct.setCollection(row2.getCell(1).toString());
                        finishProduct.setFinishes(row2.getCell(3).toString());
                        finishProduct.setListPrice(row2.getCell(5).toString());
                        finishProduct.setName(finishProduct.getName()
                                + " " + row2.getCell(2).toString()
                                + " - " + finishProduct.getFinishes());
                        finishProduct.setName(finishProduct.getName().replace("null", ""));
                        products.add(finishProduct);
                    }
                }



                for(Product p : products){
                    ProductService.createProduct(p, this.manufacturer_id);
                }


//                System.exit(0);
                //og:price:amount
//                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 10000000) {
//                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
