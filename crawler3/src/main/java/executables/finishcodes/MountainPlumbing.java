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

public class MountainPlumbing {


    public static void main(String[] args){
        try {
            FileInputStream file = new FileInputStream(new File("finish-code-files/mp-fcs.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            JSONObject jsonObject = new JSONObject();

            for (Row row : sheet) {
               Cell key = row.getCell(0);
               Cell value = row.getCell(1);
               jsonObject.put(key.getStringCellValue().trim(), value.getStringCellValue().trim());
            }

            FileInputStream productFile = new FileInputStream(new File("finish-code-files/mp-products.xlsx"));
            Workbook workbook2 = new XSSFWorkbook(productFile);
            Sheet sheet2 = workbook2.getSheetAt(0);

            for(Row row: sheet2){
                String sku = row.getCell(0).getStringCellValue().trim();
                String[] splits = sku.split("/");
                if(splits.length > 1){
                    if(jsonObject.has(splits[1])){
                        String name = jsonObject.getString(splits[1]);
                        name = name.trim();
                        row.createCell(4);
                        Cell finishCell = row.getCell(4);
                        finishCell.setCellValue(name);

                    }
                }
            }

            String fileLocation =  "finish-code-files/mountain-plumbing-finishes.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook2.write(outputStream);
            workbook2.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
