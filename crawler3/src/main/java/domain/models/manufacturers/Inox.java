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

public class Inox extends Manufacturer implements ICrawler {
    public int manufacturer_id = 65;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("http://www.unisonhardware.com/products/BP101/");
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
                || href.contains("tumblr")

        ){
            return false;
        }else {
            if(href.contains("unisonhardware.com")){
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
                if(document.select(".productitemcontent").isEmpty()){
                    return;
                }else {

                    Product product = new Product();
                    product.setUrl(page.getWebURL().toString());

                    Elements myDocs = document.select("a");
                    for (Element d : myDocs){
                        if(d.text().toLowerCase().contains("instruct")){
                            System.out.println("here");
                            product.setInstallationDocument(d.attr("href"));
                        }
                    }

                    System.out.println(product);

                    Elements skus = document.select(".productdetailcontentleft");
                    if(!skus.isEmpty()){


                        for (Element sku : skus){
                            String mySku = sku.text().trim();

                            System.out.println(mySku);

                            FileInputStream file = new FileInputStream(new File("price-sheets/inox/i-full.xlsx"));
                            Workbook workbook = new XSSFWorkbook(file);
                            Sheet sheet = workbook.getSheetAt(0);



                            for (Row row : sheet) {
                                Cell cell = row.getCell(0);
                                String cellSku = cell.toString().trim();
                                if(cellSku.equals(mySku)){
                                    System.out.println(cellSku + " : " + mySku);
                                    Product finishProduct = new Product(product);
                                    finishProduct.setModelNumber(mySku);
//                                    finishProduct.setImageLink("http://www.unisonhardware.com" + document.select(".ad-thumb-list a").first().attr("href"));
                                    finishProduct.setName(row.getCell(1).toString());
                                    finishProduct.setFinishes(row.getCell(2).toString().split("\\(")[0].trim());
                                    finishProduct.setListPrice(row.getCell(3).toString());
                                    finishProduct.setSpecificationDocument(row.getCell(10).toString());
                                    finishProduct.setInstallationDocument(row.getCell(13).toString());
                                    finishProduct.setUpc(row.getCell(14).toString());
                                    products.add(finishProduct);

                                }
                            }
                        }
                    }
                }

                for (Product p : products) {
                    System.out.println("PRODUCT...............");
                    System.out.println(p);
                    if (!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)) {
                        ProductService.createProduct(p, this.manufacturer_id);
                    } else {
                        System.out.println(p.getModelNumber() + ": exists for that manufacturer.");
                    }
                }

//                if(SpecBooksController.PRODUCT_COUNTER == 1){
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
