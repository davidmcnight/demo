package domain.models.manufacturers;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Sonneman2 extends Manufacturer implements ICrawler {
    public int manufacturer_id = 15;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.sonnemanawayoflight.com/item2/Infinity/2660.18#anchorSpecs");
        seeds.add("https://www.sonnemanawayoflight.com/item2/Waveforms%E2%84%A2/2673.25W#anchorSpecs");
        seeds.add("https://www.sonnemanawayoflight.com/item2/Corso%20Linear/1734.98#anchorSpecs");
        seeds.add("https://www.sonnemanawayoflight.com/item2/Corso%20Linear/1735.98#anchorSpecs");
        seeds.add("https://www.sonnemanawayoflight.com/item2/Pannelo/1871.24F#anchorSpecs");
        seeds.add("https://www.sonnemanawayoflight.com/item2/Dianelli/1881.35F#anchorSpecs");
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
            if(href.contains("www.sonnemanawayoflight.com")){
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




            if(!document.select("#ContentPlaceHolder1_lblFeaturedItemCode").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");

                ArrayList<Product> products = new ArrayList<Product>();

                Elements sku = document.select("#ContentPlaceHolder1_lblFeaturedItemCode");
                if(!sku.isEmpty()){
                    System.out.println(sku.first().text());
                    product.setModelNumber(sku.first().text().split("\\.")[0]);
                }


                //spec sheet
                Elements installSheet = document.select("#ContentPlaceHolder1_lnkAI");
                if(!installSheet.isEmpty()){
                    product.setInstallationDocument(installSheet.attr("abs:href"));
                }
                FileInputStream file = new FileInputStream(new File("price-sheets/sonneman.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);
                    if(sku2.toString().toUpperCase().contains(product.getModelNumber().toUpperCase())){
                        System.out.println(sku2.toString());
                        Product finishProduct = new Product(product);
                        finishProduct.setModelNumber(sku2.toString().toUpperCase());
                        finishProduct.setUpc(row2.getCell(1).toString());
                        finishProduct.setCollection(StringManipulation.capitalCase(row2.getCell(3).toString().toLowerCase()));
                        finishProduct.setFinishes(StringManipulation.capitalCase(row2.getCell(4).toString().toLowerCase()));
                        finishProduct.setName(finishProduct.getCollection() + " " + row2.getCell(2).toString() + " - " + finishProduct.getFinishes());
                        Elements images = document.select("img");
                        for (Element image : images){
                            if(image.attr("src").toLowerCase().contains(finishProduct.getModelNumber().toLowerCase())){
                                finishProduct.setImageLink(image.attr("src").replace("\\", "/"));

                            }
                        }
                        products.add(finishProduct);
                    }
                }

                System.out.println(product);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

                for(Product p : products){
                    ProductService.createProduct(p, this.manufacturer_id);
                    System.out.println(p);
                }




            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

//

            if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
