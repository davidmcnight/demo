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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ClassBrassDoor extends Manufacturer implements ICrawler {

    public int manufacturer_id = 12;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        try {
            FileInputStream file = new FileInputStream(new File("price-sheets/cb-3.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String baseSku = row.getCell(0).toString().trim();
                if(!seeds.contains(baseSku)){
                    // System.out.println(baseSku);
                    seeds.add("https://www.classic-brass.com/door-product-details/" + baseSku.replace(".0", "").trim());
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
            if(href.contains("https://www.classic-brass.com/door-product-details/")){
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
                product.setModelNumber(page.getWebURL().toString().split("/")[page.getWebURL().toString().split("-").length]);
                product.setImageLink("https://www.classic-brass.com/" + document.select(".product-medley a").first().attr("href"));
                product.setName(document.select(".product-name").first().text().trim());

                System.out.println(product);
                if(!document.select(".product-download-tearsheet a").isEmpty()){
                    for (Element e : document.select(".product-download-tearsheet a") ){
                        if(e.text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument("https://www.classic-brass.com/" + e.attr("href"));
                        }
                    }
                }

                    FileInputStream file = new FileInputStream(new File("price-sheets/cb-3.xlsx"));
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0);

                    for (Row row : sheet) {

//                        int counter = 0;
//                        Iterator it = row.cellIterator();
//                        while (it.hasNext()){
//                            counter++;
//                        }
//
//                        if(counter < 2){
//                            continue;
//                        }




                        String sku = row.getCell(0).toString().replace(".0", "").trim();
                        if (sku.equals(product.getModelNumber().trim())) {
                            FileInputStream file2 = new FileInputStream(new File("finish-code-files/cb-door-fin.xlsx"));
                            Workbook workbook2 = new XSSFWorkbook(file2);
                            Sheet sheet2 = workbook2.getSheetAt(0);

                            for (Row row2 : sheet2) {
                                Product finishProduct = new Product(product);



                                finishProduct.setFinishes(StringManipulation.
                                        capitalCase(row2.getCell(0).toString().toLowerCase().trim()));

                                finishProduct.setName(finishProduct.getName() + " - " + StringManipulation.
                                        capitalCase(row2.getCell(0).toString().toLowerCase().trim()));

                                finishProduct.setModelNumber(finishProduct.getModelNumber() + "-" + row2.getCell(1));


                                DecimalFormat df = new DecimalFormat("#.##");



                                if (row2.getCell(2).toString().contains("1")) {
                                    finishProduct.setListPrice( df.format(Double.parseDouble(row.getCell(2).toString())));
                                }

                                if (row2.getCell(2).toString().contains("2")) {
                                    finishProduct.setListPrice( df.format(Double.parseDouble(row.getCell(3).toString())));
                                }

                                if (row2.getCell(2).toString().contains("3")) {
                                    finishProduct.setListPrice( df.format(Double.parseDouble(row.getCell(4).toString())));
                                }

                                products.add(finishProduct);

                            }

                        }

                    }


                    for (Product p : products){
                        System.out.println(p);
                        ProductService.createProduct(p, this.manufacturer_id);
                    }

                    SpecBooksController.PRODUCT_COUNTER++;

//                }


            } else {
                System.out.println("Not valid html.");
            }




            SpecBooksController.PAGE_COUNTER++;

            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");


            if (SpecBooksController.PRODUCT_COUNTER > 1000000) {
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