package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
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
import java.util.concurrent.TimeUnit;

public class Elk extends Manufacturer implements ICrawler {

    public int manufacturer_id = 60;

    //TODO: Only lighting, 4000 Lighting only

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("https://www.elkgroupinternational.com/elk-lighting");
//        seeds.add("https://www.elkgroupinternational.com/elk-lighting-categories");
//        seeds.add("https://www.elkgroupinternational.com/indoor-lighting");
//        seeds.add("https://www.elkgroupinternational.com/outdoor-lighting");
        seeds.add("https://www.elkgroupinternational.com/timberwood-4-billiard-island-oil-rubbed-bronze-33071-4");
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
            if(href.contains("www.elkgroupinternational.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {


            //TimeUnit.SECONDS.sleep(3);


            if(page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();
                Document document = Jsoup.parseBodyFragment(html);


               // System.out.println(document.html());

                if (!document.select(".product-details-code").isEmpty()) {

                    if (!document.select(".breadcrumb").first().text()
                            .toLowerCase().contains("elk lighting")) {
                        return;
                    }

                    //product
                    Product product = new Product();

                    /* Data will be mostly used on the price sheet*/

                    product.setUrl(page.getWebURL().toString());
                    product.setCategory1("Lighting");

                    //model number
                    Elements name = document.select("h1");
                    if (!name.isEmpty()) {
                        product.setName(name.first().text().trim());
                    } else {
                        System.out.println("SKU failed on page....");
                        System.out.println(document.location());
                        System.exit(0);
                    }
                    //image
                    Elements image = document.select("#product-detail-gallery-main-img");
                    if (!image.isEmpty()) {
                        System.out.println(image.first().attr("data-zoom-image"));
                        product.setImageLink("https://www.elkgroupinternational.com" + image.first().attr("data-zoom-image"));
                    } else {
                        System.out.println("no image");
                        System.out.println(document.location());
                        System.exit(0);
                    }

                    Elements sku = document.select(".product-details-code");
                    if (!sku.isEmpty()) {
                        product.setModelNumber(sku.first().text().trim());
                    } else {
                        System.out.println("SKU failed on page....");
                        System.out.println(document.location());
                        System.exit(0);
                    }


                    FileInputStream file = new FileInputStream(new File("price-sheets/elk-pricing.xlsx"));
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0);
//
//

                    for (Row row2 : sheet) {
                        Cell sku2 = row2.getCell(0);
                        if (product.getModelNumber().toUpperCase().contains(sku2.toString().toUpperCase())) {
                            product.setListPrice(row2.getCell(1).toString());
                        }
                    }


                    ProductService.createProduct(product, this.manufacturer_id);
                    System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                    SpecBooksController.PRODUCT_COUNTER++;

                }


                System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + page.getWebURL());
                SpecBooksController.PAGE_COUNTER++;

//            if(SpecBooksController.PRODUCT_COUNTER > 1){
//                System.exit(0);
//            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
