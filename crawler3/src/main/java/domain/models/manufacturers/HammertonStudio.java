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

public class HammertonStudio extends Manufacturer implements ICrawler
{
    public int manufacturer_id = 50;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://hammertonstudio.com/apothecary-products/apothecary-round-chandelier-03");

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
            if(href.contains("hammertonstudio.com")){
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

            if(!document.select("h1.product-title").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");

                //
                String title =  document.select("meta[property=og:title]")
                        .first().attr("content").replace("- ", "-").trim();
                String[] titleArray = title.split(" ");
                String sku = titleArray[titleArray.length - 1];
                product.setModelNumber(sku);

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                Elements links = document.select("a");
                for (Element link: links){
                    if(link.attr("href").toLowerCase().contains(".pdf")){
                        product.setSpecificationDocument(link.attr("abs:href"));
                    }
                }






                FileInputStream file = new FileInputStream(new File("price-sheets/hammerton-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);


                ArrayList <Product> products = new ArrayList<Product>();
                boolean hasSku = false;
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(3);
                    if(sku2.toString().toUpperCase().contains(product.getModelNumber().toUpperCase())){
                        Product finishProduct = new Product(product);
                        finishProduct.setModelNumber(sku2.toString());
                        finishProduct.setName(row2.getCell(2).toString());
                        finishProduct.setListPrice(row2.getCell(7).toString());
                        finishProduct.setFinishes(row2.getCell(8).toString());

                        String color = row2.getCell(9).toString();
                        String bulb = row2.getCell(4).toString();
                        finishProduct.setName(finishProduct.getName() + " - " + finishProduct.getFinishes() + " " + color + " " + bulb);
                        finishProduct.setName(finishProduct.getName().replace("null", "").replace("  ", " ").trim());
                        products.add(finishProduct);
                    }
                }


                System.out.println(sku+ ": " + document.location());
                System.out.println(products.size());


                for(Product p : products){
                    System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + p);
                    ProductService.createProduct(p, this.manufacturer_id);
                    SpecBooksController.PRODUCT_COUNTER++;
                }




            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 10000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
