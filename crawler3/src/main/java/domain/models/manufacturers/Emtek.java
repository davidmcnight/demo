package domain.models.manufacturers;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
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
import java.util.Iterator;

public class Emtek extends Manufacturer implements ICrawler {

    public int manufacturer_id = 206;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://emtek.com/bath-hardware/sandcast-bronze-towel-ring");
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
                || href.contains("blog")

        ){
            return false;
        }else {
            if(href.contains("emtek.com")){
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

            ArrayList<Product> products = new ArrayList<Product>();

            if(!document.select(".detailsection").isEmpty()) {

                FileInputStream file = new FileInputStream(new File("price-sheets/emtek-2.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                //product
                Product product = new Product();
                product.setUrl(document.location());

                FileInputStream finishFile = new FileInputStream(new File("finish-code-files/emtek-fc.xlsx"));
                Workbook finishBook = new XSSFWorkbook(finishFile);
                Sheet finishSheet = finishBook.getSheetAt(0);
                JSONObject finishJson = new JSONObject();

                for (Row row2 : finishSheet) {
                    Cell key = row2.getCell(1);
                    Cell value = row2.getCell(0);
                    finishJson.put(key.getStringCellValue().trim(), value.getStringCellValue().trim());
                }




                JSONObject imageSrcs = new JSONObject();
                Elements images = document.select(".bs-background img");
                if(!images.isEmpty()){
                    for (Element image: images){
                        String src = image.attr("abs:src");
                        String[] imgMnAr = src.split("_");

                        if(imgMnAr[imgMnAr.length - 1].toUpperCase().contains("OS")
                        ||imgMnAr[imgMnAr.length - 1].toUpperCase().contains("emtek")){
                            String imgMn = imgMnAr[imgMnAr.length - 2].split("\\.")[0].trim()
                                    + imgMnAr[imgMnAr.length - 1].split("\\.")[0].trim();
                            imgMn = imgMn.replace("OS","").trim();
                            imgMn = imgMn.replace("emtek","").trim();
                            imageSrcs.put(imgMn.trim().replace("OS", "").trim(), src);
                        }else {

                            String imgMn = imgMnAr[imgMnAr.length - 1].split("\\.")[0].trim();
                            imageSrcs.put(imgMn.replace("OS","").trim(), src);

                        }

                    }
                }


                ArrayList<String> productCodes = new ArrayList<String>();
                Elements codesHtml = document.select("#codes");

                if(!codesHtml.isEmpty()){
                    String[] codes = codesHtml.text().split(" ");
                    for (String code : codes){
                        if (StringUtils.isNumeric(code)){
                            productCodes.add(code);
//                            productCodes.add("BTB" + code);
//                            productCodes.add("C" + code);
//                            productCodes.add("W" + code);
//                            productCodes.add("F13" + code);
//                            productCodes.add("F20" + code);
//                            productCodes.add("F" + code);
                        }
                    }
                }
//                System.exit(0);
//
//                if(!codesHtml.isEmpty()){
//                    String[] lines = codesHtml.html().split("<br>");
//                    for (String line : lines){
//                        if(line.contains(":")){
//                            productCodes.add(line.split(":")[1].replace("</p>", "").trim());
//                        }
//                    }
//                }

                Elements specs = document.select("#specs a");
                if(!specs.isEmpty()){
                    for (Element spec: specs){
                        if(spec.text().toLowerCase().contains("install")){
                            product.setInstallationDocument(spec.attr("abs:href"));
                        }
                        if(spec.text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(spec.attr("abs:href"));
                        }
                    }
                }

                for (String pc: productCodes){
                    int counter = 1;
                    for (Row row : sheet) {
                        if(counter != 1){
                            DataFormatter formatter = new DataFormatter();
                            String val = formatter.formatCellValue(row.getCell(0));
                            String name = formatter.formatCellValue(row.getCell(1));
                            String price  = String.valueOf(row.getCell(2).toString()).trim();
                            String sku = val;
                            if(sku.startsWith(pc)){
//
                                Product finishProduct = new Product(product);

                                finishProduct.setName(name);
                                finishProduct.setImageLink("");
                                finishProduct.setListPrice(price);
                                finishProduct.setModelNumber(sku);
                                Iterator<String> keys = imageSrcs.keys();

//                                System.out.println(name);
//                                System.out.println(sku);
//                                System.out.println(imageSrcs);
//                                System.out.println("_________________________________________________________________");

                                finishProduct.setImageLink("");

                                while(keys.hasNext()) {
                                    String key = keys.next();
                                    if(name.toLowerCase().contains(key.toLowerCase())){
                                        if(imageSrcs.has(key)){
                                            finishProduct.setImageLink(imageSrcs.getString(key));
                                        }else {
                                            finishProduct.setImageLink(images.first().attr("abs:src"));
                                        }
                                    }
                                }


                                if(finishProduct.getImageLink().length() < 5){
                                    finishProduct.setImageLink(images.first().attr("abs:src"));
                                }

                                Iterator<String> finishKeys = finishJson.keys();
                                while(finishKeys.hasNext()) {
                                    String finishKey = finishKeys.next();
                                    if(finishProduct.getName().toLowerCase().contains(finishKey.toLowerCase())){
                                        if(finishJson.has(finishKey)){
                                            String finishName = finishJson.getString(finishKey);
                                            finishName = finishName.toLowerCase();
                                            finishName = StringManipulation.capitalCase(finishName);
                                            finishProduct.setName(finishProduct.getName().replace(finishKey, finishName));

                                            finishProduct.setFinishes(finishName);
                                        }
                                    }
                                }



                                products.add(finishProduct);
                            }

                        }
                        counter++;
                    }
                }

                for (Product p : products){
                    System.out.println("PRODUCT...............");
                    System.out.println(p);
//                    if(!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)){
                        Thread thread = new Thread();
                        Thread th= new Thread(thread);
                        th.start();
                        Thread.sleep(500);
                        ProductService.createProduct(p, this.manufacturer_id);
//                    }else {
//                        System.out.println(p.getModelNumber() + ": exists for that manufacturer.");
//                    }

                }

//                System.exit(0);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
//                if (SpecBooksController.PRODUCT_COUNTER > 1) {
//                    System.exit(0);
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
