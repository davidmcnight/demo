package domain.models.manufacturers;


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

public class Phylrich extends Manufacturer implements ICrawler {

    public int manufacturer_id = 73;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://phylrich.com/products/widespread-faucet-501-01");
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
            if(href.contains("https://phylrich.com")){
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

                if(document.select(".product_name").isEmpty()){
                    return;
                }

                ///:TODO LOOK AT SELECT OPTION

                Product product = new Product();
                product.setName(document.select(".product_name").first().text().trim());
                product.setModelNumber(product.getName().split(" ")[product.getName().split(" ").length - 1]);
                product.setName(product.getName().replace(product.getModelNumber(), "").trim());
               // product.setListPrice(document.select(".current_price").first().text());

                product.setUrl(page.getWebURL().toString());
                Elements docs = document.select("a");
                for (Element myDoc : docs){
                    if(myDoc.text().toLowerCase().contains("specification")){
                        product.setSpecificationDocument(myDoc.attr("href").replace("//",""));
                    }
                    if(myDoc.text().toLowerCase().contains("installation")){
                        product.setInstallationDocument(myDoc.attr("href").replace("//",""));
                    }
                    if(myDoc.text().toLowerCase().contains("part")){
                        product.setPartsBreakdownDocument(myDoc.attr("href").replace("//",""));
                    }
                }

                ArrayList<Product> products = new ArrayList<Product>();

                Elements swatches = document.select(".swatch input");
                if(!swatches.isEmpty()){
                    int index = 0;
                    for (Element swatch: swatches ){
                        Product finishProduct = new Product(product);
                        finishProduct.setFinishes(swatch.attr("value"));
                        finishProduct.setName(finishProduct.getName() + " - " + swatch.attr("value"));
                        if(index >= document.select(".product_gallery img").size()){
                            finishProduct.setImageLink(document.select(".product_gallery img").get(0).attr("src")
                                    .replace("//", "").replace("100x", "2000x"));

                        }else {
                            finishProduct.setImageLink(document.select(".product_gallery img").get(index).attr("src")
                                    .replace("//", "").replace("100x", "2000x"));

                        }
                        index++;


                        finishProduct.setModelNumber(finishProduct.getModelNumber() + this.getFinishCode(finishProduct.getFinishes()));
                        //get finish price

                        FileInputStream file = new FileInputStream(new File("price-sheets/phylrich.xlsx"));
                        Workbook workbook = new XSSFWorkbook(file);
                        Sheet sheet = workbook.getSheetAt(0);
                        for (Row row : sheet) {
                          //  System.out.println(row);
                            Cell sheetSku = row.getCell(0);
                            if(sheetSku.toString().replace("/", "-").toUpperCase().trim().contains(finishProduct.getModelNumber().trim().toUpperCase())){
                                finishProduct.setListPrice(row.getCell(2).toString());
                            }
                        }
                        products.add(finishProduct);
                    }
                }

                System.out.println(document.select(".swatch input").size());
                System.out.println(document.select(".product_gallery img").size());

                for (Product p: products){
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


            if(SpecBooksController.PRODUCT_COUNTER > 10000000){
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getFinishCode(String name){
        if(name.trim().contains("Polished Chrome")){
            return "-026";
        }

        if(name.trim().contains("Satin Brass")){
            return "-004";
        }

        if(name.trim().contains("Polished Nickel")){
            return "-014";
        }

        if(name.trim().contains("Matte Black")){
            return "-040";
        }

        if(name.trim().contains("Satin Nickel")){
            return "-015";
        }

        if(name.trim().contains("Polished Brass")){
            return "-003";
        }

        if(name.trim().contains("French Brass")){
            return "-002";
        }

        if(name.trim().contains("Polished Gold")){
            return "-025";
        }

        if(name.trim().contains("Satin Gold")){
            return "-024";
        }

        if(name.trim().contains("Burnished Gold")){
            return "-24B";
        }

        if(name.trim().contains("Satin Chrome")){
            return "-26D";
        }

        if(name.trim().contains("Pewter")){
            return "-15A";
        }

        if(name.trim().contains("Burnished Nickel")){
            return "-15B";
        }

        if(name.trim().contains("Polished Brass Uncoated")){
            return "-03U";
        }

        if(name.trim().contains("Old English Brass")){
            return "-OEB";
        }

        if(name.trim().contains("Antique Brass")){
            return "-047";
        }

        if(name.trim().contains("Antique Bronze")){
            return "-11B";
        }

        if(name.trim().contains("Oil Rubbed Bronze")){
            return "-10B";
        }

        if(name.trim().contains("Weathered Copper")){
            return "-05W";
        }

        if(name.trim().contains("Polished Copper")){
            return "-005";
        }

        if(name.trim().contains("Antique Copper")){
            return "-05A";
        }

        if(name.trim().contains("Gloss Black")){
            return "-041";
        }

        if(name.trim().contains("Satin White")){
            return "-050";
        }

        return null;
    }

}



//  235981760
//  1565994328