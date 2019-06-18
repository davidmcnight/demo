package executables.priceupdates;

import domain.models.Product;
import domain.services.ProductService;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WatermarkPricing {


    public static void main(String[] args){
        try {

            System.out.println("Watermark pricing merge");

            ResultSet products = ProductService.getAllProductsByManufacturer(1);

            FileInputStream file = new FileInputStream(new File("price-sheets/watermark-price.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int mId = 4;


            FileInputStream file2 = new FileInputStream(new File("finish-code-files/watermark.xlsx"));
            Workbook workbook2 = new XSSFWorkbook(file2);
            Sheet sheet2 = workbook2.getSheetAt(0);
//

            ArrayList<Product> productArrayList = new ArrayList<Product>();
            ArrayList<String> modelNumbers = new ArrayList<String>();

            while (products.next()){

                for (Row row : sheet) {

                    Product product = new Product();
                    product.setName(products.getString("name").trim());
                    product.setModelNumber(products.getString("modelNumber").trim());
                    product.setImageLink(products.getString("imageLink").trim());
                    product.setSpecificationDocument(products.getString("specificationDocument").trim());
                    product.setInstallationDocument(products.getString("installationDocument").trim());
                    product.setUrl(products.getString("url").trim());

                    String sku = row.getCell(0).toString();
                    if(products.getString("modelNumber").toUpperCase().trim().equals(sku.toUpperCase().trim())){

                        for (Row row2 : sheet2) {

                            String finishName = StringManipulation.capitalCase(row2.getCell(0).toString().toLowerCase());
                            Product finishProduct = new Product(product);
                            finishProduct.setFinishes(finishName);
                            finishProduct.setName(finishProduct.getName() + " - " + finishName);
                            String finishCode = row2.getCell(1).toString();
                            finishProduct.setModelNumber(finishProduct.getModelNumber() + finishCode);

                            if (row2.getCell(3).toString().toLowerCase().contains("category a")) {
                                finishProduct.setListPrice(row.getCell(2).toString());
                            }

                            if (row2.getCell(3).toString().toLowerCase().contains("category b")) {
                                finishProduct.setListPrice(row.getCell(3).toString());
                            }

                            if (row2.getCell(3).toString().toLowerCase().contains("category c")) {
                                finishProduct.setListPrice(row.getCell(4).toString());
                            }

                            if (row2.getCell(3).toString().toLowerCase().contains("category d")) {
                                finishProduct.setListPrice(row.getCell(5).toString());
                            }

                            if (row2.getCell(3).toString().toLowerCase().contains("category e")) {
                                finishProduct.setListPrice(row.getCell(6).toString());
                            }

                            if(!modelNumbers.contains(finishProduct.getModelNumber())){
                                modelNumbers.add(finishProduct.getModelNumber());
                               // productArrayList.add(finishProduct);
                            }


                        }

                    }else {

                        String baseSku = products.getString("modelNumber").toUpperCase().trim();

                        if(baseSku.split("-").length > 2) {

                            baseSku = baseSku.replace(baseSku.split("-")[baseSku.split("-").length - 1], "");
                            baseSku = baseSku.substring(0, baseSku.length() - 1);

                            if(baseSku.equals(sku.toUpperCase().trim())) {

                                for (Row row2 : sheet2) {

                                    String finishName = StringManipulation.capitalCase(row2.getCell(0).toString().toLowerCase());
                                    Product finishProduct = new Product(product);
                                    finishProduct.setFinishes(finishName);
                                    finishProduct.setName(finishProduct.getName() + " - " + finishName);
                                    String finishCode = row2.getCell(1).toString();
                                    finishProduct.setModelNumber(finishProduct.getModelNumber() + finishCode);

                                    if (row2.getCell(3).toString().toLowerCase().contains("category a")) {
                                        finishProduct.setListPrice(row.getCell(2).toString());
                                    }

                                    if (row2.getCell(3).toString().toLowerCase().contains("category b")) {
                                        finishProduct.setListPrice(row.getCell(3).toString());
                                    }

                                    if (row2.getCell(3).toString().toLowerCase().contains("category c")) {
                                        finishProduct.setListPrice(row.getCell(4).toString());
                                    }

                                    if (row2.getCell(3).toString().toLowerCase().contains("category d")) {
                                        finishProduct.setListPrice(row.getCell(5).toString());
                                    }

                                    if (row2.getCell(3).toString().toLowerCase().contains("category e")) {
                                        finishProduct.setListPrice(row.getCell(6).toString());
                                    }

                                    if (!modelNumbers.contains(finishProduct.getModelNumber())) {
                                        modelNumbers.add(finishProduct.getModelNumber());
                                        productArrayList.add(finishProduct);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Product p : productArrayList) {
                ProductService.createProduct(p,mId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
