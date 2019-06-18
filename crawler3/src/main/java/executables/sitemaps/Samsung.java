package executables.sitemaps;

import config.Config;
import domain.models.Product;
import domain.services.ProductService;
import helpers.StringManipulation;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Samsung {


    public static void main(String[] args) {
        try {

            ArrayList<Product> products = new ArrayList<Product>();
            ArrayList<String> siteMapPages = new ArrayList<String>();
            siteMapPages.add("https://www.samsung.com/us/consumer_sitemap.xml");

            for (String sitemap: siteMapPages){
                Connection.Response response = null;

                response = Jsoup.connect(sitemap)
                        .userAgent(Config.USER_AGENT)
                        .timeout(0).followRedirects(true)
                        .execute();
                if(response.statusCode() != 200){
                    System.out.println("Error");
                }else {
                    Document sitemapDoc = response.parse();
                    Elements urls =  sitemapDoc.select("url");
                    int counter = 1;
                    for (Element url: urls){
                        String productUrl = url.select("loc").text();
                        if(productUrl.contains("/us/home-appliances/")){
                            Connection.Response response2 = null;
                            response2 = Jsoup.connect(productUrl)
                                    .userAgent(Config.USER_AGENT)
                                    .timeout(0).followRedirects(true)
                                    .execute();
                            if(response.statusCode() != 200){
                                System.out.println("Error");
                            }else {
                                Document document = response2.parse();

                                if(document.select(".product-details").isEmpty()){
                                    continue;
                                }

                                System.out.println(counter + ") " + productUrl);
                                Product product = new Product();
                                String sku = document.select(".product-details__info-sku").first().text().trim();
                                String name = document.select(".product-details__info-title").first().text();
                                name = StringManipulation.stripSpecialCharacters(name);

                                Elements options = document.select(".selector-option .selected .name");
                                if(!options.isEmpty()){
                                    String finish = options.text().trim();
                                    name = name + " " + finish;
                                    product.setFinishes(finish);
                                }

                                product.setModelNumber(sku);
                                product.setName(name);
                                product.setImageLink(document.select("meta[property=og:image]")
                                        .first().attr("abs:content"));

                                Elements price = document.select(".epp-price");
                                if(!price.isEmpty()){
                                    product.setListPrice(price.first().text());
                                }

                                if(!document.select(".product-details__info-description--line1").isEmpty()){
                                    product.setDescription(
                                            StringManipulation.stripSpecialCharacters(
                                                    document.select(".product-details__info-description--line1").first().text()));

                                }

                                Elements specs = document.select(".spec-download a");
                                if(!specs.isEmpty()){
                                    for (Element spec: specs){
                                        product.setSpecificationDocument(spec.attr("abs:href"));
                                    }
                                }

                                product.setUrl(document.location());
                                ProductService.createProduct(product, 28);
                                products.add(product);


                                System.out.println(product);
                                System.out.println("______________________");
                                counter++;
                            }

                        }

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
