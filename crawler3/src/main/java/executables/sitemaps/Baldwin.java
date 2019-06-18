package executables.sitemaps;

import config.Config;
import domain.models.Product;
import domain.services.ProductService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Baldwin {


    public static void main(String[] args) {
        try {

            ArrayList<Product> products = new ArrayList<Product>();
            Connection.Response response = null;
            response = Jsoup.connect("https://www.baldwinhardware.com/sitemap.xml")
                    .userAgent(Config.USER_AGENT)
                    .timeout(0).followRedirects(true)
                    .execute();
            if (response.statusCode() != 200) {
                System.out.println("Error");
            } else {
                Document sitemapDoc = response.parse();
                Elements urls = sitemapDoc.select("url");
                int counter = 1;

                for (Element url : urls) {

                    if(counter < 6739){
                        counter++;
                        continue;
                    }

                    if (url.text().contains("products/details")) {
                        System.out.println(counter + ") " + url.text());
                        counter++;

                        Connection.Response response2 = null;
                        response2 = Jsoup.connect(url.text().trim())
                                .userAgent(Config.USER_AGENT)
                                .timeout(0).followRedirects(true)
                                .execute();
                        if (response.statusCode() != 200) {
                            System.out.println("Error");
                        } else {
                            Document document = response2.parse();
                            Product product = new Product();
                            if (document.select("#product-details").isEmpty()) {
                                continue;
                            }

                            product.setModelNumber(
                                    document.select("#lblModelNumber").first().text()
                            );

                            product.setName(
                                    document.select("#lblDisplayName").first().text()
                            );



                            Elements images = document.select("meta[name=og:image]");
                            if(!images.isEmpty()){
                                product.setImageLink(document.select("meta[name=og:image]")
                                        .first().attr("abs:content"));
                            }

                            Elements docs = document.select("#documents li a");
                            if(!docs.isEmpty()){
                                for (Element doc: docs){
                                    if(doc.text().toLowerCase().contains("spec")){
                                        product.setSpecificationDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("warranty")){
                                        product.setWarrantyDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("parts")){
                                        product.setPartsBreakdownDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("maintenance")){
                                        product.setCareCleaningDocument(doc.attr("abs:href"));
                                    }
                                }
                            }



                            Elements baseFinish = document
                                    .select("#OneColumnContent_C005_ProductFinishes_lbFinishName");
                            if(!baseFinish.isEmpty()){
                                product.setFinishes(baseFinish.first().text());
                            }
                            product.setName(product.getName() + " " + product.getFinishes().replace("null", "").trim());
                            ProductService.createProduct(product, 13);
//                            Elements finishes = document.select("#finishes-container a");
//                            if(!finishes.isEmpty()){
//                                for (Element finish : finishes) {
//                                    Connection.Response response3 = null;
//                                    response3 = Jsoup.connect(finish.attr("abs:href"))
//                                            .userAgent(Config.USER_AGENT)
//                                            .timeout(0).followRedirects(true)
//                                            .execute();
//                                    if (response.statusCode() != 200) {
//                                        continue;
//                                    }else {
//                                        Document finishDocument = response3.parse();
//                                        Product finishProduct = new Product(product);
//
//                                        Elements finishImages = finishDocument.select("meta[name=og:image]");
//                                        if(!finishImages.isEmpty()){
//                                            finishProduct.setImageLink(finishDocument.select("meta[name=og:image]")
//                                                    .first().attr("abs:content"));
//                                        }
//
//                                        Elements myFinish = finishDocument
//                                                .select("#OneColumnContent_C005_ProductFinishes_lbFinishName");
//                                        if(!myFinish.isEmpty()){
//                                            finishProduct.setFinishes(myFinish.first().text());
//                                        }
//
//                                        finishProduct.setModelNumber(
//                                                finishDocument.select("#lblModelNumber").first().text()
//                                        );
//
//                                        finishProduct.setName(finishProduct.getName() + " " + finishProduct.getFinishes().replace("null", "").trim());
//
//                                        ProductService.createProduct(finishProduct, 13);
//                                    }
//                                }
//                            }


                            System.out.println(product);
                            System.out.println("________________________________________________________________________________________");

//                            if(counter >50){
//                                System.exit(0);
//                            }

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
