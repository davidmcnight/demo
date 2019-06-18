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
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Delaney extends Manufacturer implements ICrawler {
    public int manufacturer_id = 14;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://delaneyhardware.com/products/wicklow/");
        //seeds.add("https://delaneyhardware.com/products/delaney/");
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
            if(href.contains("https://delaneyhardware.com/products/")){
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



            if(!document.select("#product-finishes").isEmpty()){


                ArrayList<Product> products = new ArrayList<Product>();

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setName(StringManipulation.stripSpecialCharacters(document.select("h1").first().text().trim()));
                Elements docs = document.select("#product-metadata");
                for (Element doc : docs){
                    if(doc.text().toLowerCase().contains("spec")){
                        product.setSpecificationDocument(doc.attr("abs:href"));
                    }
                }

                DecimalFormat df = new DecimalFormat("#.##");

                //cards
                Elements cards = document.select(".product-card");
                for (Element card: cards){
                    Product cardProduct = new Product(product);
                    if(!card.select("a").isEmpty()){
                        cardProduct.setImageLink(card.select("a").first().attr("abs:href"));
                    }
                    cardProduct.setFinishes(card.select("h2").text().trim());
                    for (Element finish : card.select("option")){
                        Product finishProduct = new Product(cardProduct);
                        finishProduct.setModelNumber(finish.attr("value"));
//                        System.out.println(finishProduct.getModelNumber());
                        finishProduct.setName(finishProduct.getName() + " " + finish.text().trim() + " - " + finishProduct.getFinishes());

                        FileInputStream file = new FileInputStream(new File("price-sheets/del-3.xlsx"));
                        Workbook workbook = new XSSFWorkbook(file);
                        Sheet sheet = workbook.getSheetAt(0);

                        int countCol=0;
                        for (Row row : sheet) {
                            if (finishProduct.getModelNumber().equals(row.getCell(0).toString().replace(".0", "").trim())){
                                finishProduct.setListPrice(df.format(Double.parseDouble(row.getCell(3).toString())));
                                 products.add(finishProduct);
                            }

                        }
                    }
                }

                System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

                for(Product p : products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

            }



            SpecBooksController.PAGE_COUNTER++;

//

            if(SpecBooksController.PRODUCT_COUNTER > 1000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



