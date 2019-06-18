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
import java.util.ArrayList;

public class Waterstone {


    public static void main(String[] args){
        try {
            FileInputStream file = new FileInputStream(new File("finish-code-files/waterstone-fcs.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            JSONObject jsonObject = new JSONObject();

            for (Row row : sheet) {
                Cell key = row.getCell(0);
                Cell value = row.getCell(1);
                jsonObject.put(key.getStringCellValue().trim(), value.getStringCellValue().trim());
            }



            FileInputStream productFile = new FileInputStream(new File("price-sheets/waterstone-price.xlsx"));
            Workbook workbook2 = new XSSFWorkbook(productFile);
            Sheet sheet2 = workbook2.getSheetAt(0);

            int counter = 0;

            for(Row row: sheet2){
                System.out.println(counter);
                counter++;
                String sku = row.getCell(2).getStringCellValue().trim();
                String[] splits = sku.split("-");
                if(splits.length > 1){
                    if(jsonObject.has(splits[splits.length-1])){
                        String name = jsonObject.getString(splits[splits.length-1]);
                        name = name.trim();
                        row.createCell(4);
                        Cell finishCell = row.getCell(4);
                        finishCell.setCellValue(name);
                    }
                }
            }

            String fileLocation =  "finish-code-files/waterstone-fc-complete.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook2.write(outputStream);
            workbook2.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
