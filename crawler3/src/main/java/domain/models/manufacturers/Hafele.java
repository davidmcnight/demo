package domain.models.manufacturers;

import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
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

public class Hafele extends Manufacturer implements ICrawler {

    public int manufacturer_id = 211;


    public ArrayList<String> getSeeds() {
        try {
            ArrayList<String> seeds = new ArrayList<String>();
//                FileInputStream productFile = new FileInputStream(new File("price-sheets/rohl-19.xlsx"));
//                Workbook workbook2 = new XSSFWorkbook(productFile);
//                Sheet sheet2 = workbook2.getSheetAt(0);
//                int counter = 1;
//                for (Row row : sheet2) {
//                    seeds.add("https://www.rohlhome.com/search?q=" + row.getCell(1).toString().trim());
//                }

            seeds.add("https://www.hafele.com/us/en/product/handle-zinc/0000001300031d6400040023/#SearchParameter=&@QueryTerm=*&Category=Nh8KAOsFiLkAAAFd4HRO6HHz&@P.FF.followSearch=9950&PageNumber=1&OriginalPageSize=12&PageSize=12&Position=1&OrigPos=1&ProductListSize=631&PDP=true");

            return seeds;
        }catch (Exception e){
            e.printStackTrace();
        }

        System.exit(10);
        return null;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("facebook")
                || href.contains("youtube")
                || href.contains("twitter")
                || href.contains("google")
                || href.contains("pintrest")


        ){
            return false;
        }else {
            if(href.contains("www.hafele.com")){
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
                String url2 = page.getWebURL().getURL();
                System.out.println(SpecBooksController.PAGE_COUNTER + ") " + url2);
                SpecBooksController.PAGE_COUNTER++;

                ArrayList<Product> products = new ArrayList<Product>();


                if (!document.select(".productImageWrapper").isEmpty()) {

                    Product product = new Product();



//                        FileInputStream productFile = new FileInputStream(new File("price-sheets/rohl-19.xlsx"));
//                        Workbook workbook2 = new XSSFWorkbook(productFile);
//                        Sheet sheet2 = workbook2.getSheetAt(0);
//                        int counter  =1;
//                        for(Row row: sheet2) {
//                            if(row.getCell(1).toString().equals(finishProduct.getModelNumber())){
//                                finishProduct.setListPrice(row.getCell(3).toString());
//                                finishProduct.setUpc(row.getCell(4).toString());
//                            }
//                        }
//                        products.add(finishProduct);
//                    }
//                    for (Product p: products){
//                        System.out.println(p);
//                        if(!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)){
//                            ProductService.createProduct(p, this.manufacturer_id);
//                        }
//
//                    }

//                    System.exit(4);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
