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

public class Smedbo extends Manufacturer implements ICrawler {
    public int manufacturer_id = 102;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("https://www.smedbo.com/product/basket-for-shower-riser-rail-dk3006/");
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
                || href.contains("wishlist")

        ){
            return false;
        }else {
            if(href.contains("smedbo.com")){
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
                if(document.select(".sku").isEmpty()){
                    return;
                }else {

                    Product product = new Product();
                    product.setUrl(page.getWebURL().toString());
                    Elements sku = document.select(".sku");
                    if(!sku.isEmpty()){
                        product.setModelNumber(sku.first().text());
                    }
                    Elements img = document.select(".woocommerce-product-gallery__wrapper img");
                    if(!img.isEmpty()){
                        product.setImageLink("https://www.smedbo.com" + img.first().attr("src"));
                    }

                    Elements name = document.select("h1");
                    if(!name.isEmpty()){
                        product.setName(name.first().text().trim());
                    }

                    Elements cols = document.select(".col2");
                    if(!cols.isEmpty()){
                        int index = 0;
                        for (Element col : cols){
                            if(col.text().toLowerCase().trim().contains("finish")){
                                product.setFinishes(cols.get(index + 1).text().trim());
                                product.setName(product.getName() + " - " + product.getFinishes());
                                product.setName(product.getName().trim());
                            }
                            index++;
                        }
                    }

                    Elements install = document.select(".meta_assembly a");
                    if(!install.isEmpty()){
                        product.setInstallationDocument("https://www.smedbo.com" + install.attr("href"));
                    }


                    FileInputStream file = new FileInputStream(new File("price-sheets/smedbo.xlsx"));
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row2 : sheet) {
                        if(row2.getCell(0).toString().trim().equals(product.getModelNumber())){
                            product.setListPrice(row2.getCell(1).toString().trim());
                        }
                    }



                    Elements colllection = document.select(".prodpage_line");
                        if(!colllection.isEmpty()){
                            product.setCollection(StringManipulation.capitalCase(colllection.first().text().toLowerCase()));
                        }


                    product.setName(product.getCollection() + " " + product.getName().replace("- Finish", "").trim());

                    if(!ProductService.doesSkuExist(product.getModelNumber(), this.manufacturer_id)){
                        ProductService.createProduct(product, this.manufacturer_id);
                    }else {
                        System.out.println(product.getModelNumber() + ": exists for that manufacturer.");
                    }
                }



                SpecBooksController.PRODUCT_COUNTER++;


            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
