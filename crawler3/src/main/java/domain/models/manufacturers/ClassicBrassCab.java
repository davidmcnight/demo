package domain.models.manufacturers;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import config.Config;
import controllers.SpecBooksController;
import crawlers.SpecBooksCrawler;
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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ClassicBrassCab extends Manufacturer implements ICrawler {

    public int manufacturer_id = 3;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        try {
            FileInputStream file = new FileInputStream(new File("price-sheets/classic-brass-cabinent.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String baseSku = row.getCell(0).toString().trim().replace(row.getCell(1).toString(), "").trim();
                if(!seeds.contains(baseSku)){
                   // System.out.println(baseSku);
                    if(!baseSku.trim().equals("")){
                        seeds.add("https://www.classic-brass.com/cabinet-product-details/" + baseSku);
                    }

                }
            }


            return seeds;
        }catch (Exception e){
            e.printStackTrace();
        }
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
            if(href.contains("https://www.classic-brass.com/cabinet-product-details/")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {

            if (page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();
                Document document = Jsoup.parseBodyFragment(html);
                System.out.println(SpecBooksController.PAGE_COUNTER + ") " + page.getWebURL());
                SpecBooksController.PAGE_COUNTER++;


                Elements nameSku = document.select(".product-name");
                if(nameSku.isEmpty()){
                    return;
                }

                ArrayList <Product> products = new ArrayList<Product>();
                Product product = new Product();

                product.setUrl(page.getWebURL().toString());
                product.setModelNumber(nameSku.html().split("<br>")[0].replace("<b>", "").trim());
                product.setName(nameSku.html().split("<br>")[1].replace("</b>", "").trim());
                product.setImageLink("https://www.classic-brass.com/" + document.select(".product-medley a").first().attr("href"));

                FileInputStream file = new FileInputStream(new File("price-sheets/classic-brass-cabinent.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if(row.getCell(0).toString().startsWith(product.getModelNumber())){
                        Product finishProduct = new Product(product);
                        finishProduct.setModelNumber(row.getCell(0).toString().trim());
                        finishProduct.setName(finishProduct.getName() + " - " + row.getCell(2).toString().trim());
                        finishProduct.setListPrice(row.getCell(4).toString().trim());
                        products.add(finishProduct);
                    }
                }

                for (Product p : products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

                SpecBooksController.PRODUCT_COUNTER++;
            } else {
                System.out.println("Not valid html.");
            }



            System.out.println(SpecBooksController.PAGE_COUNTER + ") " + page.getWebURL());
            SpecBooksController.PAGE_COUNTER++;

            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");


            if (SpecBooksController.PRODUCT_COUNTER > 100000000) {
                System.exit(1);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}



//  235981760
//  1565994328