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

public class Watermark extends Manufacturer implements ICrawler {

    public int manufacturer_id = 1;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        try {

            FileInputStream file = new FileInputStream(new File("price-sheets/watermark-price.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int index =1;

            for (Row row : sheet) {
                if(index < 500 && index > 200){
                    Cell sheetSku = row.getCell(0);
                    String sku = sheetSku.toString().replace("-__-__-__", "").trim();

                    seeds.add("https://watermark-designs.com/search?query=" + sku);
                }
                index++;
                //seeds.add("https://watermark-designs.com/search/p2?query=" + sku);
//                seeds.add("https://watermark-designs.com/product/" + sku);
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
            if(href.contains("https://watermark-designs.com")){
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

                if(document.select(".product-info").isEmpty()){
                    return;
                }

                Product product = new Product();

                product.setUrl(page.getWebURL().toString());
                product.setModelNumber(document.select(".product-info p").first().text().trim());
                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));


                for (Element element : document.select(".produ" +
                        "ct-button-group a")){
                    if(element.text().toLowerCase().contains("specs")){
                        product.setSpecificationDocument(element.attr("abs:href"));
                    }
                    if(element.text().toLowerCase().contains("install")){
                        product.setInstallationDocument(element.attr("abs:href"));
                    }
                }

                ArrayList<Product> products = new ArrayList<Product>();

                product.setName(document.select(".product-top-heading h2").first().text().trim());

//                FileInputStream file = new FileInputStream(new File("price-sheets/watermark-price.xlsx"));
//                Workbook workbook = new XSSFWorkbook(file);
//                Sheet sheet = workbook.getSheetAt(0);
//                for (Row row : sheet) {
//                    Cell sheetSku = row.getCell(0);
//
//                    Elements collection = document.select("h3 a");
//                    if(!collection.isEmpty()){
//                        product.setCollection(collection.first().text().trim());
//                        product.setName(product.getCollection() + " " +row.getCell(1).toString());
//                    }else {
//                        product.setName(row.getCell(1).toString());
//                    }


//
//                    if(sheetSku.toString().toUpperCase().trim().equals(product.getModelNumber().trim().toUpperCase().trim())){
//
//                        FileInputStream file2 = new FileInputStream(new File("finish-code-files/watermark.xlsx"));
//                        Workbook workbook2 = new XSSFWorkbook(file2);
//                        Sheet sheet2 = workbook2.getSheetAt(0);
//
//
//
//                        int counter = 1;
//                        for (Row row2 : sheet2) {
//
//                            if(counter > 33){
//                                break;
//                            }
//
//                            Product finishProduct = new Product(product);
//                            String finishName = StringManipulation.capitalCase(row2.getCell(0).toString().toLowerCase());
//
//                            finishProduct.setFinishes(finishName);
//
//                            finishProduct.setName(finishProduct.getName() + " - " + finishName);
//
//                            String finishCode = row2.getCell(1).toString();
//                            finishProduct.setModelNumber(finishProduct.getModelNumber() + finishCode);
//
//                            if(row2.getCell(3).toString().toLowerCase().contains("category a")){
//                                finishProduct.setListPrice(row.getCell(2).toString());
//                            }
//
//                            if(row2.getCell(3).toString().toLowerCase().contains("category b")){
//                                finishProduct.setListPrice(row.getCell(3).toString());
//                            }
//
//                            if(row2.getCell(3).toString().toLowerCase().contains("category c")){
//                                finishProduct.setListPrice(row.getCell(4).toString());
//                            }
//
//                            if(row2.getCell(3).toString().toLowerCase().contains("category d")){
//                                finishProduct.setListPrice(row.getCell(5).toString());
//                            }
//
//                            if(row2.getCell(3).toString().toLowerCase().contains("category e")){
//                                finishProduct.setListPrice(row.getCell(6).toString());
//                            }
//                            products.add(finishProduct);
//                            counter++;
//                        }
//
//                    }
//                }

                products.add(product);

                for (Product p : products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

                SpecBooksController.PRODUCT_COUNTER++;
            }else {
                System.out.println("Not valid html.");
            }




            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");


            if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                System.exit(1);
            }

        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}



//  235981760
//  1565994328