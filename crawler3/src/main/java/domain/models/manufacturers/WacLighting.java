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
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class WacLighting extends Manufacturer implements ICrawler {
    public int manufacturer_id = 20;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.waclighting.com/product/3357");
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
                || href.contains("pintrest")

        ){
            return false;
        }else {
            if(href.contains("www.waclighting.com")){
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



            if(document.location().contains("/product/")){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");

                FileInputStream file = new FileInputStream(new File("price-sheets/wac-lighting.xlsx"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                ArrayList<Product> products = new ArrayList<Product>();


                //base sku -- use array?
                Elements image = document.select("#BigImg");
                if(!image.isEmpty()){
                    product.setImageLink(image.attr("abs:src"));
                }


                int fIndex = 0;
                boolean hasFinish = false;
                Elements myHeaders = document.select("#overview tr th");
                if(!myHeaders.isEmpty()){
                    for (Element h : myHeaders){
                        if(h.text().toLowerCase().contains("finish")){
                            hasFinish = true;
                            break;
                        }
                        fIndex++;
                    }
                }

                JSONObject jsonObject = new JSONObject();
                Elements myFinishes = document.select("#overview tr td");
                if(hasFinish){
                    Elements codes = myFinishes.get(fIndex).select("p");
                    for (Element c : codes){
                        jsonObject.put(c.select("strong").first().text(), c.ownText().trim().replace("-","").trim());
                    }
                }


                Elements docs = document.select("a");
                for (Element d: docs){
                    if(d.attr("href").contains("specsheet")){
                        product.setSpecificationDocument(d.attr("abs:href"));
                    }
                    if(d.attr("href").contains("instrsheet")){
                        product.setInstallationDocument(d.attr("abs:href"));
                    }

                }


                Elements skutext = document.select("#overview strong");
                if(!skutext.isEmpty()) {

                    String baseSKu = skutext.first().text().trim();

                        for (Row row2 : sheet) {
                            Cell sku2 = row2.getCell(0);
                            if (sku2.toString().toUpperCase().trim().startsWith(baseSKu.toUpperCase().trim())) {
                                Product finishProduct = new Product(product);
                                finishProduct.setModelNumber(row2.getCell(0).toString());
                                finishProduct.setName(StringManipulation.capitalCase(row2.getCell(1).toString().toLowerCase()));
                                finishProduct.setUpc(row2.getCell(2).toString());
                                finishProduct.setListPrice(row2.getCell(3).toString());
                                Iterator<String> keys = jsonObject.keys();
                                while(keys.hasNext()) {
                                    String k = keys.next();
                                    if(finishProduct.getModelNumber().contains(k)){
                                        finishProduct.setFinishes(jsonObject.getString(k));
                                        finishProduct.setName(finishProduct.getName() + " - " + finishProduct.getFinishes());
                                        Elements myImages = document.select("#ImgListLayout img");
                                        if(!myImages.isEmpty()){
                                            for (Element img : myImages){
                                                if(img.attr("src").toLowerCase().contains(k.toLowerCase())){
                                                    finishProduct.setImageLink(img.attr("abs:src")
                                                            .replace("/prod-thumb/", "/prod-zoom/"));
                                                }
                                            }
                                        }
                                    }
                                }

                                products.add(finishProduct);
                            }else {
//                                baseSKu = skutext.get(1).text().trim();
//                                System.out.println(sku2);
//                                Product finishProduct = new Product(product);
//                                finishProduct.setModelNumber(row2.getCell(0).toString());
//                                finishProduct.setName(row2.getCell(1).toString());
//                                finishProduct.setUpc(row2.getCell(2).toString());
//                                finishProduct.setListPrice(row2.getCell(3).toString());
//                                products.add(finishProduct);
                            }
                        }

                }



                System.out.println(product);
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") ");
                SpecBooksController.PRODUCT_COUNTER++;

                for(Product p : products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

            }

            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 10000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
