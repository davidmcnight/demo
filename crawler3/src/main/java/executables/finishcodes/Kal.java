package executables.finishcodes;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Kal {

    public static void main(String[] args){
        try {
            FileInputStream file = new FileInputStream(new File("finish-code-files/kal-fcs.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            JSONObject jsonObject = new JSONObject();

            for (Row row : sheet) {
                Cell key = row.getCell(0);
                Cell value = row.getCell(1);
                jsonObject.put(key.getStringCellValue().trim(), value.getStringCellValue().trim());
            }


            FileInputStream productFile = new FileInputStream(new File("finish-code-files/kal-products.xlsx"));
            Workbook workbook2 = new XSSFWorkbook(productFile);
            Sheet sheet2 = workbook2.getSheetAt(0);
            int counter  =1;
            for(Row row: sheet2){
                System.out.println(counter);
                counter++;
                String sku = String.valueOf(row.getCell(0)).trim();
                String[] splits = sku.split("-");

                if(splits.length > 1 ){

                    String finishName = "";

                    for (String s : splits){
                        if(jsonObject.has(s)){
                            finishName = finishName + jsonObject.getString(s) + " ";
                        }
                    }
                    finishName = finishName.trim();
                    row.createCell(9);
                    Cell finishCell = row.getCell(9);
                    finishCell.setCellValue(finishName);


                }
            }

            String fileLocation =  "finish-code-files/kal-final-finishes.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook2.write(outputStream);
            workbook2.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
