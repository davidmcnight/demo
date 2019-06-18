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

public class ET2 extends Manufacturer implements ICrawler {

    public int manufacturer_id = 42;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.et2online.com/indoor-collection/collection/anastasia");
        seeds.add("http://www.et2online.com/indoor-collection/collection/hue");
        seeds.add("http://www.et2online.com/indoor-collection/collection/caps");
        seeds.add("http://www.et2online.com//indoor-collection/collection/elements");
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
            if(href.contains("www.et2online.com")){
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


            System.out.println("VISITING: " + url);

            if(!document.select(".product-detail-area").isEmpty()){

                //product
                Product product = new Product();

                Elements name = document.select("#ctl00_Main_lblDescription");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                Elements sku = document.select("#ctl00_Main_lblSku");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim());
                }


                Elements collection = document.select("#ctl00_Main_lblFamilyName");
                if(!sku.isEmpty()){
                    product.setCollection(collection.first().ownText().trim());
                }

                Elements finishes = document.select("#ctl00_Main_lblFinishName");
                if(!finishes.isEmpty()){
                    product.setFinishes(finishes.first().ownText().trim());
                }else {
                    product.setFinishes("");
                }

                Elements imgages = document.select("#ctl00_Main_imgItemPicture");
                if(!imgages.isEmpty()){
                    product.setImageLink(imgages.first().attr("abs:src"));
                }


                Elements files = document.select("a");
                if(!files.isEmpty()){
                    for (Element  file : files){
                        if(file.text().toLowerCase().trim().contains("spec sheet")){
                            product.setSpecificationDocument(file.attr("abs:href"));
                        }
                        if(file.text().toLowerCase().trim().equals("instr sheet")){
                            product.setInstallationDocument(file.attr("abs:href"));
                        }
                        if(file.text().toLowerCase().trim().equals("instr sheet")){
                            product.setInstallationDocument(file.attr("abs:href"));
                        }
                    }
                }


                Elements width = document.select("#ctl00_Main_lblWidth");
                if(!width.isEmpty()){
                    product.setWidth(width.first().ownText().trim());
                }

                Elements length = document.select("#ctl00_Main_lblLength");
                if(!length.isEmpty()){
                    product.setLength(length.first().ownText().trim());
                }

                Elements height = document.select("#ctl00_Main_lblHeight");
                if(!height.isEmpty()){
                    product.setHeight(height.first().ownText().trim());
                }



                product.setUrl(document.location());
                product.setName(product.getName() + " " + product.getFinishes());

                FileInputStream file = new FileInputStream(new File("price-sheets/et2-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);


//                boolean hasSku = false;
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);
                    if(product.getModelNumber().contains(sku2.toString().toUpperCase())){
                        product.setListPrice(row2.getCell(1).toString());
                        product.setUpc(row2.getCell(3).toString());

                    }
                }




                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
