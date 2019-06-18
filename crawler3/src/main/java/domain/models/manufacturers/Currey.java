package domain.models.manufacturers;

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

public class Currey extends Manufacturer implements ICrawler {

    public int manufacturer_id = 30;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.curreyandcompany.com/p-9056-moineaux-wall-sconce.aspx?EID=149&EN=Category#.XEIU7c9KijQ");
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
            if(href.contains("www.curreyandcompany.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {
            Document document = Jsoup.connect(page.getWebURL().getURL()).get();
            String url = document.location();


            Product product = new Product();

            product.setUrl(url);
            product.setCategory1("Lighting");

            if(!document.select(".prodDetailPage").isEmpty()){

//                if(!document.select(".SectionTitleText").text().toLowerCase().contains("lighting")){
//                    System.out.println("Not Lighting");
//                    System.out.println(document.location());
//                    return;
//                }

                Elements name = document.select(".ProductNameText");
                if(!name.isEmpty()){
                    product.setName(name.first().text().trim());
                }

                Elements sku = document.select(".sku");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim().replace("#",""));
                }

                Elements image = document.select(".ProductDiv img");
                if(!image.isEmpty()){
                    product.setImageLink(image.attr("abs:src"));

                }

                Elements specs = document.select(".specsDetails li");
                for (Element spec :specs){
                    if(spec.select("span").text().toLowerCase().contains("finish")){
                        product.setName(product.getName() + " " + spec.ownText().trim());
                    }

                }

                Elements docs = document.select(".downloadsSection a");
                for (Element doc :docs){
                    if(doc.text().toLowerCase().contains("tear")){
                        product.setSpecificationDocument(doc.attr("abs:href"));
                    }

                }



                FileInputStream file = new FileInputStream(new File("price-sheets/currey-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);


                boolean hasSku = false;
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);
                    if(product.getModelNumber().contains(sku2.toString().toUpperCase())){
                       product.setListPrice(row2.getCell(1).toString());
                       hasSku = true;
                    }
                }

                if(hasSku){
                    ProductService.createProduct(product, this.manufacturer_id);
                }


                System.out.println(SpecBooksController.PRODUCT_COUNTER + ")" + product);
                SpecBooksController.PRODUCT_COUNTER++;

                if(SpecBooksController.PRODUCT_COUNTER > 10){
//                    System.exit(0);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
