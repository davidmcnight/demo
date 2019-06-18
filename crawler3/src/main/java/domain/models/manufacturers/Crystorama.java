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

public class Crystorama extends Manufacturer implements ICrawler {

    public int manufacturer_id = 38;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.crystorama.com/collection_1300");
        seeds.add("https://www.crystorama.com/outdoor-lighting_1605");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=11");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=111");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=112");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=113");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=114");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=115");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=116");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=117");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=118");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=119");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=440");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=441");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=442");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=443");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=444");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=445");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=446");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=447");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=508");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=504");
        seeds.add("https://www.crystorama.com/searchadv.aspx?searchterm=553");
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
            if(href.contains("www.crystorama.com")){
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


            //System.out.println("VISITING: " + url);

            if(!document.select(".pdetailname").isEmpty()){

                //product
                Product product = new Product();

                /* Data will be mostly used on the price sheet*/

                product.setUrl(document.location());
                product.setCategory1("Lighting");

                //model number
                Elements sku = document.select(".pdetailsku");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text().trim().replace("#", ""));
                }else {
                    System.out.println("SKU failed on page....");
                    System.out.println(document.location());
                    System.exit(0);
                }
                //image
                Elements image = document.select(".product-image img");
                if(!image.isEmpty()){
                    product.setImageLink(image.attr("abs:src"));
                }else {
                    System.out.println("no image");
                    System.out.println(document.location());
                    System.exit(0);
                }

                Elements docs = document.select("a");
                for (Element doc : docs){
                    if(doc.text().toLowerCase().contains("tear sheet")){
                        product.setSpecificationDocument(doc.attr("abs:href"));
                    }
                }


                FileInputStream file = new FileInputStream(new File("price-sheets/crys-pricing.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);


                boolean hasSku = false;
                for (Row row2 : sheet) {
                    Cell sku2 = row2.getCell(0);
                    if(product.getModelNumber().toUpperCase().contains(sku2.toString().toUpperCase())){
                        product.setUpc(row2.getCell(1).toString());
                        product.setName(row2.getCell(2).toString().replace("Crystorama","")
                                .trim().replace("  ", " ").trim());
                        product.setListPrice(row2.getCell(3).toString());
                        hasSku = true;
                    }
                }

                if(hasSku){
                    System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                    SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);
                }else {
//                    System.out.println(product.getModelNumber());
//                    System.out.println(product);
                }




            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 1000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
