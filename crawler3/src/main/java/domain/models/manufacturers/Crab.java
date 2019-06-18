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

public class Crab extends Manufacturer implements ICrawler {

    public int manufacturer_id = 202;


    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        //seeds.add("https://signofthecrab.com/specifications-installation/p0342/");
        seeds.add("https://signofthecrab.com/specifications-installation/p0731/");
        seeds.add("https://signofthecrab.com/specifications-installation/p1008-2/");
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


        ){
            return false;
        }else {
            if(href.contains("signofthecrab.com")){
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
                System.out.println(page.getWebURL());

                ArrayList<Product> products = new ArrayList<Product>();


                if (!document.select("#product_details_container").isEmpty()) {
                    Product product = new Product();
                    product.setUrl(page.getWebURL().getURL());
                    String[] urls = product.getUrl().split("/");
                    product.setModelNumber(urls[urls.length-1].toUpperCase());

                    product.setImageLink(document.select("meta[property=og:image]")
                            .first().attr("abs:content"));

                    for (Element doc: document.select("#product_description a")){
                        if(doc.text().contains("instructions")){
                            product.setInstallationDocument(doc.attr("href"));
                        }
                    }


                    System.out.println(product);
                    boolean has = false;
                    FileInputStream productFile = new FileInputStream(new File("price-sheets/crab.xlsx"));
                    Workbook workbook2 = new XSSFWorkbook(productFile);
                    Sheet sheet2 = workbook2.getSheetAt(0);
                    int counter  = 1;
                    for(Row row: sheet2) {
                        if(row.getCell(1).toString().startsWith(product.getModelNumber())){
                            has = true;
                            Product finishProduct = new Product(product);
                            finishProduct.setModelNumber(row.getCell(0).toString());
                            finishProduct.setName(row.getCell(1).toString());
                            if(row.getCell(2) != null){
                                finishProduct.setListPrice(row.getCell(2).toString());
                            }
                            products.add(finishProduct);
                        }



                    }
                    if(!has){
                        product.setName(product.getModelNumber());
                        products.add(product);
                    }
                }



                for (Product p: products){
                    System.out.println(p);
                    if(!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)){
                        ProductService.createProduct(p, this.manufacturer_id);
                    }

                }

//                System.exit(9);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
