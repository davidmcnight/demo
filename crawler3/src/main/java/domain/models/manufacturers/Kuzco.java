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

public class Kuzco extends Manufacturer implements ICrawler {
    public int manufacturer_id = 70;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("http://kuzcolighting.com/product/461412-deco/");
        seeds.add("http://kuzcolighting.com/product/adp001/");
        return seeds;
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
                || href.contains("?product_style=")
                || href.contains("?product_finish")
                || href.contains("?product_room")

        ){
            return false;
        }else {
            if(href.contains("kuzcolighting.com")){
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


                ArrayList<Product> products = new ArrayList<Product>();
                if(document.select(".product-page-title").isEmpty()){
                    return;
                }else {

                    Product product = new Product();
                    product.setUrl(page.getWebURL().toString());
                    Elements title = document.select(".product-title");
                    if(!title.isEmpty()){
                        if(title.first().html().split("<br>").length > 1){
                            product.setModelNumber(title.first().html().split("<br>")[1].trim());
                            product.setCollection(title.first().html().split("<br>")[0].trim());
                        }else {
                            product.setModelNumber(title.first().text().trim());
                            product.setCollection("");
                        }

                    }

                    for (Element myLinks : document.select("a")){
                        if(myLinks.text().toLowerCase().contains("spec")){
                            product.setSpecificationDocument(myLinks.attr("href"));
                        }
                        if(myLinks.text().toLowerCase().contains("install")){
                            product.setInstallationDocument(myLinks.attr("href"));
                        }
                    }

                    Elements breadCrumbs = document.select(".breadcrumbs a");
                    if(!breadCrumbs.isEmpty()){
                        product.setName(breadCrumbs.last().text().trim());
                        if(product.getName().substring((product.getName().length() - 1)).equals("s")){
                            if(!product.getName().equals("Accessories")){
                                product.setName(product.getName().substring(0, product.getName().length() - 1).trim());
                            }else {
                                product.setName(product.getName().replace("Accessories", "Accessory").trim());
                            }
                        }
                        product.setName(product.getCollection() + " " + product.getName());
                        product.setName(product.getName().trim());
                    }

                    FileInputStream file = new FileInputStream(new File("price-sheets/kuzco.xlsx"));
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row2 : sheet) {
                        Cell sku2 = row2.getCell(0);
                        if(sku2.toString().toUpperCase().contains(product.getModelNumber().toUpperCase())){
                            Product finishProduct = new Product(product);
                            finishProduct.setModelNumber(sku2.toString());
                            for (Element img : document.select(".product-gallery-slider img")){
                               if(img.attr("src").replace("-","").contains(finishProduct.getModelNumber().replace("-", ""))){

                                   finishProduct.setImageLink(img.attr("src"));

                                   FileInputStream file2 = new FileInputStream(new File("price-sheets/k-fc.xlsx"));
                                   Workbook workbook2 = new XSSFWorkbook(file2);
                                   Sheet sheet2 = workbook2.getSheetAt(0);

                                   boolean if_used = false;

                                   for (Row row3 : sheet2) {
                                       if(finishProduct.getModelNumber().contains(row3.getCell(1).toString())){
                                           if(!if_used){
                                               finishProduct.setFinishes(StringManipulation.capitalCase(row3.getCell(0).toString().toLowerCase()));
                                               System.out.println(finishProduct.getFinishes());
                                               finishProduct.setName(finishProduct.getName().replace(finishProduct.getFinishes(), "")
                                                       .replace("-", "").replace("  ", "") +
                                                       " - " + StringManipulation.capitalCase(finishProduct.getFinishes().toLowerCase().trim()));
                                               if_used =true;
                                           }
                                       }
                                   }
                                   products.add(finishProduct);
                               }
                            }
                        }
                    }
                }

                for (Product p : products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

//                if(SpecBooksController.PRODUCT_COUNTER == 10000){
//                    System.exit(4);
//                }

                SpecBooksController.PRODUCT_COUNTER++;


            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
