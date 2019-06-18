package domain.models.manufacturers;

import com.sleepycat.utilint.StringUtils;
import com.sun.beans.decoder.ElementHandler;
import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.ElementPatterns;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.json.Json;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Richlue extends Manufacturer implements ICrawler {


    public int manufacturer_id = 10;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.richelieu.com/us/en/category/decorative-hardware/cabinet-hardware-pulls-and-knobs/pulls/contemporary-metal-pull-8442/1176559?nf_1007552=%281395827%29");
        //seeds.add("https://www.richelieu.com/us/en/category/screws-and-fasteners/casings-and-dowels/casings-for-multi-shape-connections/knock-in-housing-non-outrigger-drop-in/1038888");
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
                || href.contains("add-to-cart")

        ){
            return false;
        }else {
            if(href.contains("https://www.richelieu.com/us/en/")){
                return super.shouldVisit(referringPage, url);
            }
        }

        return false;

    }


    @Override
    public void visit(Page page) {
        try {

            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + page.getWebURL().toString());
            SpecBooksController.PAGE_COUNTER++;


            if(!page.getWebURL().toString().toLowerCase().contains("decorative-hardware")){
                return;
            }

            Document document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();;

            ArrayList<Product> products = new ArrayList<Product>();



            if(!document.select("#skuModelModal").isEmpty()) {


                Product product = new Product();
                product.setUrl(page.getWebURL().toString());

                Elements name = document.select("#pm2_topInfo h1");
                if(!name.isEmpty()){
                    if(name.first().text().split("-").length == 2){
                        product.setName(name.first().text().split("-")[0].trim());
                    }else {
                        product.setName(name.first().text().trim());
                    }
                }

                //if finishes!

                FileInputStream file = new FileInputStream(new File("price-sheets/richleu.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);



                Elements trs = document.select(".prodTableContainer table tr");
                if(!trs.isEmpty()){
                    for (Element tr: trs){
                        Product finishProduct = new Product(product);
                        Elements tds = tr.select("td");
                        if (tds.isEmpty()){
                            continue;
                        }
                        if(tds.get(0).hasClass("sku ")){
                            finishProduct.setModelNumber(tds.get(0).text().trim());
                            finishProduct.setFinishes(tds.get(1).text().trim());
                            finishProduct.setName(finishProduct.getName() + " - " + finishProduct.getFinishes());
                            for (Row row : sheet) {
                                Cell sheetSku = row.getCell(0);
                                if (sheetSku.toString().toUpperCase().trim().equals(finishProduct.getModelNumber())){
                                    finishProduct.setListPrice(row.getCell(1).toString());
                                }
                            }
                            products.add(finishProduct);
                        }
                    }


                }else {

                    //simple product




                }






                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER));
                System.out.println(products.size());
                for (Product p : products){
                    System.out.println("PRODUCT...............");
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }




                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 100000000) {
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
