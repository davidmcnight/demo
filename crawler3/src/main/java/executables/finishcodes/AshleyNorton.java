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

public class AshleyNorton {

    public static void main(String[] args){
        try {
            FileInputStream file = new FileInputStream(new File("finish-code-files/ash-fc.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            JSONObject jsonObject = new JSONObject();

            for (Row row : sheet) {
                Cell key = row.getCell(1);
                Cell value = row.getCell(0);
                jsonObject.put(key.toString().trim().replace(".0", "").trim(), value.toString().trim());
            }

            System.out.println(jsonObject);

            FileInputStream productFile = new FileInputStream(new File("finish-code-files/ash-crawl.xlsx"));
            Workbook workbook2 = new XSSFWorkbook(productFile);
            Sheet sheet2 = workbook2.getSheetAt(0);
            int counter  =1;
            for(Row row: sheet2){

                if(counter == 1){
                    counter++;
                    continue;
                }

                String finalVal = "";

                String val = row.getCell(0).toString();

                if(!val.contains(".")){
                    System.out.println(val);
                    finalVal = val.split("-")[val.split("-").length - 1];
                }
                else if(val.contains("/")){
                    String[] vals = val.split("\\/");
                    if(vals[vals.length-1].contains("-")){
                        String[] vals2 = vals[vals.length-1].split("-");
                        finalVal = vals2[0];
                    }else {
                        finalVal = vals[vals.length-1];
                    }

                }else {
                    String[] vals = val.split("\\.");
                    if(vals[vals.length-1].contains("-")){
                        String[] vals2 = vals[vals.length-1].split("-");
                        finalVal = vals2[0];
                    }else {
                        finalVal = vals[vals.length-1];
                    }

                }

                finalVal = finalVal.replace("DBL", "").trim();

                String myVal = jsonObject.getString(finalVal);
                row.createCell(4);
                Cell finishCell = row.getCell(4);
                finishCell.setCellValue(myVal);




            }

            String fileLocation =  "finish-code-files/ash-final-finishes.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook2.write(outputStream);
            workbook2.close();

//
//            String fileLocation =  "finish-code-files/ash-final-finishes.xlsx";
//            FileOutputStream outputStream = new FileOutputStream(fileLocation);
//            workbook2.write(outputStream);
//            workbook2.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
