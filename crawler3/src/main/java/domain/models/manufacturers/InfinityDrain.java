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
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class InfinityDrain extends Manufacturer implements ICrawler {

    public int manufacturer_id = 220;


    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
//        seeds.add("http://www.capitallightingfixture.com/product/decorative-mirror-57/");
        seeds.add("https://infinitydrain.com/products-squares-rqd5-2-5x5-standard-kit");

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
                || href.contains("pinterest")
        ){
            return false;
        }else {
            if(href.contains("infinitydrain.com")){
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


                if (!document.select(".product h1").isEmpty()) {
                    Product product = new Product();
                    product.setUrl(page.getWebURL().getURL());
                    Elements specs = document.select(".download-pdf");
                    for (Element spec: specs){
                        System.out.println(spec);
                        System.out.println(spec);
                        if(spec.text().toLowerCase().contains("submit")){
                            product.setModelNumber(spec.text().split("SUBMIT")[0]
                                    .replace("-", "").replace(" ", "")
                                    .trim().toUpperCase());
                        }
                    }


                    for (Element doc : document.select(".download-pdf")){
                        if(doc.text().toLowerCase().contains("install")){
                            product.setInstallationDocument(doc.attr("href"));
                        }
                        if(doc.text().toLowerCase().contains("submittal")){
                            product.setSpecificationDocument(doc.attr("href"));
                        }

                    }

                    FileInputStream productFile = new FileInputStream(new File("price-sheets/infinity-drain.xlsx"));
                    Workbook workbook2 = new XSSFWorkbook(productFile);
                    Sheet sheet2 = workbook2.getSheetAt(0);
                    int counter  = 1;
                    for(Row row: sheet2) {
                        if(row.getCell(0).toString().startsWith(product.getModelNumber())){
                            Product finishProduct = new Product(product);
                            finishProduct.setModelNumber(row.getCell(0).toString());
                            finishProduct.setUpc(row.getCell(1).toString());
                            finishProduct.setName(row.getCell(2).toString());
                            finishProduct.setListPrice(row.getCell(3).toString());


                            boolean hasImg = false;
                            int index = 0;
                            for (Element caption : document.select(".caption")){
                                String[] splits = caption.text().split("-");
                                String fc = splits[splits.length-1].trim();
                                if(finishProduct.getModelNumber().contains(fc)){
                                    finishProduct.setImageLink(document.select(".slideshow img").get(index).attr("src"));
                                    hasImg =true;
                                }
                                index++;
                            }

                            if(!hasImg){
                                finishProduct.setImageLink(document.select("meta[property=og:image]")
                                        .first().attr("abs:content"));

                            }
                            products.add(finishProduct);

                        }
                    }

                }

                for (Product p: products){
                    System.out.println(p);
                    ProductService.createProduct(p, this.manufacturer_id);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
