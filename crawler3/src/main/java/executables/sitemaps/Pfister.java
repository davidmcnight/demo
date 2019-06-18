package executables.sitemaps;

import config.Config;
import controllers.SpecBooksController;
import domain.models.Product;
import domain.services.ProductService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Pfister {


    public static void main(String[] args) {
        try {

            ArrayList<Product> products = new ArrayList<Product>();
            Connection.Response response = null;
            response = Jsoup.connect("https://www.pfisterfaucets.com/sitemap.xml")
                    .userAgent(Config.USER_AGENT)
                    .timeout(0).followRedirects(true)
                    .execute();
            if (response.statusCode() != 200) {
                System.out.println("Error");
            } else {
                Document sitemapDoc = response.parse();
                Elements urls = sitemapDoc.select("loc");
                int counter = 0;

                for (Element url : urls) {

                    products.clear();

                    if (url.text().contains("/product/")) {
                        System.out.println(counter + ") " + url.text());
                        counter++;

//                        if(counter < 1487){
//                            continue;
//                        }

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

                            Elements name = document.select(".v-product-detail__hero-title .h2");
                            if(!name.isEmpty()){
                                product.setName(name.first().ownText().trim());
                            }

                            Elements collection = document.select("h1");
                            if(!collection.isEmpty()){
                                product.setCollection(collection.first().ownText().trim());
                            }



                            //categories
                            Elements categories = document.select(".breadcrumbs__item a");
                            if(!categories.isEmpty()){
                                int categoryCounter = 1;
                                for (Element category: categories){
                                    if(categoryCounter == 2){
                                        product.setCategory1(category.text());
                                    }
                                    if(categoryCounter == 3){
                                        product.setCategory2(category.text());
                                    }
                                    if(categoryCounter == 4){
                                        product.setCategory3(category.text());
                                    }
                                    categoryCounter++;
                                }

                            }

                            Elements details = document.select(".v-product-detail__info-bar li");
                            if(!details.isEmpty()){
                                product.setFinishes(details.get(0).text().trim());
                                product.setModelNumber(details.get(1).text().trim().replace("Model:", "").trim());
                            }
                            //




                            Elements images = document.select("meta[name=og:image]");
                            if(!images.isEmpty()){
                                product.setImageLink(document.select("meta[name=og:image]")
                                        .first().attr("abs:content"));
                            }else {
                                System.out.println(document.location());
                                continue;
                            }

                            Elements pricing = document.select("meta[name=product:price:amount]");
                            if(!pricing.isEmpty()){
                                product.setListPrice(document.select("meta[name=product:price:amount]")
                                        .first().attr("content"));
                            }


                            product.setName(product.getCollection() + " " + product.getName() + " " + product.getFinishes());
                            product.setName(product.getName().replace("null", "").trim());
                            product.setUrl(document.location());


                            if(document.text().toLowerCase().contains("this product has been discontinued")){
                                product.setDiscontinued("Y");
                            }


                            Elements docs = document.select(".icons-block__link");
                            if(!docs.isEmpty()){
                                for (Element doc: docs){
                                    if(doc.text().toLowerCase().contains("instruction")){
                                        product.setInstallationDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("specification")){
                                        product.setSpecificationDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("maintenance")){
                                        product.setCareCleaningDocument(doc.attr("abs:href"));
                                    }
                                    if(doc.text().toLowerCase().contains("parts")){
                                        product.setPartsBreakdownDocument(doc.attr("abs:href"));
                                    }
                                }
                            }

                            Elements desc = document.select("#features .lead");
                            if(!desc.isEmpty()){
                                product.setDescription(desc.first().text());
                            }

                            Elements swatches = document.select(".finish-swatch-list li a");
                            if(!swatches.isEmpty()){
                                for (Element swatch: swatches){

                                    Connection.Response finishResponse = null;
                                    finishResponse = Jsoup.connect(swatch.attr("abs:href"))
                                            .userAgent(Config.USER_AGENT)
                                            .timeout(0).followRedirects(true)
                                            .execute();
                                    if (finishResponse.statusCode() != 200) {
                                        System.out.println("Error");
                                    }else {

                                        Document finishDocument = finishResponse.parse();
                                        Product finishProduct = new Product(product);

                                        Elements name2 = finishDocument.select(".v-product-detail__hero-title .h2");
                                        if(!name2.isEmpty()){
                                            finishProduct.setName(name2.first().ownText().trim());
                                        }

                                        Elements details2 = finishDocument.select(".v-product-detail__info-bar li");
                                        if(!details2.isEmpty()){
                                            finishProduct.setFinishes(details2.get(0).text().trim());
                                            finishProduct.setModelNumber(details2.get(1).text().trim().replace("Model:", "").trim());
                                        }

                                        finishProduct.setName(finishProduct.getCollection() + " " + finishProduct.getName() + " " + finishProduct.getFinishes());
                                        finishProduct.setName(finishProduct.getName().replace("null", "").trim());
                                        finishProduct.setUrl(finishDocument.location());

                                        Elements finishPricing = finishDocument.select("meta[name=product:price:amount]");
                                        if(!finishPricing.isEmpty()){
                                            finishProduct.setListPrice(finishDocument.select("meta[name=product:price:amount]")
                                                    .first().attr("content"));
                                        }

                                        Elements finishImage = finishDocument.select("meta[name=og:image]");
                                        if(!finishImage.isEmpty()){
                                            finishProduct.setImageLink(finishDocument.select("meta[name=og:image]")
                                                    .first().attr("abs:content"));
                                        }else {
                                            finishProduct.setImageLink(product.getImageLink());
                                        }

                                        products.add(finishProduct);


                                    }




                                }

                            }else {

                                products.add(product);
                            }


                            for (Product p: products){
                                System.out.println(counter+ ") "+  product);
                                SpecBooksController.PRODUCT_COUNTER++;
                                ProductService.createProduct(p, 11);
                            }


                            System.out.println("________________________________________________________________________________________");

//                            if(counter >1){
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
