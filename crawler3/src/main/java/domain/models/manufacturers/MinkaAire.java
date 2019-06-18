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

public class MinkaAire extends Manufacturer implements ICrawler {

    public int manufacturer_id = 70;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.minkagroup.net/f304l-bn-sl.html");
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
            if(href.contains("https://www.minkagroup.net")){
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

                Elements logoImage = document.select(".details-title-logo img");
                if(!logoImage.isEmpty()){
                    if(logoImage.first().attr("src").contains("Minka-Aire.png")){
                        if(!document.select(".p-details-top").isEmpty()){
                            // parse data
                            System.out.println("Product Page: ");
                            System.out.println(page.getWebURL());

                            ArrayList<Product> products = new ArrayList<Product>();
                            Product product = new Product();

                            Elements specs  = document.select(".manual-specs a");
                            if(!specs.isEmpty()) {
                                for (Element spec : specs) {
                                    if (spec.text().toLowerCase().trim().contains("instruction")) {
                                        product.setInstallationDocument(spec.attr("abs:href"));
                                    }
                                    if (spec.text().toLowerCase().trim().contains("specifications")) {
                                        product.setSpecificationDocument(spec.attr("abs:href"));
                                    }
                                }
                            }


                                //get other url
                                Elements finishes = document.select(".color-img-list a");
                                if(!finishes.isEmpty()){

                                    for (Element finishUrl : finishes){
                                        Connection.Response response = null;
                                        response = Jsoup.connect(finishUrl.attr("abs:href"))
                                                .userAgent(Config.USER_AGENT)
                                                .timeout(0).followRedirects(true)
                                                .execute();
                                        if (response.statusCode() != 200) {
                                            System.out.println("Error");
                                        }else {
                                            continue;
                                        }

                                        //
                                        Document finishDoc = response.parse();
                                        Product finishProduct = new Product(product);
                                        Elements image = finishDoc.select("#zoom1 img");
                                        if(!image.isEmpty()){
                                            finishProduct.setImageLink(image.first().attr("abs:src"));
                                        }

                                        Elements sku = finishDoc.select(".p-details-txt h5");
                                        if(!sku.isEmpty()){
                                            finishProduct.setModelNumber(sku.first().ownText().trim());
                                        }

                                        FileInputStream file = new FileInputStream(new File("price-sheets/minka-price-sheet.xlsx"));
                                        Workbook workbook = new XSSFWorkbook(file);
                                        Sheet sheet = workbook.getSheetAt(0);
                                        for (Row row : sheet) {
                                            Cell sheetSku = row.getCell(0);
                                            if(sheetSku.toString().toUpperCase().trim().contains(finishProduct.getModelNumber().toUpperCase().trim())){
                                                finishProduct.setName(StringManipulation.stripSpecialCharacters(
                                                        StringManipulation.capitalCase(row.getCell(2)
                                                                .toString().toLowerCase())));
                                            }
                                        }

                                        products.add(finishProduct);

                                    }
                                }else {
                                    //set product data for one product
                                    //wont create a finish product
                                }

                                for (Product p : products){
                                    System.out.println(p);
                                    ProductService.createProduct(p, this.manufacturer_id);
                                }

                                SpecBooksController.PRODUCT_COUNTER++;
                                if(SpecBooksController.PRODUCT_COUNTER > 1){
                                    System.exit(3);
                                }

                            }







                        }

                }
            }else {
                System.out.println("Not valid html.");
            }
            System.gc();
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());
            System.out.println("_____________________________________________________________");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


//  235981760
//  1565994328