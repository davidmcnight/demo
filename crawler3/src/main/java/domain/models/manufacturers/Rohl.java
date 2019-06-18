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

public class Rohl extends Manufacturer implements ICrawler {

    public int manufacturer_id = 205;


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

            seeds.add("https://www.rohlhome.com/bath/bath-sinks/8446-non-slotted-lift-and-turn-drain#8446PN");

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
            if(href.contains("www.rohlhome.com")){
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


                if (!document.select(".product-title").isEmpty()) {

                    Product product = new Product();

                    product.setName(document.select("h1").first().text().trim());
                    String url = page.getWebURL().getURL();
                    product.setUrl(url);

                    product.setModelNumber(document.select("#model_id").first().text().trim());
                    product.setModelNumber(product.getModelNumber().replace(
                            "-" + product.getModelNumber().split("-")[product.getModelNumber().split("-").length-1]
                            ,"").trim()
                    );


                    //docs
                    for (Element e: document.select(".grid__item a")) {
                        if(e.text().toLowerCase().contains("specifications")){
                            product.setSpecificationDocument(e.attr("href"));
                        }
                        if(e.text().toLowerCase().contains("install")){
                            System.out.println(e);
                            product.setInstallationDocument(e.attr("href"));
                        }
                    }




                    for (Element f : document.select(".finishes .finish")){

                        //System.out.println(f);

                        Product finishProduct = new Product(product);

                        String pfc = document.select(".finishes .finish").get(0).select("img").first()
                                .attr("alt").split(" ")[0];

                        String fc = f.select("img").first()
                                .attr("alt").split(" ")[0];



                        finishProduct.setModelNumber(
                                finishProduct.getModelNumber().replace(pfc, "") + fc);
                        finishProduct.setFinishes(f.attr("data-finish"));
                        finishProduct.setImageLink(f.attr("data-image"));
                        finishProduct.setName(finishProduct.getName() + " - " + finishProduct.getFinishes());


                        FileInputStream productFile = new FileInputStream(new File("price-sheets/rohl-19.xlsx"));
                        Workbook workbook2 = new XSSFWorkbook(productFile);
                        Sheet sheet2 = workbook2.getSheetAt(0);
                        int counter  =1;
                        for(Row row: sheet2) {
                            if(row.getCell(1).toString().equals(finishProduct.getModelNumber())){
                                finishProduct.setListPrice(row.getCell(3).toString());
                                finishProduct.setUpc(row.getCell(4).toString());
                            }
                        }
                        products.add(finishProduct);
                    }
                    for (Product p: products){
                        System.out.println(p);
                        if(!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)){
                            ProductService.createProduct(p, this.manufacturer_id);
                        }

                    }

//                    System.exit(4);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
