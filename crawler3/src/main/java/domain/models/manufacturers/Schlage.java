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

public class Schlage extends Manufacturer implements ICrawler {
    public int manufacturer_id = 101;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("https://www.schlage.com/en/home/products/products-deadbolt.html");
        seeds.add("https://www.schlage.com/en/home/products/products-knobs.html");
        seeds.add("https://www.schlage.com/en/home/products/products-levers.html");
        seeds.add("https://www.schlage.com/en/home/products/products-handlesets.html");
        seeds.add("https://www.schlage.com/en/home/products/products-smart-locks.html");
        seeds.add("https://www.schlage.com/en/home/products/products-electronic-locks.html");
        seeds.add("https://www.schlage.com/en/home/products/products-hardware-accessories.html");
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
                || href.contains("faq.html")

        ){
            return false;
        }else {
            if(href.contains("www.schlage.com")){
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
                if(document.select(".product-details-page").isEmpty()){
                    return;
                }else {

                    Product product = new Product();
                    product.setUrl(page.getWebURL().toString());
                    Elements name = document.select("#pdpLabelTitle h4");
                    if (!name.isEmpty()) {
                        product.setName(name.first().text().trim());
                    }


                    for (Element myLink : document.select("a")) {
                        if (myLink.text().toLowerCase().contains("install")) {
                            product.setInstallationDocument("https://www.schlage.com" + myLink.attr("href"));
                        }
                    }

                    String finishCode = "";
                    String primaryCode = "";
                    String collectionCode = "";
                    //finishes
                    Elements finishes = document.select(".finish-selector");
                    if (!finishes.isEmpty()) {
                        for (Element finish : finishes) {

                            //get image//finishcode/finishname
                            Product finishParent = new Product(product);

                            finishParent.setFinishes(finish.select("img").first().attr("title"));
                            finishParent.setName(finishParent.getName() + " - " + finishParent.getFinishes());

                            String code = finish.attr("data-product-id").trim();
                            primaryCode = code.split(" ")[0];
                            collectionCode = code.split(" ")[1];
                            finishCode = code.split(" ")[code.split(" ").length - 1];

                            Elements images = document.select(".slide img");

                            if (!images.isEmpty()) {
                                for (Element image : images) {
                                    if (image.attr("src").contains(finishCode)) {
                                        finishParent.setImageLink("https://www.schlage.com" + image.attr("src"));
                                    }
                                }
                            }

                            FileInputStream file = new FileInputStream(new File("price-sheets/schlage.xlsx"));
                            Workbook workbook = new XSSFWorkbook(file);
                            Sheet sheet = workbook.getSheetAt(0);
                            for (Row row : sheet) {

                                String[] skuSplit = row.getCell(1).toString().split(".");

                                String sub = row.getCell(1).toString().substring(0, 13);
                                //System.out.println(sub);

                                if (sub.startsWith(primaryCode)
                                        && sub.contains(collectionCode)
                                        && sub.contains(finishCode)) {

                                    Product finishChild = new Product(finishParent);
                                    finishChild.setModelNumber(row.getCell(1).toString());
                                    finishChild.setUpc(row.getCell(0).toString());
                                    finishChild.setListPrice(row.getCell(2).toString());
                                    products.add(finishChild);
                                }


                            }


//                                System.out.println(primaryCode);
//                            System.out.println(finishCode);
//                            System.out.println(finishParent);
                        }

                    }


                    SpecBooksController.PRODUCT_COUNTER++;
                    for (Product p : products) {
                        System.out.println("PRODUCT...............");
                        System.out.println(p);
                        if (!ProductService.doesSkuExist(p.getModelNumber(), this.manufacturer_id)) {
                            ProductService.createProduct(p, this.manufacturer_id);
                        } else {
                            System.out.println(p.getModelNumber() + ": exists for that manufacturer.");
                        }
                    }
                }
//                System.exit(4);






            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
