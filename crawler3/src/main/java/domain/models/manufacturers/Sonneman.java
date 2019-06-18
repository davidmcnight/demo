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

public class Sonneman extends Manufacturer implements ICrawler {
    public int manufacturer_id = 91;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.sonnemanawayoflight.com/item2/Votives%E2%84%A2/2857.03-SW#anchorSpecs");

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
                || href.contains("/Collections/Category/Collections/")

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

                Elements sku = document.select("#ContentPlaceHolder1_lblFeaturedItemCode");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text());
                }

                Elements image = document.select("#imgFeaturedItem");
                if(!image.isEmpty()){
                    product.setImageLink(image.attr("abs:src"));
                    if (product.getImageLink().contains(".png")){
                        product.setImageLink("https://www.sonnemanawayoflight.com/uploads/products/800x800/" + product.getModelNumber() + ".png");
                    }else {
                        product.setImageLink("https://www.sonnemanawayoflight.com/uploads/products/800x800/" + product.getModelNumber() + ".jpg");
                    }
                }


                //image
//                Elements image = document.select("#imgFeaturedItem");
//                if(!image.isEmpty()){
//                    product.setImageLink(image.attr("abs:src"));
//                }

                //spec sheet
                Elements specSheet = document.select("#ContentPlaceHolder1_specSheet");
                if(!specSheet.isEmpty()){
                    product.setSpecificationDocument(specSheet.attr("abs:href"));
                }

                Elements installSheet = document.select("#ContentPlaceHolder1_lnkAI");
                if(!specSheet.isEmpty()){
                    product.setInstallationDocument(installSheet.attr("abs:href"));
                }


                FileInputStream file = new FileInputStream(new File("price-sheets/sonneman.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);






                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);

                    if(sku2.toString().toUpperCase().contains(product.getModelNumber().toUpperCase())){
                        System.out.println(sku2.toString());
                        product.setUpc(row2.getCell(1).toString());
                        product.setName(row2.getCell(2).toString());
                        product.setCollection(row2.getCell(3).toString());
                        product.setFinishes(row2.getCell(4).toString());
                        product.setListPrice(row2.getCell(9).toString());

                        String finalName = product.getName();
                        if(product.getCollection().trim().equals("null")){
                            product.setCollection("");
                        }else {
                            finalName = product.getCollection() + " " + product.getName();
                        }

                        if(product.getFinishes().trim().equals("null")){
                            product.setFinishes("");
                        }else {
                            finalName += " - " + product.getFinishes()
;                        }
                        product.setName(finalName);

                    }
                }


                System.out.println(product);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

//                for(Product p : products){

//                    ProductService.createProduct(p, this.manufacturer_id);
//
//                }

                ProductService.createProduct(product, this.manufacturer_id);


            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

//

            if(SpecBooksController.PRODUCT_COUNTER > 1){
//                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
